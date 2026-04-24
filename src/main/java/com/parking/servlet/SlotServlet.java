package com.parking.servlet;

import com.parking.dao.ParkingAreaDAO;
import com.parking.dao.ParkingSlotDAO;
import com.parking.dao.PricingDAO;
import com.parking.model.ParkingArea;
import com.parking.model.ParkingSlot;
import com.parking.model.Pricing;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Loads slot grid for a given area + date + start time + duration.
 * Renders slots.jsp.
 */
@WebServlet("/user/SlotServlet")
public class SlotServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String areaIdStr = req.getParameter("areaId");
        String date      = req.getParameter("date");
        String time      = req.getParameter("time");
        String hoursStr  = req.getParameter("hours");

        try {
            ParkingAreaDAO areaDao = new ParkingAreaDAO();
            req.setAttribute("areas", areaDao.getAll());

            if (areaIdStr == null) {
                req.getRequestDispatcher("/user/slots.jsp").forward(req, resp);
                return;
            }

            int areaId = Integer.parseInt(areaIdStr);
            int hours  = (hoursStr == null || hoursStr.isBlank()) ? 1 : Integer.parseInt(hoursStr);

            if (date == null || date.isBlank()) date = java.time.LocalDate.now().toString();
            if (time == null || time.isBlank()) time = "10:00";

            LocalDateTime startLdt = LocalDateTime.parse(date + "T" + time + ":00");
            LocalDateTime endLdt   = startLdt.plusHours(hours);
            Timestamp start = Timestamp.valueOf(startLdt);
            Timestamp end   = Timestamp.valueOf(endLdt);

            ParkingSlotDAO slotDao = new ParkingSlotDAO();
            List<ParkingSlot> slots = slotDao.getAvailability(areaId, start, end);

            ParkingArea area = areaDao.getById(areaId);
            PricingDAO pdao = new PricingDAO();
            Pricing p4w = pdao.get(areaId, "4W");
            Pricing p2w = pdao.get(areaId, "2W");

            req.setAttribute("selectedArea", area);
            req.setAttribute("slots", slots);
            req.setAttribute("date", date);
            req.setAttribute("time", time);
            req.setAttribute("hours", hours);
            req.setAttribute("p4w", p4w);
            req.setAttribute("p2w", p2w);
            req.getRequestDispatcher("/user/slots.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
