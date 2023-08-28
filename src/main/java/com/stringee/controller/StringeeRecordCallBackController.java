package com.stringee.controller;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author dautv@stringee.com on 8/12/2023
 */
@RestController
@RequestMapping("/record_call_back")
public class StringeeRecordCallBackController extends BaseController {

    @PostMapping("")
    public String callBackPost(@RequestBody String payload) {
        logger.info("callBackPost: {}", payload);
        return "OK";
    }

    @GetMapping("")
    public String callBackGet(HttpServletRequest request) {
        String query = request.getQueryString();
        logger.info("callBackGet: {}", query);
        return "OK";
    }

}
