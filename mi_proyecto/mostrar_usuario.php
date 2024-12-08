<?php 
session_start();

if (!isset($_SESSION['usuario_id'])) {
    echo "<p>No tienes acceso a esta sección.</p>";
    exit();
}

include 'db.php';

$usuario_id = $_SESSION['usuario_id'];

try {
    $stmt = $conn->prepare("SELECT id, permisos, nombre, primer_apellido, segundo_apellido, domicilio, numero_movil, usuario_nombre FROM usuarios WHERE id = :id");
    $stmt->bindParam(':id', $usuario_id);
    $stmt->execute();

    $usuario = $stmt->fetch(PDO::FETCH_ASSOC);

    echo "<!DOCTYPE html>
<html lang='es'>
<head>
    <meta charset='UTF-8'>
    <meta name='viewport' content='width=device-width, initial-scale=1.0'>
    <title>Datos del Usuario</title>
    <style>
        body {
            margin: 0;
            padding: 0;
            font-family: Arial, sans-serif;
            background-color: #CC6600;
            color: white;
            text-align: center;
        }
        table {
            width: 80%;
            margin: 20px auto;
            border-collapse: collapse;
            background-color: white;
            color: black;
        }
        table, th, td {
            border: 1px solid black;
        }
        th, td {
            padding: 8px;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
        }
        .back-link {
            display: inline-block;
            margin-top: 20px;
            padding: 10px 20px;
            background-color: #333;
            color: white;
            text-decoration: none;
            border-radius: 4px;
        }
        .back-link:hover {
            background-color: #555;
        }
    </style>
</head>
<body>
    <h1>Datos del Usuario</h1>";

    if ($usuario) {
        echo "<table>
                <tr>
                    <th>ID</th>
                    <th>Permisos</th>
                    <th>Nombre</th>
                    <th>Primer Apellido</th>
                    <th>Segundo Apellido</th>
                    <th>Domicilio</th>
                    <th>Número Móvil</th>
                    <th>Usuario</th>
                </tr>
                <tr>
                    <td>{$usuario['id']}</td>
                    <td>{$usuario['permisos']}</td>
                    <td>{$usuario['nombre']}</td>
                    <td>{$usuario['primer_apellido']}</td>
                    <td>{$usuario['segundo_apellido']}</td>
                    <td>{$usuario['domicilio']}</td>
                    <td>{$usuario['numero_movil']}</td>
                    <td>{$usuario['usuario_nombre']}</td>
                </tr>
              </table>";
    } else {
        echo "<p>No se encontraron datos del usuario.</p>";
    }

    echo "<a href='index.php' class='back-link'>Volver al Panel Principal</a>
</body>
</html>";
} catch (PDOException $e) {
    echo "<p>Error al obtener los datos del usuario: " . $e->getMessage() . "</p>";
}
?>