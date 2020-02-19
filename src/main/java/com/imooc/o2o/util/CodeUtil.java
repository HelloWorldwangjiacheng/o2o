package com.imooc.o2o.util;

import javax.servlet.http.HttpServletRequest;

public class CodeUtil {
    public static boolean checkVerifyCode(HttpServletRequest request) {
        //实际的验证码
        String verifyCodeExpected = (String) request.getSession().getAttribute(
                com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        //输入的验证码
        String verifyCodeActual = HttpServletRequestUtil.getString(request,
                "verifyCodeActual");
        //判断
        if (verifyCodeActual == null
                || !verifyCodeActual.equalsIgnoreCase(verifyCodeExpected)) {
            return false;
        }
        return true;
    }
}
