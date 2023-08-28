package com.stringee.domain.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import lombok.Builder;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;

/**
 * @author dautv@stringee.com on 8/12/2023
 */
@Data
@Builder
public class StringeeAnswerRequest {
    @Expose
    @JsonIgnore
    private transient HttpServletRequest request;
    private String signature;
    private String from;
    private String to;
    private String fromInternal;
    private String userId;
    private String projectId;
    private String custom;
    private String uuid;
}
