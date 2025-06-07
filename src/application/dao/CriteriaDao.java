/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application.dao;

import application.models.CriteriaModel;
import application.models.UserModel;
import java.util.List;

/**
 *
 * @author mhdja
 */
public interface CriteriaDao {
    public List<CriteriaModel> findByOneId(int id);
    public List<CriteriaModel> findAll();
    public int create(CriteriaModel criteria);
    public int upsert(UserModel user);
    public int delete(int id);
}
