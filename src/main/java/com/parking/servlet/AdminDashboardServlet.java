package com.parking.servlet;

import com.parking.dao.BookingDAO;
import com.parking.dao.ParkingAreaDAO;
import com.parking.dao.ParkingSlotDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

/** Builds dashboard stats for admin. */
@WebServlet("/admin/AdminDashboardServlet")
public class AdminDashboardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            BookingDAO bdao = new BookingDAO();
            req.setAttribute("totalBookings", bdao.countAll());
            req.setAttribute("totalRevenue", bdao.totalRevenue());
            req.setAttribute("totalAreas", new ParkingAreaDAO().getAll().size());
            req.getRequestDispatcher("/admin/dashboard.jsp").forward(req, resp);
        } catch (Exception e) { throw new ServletException(e); }
    }
}
