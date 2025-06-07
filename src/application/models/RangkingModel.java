/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application.models;

/**
 *
 * @author mhdja
 */
public class RangkingModel {
    private String namaAlternatif;
    private double totalNilai;
    private int peringkat;

    public String getNamaAlternatif() {
        return namaAlternatif;
    }

    public void setNamaAlternatif(String namaAlternatif) {
        this.namaAlternatif = namaAlternatif;
    }

    public double getTotalNilai() {
        return totalNilai;
    }

    public void setTotalNilai(double totalNilai) {
        this.totalNilai = totalNilai;
    }

    public int getPeringkat() {
        return peringkat;
    }

    public void setPeringkat(int peringkat) {
        this.peringkat = peringkat;
    }
}