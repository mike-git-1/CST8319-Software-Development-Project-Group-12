package com.inventra.servlet;

// import com.inventra.database.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

@WebServlet(loadOnStartup = 1, urlPatterns = { "/init" })
public class InitServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        super.init();
        // DBConnection.createDatabase();
        System.out.println("[Inventra]: Database initialized via servlet startup.");
    }
}