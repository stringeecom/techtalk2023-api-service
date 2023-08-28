package com.stringee.manager;

import com.stringee.common.Const;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dautv@stringee.com on 8/12/2023
 */
@Data
public class AppManager {

    private static AppManager app;

    private AppManager() {
        this.phones = new ArrayList<>();
    }

    public static AppManager getInstance() {
        if (app == null) {
            app = new AppManager();
        }
        return app;
    }

    private String lastEmployee = "dautv";
    private int mode = Const.PHONE_TO_APP;
    private final List<String> phones;

    private String lastPccEmployee;

    private String fakeAlias;

    private boolean record;

    private boolean play;
    private String filePlay;

    public void addPhone(String phone) {
        phones.clear();
        phones.add(phone);
    }

}
