package src.ContactManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// 管理员控制台界面类，继承自 JFrame
public class AdminFrame extends JFrame {
    private final UserSystem userSystem;// 用户系统对象，用于管理用户信息
    private DefaultListModel<String> userListModel; // 用于存储用户列表数据的模型，修改为存储 String 类型
    private JTextField searchField;// 搜索框，用于输入搜索关键字

    // 构造函数，接收一个 UserSystem 对象作为参数
    public AdminFrame(UserSystem userSystem) {
        this.userSystem = userSystem;
        initUI();// 初始化用户界面
        loadUsers(); // 加载用户信息到列表中
    }

    // 初始化用户界面的方法
    private void initUI() {
        setTitle("管理员控制台");// 设置窗口标题
        setSize(1100, 550); // 设置窗口大小
        setLocationRelativeTo(null);  // 将窗口居中显示
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);  // 设置窗口关闭操作，关闭窗口时释放资源

        userListModel = new DefaultListModel<>();// 初始化用户列表模型
        JList<String> userList = new JList<>(userListModel); // 创建用户列表，使用用户列表模型
        userList.setFont(new Font("正楷", Font.PLAIN, 30));// 设置用户列表的字体
        JScrollPane scrollPane = new JScrollPane(userList);// 创建滚动面板，将用户列表添加到滚动面板中

        JButton addBtn = new JButton("添加用户");// 创建添加用户按钮
        addBtn.setFont(new Font("正楷", Font.PLAIN, 30)); // 设置按钮字体
        addBtn.setBorder(BorderFactory.createLineBorder(Color.GREEN)); // 添加边框颜色
        JButton editBtn = new JButton("编辑用户");
        editBtn.setFont(new Font("正楷", Font.PLAIN, 30));
        editBtn.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        JButton deleteBtn = new JButton("删除用户");
        deleteBtn.setFont(new Font("正楷", Font.PLAIN, 30));
        deleteBtn.setBorder(BorderFactory.createLineBorder(Color.RED));
        JButton backBtn = new JButton("返回登录");
        backBtn.setFont(new Font("正楷", Font.PLAIN, 30));
        backBtn.setBorder(BorderFactory.createLineBorder(Color.YELLOW));
        JButton sortBtn = new JButton("按字母排序");
        sortBtn.setFont(new Font("正楷", Font.PLAIN, 30));
        sortBtn.setBorder(BorderFactory.createLineBorder(Color.ORANGE));
        searchField = new JTextField();// 创建搜索框
        searchField.setPreferredSize(new Dimension(50, 30));// 设置搜索框的首选大小
        JButton searchBtn = new JButton("搜索");// 创建搜索按钮
        searchBtn.setFont(new Font("正楷", Font.PLAIN, 30));// 设置按钮字体
        searchBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // 添加边框颜色

        addBtn.addActionListener(this::addUser);// 为添加用户按钮添加事件监听器，点击时调用 addUser 方法
        editBtn.addActionListener(e -> editUser(userList.getSelectedIndex())); // 为编辑用户按钮添加事件监听器，点击时调用 editUser 方法，并传递所选用户的索引
        deleteBtn.addActionListener(e -> deleteUser(userList.getSelectedIndex())); // 为删除用户按钮添加事件监听器，点击时调用 deleteUser 方法，并传递所选用户的索引
        backBtn.addActionListener(e -> { // 为返回登录按钮添加事件监听器，点击时打开登录选择界面并关闭当前窗口
            new LoginChoiceFrame(userSystem).setVisible(true);
            dispose();
        });
        sortBtn.addActionListener(e -> sortUsers());// 为按字母排序按钮添加事件监听器，点击时调用 sortUsers 方法
        searchBtn.addActionListener(e -> searchUsers());// 为搜索按钮添加事件监听器，点击时调用 searchUsers 方法

