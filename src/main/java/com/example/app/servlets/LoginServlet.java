// src/main/java/com/example/app/servlets/LoginServlet.java
package com.example.app.servlets;

import com.example.app.dao.UsuarioDAO;
import com.example.app.models.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
                System.out.println("Autenticación exitosa para: " + username); // Debug
                request.getSession().setAttribute("usuario", usuario);
                response.sendRedirect("dashboard"); // Cambia esto a tu página de destino
                return;
            } else {
                System.out.println("Credenciales inválidas para: " + username); // Debug
                request.setAttribute("error", "Usuario o contraseña incorrectos");
            }
        } catch (SQLException e) {
            System.err.println("Error en BD durante login:");
            e.printStackTrace();
            request.setAttribute("error", "Error del sistema");
        }

        request.getRequestDispatcher("/WEB-INF/vistas/login.jsp").forward(request, response);
    }
}