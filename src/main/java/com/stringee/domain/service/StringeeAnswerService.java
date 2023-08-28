package com.stringee.domain.service;


import com.stringee.domain.entities.AnswerAction;
import com.stringee.domain.request.StringeeAnswerRequest;

import java.util.List;

/**
 * @author dautv@stringee.com on 8/12/2023
 */
public interface StringeeAnswerService {
    List<AnswerAction> answer(StringeeAnswerRequest request);
}
