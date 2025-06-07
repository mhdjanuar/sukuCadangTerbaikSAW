/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application.dao;

import application.models.SubCriteriaModel;
import java.util.List;

/**
 *
 * @author mhdja
 */
public interface SubCriteriaDao {
    public List<SubCriteriaModel> findAll();
    public List<SubCriteriaModel> findAllByCriteriaId(int criteriaId);
    public int create(SubCriteriaModel subCriteria);
    public int update(SubCriteriaModel subCriteria);
    public void delete(SubCriteriaModel subCriteria);
}
