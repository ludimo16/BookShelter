package App;

import javax.swing.JOptionPane;
import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.Utility to edit this template
 */


/**
 *
 * @author ludimo16
 */
public class Utility {
    // Método para cambiar el panel en el contenedor
    public static void PanelPrint(JPanel content, JPanel newPanel) {
        content.removeAll(); // Limpia el contenedor
        content.setLayout(new BorderLayout()); // Asegúrate de que usa BorderLayout
        content.add(newPanel, BorderLayout.CENTER); // Agrega el nuevo panel
        content.revalidate(); // Refresca el contenedor
        content.repaint(); // Repinta el contenedor
    }
    
    public static void refrescarTabla(JTable table, String query) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            // Obtén el modelo de la tabla
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0); // Limpia la tabla para evitar duplicados

            int columnCount = resultSet.getMetaData().getColumnCount();

            // Agrega cada fila de la base de datos al modelo de la tabla
            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    rowData[i] = resultSet.getObject(i + 1);
                }
                model.addRow(rowData); // Añade la fila al modelo de tabla
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void eliminarElementoSeleccionado(JTable table, String databaseTableName, String query, String refreshQuery) {
    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(null, "Selecciona un elemento para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Obtén el ID del elemento desde la tabla y trata de castearlo a Integer
    Integer id = (Integer) table.getValueAt(selectedRow, 0); // Asegúrate de que el ID está en la primera columna

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {

        statement.setInt(1, id);  // Usa setInt para el tipo Integer

        int rowsAffected = statement.executeUpdate();

        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null, databaseTableName + " eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "No se pudo eliminar " + databaseTableName + ".", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Refresca la tabla después de eliminar el elemento
        refrescarTabla(table, refreshQuery);

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error al eliminar " + databaseTableName + ".", "Error", JOptionPane.ERROR_MESSAGE);
    }
    }

    // Método para mostrar el panel de entrada de usuario
    public static void mostrarPanelAgregarUsuario() {
        JPanel panel = crearPanelAgregarUsuario();
        
        int result = JOptionPane.showConfirmDialog(null, panel, "Agregar Usuario", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            procesarEntradaUsuario(panel);
        }
    }

    // Método para crear el panel con los campos de entrada y las etiquetas
    public static JPanel crearPanelAgregarUsuario() {
        JTextField nombreField = new JTextField(20);
        JTextField primerApellidoField = new JTextField(20);
        JTextField segundoApellidoField = new JTextField(20);
        JTextField domicilioField = new JTextField(20);
        JTextField numeroMovilField = new JTextField(15);
        JTextField usuarioNombreField = new JTextField(20);
        JPasswordField usuarioContrasenaField = new JPasswordField(20);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(new JLabel("Nombre (solo letras):"));
        panel.add(nombreField);
        panel.add(new JLabel("Primer Apellido (solo letras):"));
        panel.add(primerApellidoField);
        panel.add(new JLabel("Segundo Apellido (solo letras):"));
        panel.add(segundoApellidoField);
        panel.add(new JLabel("Domicilio (letras y números):"));
        panel.add(domicilioField);
        panel.add(new JLabel("Número Móvil (solo números):"));
        panel.add(numeroMovilField);
        panel.add(new JLabel("Usuario Nombre (letras y números, debe ser único):"));
        panel.add(usuarioNombreField);
        panel.add(new JLabel("Usuario Contraseña (letras y números, será hasheada):"));
        panel.add(usuarioContrasenaField);

        // Guardar los campos como propiedades del panel para su fácil acceso
        panel.putClientProperty("nombreField", nombreField);
        panel.putClientProperty("primerApellidoField", primerApellidoField);
        panel.putClientProperty("segundoApellidoField", segundoApellidoField);
        panel.putClientProperty("domicilioField", domicilioField);
        panel.putClientProperty("numeroMovilField", numeroMovilField);
        panel.putClientProperty("usuarioNombreField", usuarioNombreField);
        panel.putClientProperty("usuarioContrasenaField", usuarioContrasenaField);

        return panel;
    }

    // Método para procesar los datos ingresados y realizar las validaciones
    public static void procesarEntradaUsuario(JPanel panel) {
        JTextField nombreField = (JTextField) panel.getClientProperty("nombreField");
        JTextField primerApellidoField = (JTextField) panel.getClientProperty("primerApellidoField");
        JTextField segundoApellidoField = (JTextField) panel.getClientProperty("segundoApellidoField");
        JTextField domicilioField = (JTextField) panel.getClientProperty("domicilioField");
        JTextField numeroMovilField = (JTextField) panel.getClientProperty("numeroMovilField");
        JTextField usuarioNombreField = (JTextField) panel.getClientProperty("usuarioNombreField");
        JPasswordField usuarioContrasenaField = (JPasswordField) panel.getClientProperty("usuarioContrasenaField");

        String nombre = nombreField.getText().trim();
        String primerApellido = primerApellidoField.getText().trim();
        String segundoApellido = segundoApellidoField.getText().trim();
        String domicilio = domicilioField.getText().trim();
        String numeroMovil = numeroMovilField.getText().trim();
        String usuarioNombre = usuarioNombreField.getText().trim();
        String usuarioContrasena = new String(usuarioContrasenaField.getPassword()).trim();

        if (!validarEntrada(nombre, "[a-zA-Z]+", "Nombre solo puede contener letras") ||
            !validarEntrada(primerApellido, "[a-zA-Z]+", "Primer Apellido solo puede contener letras") ||
            !validarEntrada(segundoApellido, "[a-zA-Z]*", "Segundo Apellido solo puede contener letras") ||
            !validarEntrada(domicilio, "[a-zA-Z0-9 ]+", "Domicilio solo puede contener letras y números") ||
            !validarEntrada(numeroMovil, "\\d+", "Número Móvil solo puede contener números") ||
            !validarEntrada(usuarioNombre, "[a-zA-Z0-9]+", "Usuario Nombre solo puede contener letras y números") ||
            !validarEntrada(usuarioContrasena, "[a-zA-Z0-9]+", "Usuario Contraseña solo puede contener letras y números")) {
            return;
        }

        if (usuarioNombreExiste(usuarioNombre)) {
            JOptionPane.showMessageDialog(null, "El nombre de usuario ya existe. Por favor elige otro.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String contrasenaHasheada = hashPassword(usuarioContrasena);

        agregarUsuarioABaseDeDatos(nombre, primerApellido, segundoApellido, domicilio, numeroMovil, usuarioNombre, contrasenaHasheada);
    }

    // Método para validar entradas de texto usando expresiones regulares
    public static boolean validarEntrada(String input, String regex, String mensajeError) {
        if (!Pattern.matches(regex, input)) {
            JOptionPane.showMessageDialog(null, mensajeError, "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    // Método para verificar si el nombre de usuario ya existe en la base de datos
    public static boolean usuarioNombreExiste(String usuarioNombre) {
        String query = "SELECT COUNT(*) FROM usuarios WHERE usuario_nombre = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, usuarioNombre);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1) > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al verificar el nombre de usuario.", "Error", JOptionPane.ERROR_MESSAGE);
            return true;
        }
    }

    // Método para agregar un usuario a la base de datos
    public static void agregarUsuarioABaseDeDatos(String nombre, String primerApellido, String segundoApellido, String domicilio, String numeroMovil, String usuarioNombre, String contrasenaHasheada) {
        String insertQuery = "INSERT INTO usuarios (nombre, primer_apellido, segundo_apellido, domicilio, numero_movil, usuario_nombre, usuario_contraseña, permisos) VALUES (?, ?, ?, ?, ?, ?, ?, 0)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(insertQuery)) {

            statement.setString(1, nombre);
            statement.setString(2, primerApellido);
            statement.setString(3, segundoApellido);
            statement.setString(4, domicilio);
            statement.setString(5, numeroMovil);
            statement.setString(6, usuarioNombre);
            statement.setString(7, contrasenaHasheada);

            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Usuario agregado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al agregar usuario.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para hashear una contraseña con SHA-256
    public static String hashPassword(String password) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al hashear la contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    
    // Método para mostrar el panel de entrada de administrador
    public static void mostrarPanelAgregarAdministrador() {
        JPanel panel = crearPanelAgregarAdministrador();

        int result = JOptionPane.showConfirmDialog(null, panel, "Agregar Administrador", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            procesarEntradaAdministrador(panel);
        }
    }

    // Método para crear el panel con los campos de entrada y las etiquetas
    public static JPanel crearPanelAgregarAdministrador() {
        JTextField nombreField = new JTextField(20);
        JTextField primerApellidoField = new JTextField(20);
        JTextField segundoApellidoField = new JTextField(20);
        JTextField domicilioField = new JTextField(20);
        JTextField numeroMovilField = new JTextField(15);
        JTextField usuarioNombreField = new JTextField(20);
        JPasswordField usuarioContrasenaField = new JPasswordField(20);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(new JLabel("Nombre (solo letras):"));
        panel.add(nombreField);
        panel.add(new JLabel("Primer Apellido (solo letras):"));
        panel.add(primerApellidoField);
        panel.add(new JLabel("Segundo Apellido (solo letras):"));
        panel.add(segundoApellidoField);
        panel.add(new JLabel("Domicilio (letras y números):"));
        panel.add(domicilioField);
        panel.add(new JLabel("Número Móvil (solo números):"));
        panel.add(numeroMovilField);
        panel.add(new JLabel("Usuario Nombre (letras y números, debe ser único):"));
        panel.add(usuarioNombreField);
        panel.add(new JLabel("Usuario Contraseña (letras y números, será hasheada):"));
        panel.add(usuarioContrasenaField);

        // Guardar los campos como propiedades del panel para su fácil acceso
        panel.putClientProperty("nombreField", nombreField);
        panel.putClientProperty("primerApellidoField", primerApellidoField);
        panel.putClientProperty("segundoApellidoField", segundoApellidoField);
        panel.putClientProperty("domicilioField", domicilioField);
        panel.putClientProperty("numeroMovilField", numeroMovilField);
        panel.putClientProperty("usuarioNombreField", usuarioNombreField);
        panel.putClientProperty("usuarioContrasenaField", usuarioContrasenaField);

        return panel;
    }

    // Método para procesar los datos ingresados y realizar las validaciones
    public static void procesarEntradaAdministrador(JPanel panel) {
        JTextField nombreField = (JTextField) panel.getClientProperty("nombreField");
        JTextField primerApellidoField = (JTextField) panel.getClientProperty("primerApellidoField");
        JTextField segundoApellidoField = (JTextField) panel.getClientProperty("segundoApellidoField");
        JTextField domicilioField = (JTextField) panel.getClientProperty("domicilioField");
        JTextField numeroMovilField = (JTextField) panel.getClientProperty("numeroMovilField");
        JTextField usuarioNombreField = (JTextField) panel.getClientProperty("usuarioNombreField");
        JPasswordField usuarioContrasenaField = (JPasswordField) panel.getClientProperty("usuarioContrasenaField");

        String nombre = nombreField.getText().trim();
        String primerApellido = primerApellidoField.getText().trim();
        String segundoApellido = segundoApellidoField.getText().trim();
        String domicilio = domicilioField.getText().trim();
        String numeroMovil = numeroMovilField.getText().trim();
        String usuarioNombre = usuarioNombreField.getText().trim();
        String usuarioContrasena = new String(usuarioContrasenaField.getPassword()).trim();

        if (!validarEntrada(nombre, "[a-zA-Z]+", "Nombre solo puede contener letras") ||
            !validarEntrada(primerApellido, "[a-zA-Z]+", "Primer Apellido solo puede contener letras") ||
            !validarEntrada(segundoApellido, "[a-zA-Z]*", "Segundo Apellido solo puede contener letras") ||
            !validarEntrada(domicilio, "[a-zA-Z0-9 ]+", "Domicilio solo puede contener letras y números") ||
            !validarEntrada(numeroMovil, "\\d+", "Número Móvil solo puede contener números") ||
            !validarEntrada(usuarioNombre, "[a-zA-Z0-9]+", "Usuario Nombre solo puede contener letras y números") ||
            !validarEntrada(usuarioContrasena, "[a-zA-Z0-9]+", "Usuario Contraseña solo puede contener letras y números")) {
            return;
        }

        if (usuarioNombreAdministradorExiste(usuarioNombre)) {
            JOptionPane.showMessageDialog(null, "El nombre de usuario ya existe. Por favor elige otro.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String contrasenaHasheada = hashPassword(usuarioContrasena);

        agregarAdministradorABaseDeDatos(nombre, primerApellido, segundoApellido, domicilio, numeroMovil, usuarioNombre, contrasenaHasheada);
    }

    // Método para verificar si el nombre de usuario ya existe en la tabla administradores
    public static boolean usuarioNombreAdministradorExiste(String usuarioNombre) {
        String query = "SELECT COUNT(*) FROM administradores WHERE usuario_nombre = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, usuarioNombre);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1) > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al verificar el nombre de usuario.", "Error", JOptionPane.ERROR_MESSAGE);
            return true;
        }
    }

    // Método para agregar un administrador a la base de datos
    public static void agregarAdministradorABaseDeDatos(String nombre, String primerApellido, String segundoApellido, String domicilio, String numeroMovil, String usuarioNombre, String contrasenaHasheada) {
        String insertQuery = "INSERT INTO administradores (nombre, primer_apellido, segundo_apellido, domicilio, numero_movil, usuario_nombre, usuario_contraseña, permisos) VALUES (?, ?, ?, ?, ?, ?, ?, 1)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(insertQuery)) {

            statement.setString(1, nombre);
            statement.setString(2, primerApellido);
            statement.setString(3, segundoApellido);
            statement.setString(4, domicilio);
            statement.setString(5, numeroMovil);
            statement.setString(6, usuarioNombre);
            statement.setString(7, contrasenaHasheada);

            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Administrador agregado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al agregar administrador.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        }
    
    public static void crearCarpetaRespaldo() {
        String userHome = System.getProperty("user.home");
        File carpeta = new File(userHome + "/Desktop/RespaldoBD");
        if (!carpeta.exists()) {
            if (carpeta.mkdir()) {
                System.out.println("Carpeta de respaldo creada correctamente.");
            } else {
                System.out.println("Error al crear la carpeta de respaldo.");
            }
        }
    }
    
    public static void guardarBaseDeDatos() {
        crearCarpetaRespaldo(); // Crea la carpeta en el escritorio si no existe
        String userHome = System.getProperty("user.home");
        String rutaRespaldo = userHome + "/Desktop/RespaldoBD/respaldo_bookshelter.sql";

        try {
            // Ruta completa al ejecutable mysqldump
            String comando = String.format(
                "\"C:\\Archivos de programa\\MariaDB 11.4\\bin\\mysqldump\" -u%s -p%s --databases bookshelter -r %s",
                "toadadministrador", "123", rutaRespaldo
            );

            Process proceso = Runtime.getRuntime().exec(comando);
            int procesoTerminado = proceso.waitFor();

            if (procesoTerminado == 0) {
                System.out.println("Copia de seguridad creada correctamente en: " + rutaRespaldo);
                // Mostrar notificación de éxito
                JOptionPane.showMessageDialog(
                    null,
                    "Copia de seguridad creada correctamente.\nGuardada en: " + rutaRespaldo,
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                System.err.println("Error al crear la copia de seguridad.");
                // Mostrar notificación de error
                JOptionPane.showMessageDialog(
                    null,
                    "Error al crear la copia de seguridad.\nPor favor, verifica la configuración.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Mostrar notificación de excepción
            JOptionPane.showMessageDialog(
                null,
                "Ocurrió un error inesperado: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    public static void cargarBaseDeDatos() {
        String userHome = System.getProperty("user.home");
        String rutaRespaldo = userHome + "/Desktop/RespaldoBD/respaldo_bookshelter.sql";

        File archivoRespaldo = new File(rutaRespaldo);
        if (!archivoRespaldo.exists()) {
            System.err.println("El archivo de respaldo no existe: " + rutaRespaldo);
            return;
        }

        try {
            // Ruta completa al ejecutable mysql
            String comando = String.format(
                "\"C:\\Archivos de programa\\MariaDB 11.4\\bin\\mysql\" -u%s -p%s bookshelter < %s",
                "toadadministrador", "123", rutaRespaldo
            );

            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", comando);
            processBuilder.redirectErrorStream(true);
            Process proceso = processBuilder.start();

            // Leer la salida del proceso
            BufferedReader reader = new BufferedReader(new InputStreamReader(proceso.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int procesoTerminado = proceso.waitFor();
            if (procesoTerminado == 0) {
                System.out.println("Base de datos restaurada correctamente desde: " + rutaRespaldo);
            } else {
                System.err.println("Error al restaurar la base de datos.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    public static void mostrarAuditoriaPorFecha(JTable table, String fecha) {
        String query = "SELECT * FROM auditoria WHERE DATE(fecha_hora) = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, fecha);
            try (ResultSet resultSet = statement.executeQuery()) {
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.setRowCount(0); // Limpia la tabla
                int columnCount = resultSet.getMetaData().getColumnCount();
                while (resultSet.next()) {
                    Object[] rowData = new Object[columnCount];
                    for (int i = 0; i < columnCount; i++) {
                        rowData[i] = resultSet.getObject(i + 1);
                    }
                    model.addRow(rowData);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar la auditoría.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void registrarAccion(String tabla, String accion, Integer adminId) {
        System.out.println("Registrar acción: tabla=" + tabla + ", acción=" + accion + ", adminId=" + adminId); // Depuración

        String sql = "INSERT INTO " + tabla + " (accion, admin_id, fecha_hora) VALUES (?, ?, NOW())";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, accion);
            if (adminId != null) {
                statement.setInt(2, adminId);
            } else {
                statement.setObject(2, null); // Si no hay adminId, registrar NULL
            }

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al registrar la acción en la tabla " + tabla + ": " + e.getMessage());
        }
    }
}