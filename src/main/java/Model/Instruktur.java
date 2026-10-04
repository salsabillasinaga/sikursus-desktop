package model;

public class Instruktur extends Orang {
    private String keahlian;

    public Instruktur(int id, String nama, String noHp, String keahlian) {
        super(id, nama, noHp); // Memanggil constructor parent[cite: 28, 29]
        this.keahlian = keahlian;
    }

    public String getKeahlian() {
        return keahlian;
    }

    public void setKeahlian(String keahlian) {
        this.keahlian = keahlian;
    }

    @Override
    public String getInfo() {
        return "[Instruktur] " + super.getInfo() + " | Keahlian: " + keahlian;
    }
}