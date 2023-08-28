package com.stringee.domain.service;

import com.stringee.common.AnswerActionType;
import com.stringee.common.AppUtils;
import com.stringee.common.CallSourceType;
import com.stringee.common.Const;
import com.stringee.domain.entities.Address;
import com.stringee.domain.entities.AnswerAction;
import com.stringee.domain.request.StringeeAnswerRequest;
import com.stringee.manager.AppManager;
import com.google.common.base.Strings;
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
public class StringeeAnswerExternalService extends BaseService implements StringeeAnswerService {

    @Value("${app.record_call_back}")
    private String recordCallbackUrl;

    @Override
    public List<AnswerAction> answer(StringeeAnswerRequest request) {

        String uri = request.getRequest().getRequestURI();
        String query = request.getRequest().getQueryString();

        if (!Strings.isNullOrEmpty(query)) {
            uri += "?" + query;
        }

        logger.info("uri: " + uri);

        List<String> headers = AppUtils.getHeadersValueInString(request.getRequest());
        logger.info("headers: {}", new Gson().toJson(headers));

        String from = request.getFrom();
        String userId = request.getUserId();
        String callTo = null;

        int mode = AppManager.getInstance().getMode();
        String callToType;

        if (mode == Const.PHONE_TO_APP) {

            callToType = CallSourceType.INTERNAL;
            if (!Strings.isNullOrEmpty(userId)) {
                callTo = userId;
            } else {
                callTo = AppManager.getInstance().getLastEmployee();
            }

        } else {
            callToType = CallSourceType.EXTERNAL;
            List<String> phones = AppManager.getInstance().getPhones();
            if (!phones.isEmpty()) {
                callTo = phones.get(0);
            }
        }

        String callId = "dautv|" + UUID.randomUUID();
        String alias = request.getFrom();

        if (!Strings.isNullOrEmpty(AppManager.getInstance().getFakeAlias())) {
            alias = AppManager.getInstance().getFakeAlias();
        }

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
                        .type(CallSourceType.EXTERNAL)
                        .number(from)
                        .alias(alias)
                        .build())
                .to(Address.builder()
                        .type(callToType)
                        .number(callTo)
                        .alias(request.getTo())
                        .build())
                .customData(callId)
                .timeout(45)
                .build();

        actions.add(connect);
        return actions;

    }

}