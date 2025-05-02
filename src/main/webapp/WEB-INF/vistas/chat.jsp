<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.app.models.Mensaje" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Chat</title>
    <link rel="stylesheet" type="text/css" href="css/chat.css">
</head>
<body>
<div class="chat-container">
    <h2>Chat</h2>

    <div class="messages-container">
        <c:forEach var="mensaje" items="${mensajes}">
            <div class="${mensaje.usuarioId eq usuarioId ? 'user-message' : 'admin-message'}">
                ${mensaje.contenido}
            </div>
        </c:forEach>
    </div>

    <form class="new-message-form" action="chat" method="post">
        <textarea name="contenido" placeholder="Escribe tu mensaje" required></textarea><br>
        <button type="submit">Enviar</button>
        <button type="button" onclick="window.location.href='chat?action=eliminarChat'">Eliminar Chat</button>
    </form>

    <form action="logout.jsp" method="post">
        <button type="submit">Cerrar sesión</button>
    </form>
</div>
</body>
</html>