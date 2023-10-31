package com.skyler.cloud.skyler.common.mybatis.handler;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.skyler.cloud.skyler.common.core.beans.SkylerUser;
import com.skyler.cloud.skyler.common.core.exception.enums.ErrorCodeEnum;
import com.skyler.cloud.skyler.common.core.exception.utils.ServiceExceptionUtil;
import com.skyler.cloud.skyler.common.core.util.SecurityUtils;
import com.skyler.cloud.skyler.common.core.util.SpringUtils;
import com.skyler.cloud.skyler.common.core.util.StreamUtils;
import com.skyler.cloud.skyler.common.mybatis.annotation.DataColumn;
import com.skyler.cloud.skyler.common.mybatis.annotation.DataPermission;
import com.skyler.cloud.skyler.common.mybatis.enums.DataScopeType;
import com.skyler.cloud.skyler.common.mybatis.helper.DataPermissionHelper;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 数据权限过滤
 * </p>
 *
 * @author lzy
 * @since 2023/10/18 15:40
 */
@Slf4j
public class SkylerDataPermissionHandler {
	/**
	 * 方法或类(名称) 与 注解的映射关系缓存
	 */
	private final Map<String, DataPermission> dataPermissionCacheMap = new ConcurrentHashMap<>();

	/**
	 * 无效注解方法缓存用于快速返回
	 */
	private final Set<String> invalidCacheSet = new ConcurrentHashSet<>();

	/**
	 * spel 解析器
	 */
	private final ExpressionParser parser = new SpelExpressionParser();
	private final ParserContext parserContext = new TemplateParserContext();
	/**
	 * bean解析器 用于处理 spel 表达式中对 bean 的调用
	 */
	private final BeanResolver beanResolver = new BeanFactoryResolver(SpringUtils.getBeanFactory());

	public Expression getSqlSegment(Expression where, String mappedStatementId, boolean isSelect) {
		DataColumn[] dataColumns = findAnnotation(mappedStatementId);
		if (ArrayUtil.isEmpty(dataColumns)) {
			invalidCacheSet.add(mappedStatementId);
			return where;
		}
		SkylerUser currentUser = SecurityUtils.getUser();
		DataPermissionHelper.setVariable("user", currentUser);

		// 如果是超级管理员，则不过滤数据
		if (ObjectUtil.isNull(currentUser) || SecurityUtils.isAdmin(currentUser.getId())) {
			return where;
		}
		String dataFilterSql = buildDataFilter(dataColumns, isSelect);
		if (StrUtil.isBlank(dataFilterSql)) {
			return where;
		}
		try {
			Expression expression = CCJSqlParserUtil.parseExpression(dataFilterSql);
			// 数据权限使用单独的括号 防止与其他条件冲突
			Parenthesis parenthesis = new Parenthesis(expression);
			if (ObjectUtil.isNotNull(where)) {
				return new AndExpression(where, parenthesis);
			} else {
				return parenthesis;
			}
		} catch (JSQLParserException e) {
			throw ServiceExceptionUtil.exception(ErrorCodeEnum.DATA_PERMISSION_EXCEPTION, e.getMessage());
		}
	}

	/**
	 * 构造数据过滤sql
	 */
	private String buildDataFilter(DataColumn[] dataColumns, boolean isSelect) {
		// 更新或删除需满足所有条件
		String joinStr = isSelect ? " OR " : " AND ";
		SkylerUser user = DataPermissionHelper.getVariable("user");
		StandardEvaluationContext context = new StandardEvaluationContext();
		context.setBeanResolver(beanResolver);
		DataPermissionHelper.getContext().forEach(context::setVariable);
		Set<String> conditions = new HashSet<>();
		for (String dataScope : user.getRoleDataScopes()) {
			// 获取角色权限泛型
			DataScopeType type = DataScopeType.findCode(dataScope);
			if (ObjectUtil.isNull(type)) {
				throw ServiceExceptionUtil.exception(ErrorCodeEnum.ROLE_DATA_EXCEPTION, dataScope);
			}
			// 全部数据权限直接返回
			if (type == DataScopeType.ALL) {
				return "";
			}
			boolean isSuccess = false;
			for (DataColumn dataColumn : dataColumns) {
				if (dataColumn.key().length != dataColumn.value().length) {
					throw ServiceExceptionUtil.exception(ErrorCodeEnum.ROLE_DATA_EXCEPTION, "key与value长度不匹配");
				}
				// 不包含 key 变量 则不处理
				if (!StrUtil.containsAny(type.getSqlTemplate(),
						Arrays.stream(dataColumn.key()).map(key -> "#" + key).toArray(String[]::new)
				)) {
					continue;
				}
				// 设置注解变量 key 为表达式变量 value 为变量值
				for (int i = 0; i < dataColumn.key().length; i++) {
					context.setVariable(dataColumn.key()[i], dataColumn.value()[i]);
				}

				// 解析sql模板并填充
				String sql = parser.parseExpression(type.getSqlTemplate(), parserContext).getValue(context, String.class);
				conditions.add(joinStr + sql);
				isSuccess = true;
			}
			// 未处理成功则填充兜底方案
			if (!isSuccess && StrUtil.isNotBlank(type.getElseSql())) {
				conditions.add(joinStr + type.getElseSql());
			}
		}

		if (CollUtil.isNotEmpty(conditions)) {
			String sql = StreamUtils.join(conditions, Function.identity(), "");
			return sql.substring(joinStr.length());
		}
		return "";
	}

	private DataColumn[] findAnnotation(String mappedStatementId) {
		StringBuilder sb = new StringBuilder(mappedStatementId);
		int index = sb.lastIndexOf(".");
		String clazzName = sb.substring(0, index);
		String methodName = sb.substring(index + 1, sb.length());
		Class<?> clazz = ClassUtil.loadClass(clazzName);
		List<Method> methods = Arrays.stream(ClassUtil.getDeclaredMethods(clazz))
				.filter(method -> method.getName().equals(methodName)).collect(Collectors.toList());
		DataPermission dataPermission;
		// 获取方法注解
		for (Method method : methods) {
			dataPermission = dataPermissionCacheMap.get(mappedStatementId);
			if (ObjectUtil.isNotNull(dataPermission)) {
				return dataPermission.value();
			}
			if (AnnotationUtil.hasAnnotation(method, DataPermission.class)) {
				dataPermission = AnnotationUtil.getAnnotation(method, DataPermission.class);
				dataPermissionCacheMap.put(mappedStatementId, dataPermission);
				return dataPermission.value();
			}
		}
		dataPermission = dataPermissionCacheMap.get(clazz.getName());
		if (ObjectUtil.isNotNull(dataPermission)) {
			return dataPermission.value();
		}
		// 获取类注解
		if (AnnotationUtil.hasAnnotation(clazz, DataPermission.class)) {
			dataPermission = AnnotationUtil.getAnnotation(clazz, DataPermission.class);
			dataPermissionCacheMap.put(clazz.getName(), dataPermission);
			return dataPermission.value();
		}
		return null;
	}

	/**
	 * 是否为无效方法 无数据权限
	 */
	public boolean isInvalid(String mappedStatementId) {
		return invalidCacheSet.contains(mappedStatementId);
	}
}
