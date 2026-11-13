
package util;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.function.Function;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public final class Theme {

    private Theme() {}

    // ==================================================
    // PALET WARNA
    // ==================================================

    public static final Color DARK_BROWN = new Color(92, 64, 51);
    public static final Color MID_BROWN = new Color(166, 119, 78);
    public static final Color LIGHT_BROWN = new Color(205, 175, 145);

    public static final Color CREAM = new Color(248, 241, 232);
    public static final Color IVORY = new Color(255, 251, 245);
    public static final Color WHITE = Color.WHITE;

    public static final Color TEXT = new Color(70, 52, 42);
    public static final Color MUTED = new Color(135, 119, 103);
    public static final Color BORDER = new Color(222, 208, 192);

    public static final Color ROW_ALT = new Color(251, 246, 239);
    public static final Color SELECT = new Color(236, 220, 200);

    public static final Color BTN_SAVE = new Color(166, 119, 78);
    public static final Color BTN_EDIT = new Color(125, 91, 67);
    public static final Color BTN_DELETE = new Color(169, 76, 63);
    public static final Color BTN_NEUTRAL = new Color(135, 119, 103);
    public static final Color BTN_SUCCESS = new Color(111, 125, 91);

    public static final Color SIDEBAR = new Color(73, 49, 39);
    public static final Color SIDEBAR_HOVER = new Color(105, 73, 57);
    public static final Color SIDEBAR_ACTIVE = new Color(166, 119, 78);

    // ==================================================
    // FONT
    // ==================================================

    public static final Font TITLE_FONT =
            new Font("SansSerif", Font.BOLD, 24);

    public static final Font NORMAL_FONT =
            new Font("SansSerif", Font.PLAIN, 13);

    public static Font font(int style, int size) {
        return new Font("SansSerif", style, size);
    }

    public static Font titleFont(int size) {
        return new Font("SansSerif", Font.BOLD, size);
    }

    public static Border emptyBorder(
            int top, int left, int bottom, int right) {

        return BorderFactory.createEmptyBorder(
                top, left, bottom, right
        );
    }

    // ==================================================
    // FRAME
    // ==================================================

    public static void styleFrame(JFrame frame) {

        frame.setMinimumSize(new Dimension(850, 560));
        frame.setLocationRelativeTo(null);

        frame.setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        frame.getContentPane().setBackground(CREAM);
        frame.setFont(NORMAL_FONT);
    }

    // ==================================================
    // PANEL
    // ==================================================

    public static void stylePanel(
            JPanel panel, Color background) {

        panel.setBackground(background);
        panel.setFont(NORMAL_FONT);
    }

    // ==================================================
    // LABEL
    // ==================================================

    public static void styleLabel(
            JLabel label, int size, Color color, int style) {

        label.setFont(font(style, size));
        label.setForeground(color);
    }

    // ==================================================
    // TOMBOL
    // ==================================================

    public static void styleButton(JButton button) {
        styleButton(button, BTN_SAVE);
    }

    public static void styleButton(
            JButton button, Color color) {

        button.setUI(new RoundButtonUI());

        button.setFont(font(Font.BOLD, 12));
        button.setForeground(WHITE);
        button.setBackground(color);

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);

        button.setRolloverEnabled(true);

        button.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        button.setBorder(
                emptyBorder(10, 22, 10, 22)
        );

        updateButtonSize(button);
    }

    // Ukuran tombol mengikuti panjang teks

    private static void updateButtonSize(JButton button) {

        FontMetrics fm = button.getFontMetrics(
                button.getFont()
        );

        String text = button.getText() == null
                ? ""
                : button.getText();

        int textWidth = fm.stringWidth(text);

        Insets insets = button.getInsets();

        int width = Math.max(
                115,
                textWidth + insets.left + insets.right + 12
        );

        int height = 42;

        Dimension size = new Dimension(width, height);

        button.setPreferredSize(size);
        button.setMinimumSize(size);
    }

    // Mengubah teks sekaligus memperbarui ukuran tombol

    public static void setButtonText(
            JButton button, String text) {

        button.setText(text);
        updateButtonSize(button);

        button.revalidate();
        button.repaint();
    }

    public static JButton createButton(
            String text, Color color) {

        JButton button = new JButton(text);
        styleButton(button, color);

        return button;
    }

    // ==================================================
    // EFEK TOMBOL
    // ==================================================

    private static Color lighten(
            Color color, double factor) {

        int r = (int) (
                color.getRed()
                + (255 - color.getRed()) * factor
        );

        int g = (int) (
                color.getGreen()
                + (255 - color.getGreen()) * factor
        );

        int b = (int) (
                color.getBlue()
                + (255 - color.getBlue()) * factor
        );

        return new Color(r, g, b);
    }

    private static class RoundButtonUI
            extends BasicButtonUI {

        @Override
        public void paint(Graphics g, JComponent c) {

            AbstractButton button = (AbstractButton) c;

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            Color base = button.getBackground();
            Color fill;

            if (!button.isEnabled()) {
                fill = new Color(190, 181, 171);

            } else if (button.getModel().isPressed()) {
                fill = base.darker();

            } else if (button.getModel().isRollover()) {
                fill = lighten(base, 0.17);

            } else {
                fill = base;
            }

            g2.setColor(fill);

            g2.fillRoundRect(
                    0, 0,
                    c.getWidth(),
                    c.getHeight(),
                    14, 14
            );

            g2.dispose();

            super.paint(g, c);
        }
    }

    // ==================================================
    // INPUT TEXT FIELD
    // ==================================================

    private static Border inputBorder(Color color) {

        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        color, 1, true
                ),
                emptyBorder(7, 11, 7, 11)
        );
    }

    public static void styleTextField(JTextField field) {

        field.setFont(NORMAL_FONT);
        field.setForeground(TEXT);
        field.setBackground(WHITE);
        field.setCaretColor(DARK_BROWN);

        field.setBorder(inputBorder(BORDER));

        field.setPreferredSize(
                new Dimension(100, 38)
        );

        // Mencegah focus listener dipasang berulang kali

        if (!Boolean.TRUE.equals(
                field.getClientProperty("themeStyled"))) {

            field.addFocusListener(new FocusAdapter() {

                @Override
                public void focusGained(FocusEvent e) {
                    field.setBorder(inputBorder(MID_BROWN));
                }

                @Override
                public void focusLost(FocusEvent e) {
                    field.setBorder(inputBorder(BORDER));
                }
            });

            field.putClientProperty("themeStyled", true);
        }
    }

    // ==================================================
    // PASSWORD FIELD
    // ==================================================

    public static void stylePasswordField(
            JPasswordField field) {

        styleTextField(field);
    }

    // ==================================================
    // COMBO BOX
    // ==================================================

    public static void styleCombo(
            JComboBox<?> combo) {

        combo.setFont(NORMAL_FONT);
        combo.setForeground(TEXT);
        combo.setBackground(WHITE);

        combo.setPreferredSize(
                new Dimension(100, 38)
        );

        combo.setFocusable(true);
    }

    // ==================================================
    // TABEL
    // ==================================================

    public static class TableRenderer
            extends DefaultTableCellRenderer {

        private final Function<Object, String> formatter;

        public TableRenderer(
                int alignment,
                Function<Object, String> formatter) {

            this.formatter = formatter;
            setHorizontalAlignment(alignment);
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean selected,
                boolean focused,
                int row,
                int column) {

            Object result =
                    (formatter != null && value != null)
                    ? formatter.apply(value)
                    : value;

            super.getTableCellRendererComponent(
                    table,
                    result,
                    selected,
                    false,
                    row,
                    column
            );

            setFont(font(Font.PLAIN, 12));

            setBackground(
                    selected
                    ? SELECT
                    : (row % 2 == 0 ? WHITE : ROW_ALT)
            );

            setForeground(
                    selected ? DARK_BROWN : TEXT
            );

            setBorder(
                    emptyBorder(0, 10, 0, 10)
            );

            return this;
        }
    }

    public static void styleTable(JTable table) {

        table.setRowHeight(38);
        table.setFont(font(Font.PLAIN, 12));

        table.setGridColor(BORDER);

        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);

        table.setIntercellSpacing(
                new Dimension(0, 1)
        );

        table.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        table.setFillsViewportHeight(true);

        table.setAutoResizeMode(
                JTable.AUTO_RESIZE_ALL_COLUMNS
        );

        table.setSelectionBackground(SELECT);
        table.setSelectionForeground(DARK_BROWN);

        table.setDefaultRenderer(
                Object.class,
                new TableRenderer(
                        SwingConstants.LEFT, null
                )
        );

        JTableHeader header = table.getTableHeader();

        header.setReorderingAllowed(false);

        header.setPreferredSize(
                new Dimension(100, 42)
        );

        header.setDefaultRenderer(
                new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(
                    JTable t,
                    Object value,
                    boolean selected,
                    boolean focused,
                    int row,
                    int column) {

                JLabel label = (JLabel)
                        super.getTableCellRendererComponent(
                                t,
                                value,
                                false,
                                false,
                                row,
                                column
                        );

                label.setOpaque(true);
                label.setBackground(DARK_BROWN);
                label.setForeground(WHITE);

                label.setFont(
                        font(Font.BOLD, 12)
                );

                label.setBorder(
                        emptyBorder(0, 10, 0, 10)
                );

                return label;
            }
        });
    }

    // ==================================================
    // LEBAR KOLOM TABEL
    // ==================================================

    public static void fixedWidth(
            JTable table,
            int column,
            int width) {

        table.getColumnModel()
                .getColumn(column)
                .setPreferredWidth(width);

        table.getColumnModel()
                .getColumn(column)
                .setMaxWidth(width);
    }

    // ==================================================
    // SCROLL PANE
    // ==================================================

    public static JScrollPane createScroll(JTable table) {

        JScrollPane scroll = new JScrollPane(table);

        scroll.setBorder(
                BorderFactory.createLineBorder(BORDER)
        );

        scroll.getViewport().setBackground(WHITE);

        scroll.getVerticalScrollBar().setUnitIncrement(16);

        return scroll;
    }

    // ==================================================
    // HEADER HALAMAN
    // ==================================================

    public static JPanel createHeader(
            String title,
            String subtitle) {

        JPanel header = new JPanel(
                new GridLayout(2, 1, 0, 5)
        );

        header.setBackground(DARK_BROWN);

        header.setBorder(
                emptyBorder(22, 30, 22, 30)
        );

        JLabel lblTitle = new JLabel(title);

        lblTitle.setFont(titleFont(25));
        lblTitle.setForeground(WHITE);

        JLabel lblSubtitle = new JLabel(subtitle);

        lblSubtitle.setFont(
                font(Font.PLAIN, 13)
        );

        lblSubtitle.setForeground(CREAM);

        header.add(lblTitle);
        header.add(lblSubtitle);

        return header;
    }

    // ==================================================
    // CARD
    // ==================================================

    public static JPanel createCard(
            LayoutManager layout,
            String title) {

        JPanel panel = new JPanel(layout);

        panel.setBackground(IVORY);

        Border inner;

        if (title == null || title.trim().isEmpty()) {

            inner = emptyBorder(
                    18, 20, 18, 20
            );

        } else {

            inner = BorderFactory.createTitledBorder(
                    emptyBorder(12, 14, 14, 14),
                    title,
                    TitledBorder.LEADING,
                    TitledBorder.TOP,
                    font(Font.BOLD, 13),
                    DARK_BROWN
            );
        }

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER, 1, true
                        ),
                        inner
                )
        );

        return panel;
    }

    // ==================================================
    // HALAMAN UTAMA
    // ==================================================

    public static JPanel createPage(
            String title,
            String subtitle,
            JComponent top,
            JComponent center) {

        JPanel root = new JPanel(
                new BorderLayout()
        );

        root.setBackground(CREAM);

        root.add(
                createHeader(title, subtitle),
                BorderLayout.NORTH
        );

        JPanel body = new JPanel(
                new BorderLayout(0, 16)
        );

        body.setOpaque(false);

        body.setBorder(
                emptyBorder(22, 26, 22, 26)
        );

        if (top != null) {
            body.add(top, BorderLayout.NORTH);
        }

        if (center != null) {
            body.add(center, BorderLayout.CENTER);
        }

        root.add(body, BorderLayout.CENTER);

        return root;
    }

    // ==================================================
    // LABEL DAN INPUT FORM
    // ==================================================

    public static void addField(
            JPanel panel,
            int row,
            int column,
            String label,
            JComponent component) {

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.gridy = row;

        gbc.insets = new Insets(7, 10, 7, 10);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lbl = new JLabel(label);

        lbl.setFont(font(Font.BOLD, 12));
        lbl.setForeground(TEXT);

        if (component instanceof JPasswordField) {

            stylePasswordField(
                    (JPasswordField) component
            );

        } else if (component instanceof JTextField) {

            styleTextField(
                    (JTextField) component
            );

        } else if (component instanceof JComboBox) {

            styleCombo(
                    (JComboBox<?>) component
            );
        }

        gbc.gridx = column * 2;
        gbc.weightx = 0;

        panel.add(lbl, gbc);

        gbc.gridx = column * 2 + 1;
        gbc.weightx = 1;

        panel.add(component, gbc);
    }

    // ==================================================
    // BARIS TOMBOL AKSI
    // ==================================================

    public static void addButtonRow(
            JPanel panel,
            int row,
            int span,
            JButton... buttons) {

        JPanel bar = new JPanel(
                new FlowLayout(
                        FlowLayout.CENTER,
                        10,
                        5
                )
        );

        bar.setOpaque(false);

        for (JButton button : buttons) {
            bar.add(button);
        }

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = span;

        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.insets = new Insets(15, 5, 8, 5);

        panel.add(bar, gbc);
    }

    // ==================================================
    // SEARCH BAR
    // ==================================================

    public static JPanel createSearchBar(
            JTextField txt,
            JButton... buttons) {

        JPanel bar = new JPanel(
                new BorderLayout(12, 0)
        );

        bar.setOpaque(false);

        JLabel lbl = new JLabel("Pencarian");

        lbl.setFont(font(Font.BOLD, 12));
        lbl.setForeground(TEXT);

        styleTextField(txt);

        JPanel actions = new JPanel(
                new FlowLayout(
                        FlowLayout.RIGHT,
                        8,
                        0
                )
        );

        actions.setOpaque(false);

        for (JButton button : buttons) {
            actions.add(button);
        }

        bar.add(lbl, BorderLayout.WEST);
        bar.add(txt, BorderLayout.CENTER);
        bar.add(actions, BorderLayout.EAST);

        return bar;
    }

    // ==================================================
    // UTILITAS STRING
    // ==================================================

    public static String str(Object value) {

        return value == null
                ? ""
                : value.toString();
    }
}
