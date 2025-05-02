// src/main/java/com/example/app/servlets/ChatServlet.java
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

@WebServlet(name = "ChatServlet", urlPatterns = {"/chat"})
public class ChatServlet extends HttpServlet {

    private final ChatDAO chatDAO = new ChatDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final MensajeDAO mensajeDAO = new MensajeDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            System.out.println("[ChatServlet] Usuario no autenticado, redirigiendo a login");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");
        if ("eliminarChat".equals(action)) {
            eliminarChat(request, response, usuario);
        } else {
            try {
                mostrarChat(request, response, usuario);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            System.out.println("[ChatServlet] Usuario no autenticado, redirigiendo a login");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String contenido = request.getParameter("contenido");
        if (contenido != null && !contenido.trim().isEmpty()) {
            try {
                enviarMensaje(request, response, usuario, contenido);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                mostrarChat(request, response, usuario);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void mostrarChat(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws ServletException, IOException, SQLException {

        Chat chat = chatDAO.obtenerChatPorUsuario(usuario.getUsuarioId());
        List<Mensaje> mensajes = null;

        if (chat == null) {
            System.out.println("[ChatServlet] No se encontró chat para el usuario, creando uno nuevo");
            chat = crearChat(usuario);
        } else {
            System.out.println("[ChatServlet] Chat existente ID: " + chat.getChatId());
            mensajes = mensajeDAO.listarMensajesPorChat(chat.getChatId());
            for (Mensaje m : mensajes) {
                Usuario u = usuarioDAO.obtenerPorId(m.getUsuarioId());
                if (u != null) {
                    m.setUsuarioNombre(u.getUsername());
                    m.setUsuarioRol(u.getRol());
                } else {
                    m.setUsuarioNombre("Sistema");
                    m.setUsuarioRol("sistema");
                }
            }
            System.out.println("[ChatServlet] Mensajes encontrados en BD: " + mensajes.size());
        }

        request.setAttribute("chat", chat);
        request.setAttribute("mensajes", mensajes);
        request.setAttribute("usuarioId", usuario.getUsuarioId());
        request.setAttribute("currentUserId", usuario.getUsuarioId()); // ID del usuario actual
        request.getRequestDispatcher("/WEB-INF/vistas/chat.jsp").forward(request, response);
    }

    private Chat crearChat(Usuario usuario) {
        Chat nuevoChat = new Chat();
        nuevoChat.setUsuarioId(usuario.getUsuarioId());
        nuevoChat.setTitulo("Chat de soporte de " + usuario.getUsername());
        nuevoChat.setEstado("abierto");
        nuevoChat.setCreadoEn(LocalDateTime.now());
        chatDAO.insertar(nuevoChat);
        System.out.println("[INFO] Chat creado con ID: " + nuevoChat.getChatId());
        return nuevoChat;
    }

    private void enviarMensaje(HttpServletRequest request, HttpServletResponse response, Usuario usuario, String contenido)
            throws ServletException, IOException, SQLException {

        Chat chat = chatDAO.obtenerChatPorUsuario(usuario.getUsuarioId());
        if (chat == null) {
            chat = crearChat(usuario);
        }

        Mensaje nuevoMensaje = new Mensaje();
        nuevoMensaje.setChatId(chat.getChatId());
        nuevoMensaje.setUsuarioId(usuario.getUsuarioId());
        nuevoMensaje.setContenido(contenido);
        nuevoMensaje.setEnviadoEn(LocalDateTime.now());
        mensajeDAO.insertar(nuevoMensaje);
        System.out.println("[INFO] Mensaje enviado al chat " + chat.getChatId());

        mostrarChat(request, response, usuario);
    }

    private void eliminarChat(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws IOException {
        Chat chat = chatDAO.obtenerChatPorUsuario(usuario.getUsuarioId());
        if (chat != null) {
            chatDAO.eliminar(chat.getChatId());
            System.out.println("[INFO] Chat eliminado para el usuario " + usuario.getUsuarioId());
        } else {
            System.out.println("[WARNING] Intento de eliminar chat para el usuario " + usuario.getUsuarioId() + ", pero no existe");
        }
        response.sendRedirect(request.getContextPath() + "/chat");
    }
}