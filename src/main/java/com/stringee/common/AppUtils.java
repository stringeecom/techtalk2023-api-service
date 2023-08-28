package com.stringee.common;

import com.google.common.base.Strings;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author dautv@stringee.com on 8/12/2023
 */
public class AppUtils {

    private static final String USERNAME_PATTERN = "^[A-Za-z0-9_]{4,15}$";

    public static int parseInt(Object o) {
        if (o == null) {
            return 0;
        }
        if (o instanceof Double) {
            return ((Double) o).intValue();
        }
        if (o instanceof Float) {
            return ((Float) o).intValue();
        }
        try {
            return Integer.parseInt(String.valueOf(o));
        } catch (Exception e) {
        }
        return 0;
    }

    public static double parseDouble(Object o) {
        if (o == null) {
            return 0;
        }
        try {
            return Double.parseDouble(String.valueOf(o));
        } catch (Exception e) {
        }
        return 0;
    }

    public static List<String> getHeadersValueInString(HttpServletRequest request) {

        List<String> rs = new ArrayList<>();

        Enumeration<String> h = request.getHeaderNames();
        if (h != null) {
            while (h.hasMoreElements()) {
                String s = h.nextElement();
                Enumeration<String> values = request.getHeaders(s);
                if (values != null) {
                    while (values.hasMoreElements()) {
                        String r = s + " : " + values.nextElement();
                        rs.add(r);
                    }
                }
            }
        }

        return rs;

    }

    public static boolean validatePhone(String phone) {
        String regex = "(09|03|05|08|07)[0-9]{8}";
        if (Strings.isNullOrEmpty(phone)) {
            return false;
        }
        return phone.matches(regex);
    }

    public static boolean validateUsername(final String username) {
        if (Strings.isNullOrEmpty(username)) {
            return false;
        }
        Pattern pattern = Pattern.compile(USERNAME_PATTERN);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }

    public static String parseString(Object obj) {
        if (obj == null) {
            return "";
        }
        try {
            return String.valueOf(obj);
        } catch (Exception ex) {
        }
        return "";
    }

    public static String hash(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-512");
            byte[] array = md.digest(md5.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : array) {
                sb.append(Integer.toHexString((b & 0xFF) | 0x100), 1, 3);
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException ignored) {
        }
        return null;
    }

}
