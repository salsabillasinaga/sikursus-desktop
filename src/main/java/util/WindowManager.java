package util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import javax.swing.JFrame;

public class WindowManager {

    private static final Map<String, JFrame> windows =
            new HashMap<String, JFrame>();

    public static void open(
            String key, Supplier<? extends JFrame> supplier) {

        JFrame frame = windows.get(key);

        if (frame != null && frame.isDisplayable()) {
            frame.setVisible(true);
            frame.toFront();
            frame.requestFocus();
            return;
        }

        frame = supplier.get();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        final String windowKey = key;

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                windows.remove(windowKey);
            }
        });

        windows.put(key, frame);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void closeAll() {
        for (JFrame frame : windows.values()) {
            if (frame != null) {
                frame.dispose();
            }
        }

        windows.clear();
    }
}