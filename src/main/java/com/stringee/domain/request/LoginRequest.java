package com.stringee.domain.request;

import com.stringee.common.AppUtils;
import com.stringee.domain.response.BaseResponse;
import com.google.common.base.Strings;
import lombok.Data;

/**
 * @author dautv@stringee.com on 8/12/2023
 */
@Data
public class LoginRequest {
    private String username;
    private String password;

    public BaseResponse validate() {
        if (Strings.isNullOrEmpty(username) || !AppUtils.validateUsername(username)) {
            return new BaseResponse(-1, "Username is invalid");
        }
        if (Strings.isNullOrEmpty(username) || username.trim().length() < 4) {
            return new BaseResponse(-1, "Password is invalid");
        }
        return null;
    }

}
