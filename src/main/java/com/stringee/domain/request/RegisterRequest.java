package com.stringee.domain.request;

import com.stringee.common.AppUtils;
import com.stringee.domain.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Strings;
import lombok.Data;

/**
 * @author dautv@stringee.com on 8/12/2023
 */
@Data
public class RegisterRequest {
    private String username;
    private String password;
    @JsonProperty("re_password")
    private String rePassword;

    public BaseResponse validate() {
        if (Strings.isNullOrEmpty(username) || !AppUtils.validateUsername(username)) {
            return new BaseResponse(-1, "Username is invalid");
        }
        if (Strings.isNullOrEmpty(username) || username.trim().length() < 4) {
            return new BaseResponse(-1, "Password is invalid");
        }
        if (Strings.isNullOrEmpty(rePassword) || rePassword.trim().length() < 4) {
            return new BaseResponse(-1, "Re Password is invalid");
        }
        if (!rePassword.equals(password)) {
            return new BaseResponse(-1, "Re Password is incorrect");
        }
        return null;
    }

}
