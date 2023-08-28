package com.stringee.controller;

import com.stringee.common.AppUtils;
import com.stringee.common.Const;
import com.stringee.domain.entities.AnswerAction;
import com.stringee.domain.request.StringeeAnswerRequest;
import com.stringee.domain.response.BaseResponse;
import com.stringee.domain.service.GenerateStringeeTokenService;
import com.stringee.domain.service.StringeeAnswerService;
import com.stringee.manager.AppManager;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author dautv@stringee.com on 8/12/2023
 */
@RestController
@RequestMapping("/video-call")
public class VideoCallController extends BaseController{

    private final StringeeAnswerService stringeeAnswerService;
    private final StringeeAnswerService stringeeAnswerExternalService;
    private final GenerateStringeeTokenService generateStringeeTokenService;

    private final Gson gson = new Gson();

    public VideoCallController(
            @Qualifier("stringeeAnswerServiceImpl") StringeeAnswerService stringeeAnswerService,
            @Qualifier("stringeeAnswerExternalService") StringeeAnswerService stringeeAnswerExternalService,
            GenerateStringeeTokenService generateStringeeTokenService) {
        this.stringeeAnswerService = stringeeAnswerService;
        this.stringeeAnswerExternalService = stringeeAnswerExternalService;
        this.generateStringeeTokenService = generateStringeeTokenService;
    }

    @GetMapping("/answer-external")
    public List<AnswerAction> answerExternal(HttpServletRequest request, @RequestHeader(value = "HTTP_X_STRINGEE_SIGNATURE", required = false) String signature, @RequestParam(value = "from", required = false) String from, @RequestParam(value = "to", required = false) String to, @RequestParam(value = "fromInternal", required = false) String fromInternal, @RequestParam(value = "userId", required = false) String userId, @RequestParam(value = "projectId", required = false) String projectId, @RequestParam(value = "uuid", required = false) String uuid, @RequestParam(value = "custom", required = false) String custom) {

        StringeeAnswerRequest req = StringeeAnswerRequest.builder().request(request).signature(signature).from(from).to(to).fromInternal(fromInternal).userId(userId).projectId(projectId).custom(custom).uuid(uuid).build();

        logger.info("=>answerExternal req: {}", gson.toJson(req));

        List<AnswerAction> response = stringeeAnswerExternalService.answer(req);

        logger.info("<=answerExternal req: {}, resp: {}", gson.toJson(req), gson.toJson(response));

        return response;

    }

    @GetMapping("/answer")
    public List<AnswerAction> answer(HttpServletRequest request, @RequestParam(value = "from", required = false) String from, @RequestParam(value = "to", required = false) String to, @RequestParam(value = "fromInternal", required = false) String fromInternal, @RequestParam(value = "userId", required = false) String userId, @RequestParam(value = "projectId", required = false) String projectId, @RequestParam(value = "custom", required = false) String custom) {

        StringeeAnswerRequest req = StringeeAnswerRequest.builder()
                .request(request)
                .from(from)
                .to(to)
                .fromInternal(fromInternal)
                .userId(userId)
                .projectId(projectId)
                .custom(custom)
                .build();

        logger.info("=>answer req: {}", gson.toJson(req));

        List<AnswerAction> response = stringeeAnswerService.answer(req);

        logger.info("<=answer req: {}, resp: {}", gson.toJson(req), gson.toJson(response));

        return response;

    }

    @PostMapping("/event")
    public String event(HttpServletRequest request, @RequestBody String payload) {
        String uri = request.getRequestURI();
        String query = request.getQueryString();
        if (!Strings.isNullOrEmpty(query)) {
            uri += "?" + query;
        }
        logger.info("=>event uri: {}", uri);
        logger.info("=>event payload: {}", payload);
        return uri;
    }

    @PostMapping("/event-external")
    public String eventExternal(HttpServletRequest request, @RequestBody String payload) {
        String uri = request.getRequestURI();
        String query = request.getQueryString();
        if (!Strings.isNullOrEmpty(query)) {
            uri += "?" + query;
        }
        logger.info("=>eventExternal uri: {}", uri);
        logger.info("=>eventExternal payload: {}", payload);
        return uri;
    }

    @GetMapping("/generate_access_token")
    public BaseResponse generateToken(@RequestParam(value = "userId", required = false) String userId, @RequestParam(value = "rest_api", required = false) Boolean restApi) {
        logger.info("generateToken userId: {}", userId);
        if (Strings.isNullOrEmpty(userId)) {
            return new BaseResponse(-1, "UserId Invalid");
        }
        return generateStringeeTokenService.generateToken(userId, restApi);
    }

    @GetMapping("/set-employee")
    public BaseResponse setLastEmployee(@RequestParam(value = "userId", required = false) String userId) {
        logger.info("setLastEmployee set userId: {}", userId);
        AppManager.getInstance().setLastEmployee(userId);
        return new BaseResponse(0, "OK");
    }

    @GetMapping("/set-phone-call-mode")
    public BaseResponse setPhoneToCallMode(@RequestParam(value = "mode", required = false) Integer mode) {
        logger.info("setPhoneToCallMode set phone call mode: {}", mode);
        String msg;
        if (mode != null && mode == Const.PHONE_TO_PHONE) {
            AppManager.getInstance().setMode(Const.PHONE_TO_PHONE);
            msg = "Phone call mode: Phone to phone";
        } else {
            AppManager.getInstance().setMode(Const.PHONE_TO_APP);
            msg = "Phone call mode: Phone to application";
        }
        return new BaseResponse(0, msg);
    }

    @GetMapping("/set-phone-call-number")
    public BaseResponse setPhoneToCallNumber(@RequestParam(value = "phone", required = false) String phone, @RequestParam(value = "alias", required = false) String alias) {
        logger.info("setPhoneToCallNumber set phone call number: {}, alias: {}", phone, alias);
        if (Strings.isNullOrEmpty(phone) || !AppUtils.validatePhone(phone)) {
            return new BaseResponse(0, "Số điện thoại không đúng định dạng");
        }
        phone = phone.substring(1);
        phone = "84" + phone;
        AppManager.getInstance().addPhone(phone);
        if (!Strings.isNullOrEmpty(alias)) {
            AppManager.getInstance().setFakeAlias(alias);
        }
        return new BaseResponse(0, "Set phone number success");
    }

    @GetMapping("/set-record")
    public BaseResponse setRecord(@RequestParam(value = "record", required = false) Boolean record) {
        logger.info("setRecord: {}", record);
        boolean f = record != null && record;
        AppManager.getInstance().setRecord(f);
        return new BaseResponse(0, "Set record to: " + f);
    }

    @GetMapping("/set-play")
    public BaseResponse setPlay(@RequestParam(value = "play") Boolean play, @RequestParam(value = "file", required = false) String file) {
        logger.info("setPlay: {}, file: {}", play, file);
        boolean f = play != null && play;
        if (f) {
            if (Strings.isNullOrEmpty(file)) {
                return new BaseResponse(-1, "Please select file name");
            }
        }
        AppManager.getInstance().setPlay(f);
        AppManager.getInstance().setFilePlay(file);
        return new BaseResponse(0, "Set play to: " + f + ", fileName: " + file);
    }
}
