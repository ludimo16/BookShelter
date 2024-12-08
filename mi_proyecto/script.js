document.getElementById('btnMostrarUsuario').addEventListener('click', function () {
    fetch('mostrar_usuario.php')
        .then(response => response.json())
        .then(data => {
            const tabla = document.getElementById('tablaUsuario');
            const tbody = tabla.querySelector('tbody');
            tbody.innerHTML = ''; // Limpiar la tabla

            if (data.error) {
                alert(data.error);
            } else {
                const fila = document.createElement('tr');
                for (const key in data) {
                    const celda = document.createElement('td');
                    celda.textContent = data[key];
                    fila.appendChild(celda);
                }
                tbody.appendChild(fila);
                tabla.style.display = 'table'; // Mostrar la tabla
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Error al cargar los datos del usuario.');
        });
});