<?php 
session_start();
include 'db.php';

if (!isset($_SESSION['usuario_id'])) {
    header("Location: index.php");
    exit;
}

$usuarioId = $_SESSION['usuario_id'];

try {
    $stmt = $conn->prepare("SELECT * FROM prestamos WHERE usuario = :usuarioId");
    $stmt->bindParam(':usuarioId', $usuarioId);
    $stmt->execute();
    $resultados = $stmt->fetchAll(PDO::FETCH_ASSOC);

    echo "<!DOCTYPE html>
<html lang='es'>
<head>
    <meta charset='UTF-8'>
    <meta name='viewport' content='width=device-width, initial-scale=1.0'>
    <title>Mis Préstamos</title>
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
    <h1>Mis Préstamos</h1>";

    if (count($resultados) === 0) {
        echo "<p>No tienes préstamos registrados.</p>";
    } else {
        echo "<table>
                <tr>
                    <th>ID</th>
                    <th>Usuario</th>
                    <th>Título</th>
                    <th>Día Prestado</th>
                    <th>Día Límite</th>
                    <th>Exceso Días</th>
                </tr>";
        foreach ($resultados as $fila) {
            echo "<tr>
                    <td>{$fila['id']}</td>
                    <td>{$fila['usuario']}</td>
                    <td>{$fila['titulo']}</td>
                    <td>{$fila['dia_prestado']}</td>
                    <td>{$fila['dia_limite']}</td>
                    <td>" . ($fila['exceso_dias'] ? 'Sí' : 'No') . "</td>
                  </tr>";
        }
        echo "</table>";
    }

    echo "<a href='index.php' class='back-link'>Volver al Panel Principal</a>
</body>
</html>";
} catch (PDOException $e) {
    echo "<p>Error al obtener los préstamos: " . $e->getMessage() . "</p>";
}
?>