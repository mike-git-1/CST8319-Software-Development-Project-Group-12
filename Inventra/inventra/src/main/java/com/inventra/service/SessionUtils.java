package com.inventra.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class SessionUtils {

    public static boolean isUserLoggedIn(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session == null) {
            return false;
        }
        return session.getAttribute("userId") != null
                && session.getAttribute("companyId") != null;
    }
}
