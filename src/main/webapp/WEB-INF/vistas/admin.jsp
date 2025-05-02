<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.app.models.Usuario" %>
<%@ page import="java.util.List" %>
<%@ page isELIgnored="false" %>
<%
    List<Usuario> usuarios = (List<Usuario>) request.getAttribute("usuarios");
    String rolActual = (String) request.getAttribute("rolActual");
    String mensaje = (String) request.getAttribute("mensaje");
    String error = (String) request.getAttribute("error");
%>

<!DOCTYPE html>
<html>
<head>
    <title>Panel de Administración</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body>
<h1>Panel de Administración</h1>

<% if (mensaje != null) { %>
    <p style="color: green;"><%= mensaje %></p>
<% } %>
<% if (error != null) { %>
    <p style="color: red;"><%= error %></p>
<% } %>

<table border="1" cellpadding="6">
    <tr>
        <th>ID</th>
        <th>Username</th>
        <th>Rol actual</th>
        <th>Nuevo rol</th>
        <th>Acciones</th>
    </tr>
    <% for (Usuario u : usuarios) { %>
        <tr>
            <td><%= u.getUsuarioId() %></td>
            <td><%= u.getUsername() %></td>
            <td><%= u.getRol() %></td>
            <td>
                <form method="post" style="display:inline;">
                    <input type="hidden" name="usuarioId" value="<%= u.getUsuarioId() %>">
                    <select name="nuevoRol">
                        <option value="usuario">usuario</option>
                        <option value="admin">admin</option>
                        <% if ("superadmin".equals(rolActual)) { %>
                            <option value="superadmin">superadmin</option>
                        <% } %>
                    </select>
                    <input type="submit" value="Cambiar rol">
                </form>
            </td>
            <td>
                <% if ("usuario".equals(u.getRol())) { %>
                    <form action="${pageContext.request.contextPath}/admin/chat" method="get" style="display:inline;">
                        <input type="hidden" name="usuarioId" value="<%= u.getUsuarioId() %>">
                        <input type="submit" value="Ver chat">
                    </form>
                <% } %>
            </td>
        </tr>
    <% } %>
</table>

<br>
<form action="logout" method="post">
    <button type="submit">Cerrar sesión</button>
</form>
</body>
</html>