        JPanel buttonPanel = new JPanel();// 创建按钮面板，使用垂直布局
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        int spacing = 30;// 设置按钮之间的间距
        buttonPanel.add(Box.createRigidArea(new Dimension(0, spacing)));// 添加间距
        buttonPanel.add(addBtn); // 将添加用户按钮添加到按钮面板中
        buttonPanel.add(Box.createRigidArea(new Dimension(0, spacing)));
        buttonPanel.add(editBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, spacing)));
        buttonPanel.add(deleteBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, spacing)));
        buttonPanel.add(backBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, spacing)));
        buttonPanel.add(sortBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, spacing)));
        buttonPanel.add(searchField);// 将搜索按钮添加到按钮面板中
        buttonPanel.add(searchBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, spacing)));

        setLayout(new BorderLayout());// 设置窗口的布局管理器为边界布局
        add(scrollPane, BorderLayout.CENTER);// 将滚动面板添加到窗口的中间位置
        add(buttonPanel, BorderLayout.EAST);// 将按钮面板添加到窗口的右侧位置
    }

    private void loadUsers() {
        userListModel.clear();
        userSystem.getUsers().forEach(user -> userListModel.addElement("用户名：" + user.getUsername() + "，密码：" + user.getPassword()));
    }

    private void addUser(ActionEvent e) {
        JTextField usernameField = new JTextField();
        JTextField passwordField = new JTextField();

        JLabel usernameLabel = new JLabel("用户名:");
        JLabel passwordLabel = new JLabel("密码:");

        Font largeFont = new Font("正楷", Font.PLAIN, 80);
        usernameLabel.setFont(largeFont);
        passwordLabel.setFont(largeFont);
        usernameField.setFont(largeFont);
        passwordField.setFont(largeFont);

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);

        panel.setPreferredSize(new Dimension(1050, 320));

        JButton okButton = new JButton("确认");
        okButton.setBorder(BorderFactory.createLineBorder(Color.red)); // 添加边框颜色
        JButton cancelButton = new JButton("取消");
        cancelButton.setBorder(BorderFactory.createLineBorder(Color.blue)); // 添加边框颜色
        okButton.setFont(largeFont);
        cancelButton.setFont(largeFont);
        okButton.setPreferredSize(new Dimension(350, 150));
        cancelButton.setPreferredSize(new Dimension(350, 150));

        Object[] options = {okButton, cancelButton};
        final int[] result = new int[1];
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                result[0] = 0;
                ((JDialog) SwingUtilities.getRoot(panel)).dispose();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                result[0] = 1;
                ((JDialog) SwingUtilities.getRoot(panel)).dispose();
            }
        });

        JOptionPane.showOptionDialog(this, panel, "添加用户",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);

        if (result[0] == 0) {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            if (!username.isEmpty() && !password.isEmpty()) {
                if (isUsernameExists(username)) {
                    JOptionPane.showMessageDialog(this, "该用户名已存在，请选择其他用户名。", "错误", JOptionPane.ERROR_MESSAGE);
                } else {
                    userSystem.addUser(new User(username, password));
                    loadUsers();
                }
            }
        }
    }

    private void editUser(int index) {
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "请先选择一个用户", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<User> users = userSystem.getUsers();
        User user = users.get(index);

        JTextField usernameField = new JTextField(user.getUsername());
        JTextField passwordField = new JTextField(user.getPassword());

        JLabel usernameLabel = new JLabel("用户名:");
        JLabel passwordLabel = new JLabel("新密码:");

        Font largeFont = new Font("正楷", Font.PLAIN, 80);
        usernameLabel.setFont(largeFont);
        passwordLabel.setFont(largeFont);
        usernameField.setFont(largeFont);
        passwordField.setFont(largeFont);

        JPanel panel = new JPanel(new GridLayout(2, 2,  10, 10));
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.setPreferredSize(new Dimension(1050, 320));

        JButton okButton = new JButton("确认");
        okButton.setBorder(BorderFactory.createLineBorder(Color.red)); // 添加边框颜色
        JButton cancelButton = new JButton("取消");
        cancelButton.setBorder(BorderFactory.createLineBorder(Color.blue)); // 添加边框颜色
        okButton.setFont(largeFont);
        cancelButton.setFont(largeFont);
        okButton.setPreferredSize(new Dimension(350, 150));
        cancelButton.setPreferredSize(new Dimension(350, 150));

        Object[] options = {okButton, cancelButton};
        final int[] result = new int[1];
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                result[0] = 0;
                ((JDialog) SwingUtilities.getRoot(panel)).dispose();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                result[0] = 1;
                ((JDialog) SwingUtilities.getRoot(panel)).dispose();
            }
        });

        JOptionPane.showOptionDialog(this, panel, "编辑用户: " + user.getUsername(),
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);

        if (result[0] == 0) {
            String newUsername = usernameField.getText().trim();
            String newPassword = passwordField.getText().trim();
            if (!newUsername.isEmpty() && !newPassword.isEmpty()) {
                if (!newUsername.equals(user.getUsername()) && isUsernameExists(newUsername)) {
                    JOptionPane.showMessageDialog(this, "该用户名已存在，请选择其他用户名。", "错误", JOptionPane.ERROR_MESSAGE);
                } else {
                    user.setUsername(newUsername);
                    user.setPassword(newPassword);
                    userSystem.saveUsers();
                    loadUsers();
                }
            }
        }
    }

    private void deleteUser(int index) {
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "请先选择一个用户", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<User> users = userSystem.getUsers();
        User user = users.get(index);

        int confirm = JOptionPane.showConfirmDialog(this,
                "确定要删除用户 " + user.getUsername() + " 吗?", "确认删除",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            userSystem.removeUser(user);
            loadUsers();
        }
    }

    private void sortUsers() {
        List<User> users = userSystem.getUsers();
        // 使用 Collator 进行中文排序
        Collator collator = Collator.getInstance();
        Collections.sort(users, Comparator.comparing(User::getUsername, collator));
        userListModel.clear();
        users.forEach(user -> userListModel.addElement("用户名：" + user.getUsername() + "，密码：" + user.getPassword()));
    }

    private void searchUsers() {
        String keyword = searchField.getText().trim();
        userListModel.clear();
        userSystem.getUsers().stream()
                .filter(u -> u.getUsername().contains(keyword))
                .forEach(user -> userListModel.addElement("用户名：" + user.getUsername() + "，密码：" + user.getPassword()));
    }

    private boolean isUsernameExists(String username) {
        return userSystem.getUsers().stream().anyMatch(u -> u.getUsername().equals(username));
    }
}