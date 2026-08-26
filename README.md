# Sistema de Gestión de eCheqs - Backend

API REST desarrollada con Spring Boot para la gestión de solicitudes de eCheqs.

## Tecnologías

- Java 21
- Spring Boot 3.5
- Spring Security y JWT
- Spring Data JPA / Hibernate
- MySQL 8
- Maven
- Docker y Docker Compose
- Swagger / OpenAPI

## Funcionalidades

- Autenticación y registro de usuarios
- Autorización mediante roles
- Gestión de bancos
- Gestión de cuentas
- Gestión de Cuenta Banco
- Gestión de Cuentas Corrientes
- Solicitudes de eCheqs
- Aprobación y rechazo de solicitudes
- Notificaciones
- Auditoría
- Administración de usuarios y roles

## Roles

### ADMIN
Acceso administrativo completo, gestión de usuarios, roles, solicitudes y auditoría.

### OPERADOR
Gestión operativa de clientes, cuentas y solicitudes. Puede aprobar y rechazar eCheqs.

### CLIENTE
Opera únicamente sobre sus propios datos, cuentas y solicitudes.

### AUDITOR
Acceso global de solo lectura y consulta de auditoría.

## Seguridad

El backend utiliza Spring Security con autenticación JWT y sesiones stateless.

Los endpoints protegidos requieren el encabezado:

Authorization: Bearer <token>

- 401: usuario no autenticado o sesión inválida
- 403: usuario autenticado sin permisos suficientes

## Flujo principal de eCheq

1. El CLIENTE crea una solicitud.
2. La solicitud queda en estado PENDIENTE.
3. Mientras está pendiente, el CLIENTE puede modificarla.
4. Un ADMIN u OPERADOR puede aprobarla o rechazarla.
5. La decisión genera un registro de aprobación.
6. La operación queda registrada en Auditoría.
7. El CLIENTE recibe una notificación.

## Docker

El proyecto puede ejecutarse mediante Docker Compose.

Antes de iniciar, crear un archivo .env tomando como referencia .env.example.

Levantar los servicios:
docker compose up -d --build

Consultar el estado:
docker compose ps

Consultar logs del backend:
docker compose logs backend

Detener los servicios:
docker compose down

Puertos utilizados:
- Backend: 8080
- MySQL Docker: 3307

Los datos de MySQL se almacenan en un volumen Docker persistente.

## Ejecución local

Requisitos:
- Java 21
- MySQL 8

Compilar:
.\mvnw.cmd clean package -DskipTests

Ejecutar:
java -jar .\target\backend-0.0.1-SNAPSHOT.jar

API disponible en:
http://localhost:8080/api

## Swagger / OpenAPI

Con el backend ejecutándose:
http://localhost:8080/swagger-ui/index.html

## Arquitectura

El backend utiliza una arquitectura por capas:
Controller -> Service -> Repository -> MySQL

También incluye DTOs, mappers, entidades, seguridad JWT y manejo global de excepciones.

## Base de datos

Motor: MySQL 8
Base principal: echeq_db

La persistencia se administra mediante Spring Data JPA e Hibernate.

## Estado del proyecto

Las funcionalidades principales se encuentran implementadas y validadas.

- Login y registro
- JWT y expiración de sesión
- Permisos por rol
- Bancos
- Cuentas
- Cuenta Banco
- Cuentas Corrientes
- Solicitudes de eCheqs
- Aprobaciones y rechazos
- Notificaciones
- Auditoría
- Gestión de usuarios
- Manejo uniforme de errores HTTP
- Docker y MySQL Docker
- Integración con frontend Flutter

## Frontend

El frontend se encuentra desarrollado en Flutter y consume esta API REST.
