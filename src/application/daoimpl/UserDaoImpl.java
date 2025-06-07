/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application.daoimpl;

import application.models.UserModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import application.dao.UserDao;
import application.models.RoleModel;
import application.utils.DatabaseUtil;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author yusuf
 */
public class UserDaoImpl implements UserDao {
    private Connection dbConnection = null;
    private PreparedStatement pstmt = null;
    private ResultSet resultSet = null;
    private String query;
    
    public UserDaoImpl() {
        dbConnection = DatabaseUtil.getInstance().getConnection();
    }

    @Override
    public UserModel findOneById(int id) {
        UserModel userFound = null; // Default null jika tidak ditemukan

        try {
            String query = "SELECT * FROM users WHERE id = ?";
            pstmt = dbConnection.prepareStatement(query);
            pstmt.setInt(1, id);
            resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                userFound = new UserModel();
                userFound.setId(resultSet.getInt("id"));
                userFound.setName(resultSet.getString("name"));
                userFound.setEmail(resultSet.getString("email"));
                userFound.setPassword(resultSet.getString("password"));
                userFound.setPhone(resultSet.getString("phone"));
                userFound.setAddress(resultSet.getString("address"));
                userFound.setKecamatan(resultSet.getString("kecamatan"));
                userFound.setKelurahan(resultSet.getString("kelurahan"));
                userFound.setRoleId(resultSet.getInt("role_id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saat mencari user dengan ID: " + id, e);
        } finally {
            closeStatement(); // Pastikan menutup Statement
        }

        return userFound;
    }

    @Override
    public UserModel findOneByUsernameAndPassword(String email, String password) {
        try {
            query = "SELECT * FROM users WHERE email=? and password=?";
 
            pstmt = dbConnection.prepareStatement(query);
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            
            resultSet = pstmt.executeQuery();
            if(resultSet.next()){
                UserModel userFound = new UserModel();
                userFound.setId(resultSet.getInt("id"));
                userFound.setEmail(resultSet.getString("email"));
                userFound.setPassword(resultSet.getString("password"));
                return userFound;
            }
            return null;
	} catch (SQLException e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            closeStatement();
        }
    }

    @Override
    public List<UserModel> findAll() {
        List<UserModel> userList = new ArrayList<>();
        String query = "SELECT * FROM users";

        try {
            pstmt = dbConnection.prepareStatement(query);
            resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                UserModel user = new UserModel();
                user.setId(resultSet.getInt("id"));
                user.setName(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));

                userList.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saat mengambil semua user: " + e.getMessage(), e);
        } finally {
            closeStatement();
        }

        return userList;
    }

    @Override
    public int create(UserModel user) {
        int result = 0;
        String query = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";

        try {
            pstmt = dbConnection.prepareStatement(query);
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, "Password1!"); // password default (hardcode)

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Gagal menyimpan user: " + e.getMessage(), e);
        } finally {
            closeStatement();
        }

