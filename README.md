# Back Fortex

BackFortex es el backend de la aplicación desarrollado con Spring Boot y Maven, utilizando MySQL como base de datos. Se implementó un modelo entidad-relación para gestionar usuarios, listas de tipos y productos, además de definir roles y sus relaciones.

## Tecnologías utilizadas

- **Spring Boot** - Framework principal para el backend
- **Maven** - Gestor de dependencias
- **MySQL** - Base de datos relacional
- **JPA/Hibernate** - Mapeo objeto-relacional
- **Spring Security** - Para la gestión de autenticación y autorización
- **Lombok** - Reducción de boilerplate en entidades y servicios

## Arquitectura

El backend sigue una arquitectura basada en capas:

1. **Capa de Entidades (Modelos)**: Definición de entidades y relaciones con JPA.
2. **Capa de Repositorios**: Interfaces para acceder a la base de datos con Spring Data JPA.
3. **Capa de Servicios**: Implementación de la lógica de negocio y gestión de entidades.
4. **Capa de Controladores (API REST)**: Exposición de endpoints para interactuar con el frontend.

Se aplicó el patrón **"Responsive Pattern"** para estructurar entidades, servicios y modelos de respuesta.

## Instalación y Configuración

1. Clonar el repositorio:
   ```sh
   [git clone https://github.com/tu-usuario/backFortex.git](https://github.com/Dwan13/backFortex.git)
   cd backFortex
   ```

2. Configurar la base de datos en `application.properties`:
   ```properties
    spring.application.name=types-backend
    spring.datasource.url=jdbc:mysql://db-backend.c3eooc88s0u1.us-east-2.rds.amazonaws.com:3306/db_backend_management
    spring.datasource.username=root
    spring.datasource.password=sictest1234
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
    spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
    spring.jpa.show-sql=true
    spring.jpa.hibernate.ddl-auto=update
    spring.jackson.serialization.FAIL_ON_EMPTY_BEANS=false
   ```

3. Compilar y ejecutar el backend:
   ```sh
   mvn clean install
   mvn spring-boot:run
   ```

## Endpoints Principales

| Método | Endpoint | Descripción |
|---------|-----------------|----------------------|
| GET     | /api/users      | Obtener todos los usuarios |
| POST    | /api/users      | Crear un nuevo usuario |
| PUT    | /api/users/{id}       | Actualiza un  usuario |
| DELETE    | /api/users/{id}       | Elimina un  usuario |

## Seguridad y Autenticación

El backend implementa autenticación con **Spring Security** y JSON Web Tokens (JWT) para gestionar el acceso a los recursos.



---

_Desarrollado por Dwan Felipe Veloza Páez

