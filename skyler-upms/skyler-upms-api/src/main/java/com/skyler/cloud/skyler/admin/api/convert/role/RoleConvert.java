package com.skyler.cloud.skyler.admin.api.convert.role;

import com.skyler.cloud.skyler.admin.api.dto.role.RoleCreateDTO;
import com.skyler.cloud.skyler.admin.api.entity.SysRole;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * <p>
 * 角色 实体转换工具
 * </p>
 *
 * @author lzy
 * @since 2023/11/1 11:23
 */
@Mapper
public interface RoleConvert {
	RoleConvert INSTANCE = Mappers.getMapper(RoleConvert.class);

	SysRole convert(RoleCreateDTO createDTO);
}
