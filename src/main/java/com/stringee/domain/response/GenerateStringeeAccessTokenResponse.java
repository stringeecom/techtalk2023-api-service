package com.stringee.domain.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dautv@stringee.com on 8/12/2023
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GenerateStringeeAccessTokenResponse extends BaseResponse {
    private String token;
}
