package info.androidhive.loginandregistration.data;

import android.widget.Button;

/**
 * Created by Kuncoro on 26/03/2016.
 */
public class DataMatkul {
    private String id, nama_matkul, semester, id_dosen, ruang, jam, status, catatan, name;

    public DataMatkul() {
    }

    public DataMatkul(String id, String nama_matkul, String semester, String id_dosen, String ruang, String jam, String status, String catatan, String name) {
        this.id = id;
        this.nama_matkul = nama_matkul;
        this.semester = semester;
        this.id_dosen = id_dosen;
        this.ruang = ruang;
        this.jam = jam;
        this.status = status;
        this.catatan = catatan;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama_matkul() {
        return nama_matkul;
    }

    public void setNama_matkul(String nama_matkul) {
        this.nama_matkul = nama_matkul;
    }

    public String getSemester() { return semester; }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getId_dosen() {
        return id_dosen;
    }

    public void setId_dosen(String id_dosen) {
        this.id_dosen = id_dosen;
    }

    public String getRuang() {
        return ruang;
    }

    public void setRuang(String ruang) {
        this.ruang = ruang;
    }

    public String getJam() {
        return jam;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) { this.status = status; }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) { this.catatan = catatan; }

    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name; }
}
