package util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.BorderLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Function;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

/**
 * Tema visual SIKURSUS: perpaduan warna coklat (espresso, mocha, karamel, krim).
 * Seluruh frame memakai class ini supaya tampilannya seragam.
 */
public final class Theme {

    // ===================== PALET WARNA =====================
    public static final Color CREAM = new Color(0xF5EDE3);       // latar halaman
    public static final Color IVORY = new Color(0xFFFBF5);       // latar kartu
    public static final Color WHITE = Color.WHITE;

    public static final Color DARK_BROWN = new Color(0x4A2F20);  // espresso
    public static final Color MID_BROWN = new Color(0x7A5238);   // mocha
    public static final Color LIGHT_BROWN = new Color(0xC9A57F); // karamel muda
    public static final Color ACCENT = new Color(0xB8864B);      // karamel

    public static final Color BORDER = new Color(0xDCCBB6);
    public static final Color TEXT = new Color(0x3B2A20);
    public static final Color MUTED = new Color(0x8A7565);

    public static final Color SIDEBAR = new Color(0x4A2F20);
    public static final Color SIDEBAR_DARK = new Color(0x2E1B10);
    public static final Color SIDEBAR_HOVER = new Color(0x6A4630);

    public static final Color ROW_ALT = new Color(0xFAF4EB);
    public static final Color SELECT = new Color(0xEAD7BF);

    public static final Color BTN_SAVE = new Color(0x7A4B2A);
    public static final Color BTN_EDIT = new Color(0xB8864B);
    public static final Color BTN_DELETE = new Color(0xA33F32);
    public static final Color BTN_NEUTRAL = new Color(0x8C7B6B);
    public static final Color BTN_SUCCESS = new Color(0x5E7F4F);

    private static final Color DISABLED_BG = new Color(0xE4D8C8);
    private static final Color DISABLED_FG = new Color(0x9C8672);

    static {
        install();
    }

    private Theme() {}

    // ===================== INSTALASI LOOK & FEEL =====================
    /** Dipanggil otomatis saat class Theme pertama kali dipakai. */
    public static void install() {
        UIManager.put("Panel.background", CREAM);
        UIManager.put("OptionPane.background", CREAM);
        UIManager.put("OptionPane.messageForeground", TEXT);
        UIManager.put("OptionPane.messageFont", font(Font.PLAIN, 13));
        UIManager.put("OptionPane.buttonFont", font(Font.BOLD, 12));
        UIManager.put("OptionPane.yesButtonText", "Ya");
        UIManager.put("OptionPane.noButtonText", "Tidak");
        UIManager.put("OptionPane.cancelButtonText", "Batal");
        UIManager.put("OptionPane.okButtonText", "OK");

        UIManager.put("Button.background", IVORY);
        UIManager.put("Button.foreground", TEXT);
        UIManager.put("Button.disabledText", DISABLED_FG);
        UIManager.put("Button.select", SELECT);
        UIManager.put("Button.focus", LIGHT_BROWN);

        UIManager.put("Label.foreground", TEXT);
        UIManager.put("CheckBox.background", IVORY);
        UIManager.put("CheckBox.foreground", TEXT);

        UIManager.put("ScrollBar.thumb", LIGHT_BROWN);
        UIManager.put("ScrollBar.thumbShadow", LIGHT_BROWN);
        UIManager.put("ScrollBar.thumbHighlight", LIGHT_BROWN);
        UIManager.put("ScrollBar.thumbDarkShadow", LIGHT_BROWN);
        UIManager.put("ScrollBar.track", ROW_ALT);

        UIManager.put("MenuBar.background", DARK_BROWN);
        UIManager.put("MenuBar.foreground", WHITE);
        UIManager.put("Menu.background", DARK_BROWN);
        UIManager.put("Menu.foreground", WHITE);
        UIManager.put("Menu.selectionBackground", SIDEBAR_HOVER);
        UIManager.put("Menu.selectionForeground", WHITE);
        UIManager.put("MenuItem.background", IVORY);
        UIManager.put("MenuItem.foreground", TEXT);
        UIManager.put("MenuItem.selectionBackground", SELECT);
        UIManager.put("MenuItem.selectionForeground", DARK_BROWN);
        UIManager.put("MenuItem.disabledForeground", DISABLED_FG);
        UIManager.put("Menu.disabledForeground", DISABLED_FG);
        UIManager.put("PopupMenu.background", IVORY);
        UIManager.put("PopupMenu.border",
                BorderFactory.createLineBorder(BORDER));
        UIManager.put("Separator.foreground", BORDER);
        UIManager.put("Separator.background", IVORY);

        UIManager.put("ComboBox.background", WHITE);
        UIManager.put("ComboBox.foreground", TEXT);
        UIManager.put("ComboBox.selectionBackground", SELECT);
        UIManager.put("ComboBox.selectionForeground", DARK_BROWN);
        UIManager.put("List.selectionBackground", SELECT);
        UIManager.put("List.selectionForeground", DARK_BROWN);
        UIManager.put("ToolTip.background", IVORY);
        UIManager.put("ToolTip.foreground", TEXT);
    }

