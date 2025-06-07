/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package application.dao;

import application.models.AlternatifModel;
import java.util.List;

/**
 *
 * @author mhdja
 */
public interface AlternatifDao {
     public List<AlternatifModel> findAll();
     public int create(List<AlternatifModel> alternatifList);
     public List<AlternatifModel> findNormalisasi();
}
