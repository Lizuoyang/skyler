package com.skyler.cloud.skyler.common.core.exception.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 *
 * </p>
 *
 * @author lzy
 * @since 2023/10/31 15:37
 */
@Getter
@RequiredArgsConstructor
public enum  ErrorCodeEnum {
	// ========== 权限模块 1-002-001-000 ==========
	DATA_PERMISSION_EXCEPTION(1_002_001_000, "数据权限解析异常【{}】"),

	// ========== 角色模块 1-002-002-000 ==========
	ROLE_DATA_EXCEPTION(1_002_002_000, "角色数据范围异常【{}】"),
	ROLE_ADMIN_CODE_ERROR(1_002_002_001, "编码【{}】不能使用"),
	ROLE_NAME_DUPLICATE(1_002_002_002, "已经存在名为【{}】的角色"),
	ROLE_CODE_DUPLICATE(1_002_002_003, "已经存在编码为【{}】的角色"),

	// ========== 租户信息 1-002-015-000 ==========
	TENANT_NAME_DUPLICATE(1_002_015_001, "名字为【{}】的租户已存在"),
	TENANT_PACKAGE_NOT_EXISTS(1_002_015_002, "租户套餐不存在"),
	TENANT_PACKAGE_DISABLE(1_002_015_003, "名字为【{}】的租户套餐已被禁用"),
	TENANT_NOT_EXISTS(1_002_015_004, "租户不存在"),
	TENANT_CAN_NOT_UPDATE_SYSTEM(1_002_015_005, "系统租户不能进行修改、删除等操作！"),
	;
	/**
	 * 错误码
	 */
	private final Integer code;
	/**
	 * 错误提示
	 */
	private final String msg;
}
