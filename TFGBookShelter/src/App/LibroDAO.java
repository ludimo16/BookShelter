/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package App;

/**
 *
 * @author ludimo16
 */
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JTable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LibroDAO {

    public void cargarLibrosDesdeCSV(String filePath, JTable tablaLibros) {
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(filePath))
                                .withSkipLines(1) // Omitir la primera línea (encabezado)
                                .build();
             Connection connection = DatabaseConnection.getConnection()) {

            System.out.println("Conexión a la base de datos establecida y archivo abierto.");

            String checkQuery = "SELECT COUNT(*) FROM libro WHERE titulo = ?";
            String insertQuery = "INSERT INTO libro (titulo, autor, anio_publicacion, isbn, disponible, usuario_prestado) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);

            String[] datos;
            // Lee cada línea del CSV
            while ((datos = reader.readNext()) != null) {
                System.out.println("Línea leída: " + String.join(", ", datos)); // Debugging

                if (datos.length >= 4) { // Verifica que haya al menos 4 columnas en cada fila
                    // Verificar si el título ya existe
                    checkStatement.setString(1, datos[0]); // Titulo
                    ResultSet resultSet = checkStatement.executeQuery();
                    resultSet.next();
                    int count = resultSet.getInt(1);

                    if (count > 0) {
                        System.out.println("El libro con el título '" + datos[0] + "' ya existe. Se omite.");
                        continue;
                    }

                    // Inserción de los datos
                    insertStatement.setString(1, datos[0]); // Titulo
                    insertStatement.setString(2, datos[1]); // Autor
                    insertStatement.setString(3, datos[2]); // Año de publicación (ahora String)
                    insertStatement.setString(4, datos[3]); // ISBN
                    insertStatement.setBoolean(5, true); // Disponible (configurado como true por defecto)
                    insertStatement.setString(6, ""); // Usuario Prestado (vacío por defecto)

                    insertStatement.executeUpdate();
                    System.out.println("Libro agregado: " + datos[0]);
                } else {
                    System.out.println("Línea con formato incorrecto: " + String.join(", ", datos));
                }
            }

            // Refresca la tabla después de cargar todos los libros
            Utility.refrescarTabla(tablaLibros, "SELECT * FROM libro");
            System.out.println("Tabla refrescada.");

        } catch (IOException | SQLException | CsvValidationException e) {
            e.printStackTrace();
        }
    }
        
    public void obtenerLibros() {
        String query = "SELECT * FROM libro";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String titulo = resultSet.getString("titulo");
                String autor = resultSet.getString("autor");
                String anioPublicacion = resultSet.getString("anio_publicacion");
                String isbn = resultSet.getString("isbn");
                boolean disponible = resultSet.getBoolean("disponible");
                String usuarioPrestado = resultSet.getString("usuario_prestado");

                System.out.println("Libro: " + titulo + ", Autor: " + autor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}