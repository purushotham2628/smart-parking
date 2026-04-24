package com.parking.servlet;

import com.parking.dao.*;
import com.parking.model.*;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@WebServlet("/user/BookingServlet")
public class BookingServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            HttpSession session = req.getSession(false);
            User user = (User) session.getAttribute("user");

            if (user == null) {
                resp.sendRedirect(req.getContextPath() + "/login.jsp");
                return;
            }

            int slotId = Integer.parseInt(req.getParameter("slotId"));
            String date = req.getParameter("date");
            String time = req.getParameter("time");
            int hours = Integer.parseInt(req.getParameter("hours"));

            // Fix datetime parsing
            String dateTime = date + "T" + time;
            if (time.length() == 5) dateTime += ":00";

            LocalDateTime start = LocalDateTime.parse(dateTime);
            LocalDateTime end = start.plusHours(hours);

            Timestamp startTs = Timestamp.valueOf(start);
            Timestamp endTs = Timestamp.valueOf(end);

            // Get slot
            ParkingSlotDAO sdao = new ParkingSlotDAO();
            ParkingSlot slot = sdao.getById(slotId);

            // Price
            PricingDAO pdao = new PricingDAO();
            Pricing pricing = pdao.get(slot.getAreaId(), slot.getVehicleType());
            BigDecimal price = pdao.calculatePrice(pricing, startTs, hours);

            // Booking
            BookingDAO bdao = new BookingDAO();
            Booking booking = bdao.createBooking(
                    user.getUserId(), slotId, startTs, endTs, hours, price
            );

            if (booking == null) {
                req.setAttribute("error", "Slot already booked!");
                req.getRequestDispatcher("/user/home.jsp").forward(req, resp);
                return;
            }

            // ✅ IMPORTANT FIX
            req.setAttribute("booking", booking);
            req.setAttribute("slot", slot);

            // ✅ Forward to booking.jsp (your current page)
            req.getRequestDispatcher("/user/booking.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println("ERROR: " + e.getMessage());
        }
    }
}