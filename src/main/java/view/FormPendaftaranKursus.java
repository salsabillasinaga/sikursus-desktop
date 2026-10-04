package view;

import model.Kursus;
import javax.swing.JOptionPane;

public class FormPendaftaranKursus extends javax.swing.JFrame {

    public FormPendaftaranKursus() {
        initComponents();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtKodeKursus = new javax.swing.JTextField();
        btnProses = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtHasil = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        cmbNamaKursus = new javax.swing.JComboBox<>();
        cmbLevel = new javax.swing.JComboBox<>();
        txtBiayaKursus = new javax.swing.JTextField();
        txtDiskon = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(255, 204, 204));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 0, 51));
        jLabel1.setText("Form Pendaftaran Kursus");

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(51, 0, 51));
        jLabel2.setText("Kode Kursus");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 0, 51));
        jLabel3.setText("Nama Kursus");

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(51, 0, 51));
        jLabel4.setText("Biaya Kursus");

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(51, 0, 51));
        jLabel5.setText("Diskon (%)");

        btnProses.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        btnProses.setForeground(new java.awt.Color(51, 0, 51));
        btnProses.setText("Proses");
        btnProses.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProsesActionPerformed(evt);
            }
        });

        btnReset.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        btnReset.setForeground(new java.awt.Color(51, 0, 51));
        btnReset.setText("Reset");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        txtHasil.setColumns(20);
        txtHasil.setRows(5);
        jScrollPane2.setViewportView(txtHasil);

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel6.setText("Level");

        cmbNamaKursus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "JAVA", "DATA SAINS", "UI/UX" }));

        cmbLevel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "BASIC", "INTEMEDIANCE" }));
        cmbLevel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbLevelActionPerformed(evt);
            }
        });

        txtDiskon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDiskonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(259, 259, 259)
                        .addComponent(btnProses)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnReset)
                        .addGap(57, 57, 57))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel5))
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(117, 117, 117)
                                        .addComponent(jScrollPane2))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(109, 109, 109)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtDiskon)
                                            .addComponent(txtBiayaKursus)))))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(108, 108, 108)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cmbNamaKursus, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cmbLevel, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(109, 109, 109)
                                .addComponent(txtKodeKursus, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addGap(48, 48, 48))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(169, 169, 169))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(21, 21, 21)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtKodeKursus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbNamaKursus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBiayaKursus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(23, 23, 23)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDiskon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(32, 32, 32)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnProses)
                    .addComponent(btnReset))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnProsesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProsesActionPerformed
try {
            // 1. Membaca input dari GUI
            String kode = txtKodeKursus.getText().trim();
            String nama = cmbNamaKursus.getSelectedItem().toString();
            String level = cmbLevel.getSelectedItem().toString();
            double biaya = Double.parseDouble(txtBiayaKursus.getText().trim());
            double diskon = Double.parseDouble(txtDiskon.getText().trim());

            // 2. Membuat object Kursus menggunakan constructor lengkap
            Kursus kursus = new Kursus(kode, nama, level, biaya);

            // 3. Memanggil method perhitungan dari model
            double total = kursus.hitungBiayaSetelahDiskon(diskon);

            // 4. Menampilkan hasil ke JTextArea (txtHasil)
            txtHasil.setText("=== HASIL PENDAFTARAN KURSUS ===\n" +
                             "Kode Kursus    : " + kode + "\n" +
                             "Nama Kursus    : " + nama + "\n" +
                             "Level          : " + level + "\n" +
                             "Biaya Awal     : Rp " + String.format("%,.0f", biaya) + "\n" +
                             "Diskon         : " + diskon + "%\n" +
                             "---------------------------------\n" +
                             "Total Biaya    : Rp " + String.format("%,.0f", total));
                         
        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Input pada kolom Biaya Kursus atau Diskon harus berupa angka!", 
                "Error Input", 
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnProsesActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
txtKodeKursus.setText("");
        txtBiayaKursus.setText("");
        txtDiskon.setText("");
        cmbNamaKursus.setSelectedIndex(0);
        cmbLevel.setSelectedIndex(0);
        txtHasil.setText("");
        txtKodeKursus.requestFocus();
    }//GEN-LAST:event_btnResetActionPerformed

    private void txtDiskonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDiskonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDiskonActionPerformed

    private void cmbLevelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbLevelActionPerformed
        cmbLevel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "BASIC", "INTEMEDIANCE" }));
    }//GEN-LAST:event_cmbLevelActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new FormPendaftaranKursus().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnProses;
    private javax.swing.JButton btnReset;
    private javax.swing.JComboBox<String> cmbLevel;
    private javax.swing.JComboBox<String> cmbNamaKursus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField txtBiayaKursus;
    private javax.swing.JTextField txtDiskon;
    private javax.swing.JTextArea txtHasil;
    private javax.swing.JTextField txtKodeKursus;
    // End of variables declaration//GEN-END:variables
}
