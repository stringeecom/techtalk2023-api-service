package com.stringee.manager;

import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author dautv@stringee.com on 8/12/2023
 */
public class RequestManager {
    public static Map<String, JSONObject> requests = new ConcurrentHashMap<>();
}
