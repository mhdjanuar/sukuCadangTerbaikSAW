package application.daoimpl;

import application.dao.RangkingDao;
import application.models.RangkingModel;
import application.utils.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RangkingDaoImpl implements RangkingDao {
    private Connection dbConnection = null;
    private PreparedStatement pstmt = null;
    private ResultSet resultSet = null;
    private String query;

    public RangkingDaoImpl() {
        dbConnection = DatabaseUtil.getInstance().getConnection();
    }

     @Override
    public List<RangkingModel> findRangking() {
        List<RangkingModel> rangkingList = new ArrayList<>();

        try {
            query = "SELECT " +
                    "p.name AS nama_pelanggan, " +
                    "ROUND(SUM( " +
                    "  CASE " +
                    "    WHEN c.type = 'benefit' THEN (sc.jumlah_bobot / pembagi_table.pembagi) * (c.bobot / total_weight.total) " +
                    "    WHEN c.type = 'cost' THEN (pembagi_table.pembagi / sc.jumlah_bobot) * (c.bobot / total_weight.total) " +
                    "  END " +
                    "), 4) AS total_nilai, " +
                    "RANK() OVER (ORDER BY " +
                    "  SUM( " +
                    "    CASE " +
                    "      WHEN c.type = 'benefit' THEN (sc.jumlah_bobot / pembagi_table.pembagi) * (c.bobot / total_weight.total) " +
                    "      WHEN c.type = 'cost' THEN (pembagi_table.pembagi / sc.jumlah_bobot) * (c.bobot / total_weight.total) " +
                    "    END " +
                    ") DESC " +
                    ") AS peringkat " +

                    "FROM alternatif AS a " +
                    "JOIN spare_parts AS p ON a.id_spare_part = p.id " +
                    "JOIN sub_criteria AS sc ON a.id_sub_kreteria = sc.id " +
                    "JOIN criteria AS c ON sc.id_kreteria = c.id " +

                    "JOIN ( " +
                    "  SELECT " +
                    "    c.id AS id_kriteria, " +
                    "    CASE " +
                    "      WHEN c.type = 'benefit' THEN MAX(sc.jumlah_bobot) " +
                    "      WHEN c.type = 'cost' THEN MIN(sc.jumlah_bobot) " +
                    "    END AS pembagi " +
                    "  FROM alternatif AS a " +
                    "  JOIN sub_criteria AS sc ON a.id_sub_kreteria = sc.id " +
                    "  JOIN criteria AS c ON sc.id_kreteria = c.id " +
                    "  GROUP BY c.id, c.type " +
                    ") AS pembagi_table ON pembagi_table.id_kriteria = c.id " +

                    "JOIN (SELECT SUM(bobot) AS total FROM criteria) AS total_weight " +

                    "GROUP BY p.name " +
                    "ORDER BY total_nilai DESC;";

            pstmt = dbConnection.prepareStatement(query);
            resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                RangkingModel rangking = new RangkingModel();
                rangking.setNamaAlternatif(resultSet.getString("nama_pelanggan"));
                rangking.setTotalNilai(resultSet.getDouble("total_nilai"));
                rangking.setPeringkat(resultSet.getInt("peringkat"));
                rangkingList.add(rangking);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeStatement();
        }

        return rangkingList;
    }

    private void closeStatement() {
        try {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
            if (resultSet != null) {
                resultSet.close();
                resultSet = null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
