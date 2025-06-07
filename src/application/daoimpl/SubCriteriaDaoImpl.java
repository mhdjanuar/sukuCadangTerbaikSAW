/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application.daoimpl;

import application.dao.SubCriteriaDao;
import application.models.SubCriteriaModel;
import application.utils.DatabaseUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mhdja
 */
public class SubCriteriaDaoImpl implements SubCriteriaDao {
    private Connection dbConnection = null;
    private PreparedStatement pstmt = null;
    private ResultSet resultSet = null;
    private String query;
    
    public SubCriteriaDaoImpl() {
        dbConnection = DatabaseUtil.getInstance().getConnection();
    }

    @Override
    public List<SubCriteriaModel> findAll() {
        List<SubCriteriaModel> subCriteriaList = new ArrayList<>();
        
        try {
            query = "SELECT sc.id, c.nama AS kriteria, sc.deskripsi, sc.jumlah_bobot "
                    + "FROM sub_criteria AS sc "
                    + "INNER JOIN criteria AS c ON sc.id_kreteria = c.id";
            
            pstmt = dbConnection.prepareStatement(query);
            resultSet = pstmt.executeQuery();
            
            while (resultSet.next()) {
                SubCriteriaModel subCriteria = new SubCriteriaModel();
                subCriteria.setId(resultSet.getInt("id"));
                subCriteria.setNameCriteria(resultSet.getString("kriteria"));
                subCriteria.setBobot(resultSet.getInt("jumlah_bobot"));
                subCriteria.setDeskripsi(resultSet.getString("deskripsi"));
                // Set other attributes accordingly
                subCriteriaList.add(subCriteria);
            }
	} catch (SQLException e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            closeStatement();
        }
        
        return subCriteriaList;
    }
    
    @Override
    public List<SubCriteriaModel> findAllByCriteriaId(int criteriaId) {
        List<SubCriteriaModel> subCriteriaList = new ArrayList<>();
    
        try {
            query = "SELECT sc.id, c.nama AS kriteria, sc.deskripsi, sc.jumlah_bobot "
                    + "FROM sub_criteria AS sc "
                    + "INNER JOIN criteria AS c ON sc.id_kreteria = c.id "
                    + "WHERE sc.id_kreteria = ?"; // Filter by id

            pstmt = dbConnection.prepareStatement(query);
            pstmt.setInt(1, criteriaId);

            resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                SubCriteriaModel subCriteria = new SubCriteriaModel();
                subCriteria.setId(resultSet.getInt("id"));
                subCriteria.setNameCriteria(resultSet.getString("kriteria"));
                subCriteria.setBobot(resultSet.getInt("jumlah_bobot"));
                subCriteria.setDeskripsi(resultSet.getString("deskripsi"));
                // Set other attributes accordingly
                subCriteriaList.add(subCriteria);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeStatement();
        }

        return subCriteriaList;
    }

    @Override
    public int create(SubCriteriaModel subCriteria) {
        try {
            query = "INSERT INTO sub_criteria(id_kreteria, jumlah_bobot, deskripsi) " +
                    "VALUES(?, ?, ?)";
            
            pstmt = dbConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, subCriteria.getIdCriteria());
            pstmt.setInt(2, subCriteria.getBobot());
            pstmt.setString(3, subCriteria.getDeskripsi());
            
            int result = pstmt.executeUpdate();
            resultSet = pstmt.getGeneratedKeys();
            return result;
	} catch (SQLException e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            closeStatement();
        }
    }

    @Override
    public int update(SubCriteriaModel subCriteria) {
        try {
            query = "UPDATE sub_criteria SET id_kreteria = ?, jumlah_bobot = ?, deskripsi = ? WHERE id = ?";

            pstmt = dbConnection.prepareStatement(query);
            pstmt.setInt(1, subCriteria.getIdCriteria());
            pstmt.setInt(2, subCriteria.getBobot());
            pstmt.setString(3, subCriteria.getDeskripsi());
            pstmt.setInt(4, subCriteria.getId()); // ID untuk menentukan record yang akan diupdate

            int result = pstmt.executeUpdate();
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeStatement();
        }
    }

    @Override
    public void delete(SubCriteriaModel subCriteria) {
        try {
            query = "DELETE FROM sub_criteria WHERE id = ?";
            pstmt = dbConnection.prepareStatement(query);
            pstmt.setInt(1, subCriteria.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
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
    
}
