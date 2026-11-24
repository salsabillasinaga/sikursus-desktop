package util;

/** Item JComboBox: menampilkan teks, menyimpan ID. */
public class ComboItem {

    private final int id;
    private final String teks;

    public ComboItem(int id, String teks) {
        this.id = id;
        this.teks = teks;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return teks;
    }
}