    // ===================== FONT & TEKS =====================
    public static Font font(int style, int size) {
        return new Font("SansSerif", style, size);
    }

    public static Font titleFont(int size) {
        return new Font("Serif", Font.BOLD, size);
    }

    public static String str(Object value) {
        return value == null ? "" : value.toString();
    }

    private static Color blend(Color a, Color b, float t) {
        int r = Math.round(a.getRed() + (b.getRed() - a.getRed()) * t);
        int g = Math.round(a.getGreen() + (b.getGreen() - a.getGreen()) * t);
        int bl = Math.round(a.getBlue() + (b.getBlue() - a.getBlue()) * t);
        return new Color(r, g, bl);
    }

    // ===================== FRAME & HALAMAN =====================
    public static void styleFrame(JFrame frame) {
        frame.getContentPane().setBackground(CREAM);
        frame.setLocationRelativeTo(null);
    }

    /** Panel dengan latar gradasi vertikal (dipakai sidebar & login). */
    public static JPanel gradientPanel(
            final Color top, final Color bottom, LayoutManager layout) {

        JPanel panel = new JPanel(layout) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new GradientPaint(
                        0, 0, top, 0, getHeight(), bottom));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        return panel;
    }

    /**
     * Susunan halaman master: banner judul coklat di atas,
     * lalu kartu form dan kartu tabel di bawahnya.
     */
    public static JPanel createPage(
            String title, String subtitle,
            JPanel formCard, JPanel tableCard) {

        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(CREAM);

        // ---- banner ----
        JPanel banner = gradientPanel(
                DARK_BROWN, MID_BROWN, new BorderLayout(0, 4));
        banner.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 3, 0, LIGHT_BROWN),
                BorderFactory.createEmptyBorder(16, 28, 14, 28)));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(titleFont(24));
        lblTitle.setForeground(WHITE);

        JLabel lblSub = new JLabel(subtitle);
        lblSub.setFont(font(Font.PLAIN, 12));
        lblSub.setForeground(new Color(0xE8D5BD));

        banner.add(lblTitle, BorderLayout.CENTER);
        banner.add(lblSub, BorderLayout.SOUTH);
        page.add(banner, BorderLayout.NORTH);

        // ---- isi ----
        JPanel body = new JPanel(new BorderLayout(0, 16));
        body.setBackground(CREAM);
        body.setBorder(BorderFactory.createEmptyBorder(18, 24, 22, 24));
        body.add(formCard, BorderLayout.NORTH);
        body.add(tableCard, BorderLayout.CENTER);
        page.add(body, BorderLayout.CENTER);

        return page;
    }

    // ===================== KARTU & FORM =====================
    public static JPanel createCard(LayoutManager layout, String title) {
        JPanel card = new JPanel(layout);
        card.setBackground(IVORY);

        Border titled = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                "  " + title + "  ",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                font(Font.BOLD, 11),
                MID_BROWN);

        card.setBorder(BorderFactory.createCompoundBorder(
                titled,
                BorderFactory.createEmptyBorder(8, 16, 14, 16)));
        return card;
    }

    /**
     * Menambah satu kolom form (label di atas, input di bawah).
     * Form memiliki 2 kolom: col 0 dan col 1.
     */
    public static void addField(JPanel form, int row, int col,
                                String label, JComponent input) {

        styleInput(input);

        JLabel lbl = new JLabel(label);
        lbl.setFont(font(Font.BOLD, 12));
        lbl.setForeground(MID_BROWN);

        int left = col == 0 ? 0 : 10;
        int right = col == 0 ? 10 : 0;

        GridBagConstraints gl = new GridBagConstraints();
        gl.gridx = col;
        gl.gridy = row * 2;
        gl.weightx = 1;
        gl.anchor = GridBagConstraints.WEST;
        gl.fill = GridBagConstraints.HORIZONTAL;
        gl.insets = new Insets(8, left, 4, right);
        form.add(lbl, gl);

        GridBagConstraints gi = new GridBagConstraints();
        gi.gridx = col;
        gi.gridy = row * 2 + 1;
        gi.weightx = 1;
        gi.anchor = GridBagConstraints.WEST;
        gi.fill = GridBagConstraints.HORIZONTAL;
        gi.insets = new Insets(0, left, 2, right);
        form.add(input, gi);
    }

    /** Baris tombol di bawah form (rata TENGAH, melebar 2 kolom). */
    public static void addButtonRow(JPanel form, int row, int count,
                                    JButton... buttons) {

        JPanel bar = new JPanel();
        bar.setLayout(new javax.swing.BoxLayout(
                bar, javax.swing.BoxLayout.X_AXIS));
        bar.setOpaque(false);

        // Glue di kiri dan kanan membuat tombol berada di tengah.
        bar.add(javax.swing.Box.createHorizontalGlue());

        for (int i = 0; i < buttons.length; i++) {
            if (i > 0) {
                bar.add(javax.swing.Box.createHorizontalStrut(10));
            }
            bar.add(buttons[i]);
        }

        bar.add(javax.swing.Box.createHorizontalGlue());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = row * 2;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(14, 0, 0, 0);
        form.add(bar, gbc);
    }

    private static void styleInput(JComponent input) {
        if (input instanceof JPasswordField) {
            stylePasswordField((JPasswordField) input);
        } else if (input instanceof JTextField) {
            styleTextField((JTextField) input);
        } else if (input instanceof JComboBox) {
            styleCombo((JComboBox<?>) input);
        } else if (input instanceof JCheckBox) {
            JCheckBox cb = (JCheckBox) input;
            cb.setOpaque(false);
            cb.setFont(font(Font.PLAIN, 13));
            cb.setForeground(TEXT);
            cb.setFocusPainted(false);
            cb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            cb.setPreferredSize(new Dimension(100, 36));
        }
    }

    // ===================== INPUT =====================
    private static Border fieldBorder(Color line, int thickness) {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(line, thickness),
                BorderFactory.createEmptyBorder(
                        thickness == 1 ? 6 : 5,
                        thickness == 1 ? 10 : 9,
                        thickness == 1 ? 6 : 5,
                        thickness == 1 ? 10 : 9));
    }

    public static void styleTextField(final JTextField field) {
        field.setFont(font(Font.PLAIN, 13));
        field.setForeground(TEXT);
        field.setBackground(WHITE);
        field.setCaretColor(DARK_BROWN);
        field.setSelectionColor(SELECT);
        field.setSelectedTextColor(DARK_BROWN);
        field.setBorder(fieldBorder(BORDER, 1));
        field.setPreferredSize(new Dimension(100, 36));

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(fieldBorder(ACCENT, 2));
            }

            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(fieldBorder(BORDER, 1));
            }
        });
    }

    public static void stylePasswordField(JPasswordField field) {
        styleTextField(field);
    }

    public static void styleCombo(JComboBox<?> combo) {
        combo.setFont(font(Font.PLAIN, 13));
        combo.setForeground(TEXT);
        combo.setBackground(WHITE);
        combo.setPreferredSize(new Dimension(100, 36));
        combo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    // ===================== TOMBOL =====================
    public static JButton createButton(String text, Color base) {
        return new RoundButton(text, base);
    }

    private static class RoundButton extends JButton {
        private final Color base;
        private boolean hover;
        private boolean pressed;

        RoundButton(String text, Color base) {
            super(text);
            this.base = base;

            setFont(font(Font.BOLD, 12));
            setForeground(WHITE);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            FontMetrics fm = getFontMetrics(getFont());
            setPreferredSize(new Dimension(
                    Math.max(96, fm.stringWidth(text) + 40), 38));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hover = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hover = false;
                    pressed = false;
                    repaint();
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    pressed = true;
                    repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    pressed = false;
                    repaint();
                }
            });
        }

        // Agar BoxLayout tidak memperkecil tinggi tombol.
        @Override
        public Dimension getMaximumSize() {
            return getPreferredSize();
        }

        @Override
        public Dimension getMinimumSize() {
            return getPreferredSize();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            Color fill;
            if (!isEnabled()) {
                fill = DISABLED_BG;
            } else if (pressed) {
                fill = blend(base, Color.BLACK, 0.18f);
            } else if (hover) {
                fill = blend(base, Color.WHITE, 0.14f);
            } else {
                fill = base;
            }

            int w = getWidth();
            int h = getHeight();

            if (isEnabled() && !pressed) {
                g2.setColor(new Color(0, 0, 0, 28));
                g2.fillRoundRect(0, 2, w, h - 2, 14, 14);
            }

            g2.setColor(fill);
            g2.fillRoundRect(0, 0, w, h - 2, 14, 14);
            g2.dispose();

            super.paintComponent(g);
        }
    }

    // ===================== TABEL =====================
    public static void styleTable(JTable table) {
        table.setFont(font(Font.PLAIN, 12));
        table.setForeground(TEXT);
        table.setBackground(WHITE);
        table.setRowHeight(32);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(0xEFE4D5));
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setSelectionBackground(SELECT);
        table.setSelectionForeground(DARK_BROWN);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);

        TableRenderer renderer = new TableRenderer(SwingConstants.LEFT, null);
        table.setDefaultRenderer(Object.class, renderer);
        table.setDefaultRenderer(Number.class, renderer);

        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(100, 36));
        header.setDefaultRenderer(new HeaderRenderer());
    }

    public static void fixedWidth(JTable table, int column, int width) {
        TableColumn c = table.getColumnModel().getColumn(column);
        c.setMinWidth(width);
        c.setMaxWidth(width);
        c.setPreferredWidth(width);
    }

    public static JScrollPane createScroll(JTable table) {
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER, 1));
        scroll.getViewport().setBackground(WHITE);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    public static JPanel createSearchBar(JTextField field, JButton button) {
        JPanel bar = new JPanel(new BorderLayout(10, 0));
        bar.setOpaque(false);

        JLabel lbl = new JLabel("Cari");
        lbl.setFont(font(Font.BOLD, 12));
        lbl.setForeground(MID_BROWN);

        styleTextField(field);

        bar.add(lbl, BorderLayout.WEST);
        bar.add(field, BorderLayout.CENTER);
        bar.add(button, BorderLayout.EAST);
        return bar;
    }

    /** Renderer sel: zebra, perataan, dan format teks opsional. */
    public static class TableRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 1L;

        private final int align;
        private final Function<Object, String> formatter;

        public TableRenderer(int align, Function<Object, String> formatter) {
            this.align = align;
            this.formatter = formatter;
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean selected,
                boolean focused, int row, int column) {

            String text = formatter != null
                    ? formatter.apply(value) : str(value);

            super.getTableCellRendererComponent(
                    table, text, selected, false, row, column);

            setHorizontalAlignment(align);
            setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

            if (selected) {
                setBackground(SELECT);
                setForeground(DARK_BROWN);
            } else {
                setBackground(row % 2 == 0 ? WHITE : ROW_ALT);
                setForeground(TEXT);
            }
            return this;
        }
    }

    private static class HeaderRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 1L;

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean selected,
                boolean focused, int row, int column) {

            super.getTableCellRendererComponent(
                    table, value, false, false, row, column);

            setFont(font(Font.BOLD, 12));
            setForeground(WHITE);
            setBackground(DARK_BROWN);
            setOpaque(true);
            setHorizontalAlignment(SwingConstants.LEFT);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(
                            0, 0, 2, 0, LIGHT_BROWN),
                    BorderFactory.createEmptyBorder(0, 10, 0, 10)));
            return this;
        }
    }

    // ===================== DIALOG =====================
    public static void showMessage(Component parent, String message,
                                   String title, int type) {
        JOptionPane.showMessageDialog(parent, message, title, type);
    }

    public static int showConfirm(Component parent, String message,
                                  String title) {
        return JOptionPane.showConfirmDialog(parent, message, title,
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    }
}