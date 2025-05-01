// src/main/java/com/example/app/filters/AuthFilter.java
package com.example.app.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@WebFilter("/*")
public class AuthFilter implements Filter {
    private static final Set<String> ALLOWED_PATHS = Set.of("/login", "/registro", "/css/", "/js/");

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String path = request.getRequestURI().substring(request.getContextPath().length());

        boolean allowedPath = ALLOWED_PATHS.stream().anyMatch(path::startsWith);

        if (allowedPath || request.getSession().getAttribute("usuario") != null) {
            chain.doFilter(req, res);
        } else {
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }
}