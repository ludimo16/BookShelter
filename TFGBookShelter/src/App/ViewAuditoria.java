/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package App;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ludimo16
 */
public class ViewAuditoria extends javax.swing.JPanel {

    /**
     * Creates new form ViewAuditoria
     */
    public ViewAuditoria() {
        initComponents();
         // Llenar tabla con datos del día actual al abrir el panel
        llenarJTableAuditoria("auditoria_administradores", null, null, null);
        llenarJTableAuditoriaUsuarios(null, null, null);

    }
    
    public void llenarJTableAuditoria(String tabla, String fecha, String accion, String id) {
        StringBuilder queryBuilder = new StringBuilder(
            "SELECT id_accion, accion, admin_id, fecha_hora FROM `" + tabla + "` WHERE 1=1"
        );

        // Condiciones dinámicas
        if (fecha != null && !fecha.isEmpty()) {
            queryBuilder.append(" AND DATE(fecha_hora) = STR_TO_DATE(?, '%d/%m/%Y')");
        } else {
            queryBuilder.append(" AND DATE(fecha_hora) = CURDATE()"); // Fecha actual si no se especifica una
        }
        if (accion != null && !accion.isEmpty()) {
            queryBuilder.append(" AND accion LIKE ?");
        }
        if (id != null && !id.isEmpty()) {
            queryBuilder.append(" AND admin_id = ?");
        }

        // Obtener el modelo de la tabla correspondiente
        DefaultTableModel modelo = tabla.equals("auditoria administradores")
            ? (DefaultTableModel) tablaAuditoriaUsuarios.getModel()
            : (DefaultTableModel) tablaAuditoriaAministrador.getModel();

        modelo.setRowCount(0); // Limpia la tabla antes de llenarla

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(queryBuilder.toString())) {

            int paramIndex = 1;
            if (fecha != null && !fecha.isEmpty()) {
                statement.setString(paramIndex++, fecha);
            }
            if (accion != null && !accion.isEmpty()) {
                statement.setString(paramIndex++, "%" + accion + "%");
            }
            if (id != null && !id.isEmpty()) {
                statement.setInt(paramIndex++, Integer.parseInt(id));
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                boolean hayRegistros = false;
                while (resultSet.next()) {
                    hayRegistros = true;
                    modelo.addRow(new Object[]{
                        resultSet.getInt("id_accion"),
                        resultSet.getString("accion"),
                        resultSet.getInt("admin_id"),
                        resultSet.getString("fecha_hora")
                    });
                }

                // Si no hay registros, muestra un mensaje
                if (!hayRegistros) {
                    JOptionPane.showMessageDialog(
                        this,
                        "No se encontraron registros en la tabla " + tabla + ".",
                        "Sin Resultados",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                this,
                "Error al consultar auditorías: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    public void llenarJTableAuditoriaUsuarios(String fecha, String accion, String id) {
        StringBuilder queryBuilder = new StringBuilder(
            "SELECT id_accion, accion, usuario_id, fecha_hora FROM `auditoria_usuarios` WHERE 1=1"
        );

        // Condiciones dinámicas
        if (fecha != null && !fecha.isEmpty()) {
            queryBuilder.append(" AND DATE(fecha_hora) = STR_TO_DATE(?, '%d/%m/%Y')");
        } else {
            queryBuilder.append(" AND DATE(fecha_hora) = CURDATE()"); // Fecha actual si no se especifica una
        }
        if (accion != null && !accion.isEmpty()) {
            queryBuilder.append(" AND accion LIKE ?");
        }
        if (id != null && !id.isEmpty()) {
            queryBuilder.append(" AND usuario_id = ?");
        }

        // Obtener el modelo de la tabla correspondiente
        DefaultTableModel modelo = (DefaultTableModel) tablaAuditoriaUsuarios.getModel();
        modelo.setRowCount(0); // Limpia la tabla antes de llenarla

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(queryBuilder.toString())) {

            int paramIndex = 1;
            if (fecha != null && !fecha.isEmpty()) {
                statement.setString(paramIndex++, fecha);
            }
            if (accion != null && !accion.isEmpty()) {
                statement.setString(paramIndex++, "%" + accion + "%");
            }
            if (id != null && !id.isEmpty()) {
                statement.setInt(paramIndex++, Integer.parseInt(id));
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                boolean hayRegistros = false;
                while (resultSet.next()) {
                    hayRegistros = true;
                    modelo.addRow(new Object[]{
                        resultSet.getInt("id_accion"),
                        resultSet.getString("accion"),
                        resultSet.getInt("usuario_id"),
                        resultSet.getString("fecha_hora")
                    });
                }

                // Si no hay registros, muestra un mensaje
                if (!hayRegistros) {
                    JOptionPane.showMessageDialog(
                        this,
                        "No se encontraron registros en la tabla auditoria_usuarios.",
                        "Sin Resultados",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                this,
                "Error al consultar auditorías de usuarios: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }    
    
    private boolean esFechaValida(String fecha) {
        if (fecha == null || fecha.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Por favor, ingresa una fecha en formato DD/MM/YYYY.",
                "Fecha Requerida",
                JOptionPane.WARNING_MESSAGE
            );
            return false;
        }
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        formato.setLenient(false);
        try {
            formato.parse(fecha);
            return true;
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(
                this,
                "La fecha ingresada no es válida. Usa el formato DD/MM/YYYY.",
                "Formato Incorrecto",
                JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tablaAuditoriaUsuarios = new javax.swing.JTable();
        textConsultarDiaAdministrador = new javax.swing.JTextField();
        btnConsultarDiaAdministrador = new javax.swing.JButton();
        textConsultarIDAdministrador = new javax.swing.JTextField();
        btnConsultarIDAdministrador = new javax.swing.JButton();
        textConsultarAccionAdministrador = new javax.swing.JTextField();
        btnConsultarAccionAdministrador = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaAuditoriaAministrador = new javax.swing.JTable();
        textConsultarDiaUsuario = new javax.swing.JTextField();
        btnConsultarDiaUsuario = new javax.swing.JButton();
        textConsultarIDUsuario = new javax.swing.JTextField();
        btnConsultarIDUsuario = new javax.swing.JButton();
        textConsultarAccionUsuario = new javax.swing.JTextField();
        btnConsultarAccionUsuario = new javax.swing.JButton();
        fondoDePantalla = new javax.swing.JLabel();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tablaAuditoriaUsuarios.setFont(new java.awt.Font("OCR A Extended", 0, 12)); // NOI18N
        tablaAuditoriaUsuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "id_accion", "accion", "id_usuario", "fecha_hora"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tablaAuditoriaUsuarios);

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 410, 1120, 380));

        textConsultarDiaAdministrador.setFont(new java.awt.Font("OCR A Extended", 0, 24)); // NOI18N
        add(textConsultarDiaAdministrador, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 280, -1));

        btnConsultarDiaAdministrador.setFont(new java.awt.Font("OCR A Extended", 0, 24)); // NOI18N
        btnConsultarDiaAdministrador.setText("Consultar Dia");
        btnConsultarDiaAdministrador.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnConsultarDiaAdministradorMouseClicked(evt);
            }
        });
        add(btnConsultarDiaAdministrador, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 280, -1));

        textConsultarIDAdministrador.setFont(new java.awt.Font("OCR A Extended", 0, 24)); // NOI18N
        add(textConsultarIDAdministrador, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 280, -1));

