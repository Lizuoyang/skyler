package com.skyler.cloud.skyler.admin.api.dto.role;

import com.skyler.cloud.skyler.admin.api.entity.SysRole;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 角色创建传输对象
 * </p>
 *
 * @author lzy
 * @since 2023/11/1 11:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleCreateDTO extends SysRole {

}
