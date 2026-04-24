package com.parking.servlet;

import com.parking.dao.UserDAO;
import com.parking.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

/** Handles new user registration. */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String name     = req.getParameter("name");
        String email    = req.getParameter("email");
        String password = req.getParameter("password");
        String phone    = req.getParameter("phone");

        // Basic input validation
        if (name == null || name.isBlank()
                || email == null || !email.contains("@")
                || password == null || password.length() < 4) {
            req.setAttribute("error", "Please fill all fields correctly. Password must be 4+ chars.");
            req.getRequestDispatcher("register.jsp").forward(req, resp);
            return;
        }

        try {
            UserDAO dao = new UserDAO();
            if (dao.emailExists(email)) {
                req.setAttribute("error", "Email already registered.");
                req.getRequestDispatcher("register.jsp").forward(req, resp);
                return;
            }
            User u = new User(name.trim(), email.trim(), password, phone, "USER");
            if (dao.register(u)) {
                resp.sendRedirect("login.jsp?registered=1");
            } else {
                req.setAttribute("error", "Registration failed. Try again.");
                req.getRequestDispatcher("register.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
