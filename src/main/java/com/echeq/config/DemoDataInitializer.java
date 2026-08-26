package com.echeq.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.demo-data.enabled", havingValue = "true")
public class DemoDataInitializer implements ApplicationRunner {

    private final JdbcTemplate jdbcTemplate;

    public DemoDataInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(ApplicationArguments args) {
        crearRol("ADMIN", "Rol administrador");
        crearRol("CLIENTE", "Usuario cliente");
        crearRol("AUDITOR", "Usuario encargado de auditoría");
        crearRol("OPERADOR", "Usuario operador del sistema");

        crearUsuario("Administrador", "Sistema", "admin@echeq.com", "$2a$10$yPe.d9neb7SnCaePBbUS.urEM.HREv3/8C3cxfv4c5cUjx2i/bMqq", "ADMIN");
        crearUsuario("Operador", "Prueba", "operador@echeq.com", "$2a$10$mpBkJODClTQRhvjNRHQwhuVwxmoajytSNsSMdWcDR56YmUOXH.Msy", "OPERADOR");
        crearUsuario("Auditor", "Prueba", "auditor@echeq.com", "$2a$10$ECY4tL4wFNPQkewffD.1IOIbncFKur9U5IzPsPUCgBKLQh6A5Xoj.", "AUDITOR");
        crearUsuario("Usuario", "Flutter", "usuario.flutter@echeq.com", "$2a$10$oEoqgX.//6xNSSIPqbGspu/iogHPuRfuT03eIZKGXlCPM9dRd/iR.", "CLIENTE");
    }

    private void crearRol(String nombre, String descripcion) {
        jdbcTemplate.update(
                "INSERT INTO rol (nombre, descripcion, created_at, updated_at) " +
                "SELECT ?, ?, NOW(6), NOW(6) " +
                "WHERE NOT EXISTS (SELECT 1 FROM rol WHERE nombre = ?)",
                nombre, descripcion, nombre
        );
    }

    private void crearUsuario(
            String nombre,
            String apellido,
            String email,
            String password,
            String rol) {

        jdbcTemplate.update(
                "INSERT INTO usuario " +
                "(email, nombre, apellido, password, activo, rol_id, created_at, updated_at) " +
                "SELECT ?, ?, ?, ?, 1, r.id, NOW(6), NOW(6) " +
                "FROM rol r " +
                "WHERE r.nombre = ? " +
                "AND NOT EXISTS (SELECT 1 FROM usuario WHERE email = ?)",
                email,
                nombre,
                apellido,
                password,
                rol,
                email
        );
    }
}
