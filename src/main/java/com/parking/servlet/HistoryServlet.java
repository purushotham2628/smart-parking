package com.parking.servlet;

import com.parking.dao.BookingDAO;
import com.parking.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

/** Loads the user's booking history and forwards to history.jsp. */
@WebServlet("/user/HistoryServlet")
public class HistoryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session == null) ? null : (User) session.getAttribute("user");
        if (user == null) { resp.sendRedirect(req.getContextPath() + "/login.jsp"); return; }
        try {
            req.setAttribute("bookings", new BookingDAO().getByUser(user.getUserId()));
            req.getRequestDispatcher("/user/history.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
