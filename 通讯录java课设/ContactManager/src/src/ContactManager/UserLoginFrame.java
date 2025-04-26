package src.ContactManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class UserLoginFrame extends JFrame {
    private final UserSystem userSystem;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public UserLoginFrame(UserSystem userSystem) {
        this.userSystem = userSystem;
        initUI();
    }

    private void initUI() {
        setTitle("用户登录");
        setSize(1100, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 3, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        // 创建 JLabel 并设置字体大小
        JLabel usernameLabel = new JLabel("用户名:");
        usernameLabel.setFont(new Font("正楷", Font.PLAIN, 80)); // 设置字体为正楷，大小 80
        JLabel passwordLabel = new JLabel("密码:");
        passwordLabel.setFont(new Font("正楷", Font.PLAIN, 80)); // 设置字体为正楷，大小 80

        // 设置输入框字体大小
        Font inputFont = new Font("正楷", Font.PLAIN, 80);
        usernameField.setFont(inputFont);
        passwordField.setFont(inputFont);

        // 将设置好字体的 JLabel 添加到面板
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);

        JButton loginBtn = new JButton("登录");
        loginBtn.setFont(new Font("正楷", Font.PLAIN, 50));
        loginBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // 添加边框颜色
        JButton backBtn = new JButton("返回");
        backBtn.setFont(new Font("正楷", Font.PLAIN, 50));
        backBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // 添加边框颜色

        loginBtn.addActionListener(this::performLogin);
        backBtn.addActionListener(e -> {
            new LoginChoiceFrame(userSystem).setVisible(true);
            dispose();
        });

        panel.add(loginBtn);
        panel.add(backBtn);

        add(panel);
    }

    private void performLogin(ActionEvent e) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        User user = userSystem.authenticateUser(username, password);
        if (user != null) {
            new ContactManagerFrame(userSystem, user).setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "用户名或密码错误", "登录失败", JOptionPane.ERROR_MESSAGE);
        }
    }
}