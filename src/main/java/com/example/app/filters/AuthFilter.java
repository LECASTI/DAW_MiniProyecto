package com.example.app.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@WebFilter("/*")
public class AuthFilter implements Filter {
    private static final Set<String> ALLOWED_PATHS = Set.of("/login", "/registro", "/css/", "/js/", "/img/");

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String path = request.getRequestURI().substring(request.getContextPath().length());

        System.out.println("Filtro AuthFilter - URL: " + path); // Log

        boolean allowedPath = ALLOWED_PATHS.stream().anyMatch(path::startsWith);

        if (allowedPath || request.getSession().getAttribute("usuario") != null) {
            System.out.println("Filtro AuthFilter - Sesión válida o ruta permitida"); // Log
            chain.doFilter(req, res);
        } else {
            System.out.println("Filtro AuthFilter - Redirigiendo a login"); // Log
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }

    @Override
    public void destroy() {
    }
}