        btnConsultarIDAdministrador.setFont(new java.awt.Font("OCR A Extended", 0, 24)); // NOI18N
        btnConsultarIDAdministrador.setText("Consultar ID");
        btnConsultarIDAdministrador.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnConsultarIDAdministradorMouseClicked(evt);
            }
        });
        add(btnConsultarIDAdministrador, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 280, -1));

        textConsultarAccionAdministrador.setFont(new java.awt.Font("OCR A Extended", 0, 24)); // NOI18N
        add(textConsultarAccionAdministrador, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, 280, -1));

        btnConsultarAccionAdministrador.setFont(new java.awt.Font("OCR A Extended", 0, 24)); // NOI18N
        btnConsultarAccionAdministrador.setText("Consultar Accion");
        btnConsultarAccionAdministrador.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnConsultarAccionAdministradorMouseClicked(evt);
            }
        });
        add(btnConsultarAccionAdministrador, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, 280, -1));

        tablaAuditoriaAministrador.setFont(new java.awt.Font("OCR A Extended", 0, 12)); // NOI18N
        tablaAuditoriaAministrador.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "id_accion", "accion", "id_administrador", "fecha_hora"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tablaAuditoriaAministrador);

        add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 20, 1120, 380));

        textConsultarDiaUsuario.setFont(new java.awt.Font("OCR A Extended", 0, 24)); // NOI18N
        add(textConsultarDiaUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 400, 280, -1));

        btnConsultarDiaUsuario.setFont(new java.awt.Font("OCR A Extended", 0, 24)); // NOI18N
        btnConsultarDiaUsuario.setText("Consultar Dia");
        btnConsultarDiaUsuario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnConsultarDiaUsuarioMouseClicked(evt);
            }
        });
        add(btnConsultarDiaUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 440, 280, -1));

        textConsultarIDUsuario.setFont(new java.awt.Font("OCR A Extended", 0, 24)); // NOI18N
        add(textConsultarIDUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 500, 280, -1));

        btnConsultarIDUsuario.setFont(new java.awt.Font("OCR A Extended", 0, 24)); // NOI18N
        btnConsultarIDUsuario.setText("Consultar ID");
        btnConsultarIDUsuario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnConsultarIDUsuarioMouseClicked(evt);
            }
        });
        add(btnConsultarIDUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 540, 280, -1));

        textConsultarAccionUsuario.setFont(new java.awt.Font("OCR A Extended", 0, 24)); // NOI18N
        add(textConsultarAccionUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 600, 280, -1));

        btnConsultarAccionUsuario.setFont(new java.awt.Font("OCR A Extended", 0, 24)); // NOI18N
        btnConsultarAccionUsuario.setText("Consultar Accion");
        btnConsultarAccionUsuario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnConsultarAccionUsuarioMouseClicked(evt);
            }
        });
        add(btnConsultarAccionUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 640, 280, -1));

        fondoDePantalla.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/minimalist_books_image_1500x900.png"))); // NOI18N
        fondoDePantalla.setMaximumSize(new java.awt.Dimension(1500, 800));
        fondoDePantalla.setMinimumSize(new java.awt.Dimension(1500, 800));
        fondoDePantalla.setPreferredSize(new java.awt.Dimension(1500, 800));
        add(fondoDePantalla, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void btnConsultarDiaAdministradorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConsultarDiaAdministradorMouseClicked
    String fecha = textConsultarDiaAdministrador.getText().trim();
    if (!esFechaValida(fecha)) {
        return; // Si la fecha no es válida, detiene la ejecución
    }

    // Llenar la tabla directamente sin mostrar mensaje
    llenarJTableAuditoria("auditoria_administradores", fecha, null, null);
    }//GEN-LAST:event_btnConsultarDiaAdministradorMouseClicked

    private void btnConsultarIDAdministradorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConsultarIDAdministradorMouseClicked
    String fecha = textConsultarDiaAdministrador.getText().trim(); // Puede ser vacío
    String idAdministrador = textConsultarIDAdministrador.getText().trim();
    String accion = textConsultarAccionAdministrador.getText().trim(); // Puede ser vacío

    if (idAdministrador.isEmpty() || !idAdministrador.matches("\\d+")) {
        JOptionPane.showMessageDialog(
            this,
            "Por favor, ingresa un ID válido (solo números).",
            "Formato Inválido",
            JOptionPane.ERROR_MESSAGE
        );
        return;
    }

    // Llama al método de llenar tabla con los criterios ingresados
    llenarJTableAuditoria("auditoria_administradores", fecha, accion.isEmpty() ? null : accion, idAdministrador);
    }//GEN-LAST:event_btnConsultarIDAdministradorMouseClicked

    private void btnConsultarAccionAdministradorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConsultarAccionAdministradorMouseClicked
    String fecha = textConsultarDiaAdministrador.getText().trim(); // Puede ser vacío
    String accion = textConsultarAccionAdministrador.getText().trim();
    String idAdministrador = textConsultarIDAdministrador.getText().trim(); // Puede ser vacío

    if (accion.isEmpty()) {
        JOptionPane.showMessageDialog(
            this,
            "Por favor, ingresa una acción para buscar.",
            "Acción Vacía",
            JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    // Llama al método de llenar tabla con los criterios ingresados
    llenarJTableAuditoria("auditoria_administradores", fecha, accion, idAdministrador.isEmpty() ? null : idAdministrador);
    }//GEN-LAST:event_btnConsultarAccionAdministradorMouseClicked

    private void btnConsultarDiaUsuarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConsultarDiaUsuarioMouseClicked
        String fecha = textConsultarDiaUsuario.getText().trim();
        if (!esFechaValida(fecha)) {
            return; // Detener si la fecha no es válida
        }

            llenarJTableAuditoriaUsuarios(fecha, null, null);
    }//GEN-LAST:event_btnConsultarDiaUsuarioMouseClicked

    private void btnConsultarIDUsuarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConsultarIDUsuarioMouseClicked
        String idUsuario = textConsultarIDUsuario.getText().trim();
        if (idUsuario.isEmpty() || !idUsuario.matches("\\d+")) {
            JOptionPane.showMessageDialog(
                this,
                "Por favor, ingresa un ID de usuario válido (solo números).",
                "Formato Inválido",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

         llenarJTableAuditoriaUsuarios(null, null, idUsuario);
    }//GEN-LAST:event_btnConsultarIDUsuarioMouseClicked

    private void btnConsultarAccionUsuarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConsultarAccionUsuarioMouseClicked
        String accion = textConsultarAccionUsuario.getText().trim();
        if (accion.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Por favor, ingresa una acción para buscar.",
                "Acción Vacía",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        llenarJTableAuditoria("auditoria_usuarios", null, accion, null);
    }//GEN-LAST:event_btnConsultarAccionUsuarioMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConsultarAccionAdministrador;
    private javax.swing.JButton btnConsultarAccionUsuario;
    private javax.swing.JButton btnConsultarDiaAdministrador;
    private javax.swing.JButton btnConsultarDiaUsuario;
    private javax.swing.JButton btnConsultarIDAdministrador;
    private javax.swing.JButton btnConsultarIDUsuario;
    private javax.swing.JLabel fondoDePantalla;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tablaAuditoriaAministrador;
    private javax.swing.JTable tablaAuditoriaUsuarios;
    private javax.swing.JTextField textConsultarAccionAdministrador;
    private javax.swing.JTextField textConsultarAccionUsuario;
    private javax.swing.JTextField textConsultarDiaAdministrador;
    private javax.swing.JTextField textConsultarDiaUsuario;
    private javax.swing.JTextField textConsultarIDAdministrador;
    private javax.swing.JTextField textConsultarIDUsuario;
    // End of variables declaration//GEN-END:variables
}
