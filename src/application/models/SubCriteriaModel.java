/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application.models;

/**
 *
 * @author mhdja
 */
public class SubCriteriaModel {
    private int id;
    private String nameCriteria;
    private int idCriteria;
    private int bobot;
    private String deskripsi;
    
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * @return the nameCriteria
     */
    public String getNameCriteria() {
        return nameCriteria;
    }

    /**
     * @param nameCriteria the nameCriteria to set
     */
    public void setNameCriteria(String nameCriteria) {
        this.nameCriteria = nameCriteria;
    }

    /**
     * @return the idCriteria
     */
    public int getIdCriteria() {
        return idCriteria;
    }

    /**
     * @param idCriteria the idCriteria to set
     */
    public void setIdCriteria(int idCriteria) {
        this.idCriteria = idCriteria;
    }

    /**
     * @return the bobot
     */
    public int getBobot() {
        return bobot;
    }

    /**
     * @param bobot the bobot to set
     */
    public void setBobot(int bobot) {
        this.bobot = bobot;
    }

    /**
     * @return the deskripsi
     */
    public String getDeskripsi() {
        return deskripsi;
    }

    /**
     * @param deskripsi the deskripsi to set
     */
    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

}
