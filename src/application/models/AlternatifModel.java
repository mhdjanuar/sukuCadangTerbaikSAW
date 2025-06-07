/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application.models;

/**
 *
 * @author mhdja
 */
public class AlternatifModel {
    private String nameAlternatif;
    private int idKriteria;
    private String nameKriteria;
    private int bobotAlternatif;
    private String typeAlternatif;
    private int idKaryawan;
    private int idSubKriteria;
    private double normalisasi;

    public AlternatifModel(int idKaryawan, int idSubKriteria) {
        this.idKaryawan = idKaryawan;
        this.idSubKriteria = idSubKriteria;
    }
    
    /**
     * @return the normalisasi
     */
    public double getNormalisasi() {
        return normalisasi;
    }

    /**
     * @param normalisasi the normalisasi to set
     */
    public void setNormalisasi(double normalisasi) {
        this.normalisasi = normalisasi;
    }
    
    /**
     * @return the idKaryawan
     */
    public int getIdKaryawan() {
        return idKaryawan;
    }

    /**
     * @param idKaryawan the idKaryawan to set
     */
    public void setIdKaryawan(int idKaryawan) {
        this.idKaryawan = idKaryawan;
    }

    /**
     * @return the idSubKriteria
     */
    public int getIdSubKriteria() {
        return idSubKriteria;
    }

    /**
     * @param idSubKriteria the idSubKriteria to set
     */
    public void setIdSubKriteria(int idSubKriteria) {
        this.idSubKriteria = idSubKriteria;
    }

    /**
     * @return the nameAlternatif
     */
    public String getNameAlternatif() {
        return nameAlternatif;
    }

    /**
     * @param nameAlternatif the nameAlternatif to set
     */
    public void setNameAlternatif(String nameAlternatif) {
        this.nameAlternatif = nameAlternatif;
    }

    /**
     * @return the idKriteria
     */
    public int getIdKriteria() {
        return idKriteria;
    }

    /**
     * @param idKriteria the idKriteria to set
     */
    public void setIdKriteria(int idKriteria) {
        this.idKriteria = idKriteria;
    }

    /**
     * @return the nameKriteria
     */
    public String getNameKriteria() {
        return nameKriteria;
    }

    /**
     * @param nameKriteria the nameKriteria to set
     */
    public void setNameKriteria(String nameKriteria) {
        this.nameKriteria = nameKriteria;
    }

    /**
     * @return the bobotAlternatif
     */
    public int getBobotAlternatif() {
        return bobotAlternatif;
    }

    /**
     * @param bobotAlternatif the bobotAlternatif to set
     */
    public void setBobotAlternatif(int bobotAlternatif) {
        this.bobotAlternatif = bobotAlternatif;
    }

    /**
     * @return the typeAlternatif
     */
    public String getTypeAlternatif() {
        return typeAlternatif;
    }

    /**
     * @param typeAlternatif the typeAlternatif to set
     */
    public void setTypeAlternatif(String typeAlternatif) {
        this.typeAlternatif = typeAlternatif;
    }
}
