<?php
if (!isset($_SESSION)) {
    session_start(); // Iniciar sesión si no está iniciada
}

// Verificar si el usuario está logueado
if (!isset($_SESSION['usuario_id'])) {
    header("Location: index.php");
    exit; // Redirigir al login si no está autenticado
}
?>
<div class="navbar">
    <div>
        <a href="prestamos.php">Préstamos</a>
        <a href="mostrar_usuario.php">Datos del Usuario</a>
    </div>
    <form action="logout.php" method="POST" style="margin: 0;">
        <button type="submit" class="logout">Cerrar Sesión</button>
    </form>
</div>
<style>
    .navbar {
        background-color: #333;
        overflow: hidden;
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 10px;
    }
    .navbar a {
        color: white;
        text-decoration: none;
        padding: 10px 15px;
    }
    .navbar a:hover {
        background-color: #575757;
    }
    .logout {
        background-color: red;
        color: white;
        border: none;
        padding: 10px 15px;
        cursor: pointer;
    }
</style>