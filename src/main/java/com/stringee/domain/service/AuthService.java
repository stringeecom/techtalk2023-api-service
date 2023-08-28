package com.stringee.domain.service;

import com.stringee.domain.request.LoginRequest;
import com.stringee.domain.request.RegisterRequest;
import com.stringee.domain.response.BaseResponse;

/**
 * @author dautv@stringee.com on 8/12/2023
 */
public interface AuthService {
    BaseResponse login(LoginRequest request);

    BaseResponse register(RegisterRequest request);
}
