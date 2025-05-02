<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.app.models.Mensaje" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>${not empty esAdmin ? 'Chat de Usuario' : 'Chat'}</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/chat.css">
</head>
<body>
<div class="chat-container">
    <h2>${not empty esAdmin ? 'Chat con Usuario' : 'Mi Chat'}</h2>

    <div class="messages-container">
        <c:forEach var="mensaje" items="${mensajes}">
            <div class="${mensaje.usuarioId eq usuarioId ? 'user-message' : 'admin-message'}">
                <!-- Mostrar nombre del remitente -->
                <div class="message-sender">
                    <c:choose>
                        <c:when test="${mensaje.usuarioId eq (not empty esAdmin ? currentUserId : usuarioId)}">
                            Tú
                        </c:when>
                        <c:otherwise>
                            ${mensaje.usuarioNombre}
                        </c:otherwise>
                    </c:choose>
                </div>
                <!-- Contenido del mensaje -->
                <div class="message-content">
                    ${mensaje.contenido}
                </div>
            </div>
        </c:forEach>
    </div>

    <form class="new-message-form"
          action="${pageContext.request.contextPath}${not empty esAdmin ? '/admin/chat' : '/chat'}"
          method="post">
        <c:if test="${not empty esAdmin}">
            <input type="hidden" name="usuarioId" value="${usuarioId}">
        </c:if>
        <textarea name="contenido" placeholder="Escribe tu mensaje" required></textarea><br>
        <button type="submit">Enviar</button>
    </form>

    <c:if test="${not empty esAdmin}">
        <button onclick="window.location.href='${pageContext.request.contextPath}/admin'">Volver al Panel</button>
    </c:if>
    <c:if test="${empty esAdmin}">
        <form action="logout" method="post">
            <button type="submit">Cerrar sesión</button>
        </form>
    </c:if>
</div>
</body>
</html>