package com.example.app.filters;

import com.example.app.models.Usuario;
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
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuario") : null;

        System.out.println("[AuthFilter] URL: " + path);
        System.out.println("[AuthFilter] Usuario en sesión: " + (usuario != null ? usuario.toString() : "null"));
        System.out.println("[AuthFilter] Rol del usuario: " + (usuario != null ? usuario.getRol() : "null"));

        // 1. Verificar si es ruta pública
        boolean esRutaPublica = PUBLIC_PATHS.stream().anyMatch(path::startsWith);
        if (esRutaPublica) {
            chain.doFilter(req, res);
            return;
        }

        // 2. Si no es ruta pública y no hay usuario, redirigir a login
        if (usuario == null) {
            System.out.println("[AuthFilter] Acceso denegado. Redirigiendo a /login");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // 3. Verificar rutas administrativas
        if (path.startsWith("/admin")) {
            if (!"admin".equals(usuario.getRol()) && !"superadmin".equals(usuario.getRol())) {
                System.out.println("[AuthFilter] Acceso denegado a ruta admin. Rol actual: " + usuario.getRol());
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }
        }

        // 4. Permitir acceso en otros casos
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {
        // No se necesita limpieza
    }
}