<?php
$host = 'localhost';
$dbname = 'bookshelter';
$user = 'toadadministrador'; // Usuario de tu base de datos
$pass = '123'; // Contraseña de tu base de datos (deja vacío si no tienes contraseña)

try {
    $conn = new PDO("mysql:host=$host;dbname=$dbname", $user, $pass);
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch (PDOException $e) {
    die("Error en la conexión: " . $e->getMessage());
}
?>