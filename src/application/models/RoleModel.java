/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application.models;

/**
 *
 * @author mhdja
 */
public class RoleModel {
    private int id;
    private String name;
    private String desctiption;
    
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
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the desctiption
     */
    public String getDesctiption() {
        return desctiption;
    }

    /**
     * @param desctiption the desctiption to set
     */
    public void setDesctiption(String desctiption) {
        this.desctiption = desctiption;
    }
}
