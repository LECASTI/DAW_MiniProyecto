// src/main/java/com/example/app/servlets/LoginServlet.java
package com.example.app.servlets;


import com.example.app.dao.UsuarioDAO;
import com.example.app.models.Usuario;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession; // Importante para las sesiones
import java.io.IOException;
import java.sql.SQLException;


@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {


    // Método para mostrar el formulario (GET)
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/vistas/login.jsp").forward(request, response);
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        String username = request.getParameter("username");
        String password = request.getParameter("password");


        System.out.println("Intento de login con usuario: " + username); // Debug


        try {
            Usuario usuario = new UsuarioDAO().autenticar(username, password);


            if (usuario != null) {
                System.out.println("[LoginServlet] Autenticación exitosa para: " + username); // Debug


                // Obtener la sesión
                HttpSession session = request.getSession();


                // Guardar el objeto Usuario en la sesión
                session.setAttribute("usuario", usuario);


                // Guardar el rol del usuario (útil para la autorización)
                session.setAttribute("usuarioRol", usuario.getRol());


                System.out.println("[LoginServlet] Usuario " + username + " guardado en sesión con ID: " + session.getId()); // Debug


                // Redirigir según el rol
                String redirectPath = request.getContextPath();
                if ("usuario".equals(usuario.getRol())) {
                    System.out.println("[LoginServlet] usuario fue redirigido a /chat");
                    redirectPath += "/chat";
                } else if ("admin".equals(usuario.getRol()) || "superadmin".equals(usuario.getRol())) {
                    System.out.println("[LoginServlet] usuario fue redirigido a /admin");
                    redirectPath += "/admin";
                } else {
                    // Rol desconocido (esto no debería ocurrir, pero es bueno tener un caso por defecto)
                    redirectPath += "/chat"; // O podrías redirigir a una página de error
                }


                response.sendRedirect(redirectPath);
                return;
            } else {
                System.out.println("[LoginServlet] Credenciales inválidas para: " + username); // Debug
                request.setAttribute("error", "Usuario o contraseña incorrectos");
            }
        } catch (SQLException e) {
            System.err.println("[LoginServlet] Error en BD durante login:");
            e.printStackTrace();
            request.setAttribute("error", "Error del sistema");
        }


        request.getRequestDispatcher("/WEB-INF/vistas/login.jsp").forward(request, response);
    }
}