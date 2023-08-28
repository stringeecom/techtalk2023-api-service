package com.stringee.controller;

import com.stringee.domain.request.LoginRequest;
import com.stringee.domain.request.RegisterRequest;
import com.stringee.domain.response.BaseResponse;
import com.stringee.domain.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dautv@stringee.com on 8/12/2023
 */
@RestController
@RequestMapping("/auth")
public class AuthController extends BaseController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public BaseResponse login(@RequestBody LoginRequest request) {
        logger.info("=>login req: {}", request);
        BaseResponse response = request.validate();
        if (response == null) {
            response = authService.login(request);
        }
        logger.info("<=login req: {}, resp: {}", request, response);
        return response;
    }

    @PostMapping("/register")
    public BaseResponse register(@RequestBody RegisterRequest request) {
        logger.info("=>register req: {}", request);
        BaseResponse response = request.validate();
        if (response == null) {
            response = authService.register(request);
        }
        logger.info("<=register req: {}, resp: {}", request, response);
        return response;
    }

}
