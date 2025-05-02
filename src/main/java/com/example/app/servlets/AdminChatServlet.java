// src/main/java/com/example/app/servlets/AdminChatServlet.java
package com.example.app.servlets;

import com.example.app.dao.ChatDAO;
import com.example.app.dao.MensajeDAO;
import com.example.app.dao.UsuarioDAO;
import com.example.app.models.Chat;
import com.example.app.models.Mensaje;
import com.example.app.models.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet(name = "AdminChatServlet", urlPatterns = {"/admin/chat"})
public class AdminChatServlet extends HttpServlet {

    private final ChatDAO chatDAO = new ChatDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final MensajeDAO mensajeDAO = new MensajeDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario admin = (Usuario) session.getAttribute("usuario");
        System.out.println("[AdminChatServlet] "+ (admin != null ? admin.getRol() : "null"));

        String usuarioIdParam = request.getParameter("usuarioId");
        if (usuarioIdParam == null || usuarioIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin");
            System.out.println("[AdminChatServlet] usuario invalido");
            return;
        }

        try {
            int usuarioId = Integer.parseInt(usuarioIdParam);
            try {
                mostrarChatAdmin(request, response, admin, usuarioId);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario admin = (Usuario) session.getAttribute("usuario");


        String usuarioIdParam = request.getParameter("usuarioId");
        String contenido = request.getParameter("contenido");

        if (usuarioIdParam == null || usuarioIdParam.isEmpty() ||
                contenido == null || contenido.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin");
            return;
        }

        try {
            int usuarioId = Integer.parseInt(usuarioIdParam);
            try {
                enviarMensajeAdmin(request, response, admin, usuarioId, contenido.trim());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin");
        }
    }

    private void mostrarChatAdmin(HttpServletRequest request, HttpServletResponse response,
                                  Usuario admin, int usuarioId)
            throws ServletException, IOException, SQLException {

        Chat chat = chatDAO.obtenerChatPorUsuario(usuarioId);
        List<Mensaje> mensajes = null;
        Usuario chatUsuario = usuarioDAO.obtenerPorId(usuarioId);

        if (chat == null) {
            // Si no existe chat, creamos uno vacío para el usuario
            chat = crearChat(chatUsuario != null ? chatUsuario : new Usuario(usuarioId));
        } else {
            mensajes = mensajeDAO.listarMensajesPorChat(chat.getChatId());
            for (Mensaje m : mensajes) {
                Usuario u = usuarioDAO.obtenerPorId(m.getUsuarioId());
                m.setUsuarioNombre(u != null ? u.getUsername() : "Sistema");
                m.setUsuarioRol(u != null ? u.getRol() : "Sistema");
            }
        }

        request.setAttribute("chat", chat);
        request.setAttribute("mensajes", mensajes);
        request.setAttribute("usuarioId", usuarioId); // ID del usuario del chat
        request.setAttribute("currentUserId", admin.getUsuarioId()); // ID del admin actual
        request.setAttribute("esAdmin", true); // Para que la JSP sepa que es vista de admin
        request.getRequestDispatcher("/WEB-INF/vistas/chat.jsp").forward(request, response);
    }

    private void enviarMensajeAdmin(HttpServletRequest request, HttpServletResponse response,
                                    Usuario admin, int usuarioId, String contenido)
            throws ServletException, IOException, SQLException {

        Chat chat = chatDAO.obtenerChatPorUsuario(usuarioId);
        Usuario chatUsuario = usuarioDAO.obtenerPorId(usuarioId);

        if (chat == null) {
            chat = crearChat(chatUsuario != null ? chatUsuario : new Usuario(usuarioId));
        }

        Mensaje nuevoMensaje = new Mensaje();
        nuevoMensaje.setChatId(chat.getChatId());
        nuevoMensaje.setUsuarioId(admin.getUsuarioId()); // El admin es quien envía
        nuevoMensaje.setContenido(contenido);
        nuevoMensaje.setEnviadoEn(LocalDateTime.now());
        mensajeDAO.insertar(nuevoMensaje);

        mostrarChatAdmin(request, response, admin, usuarioId);
    }

    private Chat crearChat(Usuario usuario) {
        Chat nuevoChat = new Chat();
        nuevoChat.setUsuarioId(usuario.getUsuarioId());
        nuevoChat.setTitulo("Chat de soporte de " + (usuario.getUsername() != null ? usuario.getUsername() : "Usuario " + usuario.getUsuarioId()));
        nuevoChat.setEstado("abierto");
        nuevoChat.setCreadoEn(LocalDateTime.now());
        chatDAO.insertar(nuevoChat);
        return nuevoChat;
    }
}