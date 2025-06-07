/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package application.views;

import application.dao.AlternatifDao;
import application.dao.CriteriaDao;
import application.dao.KaryawanDao;
import application.dao.SubCriteriaDao;
import application.daoimpl.AlternatifDaoImpl;
import application.daoimpl.CriteriaDaoImpl;
import application.daoimpl.KaryawanDaoImpl;
import application.daoimpl.SubCriteriaDaoImpl;
import application.models.AlternatifModel;
import application.models.CriteriaModel;
import application.models.KaryawanModel;
import application.models.SubCriteriaModel;
import application.utils.ComboBoxItem;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author mhdja
 */
public class AlternatifView extends javax.swing.JPanel {
    public final CriteriaDao criteriaDao;
    public final SubCriteriaDao subCriteriaDao;
    public final KaryawanDao karyawanDao;
    public final AlternatifDao alternatifDao;
    private JLabel labelContent;
    
    public void getAllData() {
        List<AlternatifModel> alternatifList = alternatifDao.findAll(); // Mengambil data dari metode findAll()

        // Membuat model untuk jTable1 dengan kolom-kolom yang sesuai
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"Nama Suku Cadang", "Nama Kriteria", "Bobot Alternatif"}); // Menentukan nama kolom

        // Mengisi model dengan data dari alternatifList
        for (AlternatifModel alternatif : alternatifList) {
            model.addRow(new Object[]{
                alternatif.getNameAlternatif(), // Nama Pelanggan
                alternatif.getNameKriteria(),   // Nama Kriteria
                alternatif.getBobotAlternatif(), // Bobot Alternatif
            });
        }

        // Set model ke jTable1
        jTable1.setModel(model);
    }


    /**
     * Creates new form AlternatifView
     */
    public AlternatifView() {
      this.criteriaDao = new CriteriaDaoImpl();
      this.subCriteriaDao = new SubCriteriaDaoImpl();
      this.karyawanDao = new KaryawanDaoImpl();
      this.alternatifDao = new AlternatifDaoImpl();

      initComponents();
      getAllData();

      List<CriteriaModel> criteriaList = criteriaDao.findAll();
      List<JComboBox<ComboBoxItem>> comboBoxes = new ArrayList<>();

      jPanel1.setLayout(new GridBagLayout());
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(5, 10, 5, 10);
      gbc.fill = GridBagConstraints.HORIZONTAL;
      gbc.anchor = GridBagConstraints.WEST;

      jPanel1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

      // === Pelanggan ===
      JLabel pelangganLabel = new JLabel("Suku Cadang: ");
      pelangganLabel.setFont(new Font("Arial", Font.BOLD, 14));
      pelangganLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

      JComboBox<ComboBoxItem> pelangganComboBox = new JComboBox<>();
      pelangganComboBox.setMaximumSize(new Dimension(300, 30));
      pelangganComboBox.setFont(new Font("Arial", Font.PLAIN, 12));

      List<KaryawanModel> pelangganList = karyawanDao.findAll();
      pelangganComboBox.addItem(new ComboBoxItem("Pilih Suku Cadang", -1));
      for (KaryawanModel pelanggan : pelangganList) {
          pelangganComboBox.addItem(new ComboBoxItem(pelanggan.getName(), pelanggan.getId()));
      }

      // Tambahkan Pelanggan di baris pertama (di atas semua)
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.gridwidth = 4;
      jPanel1.add(pelangganLabel, gbc);

      gbc.gridy++;
      jPanel1.add(pelangganComboBox, gbc);

      // Reset posisi awal untuk kriteria
      gbc.gridwidth = 1;
      int maxRow = 4;
      int row = 0;
      int col = 0;
      int maxUsedRow = 0;

      List<AlternatifModel> alternatifList = new ArrayList<>();

      // === Kriteria dan Subkriteria ===
        for (CriteriaModel criteria : criteriaList) {
            JLabel label = new JLabel(criteria.getName() + ": ");
            label.setFont(new Font("Arial", Font.BOLD, 14));

            JComboBox<ComboBoxItem> comboBox = new JComboBox<>();
            comboBox.setMaximumSize(new Dimension(300, 30));
            comboBox.setFont(new Font("Arial", Font.PLAIN, 12));

            List<SubCriteriaModel> subCriteriaList = subCriteriaDao.findAllByCriteriaId(criteria.getId());
            for (SubCriteriaModel subCriteria : subCriteriaList) {
                comboBox.addItem(new ComboBoxItem(subCriteria.getDeskripsi(), subCriteria.getId()));
            }

            comboBoxes.add(comboBox);

            gbc.gridx = col * 2;
            gbc.gridy = row + 2;
            jPanel1.add(label, gbc);

            gbc.gridx = col * 2 + 1;
            jPanel1.add(comboBox, gbc);

            maxUsedRow = Math.max(maxUsedRow, gbc.gridy);

            row++;
            if (row >= maxRow) {
                row = 0;
                col++;
            }
        }
      // === Tombol ===
      JPanel buttonPanel = new JPanel();
      buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
      buttonPanel.setBackground(Color.WHITE);

      JButton simpanButton = new JButton("Simpan");

      simpanButton.addActionListener(e -> {
          ComboBoxItem selectedPelanggan = (ComboBoxItem) pelangganComboBox.getSelectedItem();
          if (selectedPelanggan == null || selectedPelanggan.getId() == -1) {
              JOptionPane.showMessageDialog(this, "Pilih pelanggan terlebih dahulu.", "Error", JOptionPane.ERROR_MESSAGE);
              return;
          }

          alternatifList.clear();

          boolean semuaTerisi = true;
          for (int i = 0; i < criteriaList.size(); i++) {
              CriteriaModel criteria = criteriaList.get(i);
              JComboBox<ComboBoxItem> comboBox = comboBoxes.get(i);
              ComboBoxItem selectedSubKriteria = (ComboBoxItem) comboBox.getSelectedItem();

              if (selectedSubKriteria == null) {
                  semuaTerisi = false;
                  break;
              }

              alternatifList.add(new AlternatifModel(selectedPelanggan.getId(), selectedSubKriteria.getId()));
          }

          if (!semuaTerisi) {
              JOptionPane.showMessageDialog(this, "Semua kriteria harus dipilih.", "Error", JOptionPane.ERROR_MESSAGE);
              return;
          }

          int rowsInserted = alternatifDao.create(alternatifList);
          JOptionPane.showMessageDialog(this, rowsInserted + " data berhasil disimpan.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
          getAllData();
      });


      buttonPanel.add(simpanButton);

     gbc.gridx = 0;
     gbc.gridy = maxUsedRow + 1;
     gbc.gridwidth = 4;
     gbc.anchor = GridBagConstraints.WEST;
     jPanel1.add(buttonPanel, gbc);
  }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 749, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 280, Short.MAX_VALUE)
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
