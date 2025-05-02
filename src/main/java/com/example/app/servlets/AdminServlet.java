package com.example.app.servlets;

import com.example.app.dao.UsuarioDAO;
import com.example.app.models.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "AdminServlet", urlPatterns = {"/admin"})
public class AdminServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("\n====== [AdminServlet] INICIO doGet ======");

        HttpSession session = request.getSession(false);
        System.out.println("[Debug] Sesión obtenida: " + (session != null ? session.getId() : "null"));

        Usuario actual = (session != null) ? (Usuario) session.getAttribute("usuario") : null;
        System.out.println("[Debug] Usuario en sesión: " + (actual != null
                ? actual.getUsername() + " (Rol: " + actual.getRol() + ")"
                : "null"));

        // Validación de acceso
        if (actual == null) {
            System.out.println("[Debug] Redirigiendo a login - No hay usuario en sesión");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if (!"admin".equals(actual.getRol()) && !"superadmin".equals(actual.getRol())) {
            System.out.println("[Debug] Redirigiendo a login - Rol no autorizado: " + actual.getRol());
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            System.out.println("[Debug] Obteniendo lista de usuarios...");
            List<Usuario> usuarios = usuarioDAO.obtenerTodos();
            System.out.println("[Debug] Usuarios obtenidos: " + usuarios.size());

            request.setAttribute("usuarios", usuarios);
            request.setAttribute("rolActual", actual.getRol());

            System.out.println("[Debug] Haciendo forward a admin.jsp");
            request.getRequestDispatcher("/WEB-INF/vistas/admin.jsp").forward(request, response);

        } catch (SQLException e) {
            System.err.println("[ERROR] SQLException en doGet: " + e.getMessage());
            request.setAttribute("error", "Error al cargar los usuarios.");
            request.getRequestDispatcher("/WEB-INF/vistas/admin.jsp").forward(request, response);
        }

        System.out.println("====== [AdminServlet] FIN doGet ======\n");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("\n====== [AdminServlet] INICIO doPost ======");

        HttpSession session = request.getSession(false);
        Usuario actual = (session != null) ? (Usuario) session.getAttribute("usuario") : null;

        System.out.println("[Debug] Acción POST recibida de: " +
                (actual != null ? actual.getUsername() : "null"));

        // Validación de acceso
        if (actual == null) {
            System.out.println("[Debug] Redirigiendo a login - Sesión no encontrada");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if (!"admin".equals(actual.getRol()) && !"superadmin".equals(actual.getRol())) {
            System.out.println("[Debug] Acceso denegado. Rol actual: " + actual.getRol());
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            int usuarioId = Integer.parseInt(request.getParameter("usuarioId"));
            String nuevoRol = request.getParameter("nuevoRol");

            System.out.println("[Debug] Intentando cambiar rol. UsuarioID: " + usuarioId +
                    ", Nuevo rol: " + nuevoRol);

            Usuario objetivo = usuarioDAO.obtenerPorId(usuarioId);

            if (objetivo == null) {
                System.out.println("[Debug] Usuario no encontrado con ID: " + usuarioId);
                request.setAttribute("error", "Usuario no encontrado.");
            }
            else if ("superadmin".equals(nuevoRol) && !"superadmin".equals(actual.getRol())) {
                System.out.println("[Debug] Intento no autorizado de crear superadmin");
                request.setAttribute("error", "Solo un superadmin puede ascender a otro a superadmin.");
            }
            else if ("superadmin".equals(objetivo.getRol()) && !"superadmin".equals(actual.getRol())) {
                System.out.println("[Debug] Intento no autorizado de modificar superadmin");
                request.setAttribute("error", "No puedes modificar a un superadmin.");
            }
            else {
                usuarioDAO.actualizarRol(usuarioId, nuevoRol);
                System.out.println("[Debug] Rol actualizado exitosamente");
                request.setAttribute("mensaje", "Rol actualizado exitosamente.");
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] SQLException en doPost: " + e.getMessage());
            request.setAttribute("error", "Error al actualizar el rol.");
        } catch (NumberFormatException e) {
            System.err.println("[ERROR] NumberFormatException: " + e.getMessage());
            request.setAttribute("error", "ID de usuario inválido.");
        }

        System.out.println("====== [AdminServlet] FIN doPost ======\n");
        doGet(request, response); // Recarga la vista
    }
}