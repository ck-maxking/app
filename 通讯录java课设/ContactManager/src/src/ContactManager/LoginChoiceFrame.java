package src.ContactManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginChoiceFrame extends JFrame {
    private final UserSystem userSystem;

    public LoginChoiceFrame(UserSystem userSystem) {
        this.userSystem = userSystem;
        initUI();
    }

    private void initUI() {
        setTitle("通讯录管理系统 - 登录选择");
        setSize(1100, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton adminBtn = new JButton("管理员登录");
        adminBtn.setFont(new Font("正楷", Font.PLAIN, 50));
        adminBtn.setBorder(BorderFactory.createLineBorder(Color.RED)); // 添加边框颜色
        JButton userBtn = new JButton("用户登录");
        userBtn.setFont(new Font("正楷", Font.PLAIN, 50));
        userBtn.setBorder(BorderFactory.createLineBorder(Color.CYAN)); // 添加边框颜色
        JButton exitBtn = new JButton("退出系统");
        exitBtn.setFont(new Font("正楷", Font.PLAIN, 50));
        exitBtn.setBorder(BorderFactory.createLineBorder(Color.ORANGE)); // 添加边框颜色

        adminBtn.addActionListener(this::adminLogin);
        userBtn.addActionListener(this::userLogin);
        exitBtn.addActionListener(e -> System.exit(0));

        panel.add(adminBtn);
        panel.add(userBtn);
        panel.add(exitBtn);

        add(panel);
    }

    private void adminLogin(ActionEvent e) {
        new AdminFrame(userSystem).setVisible(true);
        dispose();
    }

    private void userLogin(ActionEvent e) {
        new UserLoginFrame(userSystem).setVisible(true);
        dispose();
    }
}