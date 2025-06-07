/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application.daoimpl;

import application.dao.KaryawanDao;
import application.models.KaryawanModel;
import application.utils.DatabaseUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mhdja
 */
public class KaryawanDaoImpl implements KaryawanDao {
    
    private Connection dbConnection = null;
    private PreparedStatement pstmt = null;
    private ResultSet resultSet = null;
    private String query;
    
    public KaryawanDaoImpl() {
        dbConnection = DatabaseUtil.getInstance().getConnection();
    }

    @Override
    public List<KaryawanModel> findAll() {
        List<KaryawanModel> listDataAll = new ArrayList<>();

        try {
            query = "SELECT * FROM spare_parts";
            pstmt = dbConnection.prepareStatement(query);
            resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                KaryawanModel listData = new KaryawanModel();
                listData.setId(resultSet.getInt("id"));
                listData.setName(resultSet.getString("name"));
                listData.setBrand(resultSet.getString("brand"));

                listDataAll.add(listData);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeStatement();
        }

        return listDataAll;
    }


    @Override
    public int create(KaryawanModel karyawan) {
        try {
            query = "INSERT INTO spare_parts(name, brand) VALUES (?, ?)";
            pstmt = dbConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, karyawan.getName());
            pstmt.setString(2, karyawan.getBrand());

            // Log sebelum insert
            System.out.println("=== Menyimpan Data Karyawan ===");
            System.out.println("Nama: " + karyawan.getName());
            
            int result = pstmt.executeUpdate();
            resultSet = pstmt.getGeneratedKeys();
            if (resultSet.next()) {
                int insertedId = resultSet.getInt(1);
                System.out.println("Data karyawan berhasil disimpan. ID: " + insertedId);
                return insertedId;
            }

            System.out.println("Data karyawan disimpan tanpa ID yang dihasilkan.");
            return result;
        } catch (SQLException e) {
            System.out.println("Gagal menyimpan data karyawan: " + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            closeStatement();
        }
    }

    
    private void closeStatement() {
        try {
            if(pstmt != null){
                pstmt.close();
                pstmt = null;
            }
            if(resultSet != null){
                resultSet.close();
                resultSet = null;
            }   
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int update(KaryawanModel karyawan) {
        int result = 0;
        try {
            // Query untuk mengupdate data karyawan
            query = "UPDATE spare_parts SET name = ?, brand = ? WHERE id = ?";

            pstmt = dbConnection.prepareStatement(query);
            pstmt.setString(1, karyawan.getName());  // Nama karyawan
            pstmt.setString(2, karyawan.getBrand());  // Usia karyawan
            pstmt.setInt(3, karyawan.getId());  // ID karyawan yang akan diupdate

            result = pstmt.executeUpdate();  // Eksekusi query update
        } catch (SQLException e) {
            // Jika ada error, lempar exception
            e.printStackTrace();
        } finally {
            closeStatement();  // Pastikan koneksi ditutup
        }

        return result;  // Kembalikan jumlah baris yang terupdate
    }

    @Override
    public int deleteKaryawan(int id) {
        int result = 0;
        try {
            query = "DELETE FROM spare_parts WHERE id = ?";
            pstmt = dbConnection.prepareStatement(query);
            pstmt.setInt(1, id);

            result = pstmt.executeUpdate(); // eksekusi dan simpan jumlah baris yang terhapus
        } catch (SQLException e) {
            e.printStackTrace(); // log error
        } finally {
            closeStatement(); // tutup statement
        }

        return result; // return jumlah baris yang terhapus
    }
    
}
