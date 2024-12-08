<?php
session_start();
include 'db.php';

if (!isset($_SESSION['usuario_id'])) {
    header("Location: index.php");
    exit;
}

try {
    $stmt = $conn->prepare("SELECT titulo, autor, anio_publicacion, isbn, disponible FROM libro");
    $stmt->execute();
    $libros = $stmt->fetchAll(PDO::FETCH_ASSOC);
} catch (PDOException $e) {
    echo "Error al obtener los libros: " . $e->getMessage();
    exit;
}
?>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lista de Libros</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: rgb(204, 102, 0); /* Fondo naranja */
        }
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
            display: block;
        }
        .navbar a:hover {
            background-color: #ddd;
            color: black;
        }
        .container {
            padding: 20px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
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
        .available {
            color: green;
            font-weight: bold;
        }
        .unavailable {
            color: red;
            font-weight: bold;
        }
    </style>
</head>
<body>
    <div class="navbar">
        <div>
            <a href="mis_prestamos.php">Préstamos</a>
            <a href="mostrar_usuario.php">Datos del Usuario</a>
            <a href="ver_libros.php">Lista de Libros</a>
        </div>
        <a href="logout.php" class="logout">Cerrar Sesión</a>
    </div>

    <div class="container">
        <h1>Lista de Libros</h1>
        <?php if (empty($libros)): ?>
            <p>No hay libros disponibles en este momento.</p>
        <?php else: ?>
            <table>
                <tr>
                    <th>Título</th>
                    <th>Autor</th>
                    <th>Año de Publicación</th>
                    <th>ISBN</th>
                    <th>Disponibilidad</th>
                </tr>
                <?php foreach ($libros as $libro): ?>
                    <tr>
                        <td><?php echo htmlspecialchars($libro['titulo']); ?></td>
                        <td><?php echo htmlspecialchars($libro['autor']); ?></td>
                        <td><?php echo htmlspecialchars($libro['anio_publicacion']); ?></td>
                        <td><?php echo htmlspecialchars($libro['isbn']); ?></td>
                        <td class="<?php echo $libro['disponible'] ? 'available' : 'unavailable'; ?>">
                            <?php echo $libro['disponible'] ? 'Disponible' : 'No Disponible'; ?>
                        </td>
                    </tr>
                <?php endforeach; ?>
            </table>
        <?php endif; ?>
    </div>
</body>
</html>