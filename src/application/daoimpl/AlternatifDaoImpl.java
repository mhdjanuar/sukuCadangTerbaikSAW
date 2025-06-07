/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application.daoimpl;

import application.dao.AlternatifDao;
import application.models.AlternatifModel;
import application.utils.DatabaseUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author mhdja
 */
public class AlternatifDaoImpl implements AlternatifDao {
    private Connection dbConnection = null;
    private PreparedStatement pstmt = null;
    private ResultSet resultSet = null;
    private String query;
    
     public AlternatifDaoImpl() {
        dbConnection = DatabaseUtil.getInstance().getConnection();
    }

    @Override
    public List<AlternatifModel> findAll() {
        List<AlternatifModel> alternatifList = new ArrayList<>();
        
        try {
            query = "SELECT p.name, c.id AS id_kriteria, c.nama AS nama_kriteria, " +
                "sc.jumlah_bobot AS bobot_alternatif " +
                "FROM alternatif AS a " +
                "INNER JOIN spare_parts AS p ON a.id_spare_part = p.id " +
                "INNER JOIN sub_criteria AS sc ON a.id_sub_kreteria = sc.id " +
                "INNER JOIN criteria AS c ON sc.id_kreteria = c.id";
            
            pstmt = dbConnection.prepareStatement(query);
            resultSet = pstmt.executeQuery();
            
            while (resultSet.next()) {
                AlternatifModel alternatif = new AlternatifModel(0, 0);
                alternatif.setNameAlternatif(resultSet.getString("name"));
                alternatif.setIdKriteria(resultSet.getInt("id_kriteria"));
                alternatif.setNameKriteria(resultSet.getString("nama_kriteria"));
                alternatif.setBobotAlternatif(resultSet.getInt("bobot_alternatif"));
                // Set other attributes accordingly
                alternatifList.add(alternatif);
            }
	} catch (SQLException e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            closeStatement();
        }
        
        return alternatifList;
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
    public int create(List<AlternatifModel> alternatifList) {
        String query = "INSERT INTO alternatif(id_spare_part, id_sub_kreteria) " +
                       "VALUES(?, ?)";

        try (PreparedStatement pstmt = dbConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            for (AlternatifModel alternatif : alternatifList) {
                pstmt.setInt(1, alternatif.getIdKaryawan());
                pstmt.setInt(2, alternatif.getIdSubKriteria());

                pstmt.addBatch();  // Add the current set of parameters to the batch
            }

            int[] result = pstmt.executeBatch();  // Execute the batch insert
            return Arrays.stream(result).sum();  // Sum up the number of affected rows (optional)

        } catch (SQLException e) {
            // Handle exception (logging, etc.)
            throw new RuntimeException(e);
        } finally {
            closeStatement();
        }
    }

    @Override
    public List<AlternatifModel> findNormalisasi() {
        List<AlternatifModel> normalisasiList = new ArrayList<>();

        try {
            // Ambil pembagi (max untuk benefit, min untuk cost)
            Map<Integer, Double> pembagiMap = new HashMap<>();
            Map<Integer, String> tipeMap = new HashMap<>();

            String pembagiQuery = "SELECT c.id, c.type, " +
                    "CASE WHEN c.type = 'benefit' THEN MAX(sc.jumlah_bobot) " +
                    "WHEN c.type = 'cost' THEN MIN(sc.jumlah_bobot) END AS pembagi " +
                    "FROM alternatif AS a " +
                    "JOIN sub_criteria AS sc ON a.id_sub_kreteria = sc.id " +
                    "JOIN criteria AS c ON sc.id_kreteria = c.id " +
                    "GROUP BY c.id, c.type";

            pstmt = dbConnection.prepareStatement(pembagiQuery);
            resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                int idKriteria = resultSet.getInt("id");
                double pembagi = resultSet.getDouble("pembagi");
                String tipe = resultSet.getString("type");

                pembagiMap.put(idKriteria, pembagi);
                tipeMap.put(idKriteria, tipe);
            }
            resultSet.close();
            pstmt.close();

            // Ambil data alternatif dan normalisasikan
            String query = "SELECT p.name, c.id AS id_kriteria, c.nama AS nama_kriteria, sc.jumlah_bobot AS bobot_alternatif " +
                    "FROM alternatif AS a " +
                    "INNER JOIN spare_parts AS p ON a.id_spare_part = p.id " +
                    "INNER JOIN sub_criteria AS sc ON a.id_sub_kreteria = sc.id " +
                    "INNER JOIN criteria AS c ON sc.id_kreteria = c.id";

            pstmt = dbConnection.prepareStatement(query);
            resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                AlternatifModel alternatif = new AlternatifModel(0, 0);
                alternatif.setNameAlternatif(resultSet.getString("name"));
                alternatif.setIdKriteria(resultSet.getInt("id_kriteria"));
                alternatif.setNameKriteria(resultSet.getString("nama_kriteria"));

                int idKriteria = resultSet.getInt("id_kriteria");
                double bobot = resultSet.getDouble("bobot_alternatif");
                double pembagi = pembagiMap.get(idKriteria);
                String type = tipeMap.get(idKriteria);

                double normalisasi;
                if ("benefit".equalsIgnoreCase(type)) {
                    normalisasi = bobot / pembagi;
                } else if ("cost".equalsIgnoreCase(type)) {
                    normalisasi = pembagi / bobot;
                } else {
                    normalisasi = 0.0;
                }

                alternatif.setNormalisasi(normalisasi);
                normalisasiList.add(alternatif);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeStatement();
        }

        return normalisasiList;
    }

}
