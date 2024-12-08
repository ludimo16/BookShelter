/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package App;

import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
/**
 *
 * @author ludimo16
 */
public class ViewLogin extends javax.swing.JPanel {

    private int intentosFallidos = 0; // Contador de intentos fallidos
    private javax.swing.Timer timer; // Temporizador para el bloqueo    
    /**
     * Creates new form ViewLogin
     */
    public ViewLogin() {
        initComponents();
        barraDesbloqueo.setVisible(false);

         // Agrega un ActionListener a textContraseña para detectar el "Enter"
        textContraseña.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnLoginActionPerformed(evt);
        }
        });
    }
    
    private void manejarIntentoFallido() {
        intentosFallidos++;
        if (intentosFallidos >= 3) {
            JOptionPane.showMessageDialog(this, "Demasiados intentos fallidos. Acceso bloqueado por 10 segundos.", "Acceso Bloqueado", JOptionPane.WARNING_MESSAGE);
            Utility.registrarAccion("auditoria_administradores", "Administrador bloqueado temporalmente tras 3 intentos fallidos de login.", null);
            bloquearAcceso();
            iniciarBloqueo();
        } else {
            JOptionPane.showMessageDialog(this, "Nombre de usuario o contraseña incorrectos. Intentos restantes: " + (3 - intentosFallidos), "Error de Inicio de Sesión", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void bloquearAcceso() {
        textAdministrador.setEnabled(false);
        textContraseña.setEnabled(false);
        btnLogin.setEnabled(false);
    }

    private void desbloquearAcceso() {
        textAdministrador.setEnabled(true);
        textContraseña.setEnabled(true);
        btnLogin.setEnabled(true);
    }

    public void iniciarBloqueo() {
    barraDesbloqueo.setValue(0);
    barraDesbloqueo.setVisible(true);

    // Configuramos un SwingWorker para manejar el progreso de la barra de manera más suave
    SwingWorker<Void, Integer> worker = new SwingWorker<>() {
        @Override
        protected Void doInBackground() throws Exception {
            int duracion = 100; // 100 actualizaciones en total (equivale a 10 segundos si cada actualización es de 100 ms)
            for (int i = 1; i <= duracion; i++) {
                Thread.sleep(100); // Espera de 100 ms entre cada actualización
                publish(i); // Publica el progreso (1% por cada iteración)
            }
            return null;
        }

        @Override
        protected void process(List<Integer> chunks) {
            // Actualiza la barra de progreso con el último valor publicado
            barraDesbloqueo.setValue(chunks.get(chunks.size() - 1));
        }

        @Override
        protected void done() {
            // Oculta la barra y desbloquea los campos después de completar el bloqueo
            barraDesbloqueo.setVisible(false);
            desbloquearAcceso();
            intentosFallidos = 0; // Reinicia el contador de intentos fallidos
            JOptionPane.showMessageDialog(null, "Desbloqueo completado. Intente de nuevo.");
        }
    };

    worker.execute(); // Inicia el SwingWorker
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        textAdministrador = new javax.swing.JTextField();
        textContraseña = new javax.swing.JPasswordField();
        btnLogin = new javax.swing.JButton();
        infoUsuario = new javax.swing.JLabel();
        infoPassword = new javax.swing.JLabel();
        barraDesbloqueo = new javax.swing.JProgressBar();
        labelLogo = new javax.swing.JLabel();
        labelLogo1 = new javax.swing.JLabel();
        fondoDePantalla = new javax.swing.JLabel();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        textAdministrador.setFont(new java.awt.Font("OCR A Extended", 0, 24)); // NOI18N
        textAdministrador.setPreferredSize(new java.awt.Dimension(200, 50));
        add(textAdministrador, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 340, -1, -1));

        textContraseña.setFont(new java.awt.Font("OCR A Extended", 0, 12)); // NOI18N
        textContraseña.setPreferredSize(new java.awt.Dimension(200, 50));
        add(textContraseña, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 400, -1, -1));

        btnLogin.setFont(new java.awt.Font("OCR A Extended", 0, 24)); // NOI18N
        btnLogin.setText("Iniciar");
        btnLogin.setMaximumSize(new java.awt.Dimension(150, 50));
        btnLogin.setMinimumSize(new java.awt.Dimension(150, 50));
        btnLogin.setPreferredSize(new java.awt.Dimension(200, 50));
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });
        add(btnLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 460, -1, -1));

        infoUsuario.setFont(new java.awt.Font("OCR A Extended", 0, 24)); // NOI18N
        infoUsuario.setText("Usuario");
        add(infoUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 350, -1, -1));

        infoPassword.setFont(new java.awt.Font("OCR A Extended", 0, 24)); // NOI18N
        infoPassword.setText("Contraseña");
        add(infoPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 410, -1, -1));

        barraDesbloqueo.setFont(new java.awt.Font("OCR A Extended", 0, 24)); // NOI18N
        barraDesbloqueo.setPreferredSize(new java.awt.Dimension(200, 25));
        add(barraDesbloqueo, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 530, -1, -1));

        labelLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/book_shelf_icon.png"))); // NOI18N
        labelLogo.setPreferredSize(new java.awt.Dimension(250, 250));
        add(labelLogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 320, -1, -1));

        labelLogo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/book_shelf_icon.png"))); // NOI18N
        add(labelLogo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 320, -1, -1));

        fondoDePantalla.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/minimalist_books_image_1500x900.png"))); // NOI18N
        fondoDePantalla.setMaximumSize(new java.awt.Dimension(1500, 800));
        fondoDePantalla.setMinimumSize(new java.awt.Dimension(1500, 800));
        fondoDePantalla.setPreferredSize(new java.awt.Dimension(1500, 800));
        add(fondoDePantalla, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
    String usuarioNombre = textAdministrador.getText().trim();
    String contraseñaIngresada = new String(textContraseña.getPassword()).trim();

    if (usuarioNombre.isEmpty() || contraseñaIngresada.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
        return;
    }

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(
             "SELECT id FROM administradores WHERE usuario_nombre = ? AND usuario_contraseña = ?")) {

        // Cifrar la contraseña ingresada
        String contraseñaHasheada = Utility.hashPassword(contraseñaIngresada);
        statement.setString(1, usuarioNombre);
        statement.setString(2, contraseñaHasheada);

        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                int adminId = resultSet.getInt("id");

                // Registrar el inicio de sesión
                JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso. Bienvenido, " + usuarioNombre + "!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                Utility.registrarAccion("auditoria_administradores", "Inicio de sesión exitoso", adminId);

                // Guardar el adminId en AppBase
                AppBase appBase = (AppBase) SwingUtilities.getWindowAncestor(this);
                if (appBase != null) {
                    appBase.setAdminId(adminId); // Guarda el ID del administrador en AppBase
                    appBase.desbloquearComponentes();
                }

                // Bloquear campos de login para evitar nuevos intentos
                textAdministrador.setEnabled(false);
                textContraseña.setEnabled(false);
                btnLogin.setEnabled(false);

                // Limpiar campos (opcional, ya que están bloqueados)
                textAdministrador.setText("");
                textContraseña.setText("");
            } else {
                manejarIntentoFallido();
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al conectar con la base de datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_btnLoginActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar barraDesbloqueo;
    private javax.swing.JButton btnLogin;
    private javax.swing.JLabel fondoDePantalla;
    private javax.swing.JLabel infoPassword;
    private javax.swing.JLabel infoUsuario;
    private javax.swing.JLabel labelLogo;
    private javax.swing.JLabel labelLogo1;
    private javax.swing.JTextField textAdministrador;
    private javax.swing.JPasswordField textContraseña;
    // End of variables declaration//GEN-END:variables
}