package com.skyler.cloud.skyler.admin.api.dto.tenant;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * <p>
 * 租户传输对象
 * </p>
 *
 * @author lzy
 * @since 2023/10/31 15:29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TenantCreateDTO extends TenantBaseDTO {
	@NotBlank(message = "用户账号不能为空")
	@Pattern(regexp = "^[a-zA-Z0-9]{4,30}$", message = "用户账号由 数字、字母 组成")
	@Size(min = 4, max = 30, message = "用户账号长度为 4-30 个字符")
	private String userName;

	@NotEmpty(message = "密码不能为空")
	@Length(min = 4, max = 16, message = "密码长度为 4-16 位")
	private String userPwd;
}
