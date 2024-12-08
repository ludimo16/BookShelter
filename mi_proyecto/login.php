<?php
// Activar la visualización de errores
error_reporting(E_ALL);
ini_set('display_errors', 1);

// Iniciar sesión
session_start();

// Incluir el archivo de conexión a la base de datos
include 'db.php';

// Función para hashear la contraseña
function hashPassword($password) {
    return hash("sha256", $password);
}

// Verificar que el método de solicitud sea POST
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    // Capturar los datos enviados desde el formulario
    $user = $_POST['username'] ?? null;
    $pass = $_POST['password'] ?? null;

    // Validar que los campos no estén vacíos
    if (empty($user) || empty($pass)) {
        $_SESSION['login_error'] = "Por favor, ingresa usuario y contraseña.";
        header("Location: index.php");
        exit;
    }

    // Hashear la contraseña ingresada
    $hashedPass = hashPassword($pass);

    try {
        // Preparar la consulta para buscar al usuario en la base de datos
        $stmt = $conn->prepare("SELECT id, usuario_nombre FROM usuarios WHERE usuario_nombre = :user AND usuario_contraseña = :pass");
        $stmt->bindParam(':user', $user);
        $stmt->bindParam(':pass', $hashedPass);
        $stmt->execute();

        // Verificar si se encontró un usuario
        if ($stmt->rowCount() > 0) {
            $userData = $stmt->fetch(PDO::FETCH_ASSOC);

            // Guardar el ID y nombre del usuario en la sesión
            $_SESSION['usuario_id'] = $userData['id'];
            $_SESSION['usuario_nombre'] = $userData['usuario_nombre'];

            // Registrar en la tabla de auditoría: Login exitoso
            $accion = "Login exitoso";
            $stmtAuditoria = $conn->prepare("INSERT INTO auditoria_usuarios (accion, usuario_id, fecha_hora) VALUES (:accion, :usuario_id, NOW())");
            $stmtAuditoria->bindParam(':accion', $accion);
            $stmtAuditoria->bindParam(':usuario_id', $userData['id']);
            $stmtAuditoria->execute();

            // Redirigir a la página principal
            header("Location: index.php");
            exit;
        } else {
            // Registrar en la tabla de auditoría: Intento fallido
            $accion = "Intento fallido";
            $stmtAuditoria = $conn->prepare("INSERT INTO auditoria_usuarios (accion, usuario_id, fecha_hora) VALUES (:accion, NULL, NOW())");
            $stmtAuditoria->bindParam(':accion', $accion);
            $stmtAuditoria->execute();

            $_SESSION['login_error'] = "Usuario o contraseña incorrectos.";
            header("Location: index.php");
            exit;
        }
    } catch (PDOException $e) {
        // Manejo de errores en la consulta SQL
        error_log("Error en la consulta de login: " . $e->getMessage());
        $_SESSION['login_error'] = "Ocurrió un error, inténtalo nuevamente.";
        header("Location: index.php");
        exit;
    }
}
?>