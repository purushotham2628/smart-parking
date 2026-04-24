package com.parking.servlet;

import com.parking.dao.ParkingAreaDAO;
import com.parking.dao.ParkingSlotDAO;
import com.parking.model.ParkingSlot;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

/** Admin add/list/delete parking slots. */
@WebServlet("/admin/AdminSlotServlet")
public class AdminSlotServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            ParkingAreaDAO adao = new ParkingAreaDAO();
            ParkingSlotDAO sdao = new ParkingSlotDAO();
            req.setAttribute("areas", adao.getAll());

            String areaIdStr = req.getParameter("areaId");
            if (areaIdStr != null && !areaIdStr.isBlank()) {
                int areaId = Integer.parseInt(areaIdStr);
                req.setAttribute("slots", sdao.getByArea(areaId));
                req.setAttribute("selectedAreaId", areaId);
            }
            req.getRequestDispatcher("/admin/slots.jsp").forward(req, resp);
        } catch (Exception e) { throw new ServletException(e); }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            ParkingSlotDAO dao = new ParkingSlotDAO();
            String areaIdStr = req.getParameter("areaId");
            if ("add".equals(action)) {
                ParkingSlot s = new ParkingSlot();
                s.setAreaId(Integer.parseInt(areaIdStr));
                s.setSlotNumber(req.getParameter("slotNumber"));
                s.setVehicleType(req.getParameter("vehicleType"));
                s.setActive(true);
                dao.add(s);
            } else if ("delete".equals(action)) {
                dao.delete(Integer.parseInt(req.getParameter("slotId")));
            }
            resp.sendRedirect(req.getContextPath() + "/admin/AdminSlotServlet?areaId=" + areaIdStr);
        } catch (Exception e) { throw new ServletException(e); }
    }
}
