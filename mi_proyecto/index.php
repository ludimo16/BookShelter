<?php
session_start();
?>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>BookShelter</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: rgb(204, 102, 0); /* Fondo de color naranja oscuro */
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
        .logout {
            color: white;
            background-color: red;
            padding: 10px 15px;
            text-decoration: none;
            border-radius: 3px;
        }
        .logout:hover {
            background-color: darkred;
        }
        .container {
            padding: 20px;
            text-align: center;
        }
        .welcome-box {
            background-color: white;
            padding: 20px;
            margin: 20px auto;
            max-width: 400px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            text-align: center;
        }
        .title {
            font-family: 'Georgia', serif;
            font-size: 3rem;
            color: rgb(153, 255, 255); /* Color azul claro */
            margin: 20px 0;
        }
        .subtitle {
            font-family: 'Georgia', serif;
            font-size: 1.5rem;
            color: #555;
            margin-bottom: 30px;
        }
        form {
            background-color: white;
            padding: 20px 25px; /* Ajuste del padding derecho */
            border-radius: 5px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            max-width: 400px;
            margin: 50px auto;
            box-sizing: border-box; /* Asegurar el cálculo correcto del ancho */
        }
        form input {
            display: block;
            width: 100%; /* Respetar el espacio interno del formulario */
            margin-bottom: 15px;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            box-sizing: border-box;
        }
        form button {
            background-color: #333;
            color: white;
            border: none;
            padding: 10px;
            width: 100%;
            border-radius: 5px;
            cursor: pointer;
        }
        form button:hover {
            background-color: #555;
        }
        .error-message {
            color: yellow;
            font-weight: bold;
            margin-bottom: 15px;
            text-align: center;
        }
    </style>
</head>
<body>
    <?php if (isset($_SESSION['usuario_id'])): ?>
        <!-- Mostrar barra de navegación si el usuario está logueado -->
        <div class="navbar">
            <div>
                <a href="mis_prestamos.php">Préstamos</a>
                <a href="mostrar_usuario.php">Datos del Usuario</a>
                <a href="ver_libros.php">Ver Libros</a> <!-- Nuevo botón añadido -->
            </div>
            <a href="logout.php" class="logout">Cerrar Sesión</a>
        </div>
        <div class="container">
            <div class="welcome-box">
                <h1>Bienvenido, <?php echo htmlspecialchars($_SESSION['usuario_nombre']); ?></h1>
                <p>Selecciona una opción en la barra de navegación.</p>
            </div>
        </div>
    <?php else: ?>
        <!-- Mostrar formulario de login si el usuario no está logueado -->
        <div class="container">
            <h1 class="title">BookShelter</h1>
            <p class="subtitle">Tu portal para gestionar libros y préstamos</p>
            <!-- Mostrar mensaje de error si existe -->
            <?php if (isset($_SESSION['login_error'])): ?>
                <div class="error-message">
                    <?php 
                    echo $_SESSION['login_error']; 
                    unset($_SESSION['login_error']); // Eliminar mensaje después de mostrarlo
                    ?>
                </div>
            <?php endif; ?>
            <form action="login.php" method="POST">
                <label for="username">Usuario:</label>
                <input type="text" id="username" name="username" required>
                <label for="password">Contraseña:</label>
                <input type="password" id="password" name="password" required>
                <button type="submit">Login</button>
            </form>
        </div>
    <?php endif; ?>
    <script>
    // Esperar 5 segundos y ocultar el mensaje de error
    setTimeout(() => {
        const errorMessage = document.querySelector('.error-message');
        if (errorMessage) {
            errorMessage.style.display = 'none';
        }
    }, 5000); // 5000 milisegundos = 5 segundos
</script>
</body>
</html>