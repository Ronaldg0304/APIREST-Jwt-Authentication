# API REST de Autenticación con JWT

API REST desarrollada en **Spring Boot** para el registro, autenticación, recuperación de contraseña mediante correo electrónico y actualización de credenciales de usuario.  
La seguridad se implementa con **JSON Web Tokens (JWT)** y se siguen las mejores prácticas de arquitectura y desarrollo backend.

---

## Características principales
- Registro y autenticación de usuarios con **JWT**.
- Recuperación de contraseña vía **correo electrónico** (Gmail SMTP).
- Cambio de contraseña seguro con validación de token.
- Respuestas en formato **JSON bajo el estándar RFC 9294**.
- Manejo centralizado de errores con **`@RestControllerAdvice`**.
- Validación de parámetros de entrada con **`@Valid` y Bean Validation**.
- Uso de **Lombok** para reducir boilerplate code.
- Arquitectura limpia con separación en **controller, service y repository**.

---

## Tecnologías utilizadas
- **Java 21**
- **Spring Boot 3.x**
- **Spring Security + JWT**
- **Spring Data JPA (Hibernate)**
- **MySQL**
- **Lombok**
- **Java Mail Sender (SMTP)**

---

## Estructura del proyecto
```bash
src/main/java/com/miempresa/autenticacionJWT
 ├── apiResponse/      # Clase para respuesta
 ├── config/           # Configuración de seguridad y beans
 ├── controller/       # Controladores REST
 ├── dto/              # Data Transfer Objects
 ├── exception/        # Excepciones personalizadas y manejador global
 ├── helpers/          # Builder de respuestas tipo apiResponse
 ├── models/           # modelos
 ├── repositories/     # Repositorios JPA
 ├── services/         # Lógica de negocio
 └── AutenticacionJwtApplication.java
 ```
# Configuración
1. Variables de entorno: 
Crea un archivo .env en la raíz del proyecto.

## Database
DB_URL=your_url_database
DB_USER=your_user
DB_PASSWORD=your_password

## JWT
JWT_SECRET=your_jwt_secret_here

## Mail
MAIL_USER=your_email_test
MAIL_PASS=your_app_password_here


Tu archivo application.properties debe usar estas variables:
```
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.hibernate.ddl-auto=update

jwt.secretKey=${JWT_SECRET}

spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=${MAIL_USER}
spring.mail.password=${MAIL_PASS}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
spring.mail.properties.mail.smtp.connectiontimeout=5000
spring.mail.properties.mail.smtp.timeout=5000
spring.mail.properties.mail.smtp.writetimeout=5000
spring.mail.test-connection=true
```
2. Ejecución en IntelliJ

Ve a Run → Edit Configurations….

Selecciona tu aplicación Spring Boot.

En Environment variables, agrega las variables definidas en .env.

Ejecuta la aplicación.

### Endpoints principales
Autenticación

POST /api/v1/auth/register → Registro de usuario

POST /api/v1/auth/authenticate → Autenticación y obtención de JWT

Recuperación de contraseña

POST /api/v1/auth/forgot-password → Enviar token de recuperación al correo

POST /api/v1/auth/reset-password → Cambiar contraseña con token

📋 Ejemplo de respuesta JSON (RFC 9294)
{
  "type": "https://api.miapp.com/errors/unauthorized",
  "title": "Unauthorized",
  "status": 401,
  "detail": "El token JWT es inválido o ha expirado",
  "timestamp": "2025-08-26T15:00:00Z"
}

🚀 Habilidades aplicadas y obtenidas

Este proyecto refleja habilidades clave para un desarrollador backend junior:

🏗️ Diseño y construcción de APIs REST seguras con JWT.

⚖️ Manejo de excepciones y respuestas estandarizadas con @RestControllerAdvice.

✅ Validación de datos con Bean Validation para evitar entradas inválidas.

📦 Gestión de dependencias y buenas prácticas en Spring Boot.

🛠️ Integración de correo electrónico (SMTP) para funcionalidades de negocio.

🔐 Buenas prácticas de seguridad: variables de entorno, no exponer secretos en el código.

📚 Uso de Lombok para mantener el código limpio y legible.

🧩 Arquitectura modular y escalable, fácil de mantener y extender.

📄 Licencia

Este proyecto se distribuye bajo la licencia MIT.


---

👉 Este README.md ya está listo para subir a tu repositorio y mostrar tanto tu proyecto como tus **habilidades backend**.  

¿Quieres que además te arme un **ejemplo del objeto `ApiResponse` genérico** que puedes documentar en el README para mostrar cómo estandarizas las respuestas de tu API?
