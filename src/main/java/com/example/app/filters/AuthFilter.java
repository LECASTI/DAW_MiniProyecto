package com.example.app.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Set;

@WebFilter("/*")
public class AuthFilter implements Filter {

    private static final Set<String> PUBLIC_PATHS = Set.of(
            "/login", "/registro", "/css/", "/js/", "/img/"
    );

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // No se necesita configuración inicial
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String path = request.getRequestURI().substring(request.getContextPath().length());

        HttpSession session = request.getSession(false);
        Object usuario = (session != null) ? session.getAttribute("usuario") : null;

        System.out.println("[AuthFilter] URL: " + path);
        System.out.println("[AuthFilter] Usuario en sesión: " + (usuario != null ? usuario.toString() : "null"));


        boolean esRutaPublica = PUBLIC_PATHS.stream().anyMatch(path::startsWith);

        if (esRutaPublica || usuario != null) {
            chain.doFilter(req, res); // Permitir paso
        } else {
            System.out.println("[AuthFilter] Acceso denegado. Redirigiendo a /login");
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }

    @Override
    public void destroy() {
        // No se necesita limpieza
    }
}
