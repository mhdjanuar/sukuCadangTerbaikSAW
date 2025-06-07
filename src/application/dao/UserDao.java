/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package application.dao;

import application.models.RoleModel;
import application.models.UserModel;
import java.util.List;

/**
 *
 * @author yusuf
 */
public interface UserDao {
    
    public UserModel findOneById(int id);
    
    public UserModel findOneByUsernameAndPassword(String username, String Password);
    
    public List<UserModel> findAll();
    
    public List<UserModel> findByName(String name, List<Integer> roleId);
    
    public int create(UserModel user);
    
    public int update(UserModel user);
    
    public boolean delete(int id);
    
    public void upsert(UserModel user);
    
    public List<RoleModel> findAllRole();
    
    public RoleModel findRoleById(int id);
}
