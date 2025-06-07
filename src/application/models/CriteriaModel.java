/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application.models;

/**
 *
 * @author mhdja
 */
public class CriteriaModel {
    private int id;
    private String nama;
    private String type;
    private int bobot;
    

    public CriteriaModel(int id, String name) {
        this.id = id;
        this.nama = name;
    }
    
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
     * @return the name
     */
    public String getName() {
        return nama;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.nama = name;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
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
    
}
