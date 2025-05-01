// src/main/java/com/example/app/servlets/RegistroServlet.java
package com.example.app.servlets;


import com.example.app.dao.UsuarioDAO;
import com.example.app.models.Usuario;
import org.mindrot.jbcrypt.BCrypt;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;


@WebServlet(name = "RegistroServlet", urlPatterns = {"/registro"})
public class RegistroServlet extends HttpServlet {


    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("[RegistroServlet] GET request recibido en /registro"); // Debug
        request.getRequestDispatcher("/WEB-INF/vistas/registro.jsp").forward(request, response);
        System.out.println("[RegistroServlet] Forwarded a registro.jsp"); // Debug
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("[RegistroServlet] POST request recibido en /registro"); // Debug

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        System.out.println("[RegistroServlet] Intento de registro con usuario: " + username); // Debug

        // Validaciones
        if (!password.equals(confirmPassword)) {
            System.out.println("[RegistroServlet] Error de registro para " + username + ": Las contraseñas no coinciden"); // Debug
            request.setAttribute("error", "Las contraseñas no coinciden");
            request.getRequestDispatcher("/WEB-INF/vistas/registro.jsp").forward(request, response);
            System.out.println("[RegistroServlet] Forwarded de vuelta a registro.jsp por error de contraseña"); // Debug
            return;
        }

        // Hash de la contraseña
        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
        System.out.println("[RegistroServlet] Contraseña hasheada para " + username + ": " + passwordHash); // Debug

        // Crear el objeto Usuario
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername(username);
        nuevoUsuario.setPasswordHash(passwordHash);
        nuevoUsuario.setRol("usuario"); // Rol por defecto: usuario
        nuevoUsuario.setCreadoEn(LocalDateTime.now());

        try {
            // Guardar el usuario en la base de datos
            new UsuarioDAO().insertar(nuevoUsuario);
            System.out.println("[RegistroServlet] Usuario " + username + " registrado exitosamente en la base de datos"); // Debug

            // Redirigir al login con un mensaje de éxito
            request.setAttribute("mensaje", "Registro exitoso. Ahora puedes iniciar sesión.");
            request.getRequestDispatcher("/WEB-INF/vistas/login.jsp").forward(request, response);
            System.out.println("[RegistroServlet] Forwarded a login.jsp con mensaje de éxito"); // Debug
        } catch (SQLException e) {
            System.err.println("[RegistroServlet] Error al registrar el usuario:");
            e.printStackTrace();
            request.setAttribute("error", "Error al registrar el usuario. Inténtalo de nuevo.");
            request.getRequestDispatcher("/WEB-INF/vistas/registro.jsp").forward(request, response);
            System.out.println("[RegistroServlet] Forwarded de vuelta a registro.jsp por error de SQL"); // Debug
        }
    }
}