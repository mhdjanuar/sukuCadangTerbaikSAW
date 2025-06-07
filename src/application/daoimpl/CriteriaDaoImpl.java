/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application.daoimpl;

import application.dao.CriteriaDao;
import application.models.CriteriaModel;
import application.models.UserModel;
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
public class CriteriaDaoImpl implements CriteriaDao {
    private Connection dbConnection = null;
    private PreparedStatement pstmt = null;
    private ResultSet resultSet = null;
    private String query;
    
    public CriteriaDaoImpl() {
        dbConnection = DatabaseUtil.getInstance().getConnection();
    }

    @Override
    public List<CriteriaModel> findByOneId(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    @Override
    public List<CriteriaModel> findAll() {
        List<CriteriaModel> criteriaList = new ArrayList<>();
        
        try {
            query = "SELECT * FROM criteria";
            pstmt = dbConnection.prepareStatement(query);
            resultSet = pstmt.executeQuery();
            
            while (resultSet.next()) {
                CriteriaModel criteria = new CriteriaModel(1, "Criteria 1");
                criteria.setId(resultSet.getInt("id"));
                criteria.setName(resultSet.getString("nama"));
                criteria.setType(resultSet.getString("type"));
                criteria.setBobot(resultSet.getInt("bobot"));
                // Set other attributes accordingly
                criteriaList.add(criteria);
            }
	} catch (SQLException e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            closeStatement();
        }
        
        return criteriaList;
    } 

    @Override
    public int create(CriteriaModel criteria) {
        try {
            query = "INSERT INTO criteria(nama, type, bobot) " +
                    "VALUES(?, ?, ?)";
            
            pstmt = dbConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, criteria.getName());
            pstmt.setString(2, criteria.getType());
            pstmt.setInt(3, criteria.getBobot());
            
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
    public int upsert(UserModel user) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
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
    public int delete(int id) {
        try {
            query = "DELETE FROM criteria WHERE id = ?";
            pstmt = dbConnection.prepareStatement(query);
            pstmt.setInt(1, id);
            
            return pstmt.executeUpdate(); // Returns the number of rows affected
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeStatement();
        }
    }
}
