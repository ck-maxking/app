package src.ContactManager;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                UserSystem userSystem = new UserSystem();
                new LoginChoiceFrame(userSystem).setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}