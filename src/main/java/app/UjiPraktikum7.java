package app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import view.FormPendaftaranKursus;

/**
 * Verifikasi teknis untuk skenario hitung Pertemuan 7.
 * Versi fleksibel agar kompatibel dengan berbagai penamaan variabel form.
 */
public class UjiPraktikum7 {

    public static void main(String[] args) throws Exception {
        FormPendaftaranKursus form = new FormPendaftaranKursus();
        try {
            Method biayaDasar = method("hitungBiayaDasar", String.class);
            Method biayaFasilitas = method("hitungBiayaFasilitas");
            Method jenisPeserta = method("getJenisPeserta");
            Method ringkasanFasilitas = method("buatRingkasanFasilitas");

            JRadioButton rbMahasiswa = field(form, new String[]{"rbMahasiswa"}, JRadioButton.class);
            JRadioButton rbUmum = field(form, new String[]{"rbUmum"}, JRadioButton.class);
            JComboBox<?> cmbKursus = field(form, new String[]{"cmbKursus", "cmbPilihanKursus"}, JComboBox.class);
            JCheckBox cbModul = field(form, new String[]{"cbModul", "cbModulCetak"}, JCheckBox.class);
            JCheckBox cbSertifikat = field(form, new String[]{"cbSertifikat"}, JCheckBox.class);
            JCheckBox cbRekaman = field(form, new String[]{"cbRekaman", "cbAksesRekaman"}, JCheckBox.class);

            // Skenario 5: Mahasiswa + Java Desktop + Modul + Sertifikat = Rp575.000.
            rbMahasiswa.setSelected(true);
            setSelectedItemFlexible(cmbKursus, new String[]{"Java Desktop", "Pemrograman Java Desktop"});
            cbModul.setSelected(true);
            cbSertifikat.setSelected(true);
            cbRekaman.setSelected(false);
            cekSkenario(form, biayaDasar, biayaFasilitas, jenisPeserta, "Skenario 5", 575000);

            // Skenario 6: Umum + Data Science + Rekaman = Rp690.000.
            rbUmum.setSelected(true);
            setSelectedItemFlexible(cmbKursus, new String[]{"Data Science"});
            cbModul.setSelected(false);
            cbSertifikat.setSelected(false);
            cbRekaman.setSelected(true);
            cekSkenario(form, biayaDasar, biayaFasilitas, jenisPeserta, "Skenario 6", 690000);

            // Skenario 7: Umum + UI/UX tanpa fasilitas = Rp400.000.
            setSelectedItemFlexible(cmbKursus, new String[]{"UI/UX Design", "UI/UX"});
            cbRekaman.setSelected(false);
            cekSkenario(form, biayaDasar, biayaFasilitas, jenisPeserta, "Skenario 7", 400000);

            String fasilitas = (String) ringkasanFasilitas.invoke(form);
            if (!fasilitas.contains("Tidak ada fasilitas tambahan")) {
                throw new IllegalStateException("Ringkasan fasilitas kosong tidak sesuai");
            }

            System.out.println("PASS | Ringkasan tanpa fasilitas");
            System.out.println("HASIL: seluruh skenario hitung Pertemuan 7 PASS");
        } finally {
            form.dispose();
        }
    }

    private static void setSelectedItemFlexible(JComboBox<?> cmb, String[] options) {
        for (String opt : options) {
            for (int i = 0; i < cmb.getItemCount(); i++) {
                Object item = cmb.getItemAt(i);
                if (item != null && item.toString().equalsIgnoreCase(opt)) {
                    cmb.setSelectedIndex(i);
                    return;
                }
            }
        }
        if (cmb.getItemCount() > 0) cmb.setSelectedIndex(0);
    }

    private static void cekSkenario(
            FormPendaftaranKursus form,
            Method biayaDasar,
            Method biayaFasilitas,
            Method jenisPeserta,
            String namaSkenario,
            double expected) throws Exception {

        JComboBox<?> cmbKursus = field(form, new String[]{"cmbKursus", "cmbPilihanKursus"}, JComboBox.class);
        String kursus = cmbKursus.getSelectedItem().toString();
        String jenis = (String) jenisPeserta.invoke(form);
        double dasar = (Double) biayaDasar.invoke(form, kursus);
        double fasilitas = (Double) biayaFasilitas.invoke(form);
        double diskon = "Mahasiswa".equals(jenis) ? dasar * 0.10 : 0;
        double total = dasar - diskon + fasilitas;

        if (Math.abs(total - expected) > 0.001) {
            throw new IllegalStateException(namaSkenario + " FAIL: total=" + total + " (expected " + expected + ", kursus=" + kursus + ", jenis=" + jenis + ", dasar=" + dasar + ", fasilitas=" + fasilitas + ")");
        }
        System.out.println("PASS | " + namaSkenario + " | total=" + (long) total);
    }

    private static Method method(String nama, Class<?>... parameter) throws Exception {
        Method m = FormPendaftaranKursus.class.getDeclaredMethod(nama, parameter);
        m.setAccessible(true);
        return m;
    }

    private static <T> T field(Object object, String[] names, Class<T> tipe) throws Exception {
        NoSuchFieldException lastEx = null;
        for (String nama : names) {
            try {
                Field f = object.getClass().getDeclaredField(nama);
                f.setAccessible(true);
                return tipe.cast(f.get(object));
            } catch (NoSuchFieldException e) {
                lastEx = e;
            }
        }
        throw lastEx != null ? lastEx : new NoSuchFieldException("Field not found");
    }
}