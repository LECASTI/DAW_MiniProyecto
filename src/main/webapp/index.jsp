<%@ page contentType="text/html;charset=UTF-8" %>
 <%
  String redirectPath = request.getContextPath();


  if (session.getAttribute("usuario") == null) {
  // No hay sesión, redirigir a login
  redirectPath += "/login";
  } else {
  // Hay sesión, redirigir según el rol
  String usuarioRol = (String) session.getAttribute("usuarioRol");
  if ("usuario".equals(usuarioRol)) {
  redirectPath += "/chat";
  } else if ("admin".equals(usuarioRol) || "superadmin".equals(usuarioRol)) {
  redirectPath += "/admin";
  } else {
  // Rol desconocido (esto no debería ocurrir, pero es bueno tener un caso por defecto)
  redirectPath += "/chat"; // O podrías redirigir a una página de error
  }
  }


  response.sendRedirect(redirectPath);
 %>