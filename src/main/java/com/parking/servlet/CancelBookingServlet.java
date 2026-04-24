package com.parking.servlet;

import com.parking.dao.BookingDAO;
import com.parking.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

/** Cancels a future, active booking owned by the logged-in user. */
@WebServlet("/user/CancelBookingServlet")
public class CancelBookingServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session == null) ? null : (User) session.getAttribute("user");
        if (user == null) { resp.sendRedirect(req.getContextPath() + "/login.jsp"); return; }

        String bookingId = req.getParameter("bookingId");
        try {
            new BookingDAO().cancel(bookingId, user.getUserId());
            resp.sendRedirect(req.getContextPath() + "/user/HistoryServlet");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
