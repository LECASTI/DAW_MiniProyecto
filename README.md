# Sistema de Chat con Roles de Usuario

## Descripción
Aplicación web de chat con tres roles jerárquicos:
- Usuario (chat básico)
- Admin (gestión usuarios + chat)
- Superadmin (control total)

Disponible en https://github.com/LECASTI/DAW_MiniProyecto

## Configuración postgres
Se encuentra en el archivo src/dao/conexionBD

    private static final String URL = "jdbc:postgresql://localhost:5432/";
    private static final String DB_NAME = "galeria_pulseras";
    private static final String USER = "postgres"; // Usuario por defecto de PostgreSQL
    private static final String PASS = "postgres"; // Cambia esto por tu password real

Cambiar anteriores valores para acceder a tu base de datos de postgres

## Accesos de Prueba
```plaintext
            usuario : contraseña
Superadmin: emilio:pass
Admin:      admin1:admin123  
Usuario:    usuario1:user123
```

## Comandos claves 
#### Compilar proyecto
mvn clean package

#### Desplegar en Tomcat
mvn clean compile tomcat7:run

## Flujo de Trabajo

    Login → 2. Redirección según rol → 3. Interacción con el sistema

## Estructura de directorios
```
C:.
|   README.md
|
\---main
+---java
|   \---com
|       \---example
|           \---app
|               +---dao
|               |       ChatDAO.java
|               |       ConexionBD.java
|               |       MensajeDAO.java
|               |       UsuarioDAO.java
|               |
|               +---filters
|               |       AuthFilter.java
|               |
|               +---models
|               |       Chat.java
|               |       Mensaje.java
|               |       Usuario.java
|               |
|               +---servlets
|               |       AdminChatServlet.java
|               |       AdminServlet.java
|               |       ChatServlet.java
|               |       LoginServlet.java
|               |       LogoutServlet.java
|               |       RegistroServlet.java
|               |
|               \---utils
|                       InicializadorBD.java
|
+---resources
\---webapp
|   index.jsp
|
+---css
|       admin.css
|       chat.css
|       login.css
|       registro.css
|
\---WEB-INF
|   web.xml
|
\---vistas
admin.jsp
chat.jsp
login.jsp
logout.jsp
registro.jsp
```