        return result;
    }

    @Override
    public int update(UserModel user) {
        int result = 0;
        String query = "UPDATE users SET username = ?, email = ? WHERE id = ?";

        try {
            pstmt = dbConnection.prepareStatement(query);
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setInt(3, user.getId());

            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Gagal mengupdate user: " + e.getMessage(), e);
        } finally {
            closeStatement();
        }

        return result;
    }


    @Override
    public boolean delete(int id) {
        String query = "DELETE FROM users WHERE id = ?";
        try {
            pstmt = dbConnection.prepareStatement(query);
            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
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
    public void upsert(UserModel user) {
         try {
            // Cek apakah data user dengan ID tertentu sudah ada
            String checkQuery = "SELECT COUNT(*) FROM users WHERE id = ?";
            PreparedStatement checkStmt = dbConnection.prepareStatement(checkQuery);
            checkStmt.setInt(1, user.getId());

            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            if (count > 0) {
                // Jika data ada, lakukan update
                String updateQuery = "UPDATE users SET name = ?, email = ?, phone = ?, address = ?, kecamatan = ?, kelurahan = ?, role_id = ?, password = ? WHERE id = ?";
                PreparedStatement pstmt = dbConnection.prepareStatement(updateQuery);
                pstmt.setString(1, user.getName());
                pstmt.setString(2, user.getEmail());
                pstmt.setString(3, user.getPhone());
                pstmt.setString(4, user.getAddress());
                pstmt.setString(5, user.getKecamatan());
                pstmt.setString(6, user.getKelurahan());
                pstmt.setInt(7, user.getRoleId());
                pstmt.setString(8, user.getPassword());
                pstmt.setInt(9, user.getId());

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("User berhasil diperbarui.");
                }
                pstmt.close();
            } else {
                // Jika data tidak ada, lakukan insert
                String insertQuery = "INSERT INTO users (name, email, phone, address, kecamatan, kelurahan, role_id, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmt = dbConnection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, user.getName());
                pstmt.setString(2, user.getEmail());
                pstmt.setString(3, user.getPhone());
                pstmt.setString(4, user.getAddress());
                pstmt.setString(5, user.getKecamatan());
                pstmt.setString(6, user.getKelurahan());
                pstmt.setInt(7, user.getRoleId());
                pstmt.setString(8, user.getPassword());

                int rowsInserted = pstmt.executeUpdate();
                if (rowsInserted > 0) {
                    ResultSet generatedKeys = pstmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        user.setId(generatedKeys.getInt(1)); // Set ID jika berhasil insert
                    }
                    System.out.println("User baru berhasil ditambahkan.");
                }
                pstmt.close();
            }
            checkStmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error saat melakukan upsert user: " + e.getMessage(), e);
        } finally {
            closeStatement();
        }
    }

    @Override
    public List<RoleModel> findAllRole() {
        List<RoleModel> roleList = new ArrayList<>();
        String query = "SELECT * FROM role";

        try {
            pstmt = dbConnection.prepareStatement(query);
            resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                RoleModel role = new RoleModel();
                role.setId(resultSet.getInt("id"));
                role.setName(resultSet.getString("name"));

                roleList.add(role);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saat mengambil semua user: " + e.getMessage(), e);
        } finally {
            closeStatement();
        }

        return roleList;
    }

    @Override
    public List<UserModel> findByName(String name, List<Integer> roleId) {
        List<UserModel> userList = new ArrayList<>();
        // Bangun placeholder (?) sesuai jumlah roleId
        String placeholders = String.join(",", Collections.nCopies(roleId.size(), "?"));
        String query = "SELECT * FROM users WHERE name LIKE ? AND role_id IN (" + placeholders + ")";

        try {
            pstmt = dbConnection.prepareStatement(query);
            String searchParam = "%" + name + "%";
            pstmt.setString(1, searchParam);
            // Set nilai roleId satu per satu
            for (int i = 0; i < roleId.size(); i++) {
                pstmt.setInt(i + 2, roleId.get(i));
            }

            // Debugging: Cetak query dan parameter yang digunakan
            System.out.println("[DEBUG] SQL Query: " + query);
            System.out.println("[DEBUG] Parameter: " + searchParam);

            resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                UserModel user = new UserModel();
                user.setId(resultSet.getInt("id"));
                user.setName(resultSet.getString("name"));
                user.setEmail(resultSet.getString("email"));
                user.setPhone(resultSet.getString("phone"));
                user.setAddress(resultSet.getString("address"));
                user.setKecamatan(resultSet.getString("kecamatan"));
                user.setKelurahan(resultSet.getString("kelurahan"));
                user.setRoleId(resultSet.getInt("role_id"));

                userList.add(user);
            }

            // Debugging: Cetak jumlah data yang ditemukan
            System.out.println("[DEBUG] Jumlah data ditemukan: " + userList.size());

        } catch (SQLException e) {
            System.err.println("[ERROR] Terjadi kesalahan saat mencari user: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeStatement();
        }

        return userList;
    }

    @Override
    public RoleModel findRoleById(int id) {
        RoleModel roleModel = null;
        String query = "SELECT * FROM role WHERE id = ?";

        try {
            pstmt = dbConnection.prepareStatement(query);
            pstmt.setInt(1, id); // Set nilai parameter query
            resultSet = pstmt.executeQuery();

            if (resultSet.next()) { // Gunakan if karena id unik
                roleModel = new RoleModel();
                roleModel.setId(resultSet.getInt("id"));
                roleModel.setName(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saat mengambil role: " + e.getMessage(), e);
        } finally {
            closeStatement();
        }

        return roleModel; // Return roleModel, bukan List
    }

}
