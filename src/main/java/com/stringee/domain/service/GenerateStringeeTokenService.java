package com.stringee.domain.service;


import com.stringee.domain.response.BaseResponse;

/**
 * @author dautv@stringee.com on 8/12/2023
 */
public interface GenerateStringeeTokenService {
    BaseResponse generateToken(String userId, Boolean restApi);
}
