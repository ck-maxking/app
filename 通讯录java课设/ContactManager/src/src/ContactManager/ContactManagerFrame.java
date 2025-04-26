package src.ContactManager;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ContactManagerFrame extends JFrame {
    private final UserSystem userSystem;
    private final User currentUser;
    private ArrayList<Contact> contacts;
    private DefaultListModel<String> listModel;
    private JTextField searchField;

    public ContactManagerFrame(UserSystem userSystem, User user) {
        this.userSystem = userSystem;
        this.currentUser = user;
        this.contacts = new ArrayList<>();
        this.listModel = new DefaultListModel<>();
        initUI();
        loadContacts();
    }

    private void initUI() {
        setTitle("通讯录管理器 - " + currentUser.getUsername());
        setSize(1100, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 联系人列表
        JList<String> contactList = new JList<>(listModel);
        contactList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        contactList.setFont(new Font("正楷", Font.PLAIN, 30));
        JScrollPane listScrollPane = new JScrollPane(contactList);
        listScrollPane.setBorder(BorderFactory.createTitledBorder("联系人列表"));
        mainPanel.add(listScrollPane, BorderLayout.CENTER);

        // 按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        // 定义间距大小
        int spacing = 15;
        buttonPanel.add(Box.createRigidArea(new Dimension(0, spacing)));
        buttonPanel.add(createStyledButton("添加联系人", Color.GREEN, e -> addContact()));
        buttonPanel.add(Box.createRigidArea(new Dimension(0, spacing)));
        buttonPanel.add(createStyledButton("编辑联系人", Color.BLUE, e -> editContact(contactList.getSelectedIndex())));
        buttonPanel.add(Box.createRigidArea(new Dimension(0, spacing)));
        buttonPanel.add(createStyledButton("删除联系人", Color.RED, e -> deleteContact(contactList.getSelectedIndex())));
        buttonPanel.add(Box.createRigidArea(new Dimension(0, spacing)));
        buttonPanel.add(createStyledButton("清空联系人", Color.MAGENTA, e -> clearContacts()));
        buttonPanel.add(Box.createRigidArea(new Dimension(0, spacing)));
        buttonPanel.add(createStyledButton("按字母排序", Color.YELLOW, e -> sortContacts()));
        buttonPanel.add(Box.createRigidArea(new Dimension(0, spacing)));
        buttonPanel.add(createStyledButton("返回登录", Color.ORANGE, e -> {
            new LoginChoiceFrame(userSystem).setVisible(true);
            dispose();
        }));

        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(200, 50));

        JButton searchBtn = new JButton("搜索");
        searchBtn.addActionListener(e -> searchContacts());
        searchBtn.setFont(new Font("正楷", Font.PLAIN, 30));
        searchBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // 添加边框颜色

        // 提前声明和初始化 searchPanel
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(searchField, BorderLayout.CENTER);

        searchPanel.add(Box.createRigidArea(new Dimension(10, 0)), BorderLayout.EAST);
        searchPanel.add(searchBtn, BorderLayout.LINE_END);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(buttonPanel, BorderLayout.CENTER);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)), BorderLayout.NORTH);
        rightPanel.add(searchPanel, BorderLayout.NORTH);

        mainPanel.add(rightPanel, BorderLayout.EAST);

        // 菜单栏
        createMenuBar();

        add(mainPanel);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // 文件菜单
        JMenu fileMenu = new JMenu("文件");
        JMenuItem importItem = new JMenuItem("导入通讯录");
        JMenuItem exportItem = new JMenuItem("导出通讯录");

        importItem.addActionListener(e -> importContacts());
        exportItem.addActionListener(e -> exportContacts());

        fileMenu.add(importItem);
        fileMenu.add(exportItem);

        // 帮助菜单
        JMenu helpMenu = new JMenu("帮助");
        JMenuItem aboutItem = new JMenuItem("关于");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
    }

    private JButton createStyledButton(String text, Color bgColor, ActionListener listener) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.BLACK);
        button.setFont(new Font("正楷", Font.BOLD, 30));
        button.setFocusPainted(false);
        button.addActionListener(listener);
        return button;
    }

    private void addContact() {
        JTextField nameField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField emailField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.add(new JLabel("姓名:"));
        panel.add(nameField);
        panel.add(new JLabel("电话号码:"));
        panel.add(phoneField);
        panel.add(new JLabel("电子邮箱:"));
        panel.add(emailField);

        // 设置面板大小
        panel.setPreferredSize(new Dimension(1100, 400));

        // 设置字体大小
        Font largeFont = new Font("正楷", Font.PLAIN, 80);
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JLabel || comp instanceof JTextField) {
                comp.setFont(largeFont);
            }
        }

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

        JOptionPane.showOptionDialog(this, panel, "添加联系人",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);

        if (result[0] == 0) {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();

            if (!name.isEmpty() && !phone.isEmpty()) {
                contacts.add(new Contact(name, phone, email));
                updateContactList();
                saveContacts();
            }
        }
    }

    private void editContact(int index) {
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "请先选择一个联系人", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Contact contact = contacts.get(index);
        JTextField nameField = new JTextField(contact.getName());
        JTextField phoneField = new JTextField(contact.getPhoneNumber());
        JTextField emailField = new JTextField(contact.getEmail());

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.add(new JLabel("姓名:"));
        panel.add(nameField);
        panel.add(new JLabel("电话号码:"));
        panel.add(phoneField);
        panel.add(new JLabel("电子邮箱:"));
        panel.add(emailField);

        // 设置面板大小
        panel.setPreferredSize(new Dimension(1100, 400));

        // 设置字体大小
        Font largeFont = new Font("正楷", Font.PLAIN, 80);
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JLabel || comp instanceof JTextField) {
                comp.setFont(largeFont);
            }
        }

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

        JOptionPane.showOptionDialog(this, panel, "编辑联系人",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);

        if (result[0] == 0) {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();

            if (!name.isEmpty() && !phone.isEmpty()) {
                contact.setName(name);
                contact.setPhoneNumber(phone);
                contact.setEmail(email);
                updateContactList();
                saveContacts();
            }
        }
    }

    private void deleteContact(int index) {
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "请先选择一个联系人", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "确定要删除选中的联系人吗？", "确认删除",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            contacts.remove(index);
            updateContactList();
            saveContacts();
        }
    }

    private void importContacts() {
        JFileChooser fileChooser = new JFileChooser();
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                importFromTXT(file);
                updateContactList();
                saveContacts();
                JOptionPane.showMessageDialog(this, "通讯录导入成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "导入失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exportContacts() {
        JFileChooser fileChooser = new JFileChooser();
        int returnVal = fileChooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                exportToTXT(file);
                JOptionPane.showMessageDialog(this, "通讯录导出成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "导出失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void loadContacts() {
        File file = new File(currentUser.getContactsFile());
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                contacts = (ArrayList<Contact>) ois.readObject();
                updateContactList();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveContacts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(currentUser.getContactsFile()))) {
            oos.writeObject(contacts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateContactList() {
        listModel.clear();
        contacts.forEach(contact -> listModel.addElement(contact.toString()));
    }

    private void showAboutDialog() {
        JOptionPane.showMessageDialog(this,
                "通讯录管理器\n版本 3.0\n多用户版",
                "关于", JOptionPane.INFORMATION_MESSAGE);
    }

    private void importFromTXT(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            contacts.clear();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String email = parts.length > 2 ? parts[2] : "";
                    contacts.add(new Contact(parts[0], parts[1], email));
                }
            }
        }
    }

    private void exportToTXT(File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Contact contact : contacts) {
                writer.write(String.format("%s,%s,%s",
                        contact.getName(), contact.getPhoneNumber(), contact.getEmail()));
                writer.newLine();
            }
        }
    }

    private void sortContacts() {
        // 创建一个 Collator 对象，使用默认的语言环境
        Collator collator = Collator.getInstance();
        // 对联系人列表进行排序，使用 Collator 进行比较
        Collections.sort(contacts, Comparator.comparing(Contact::getName, collator));
        updateContactList();
    }

    private void searchContacts() {
        String keyword = searchField.getText().trim();
        listModel.clear();
        contacts.stream()
                .filter(contact -> contact.getName().contains(keyword))
                .forEach(contact -> listModel.addElement(contact.toString()));
    }

    // 新增的清空联系人方法
    private void clearContacts() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "确定要清空所有联系人吗?", "确认清空",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            contacts.clear();
            updateContactList();
            saveContacts();
        }
    }
}