package com.stringee.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dautv@stringee.com on 8/12/2023
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LoginResponse extends BaseResponse {
    private String token;
    private String name;
    private double balance;
    @JsonProperty("full_name")
    private String fullName;
}
