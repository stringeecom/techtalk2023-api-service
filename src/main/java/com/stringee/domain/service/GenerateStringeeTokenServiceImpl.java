package com.stringee.domain.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.stringee.domain.response.BaseResponse;
import com.stringee.domain.response.GenerateStringeeAccessTokenResponse;
import com.stringee.manager.AppManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dautv@stringee.com on 8/12/2023
 */
@Service
public class GenerateStringeeTokenServiceImpl extends BaseService implements GenerateStringeeTokenService {

    @Value("${api.stringee.sid_key}")
    private String sidKey;
    @Value("${api.stringee.secret_key}")
    private String secretKey;

    @Override
    public BaseResponse generateToken(String userId, Boolean restApi) {
        String token = genAccessToken(sidKey, secretKey, 3600, userId, restApi);
        GenerateStringeeAccessTokenResponse response = new GenerateStringeeAccessTokenResponse();
        response.setToken(token);
        response.setSuccess();
        AppManager.getInstance().setLastEmployee(userId);
        return response;
    }

    private String genAccessToken(String keySid, String keySecret, int expireInSecond, String userId, Boolean restApi) {

        try {


            Algorithm algorithmHS = Algorithm.HMAC256(keySecret);

            Map<String, Object> headerClaims = new HashMap<>();
            headerClaims.put("typ", "JWT");
            headerClaims.put("alg", "HS256");
            headerClaims.put("cty", "stringee-api;v=1");

            long exp = System.currentTimeMillis() + expireInSecond * 1000L;

            boolean rest = restApi != null && restApi;
            return JWT.create().withHeader(headerClaims)
                    .withClaim("jti", keySid + "-" + System.currentTimeMillis())
                    .withClaim("iss", keySid)
                    .withClaim("rest_api", rest) // ban dau la true
                    .withClaim("userId", userId)
                    .withExpiresAt(new Date(exp))
                    .sign(algorithmHS);

        } catch (Exception ex) {
            logger.error("Ex: ", ex);
        }

        return null;

    }

}
