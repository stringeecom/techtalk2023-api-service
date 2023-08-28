package com.stringee.domain.service;

import com.stringee.common.AnswerActionType;
import com.stringee.common.AppUtils;
import com.stringee.common.CallSourceType;
import com.stringee.domain.entities.Address;
import com.stringee.domain.entities.AnswerAction;
import com.stringee.domain.request.StringeeAnswerRequest;
import com.stringee.manager.AppManager;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author dautv@stringee.com on 8/12/2023
 */
@Service
public class StringeeAnswerServiceImpl extends BaseService implements StringeeAnswerService {

    @Value("${app.record_call_back}")
    private String recordCallbackUrl;

    @Override
    public List<AnswerAction> answer(StringeeAnswerRequest request) {

        String uri = request.getRequest().getQueryString();
        logger.info("uri: " + uri);

        List<String> headers = AppUtils.getHeadersValueInString(request.getRequest());
        logger.info("headers: {}", new Gson().toJson(headers));

        String to = request.getTo();
        String from = request.getFrom();

        int toInt = 0;
        String toType;
        if (to != null) {
            String tmpTo = to;
            if (tmpTo.startsWith("84") && tmpTo.length() == 11) {
                tmpTo = tmpTo.substring(2);
            }
            if (tmpTo.startsWith("+84") && tmpTo.length() == 12) {
                tmpTo = tmpTo.substring(3);
            }
            toInt = AppUtils.parseInt(tmpTo);
        }

        if (toInt < 60000) {
            toType = CallSourceType.INTERNAL;
        } else {
            toType = CallSourceType.EXTERNAL;
        }

        String callId = "dautv|" + UUID.randomUUID();

        List<AnswerAction> actions = new ArrayList<>();

        // for record
        if (AppManager.getInstance().isRecord()) {
            AnswerAction record = AnswerAction.builder()
                    .action(AnswerActionType.RECORD)
                    .format("mp3")
                    .eventUrl(recordCallbackUrl)
                    .build();
            actions.add(record);
        }

        // for play
        if (AppManager.getInstance().isPlay()) {
            AnswerAction play = AnswerAction.builder()
                    .action(AnswerActionType.PLAY)
                    .fileName(AppManager.getInstance().getFilePlay())
                    .bargeIn(true)
                    .loop(1)
                    .build();
            actions.add(play);
        }

        // for connect
        AnswerAction connect = AnswerAction.builder()
                .action(AnswerActionType.CONNECT)
                .from(Address.builder()
                        .type(CallSourceType.INTERNAL)
                        .number(from)
                        .alias(from)
                        .build())
                .to(Address.builder()
                        .type(toType)
                        .number(to)
                        .alias(to)
                        .build())
                .customData(callId)
                .timeout(45)
                .maxConnectTime(-1)
                .peerToPeerCall(false)
                .build();

        actions.add(connect);
        return actions;

    }

}