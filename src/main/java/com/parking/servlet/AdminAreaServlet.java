package com.parking.servlet;

import com.parking.dao.ParkingAreaDAO;
import com.parking.model.ParkingArea;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

/** Admin CRUD for parking areas. */
@WebServlet("/admin/AdminAreaServlet")
public class AdminAreaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setAttribute("areas", new ParkingAreaDAO().getAll());
            req.getRequestDispatcher("/admin/areas.jsp").forward(req, resp);
        } catch (Exception e) { throw new ServletException(e); }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            ParkingAreaDAO dao = new ParkingAreaDAO();
            if ("add".equals(action)) {
                ParkingArea a = new ParkingArea(
                        req.getParameter("name"),
                        req.getParameter("location"),
                        req.getParameter("description"));
                dao.add(a);
            } else if ("update".equals(action)) {
                ParkingArea a = new ParkingArea(
                        req.getParameter("name"),
                        req.getParameter("location"),
                        req.getParameter("description"));
                a.setAreaId(Integer.parseInt(req.getParameter("areaId")));
                dao.update(a);
            } else if ("delete".equals(action)) {
                dao.delete(Integer.parseInt(req.getParameter("areaId")));
            }
            resp.sendRedirect(req.getContextPath() + "/admin/AdminAreaServlet");
        } catch (Exception e) { throw new ServletException(e); }
    }
}
