package com.stringee.domain.service;

import com.stringee.common.AppUtils;
import com.stringee.common.CollectionNameDefs;
import com.stringee.domain.db.MongoDbOnlineSyncActions;
import com.stringee.domain.request.LoginRequest;
import com.stringee.domain.request.RegisterRequest;
import com.stringee.domain.response.BaseResponse;
import com.stringee.domain.response.GenerateStringeeAccessTokenResponse;
import com.stringee.domain.response.LoginResponse;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

/**
 * @author dautv@stringee.com on 8/12/2023
 */
@Service
public class AuthServiceImpl extends BaseService implements AuthService {

    private final MongoDbOnlineSyncActions db;
    private final GenerateStringeeTokenService generateStringeeTokenService;

    public AuthServiceImpl(MongoDbOnlineSyncActions db, GenerateStringeeTokenService generateStringeeTokenService) {
        this.db = db;
        this.generateStringeeTokenService = generateStringeeTokenService;
    }

    @Override
    public BaseResponse login(LoginRequest request) {
        LoginResponse response = new LoginResponse();
        try {

            String userId = request.getUsername();
            Bson cond = Filters.eq("username", userId);
            Document user = db.findOne(CollectionNameDefs.COLL_USER, cond);
            if (user == null) {
                response.setFailed(-1, "User not existed");
                return response;
            }

            String originPassword = AppUtils.parseString(user.get("password"));
            String passReq = request.getPassword();
            passReq = AppUtils.hash(passReq);
            passReq = AppUtils.hash(passReq);

            if (!originPassword.equals(passReq)) {
                Bson u = Updates.combine(Updates.inc("wrong_pass_count", 1), Updates.set("last_wrong_password", System.currentTimeMillis()), Updates.set("update_at", System.currentTimeMillis()));
                db.update(CollectionNameDefs.COLL_USER, cond, u, true);
                response.setFailed(-1, "Password incorrect");
                return response;
            }

            int status = AppUtils.parseInt(user.get("status"));
            if (status == 0) {
                response.setFailed(-1, "Account blocked");
                return response;
            }

            GenerateStringeeAccessTokenResponse tokenResponse = (GenerateStringeeAccessTokenResponse) generateStringeeTokenService.generateToken(request.getUsername(), false);
            String token = tokenResponse.getToken();

            Bson updates = Updates.combine(Updates.set("wrong_pass_count", 0), Updates.set("last_login_time", System.currentTimeMillis()), Updates.set("update_at", System.currentTimeMillis()));

            db.update(CollectionNameDefs.COLL_USER, cond, updates, true);

            String fullName = AppUtils.parseString(user.get("full_name"));
            response.setToken(token);
            response.setFullName(fullName);
            response.setBalance(AppUtils.parseDouble(user.get("balance")));
            response.setSuccess();
            return response;

        } catch (Throwable ex) {
            logger.error("Ex: ", ex);
            response.setFailed(-1, "Login failed");
            return response;
        }
    }

    @Override
    public BaseResponse register(RegisterRequest request) {
        BaseResponse response = new BaseResponse();

        try {

            Bson cond = Filters.eq("username", request.getUsername());
            Document user = db.findOne(CollectionNameDefs.COLL_USER, cond);
            if (user != null) {
                response.setFailed(-1, "Account existed");
                return response;
            }

            String passReq = request.getPassword();
            passReq = AppUtils.hash(passReq);
            passReq = AppUtils.hash(passReq);

            ObjectId cursor = new ObjectId();
            Document doc = new Document();
            doc.append("username", request.getUsername());
            doc.append("password", passReq);
            doc.append("first_name", request.getUsername());
            doc.append("last_name", request.getUsername());
            doc.append("full_name", request.getUsername());
            doc.append("language", "en");
            doc.append("locate", "en-US");
            doc.append("balance", 0f);
            doc.append("status", 1);
            doc.append("last_login_time", 0);
            doc.append("wrong_pass_count", 0);
            doc.append("last_wrong_password", 0);
            doc.append("cursor", cursor);
            doc.append("create_at", System.currentTimeMillis());
            doc.append("update_at", System.currentTimeMillis());
            db.insertOne(CollectionNameDefs.COLL_USER, doc);
            response.setSuccess();
            return response;

        } catch (Throwable ex) {
            logger.error("Ex: ", ex);
            response.setFailed(-1, "Register failed");
            return response;
        }
    }
}
