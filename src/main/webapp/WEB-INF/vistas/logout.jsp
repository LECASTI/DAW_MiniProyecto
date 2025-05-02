<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    session.invalidate(); // Cierra la sesión
    response.sendRedirect("login"); // Redirige al login
%>
