<?php
session_start(); // Iniciar sesión

// Destruir variables de sesión y la sesión
session_unset();
session_destroy();

// Redirigir al index.php (login visual del proyecto)
header("Location: index.php");
exit;
?>