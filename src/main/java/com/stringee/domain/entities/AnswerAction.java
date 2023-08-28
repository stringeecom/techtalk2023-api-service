package com.stringee.domain.entities;

import lombok.Builder;
import lombok.Data;

/**
 * @author dautv@stringee.com on 8/12/2023
 */
@Data
@Builder
public class AnswerAction {
    private String action;
    private Address from;
    private Address to;
    private String customData;
    private int timeout;
    private int maxConnectTime;
    private boolean peerToPeerCall;

    private String format;
    private String eventUrl;
    private String fileName;
    private Boolean bargeIn;
    private Integer loop;
}
