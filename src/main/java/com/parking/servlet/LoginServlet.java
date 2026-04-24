package com.parking.servlet;

import com.parking.dao.UserDAO;
import com.parking.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

/** Authenticates a regular user and creates a session. */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String email = req.getParameter("email");
        String pass  = req.getParameter("password");

        if (email == null || pass == null) {
            req.setAttribute("error", "Email and password are required.");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
            return;
        }

        try {
            User u = new UserDAO().login(email.trim(), pass);
            if (u == null) {
                req.setAttribute("error", "Invalid email or password.");
                req.getRequestDispatcher("login.jsp").forward(req, resp);
                return;
            }
            HttpSession session = req.getSession(true);
            session.setAttribute("user", u);
            session.setAttribute("role", u.getRole());

            if ("ADMIN".equals(u.getRole())) {
                resp.sendRedirect("admin/dashboard.jsp");
            } else {
                resp.sendRedirect("user/home.jsp");
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
