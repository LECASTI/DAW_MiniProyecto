<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.app.models.Mensaje" %>
<%@ page import="com.example.app.servlets.ChatServlet.MensajeParaJSP" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Chat</title>
    <link rel="stylesheet" type="text/css" href="css/chat.css">
    <style>
        .user-message, .admin-message {
            border: 1px solid black;
            padding: 8px;
            margin: 5px;
            background-color: #f2f2f2;
            color: black;
        }
    </style>
</head>
<body>
<div class="chat-container">
    <h2>Chat</h2>

    <!-- Diagnóstico -->
    <p><strong>mensajesParaJSP size:</strong> ${fn:length(mensajesParaJSP)}</p>

    <div class="messages-container">
        <c:forEach var="mensajeParaJSP" items="${mensajesParaJSP}">
            <div class="${mensajeParaJSP.cssClass}">
                ${mensajeParaJSP.contenido}
            </div>
        </c:forEach>
    </div>

    <form class="new-message-form" action="chat" method="post">
        <textarea name="contenido" placeholder="Escribe tu mensaje" required></textarea><br>
        <button type="submit">Enviar</button>
        <button type="button" onclick="window.location.href='chat?action=eliminarChat'">Eliminar Chat</button>
    </form>

    <form action="login" method="get">
        <button type="submit">Logout</button>
    </form>
</div>
</body>
</html>
