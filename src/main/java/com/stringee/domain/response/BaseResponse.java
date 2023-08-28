package com.stringee.domain.response;

import lombok.Data;

/**
 * @author dautv@stringee.com on 8/12/2023
 */
@Data
public class BaseResponse {

    protected int rc;
    protected String rd;

    public BaseResponse() {

    }

    public BaseResponse(int rc, String message) {
        this.rc = rc;
        this.rd = message;
    }

    public void setFailed(int rc, String message) {
        this.rc = rc;
        this.rd = message;
    }

    public void setSuccess() {
        this.rc = 0;
        this.rd = "OK";
    }

}
