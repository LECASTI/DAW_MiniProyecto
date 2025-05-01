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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            Usuario usuario = new UsuarioDAO().autenticar(username, password);
            if (usuario != null) {
                request.getSession().setAttribute("usuario", usuario);
                response.sendRedirect("chat");
            } else {
                request.setAttribute("error", "Credenciales inválidas");
                request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException("Error de base de datos", e);
        }
    }
}