package com.parking.servlet;

import com.parking.dao.ParkingAreaDAO;
import com.parking.dao.PricingDAO;
import com.parking.model.Pricing;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Time;

/** Admin set pricing per area + vehicle type. */
@WebServlet("/admin/AdminPricingServlet")
public class AdminPricingServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setAttribute("areas", new ParkingAreaDAO().getAll());
            req.setAttribute("pricings", new PricingDAO().getAll());
            req.getRequestDispatcher("/admin/pricing.jsp").forward(req, resp);
        } catch (Exception e) { throw new ServletException(e); }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            Pricing p = new Pricing();
            p.setAreaId(Integer.parseInt(req.getParameter("areaId")));
            p.setVehicleType(req.getParameter("vehicleType"));
            p.setPricePerHour(new BigDecimal(req.getParameter("pricePerHour")));
            p.setPeakMultiplier(new BigDecimal(req.getParameter("peakMultiplier")));
            p.setPeakStart(Time.valueOf(req.getParameter("peakStart") + ":00"));
            p.setPeakEnd(Time.valueOf(req.getParameter("peakEnd") + ":00"));
            new PricingDAO().upsert(p);
            resp.sendRedirect(req.getContextPath() + "/admin/AdminPricingServlet");
        } catch (Exception e) { throw new ServletException(e); }
    }
}
