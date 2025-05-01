<%-- src/main/webapp/WEB-INF/vistas/registro.jsp --%>
 <%@ page contentType="text/html;charset=UTF-8" %>
 <html>
 <head>
  <title>Registro</title>
 </head>
 <body>
  <h2>Registro de Usuario</h2>
  <form action="registro" method="post">
  <input type="text" name="username" placeholder="Usuario" required><br>
  <input type="password" name="password" placeholder="Contraseña" required><br>
  <input type="password" name="confirmPassword" placeholder="Confirmar Contraseña" required><br>
  <button type="submit">Registrarse</button>
  </form>
  <a href="login">¿Ya tienes cuenta? Inicia sesión</a>
 </body>
 </html>