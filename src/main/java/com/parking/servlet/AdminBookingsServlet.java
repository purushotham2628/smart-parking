package com.parking.servlet;

import com.parking.dao.BookingDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

/** Admin: view all bookings. */
@WebServlet("/admin/AdminBookingsServlet")
public class AdminBookingsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setAttribute("bookings", new BookingDAO().getAll());
            req.getRequestDispatcher("/admin/bookings.jsp").forward(req, resp);
        } catch (Exception e) { throw new ServletException(e); }
    }
}
