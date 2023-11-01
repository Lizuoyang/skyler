/*
 * Copyright (c) 2020 skyler4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.skyler.cloud.skyler.common.core.exception;

import com.skyler.cloud.skyler.common.core.exception.enums.ErrorCodeEnum;

/**
 * @author lengleng
 * @date 2018年06月22日16:22:03 403
 */
public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * 业务错误码
	 *
	 */
	private Integer code;
	/**
	 * 错误提示
	 */
	private String message;

	/**
	 * 空构造方法，避免反序列化问题
	 */
	public ServiceException() {
	}

	public ServiceException(ErrorCodeEnum errorCode) {
		this.code = errorCode.getCode();
		this.message = errorCode.getMsg();
	}

	public ServiceException(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	public Integer getCode() {
		return code;
	}

	public ServiceException setCode(Integer code) {
		this.code = code;
		return this;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public ServiceException setMessage(String message) {
		this.message = message;
		return this;
	}

}
