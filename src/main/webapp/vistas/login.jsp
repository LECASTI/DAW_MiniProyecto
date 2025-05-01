<%-- src/main/webapp/WEB-INF/views/auth/login.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
    <h2>Login</h2>
    <form action="${pageContext.request.contextPath}/login" method="post">
        <input type="text" name="username" placeholder="Usuario" required>
        <input type="password" name="password" placeholder="Contraseña" required>
        <button type="submit">Entrar</button>
    </form>
    <a href="registro">Registrarse</a>
    ${error}
</body>
</html>