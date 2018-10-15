package info.androidhive.loginandregistration.data;

/**
 * Created by Kuncoro on 26/03/2016.
 */
public class Data {
    private String id_dosen, name, status, jam;

    public Data() {
    }

    public Data(String id_dosen, String name, String status, String jam) {
        this.id_dosen = id_dosen;
        this.name = name;
        this.status = status;
        this.jam = jam;
    }

    public String getId_dosen() {
        return id_dosen;
    }

    public void setId_dosen(String id_dosen) {
        this.id_dosen = id_dosen;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlamat() {
        return status;
    }

    public void setAlamat(String status) {
        this.status = status;
    }

    public String getJam() {
        return jam;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }
}
