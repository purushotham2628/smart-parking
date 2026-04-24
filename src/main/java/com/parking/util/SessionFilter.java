package com.parking.util;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;

import java.io.IOException;

/**
 * Forces login for protected URLs.
 * - /user/* requires a logged-in user.
 * - /admin/* requires role=ADMIN (except admin login page).
 */
@WebFilter(urlPatterns = {"/user/*", "/admin/*"})
public class SessionFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  request  = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);

        String uri = request.getRequestURI();
        String ctx = request.getContextPath();

        // Public admin login endpoints
        boolean isAdminLogin = uri.endsWith("/admin/login.jsp") || uri.endsWith("/AdminLoginServlet");

        Object userObj = session != null ? session.getAttribute("user") : null;
        Object roleObj = session != null ? session.getAttribute("role") : null;

        if (uri.contains("/admin/") && !isAdminLogin) {
            if (userObj == null || !"ADMIN".equals(roleObj)) {
                response.sendRedirect(ctx + "/admin/login.jsp");
                return;
            }
        } else if (uri.contains("/user/")) {
            if (userObj == null) {
                response.sendRedirect(ctx + "/login.jsp");
                return;
            }
        }
        chain.doFilter(req, res);
    }
}
