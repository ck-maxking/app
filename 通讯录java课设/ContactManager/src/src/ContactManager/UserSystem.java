package src.ContactManager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserSystem {
    private static final String USER_DATA_FILE = "users.dat";
    private List<User> users;

    public UserSystem() {
        this.users = new ArrayList<>();
        loadUsers();

        // 如果文件不存在，创建一个默认管理员用户
        if (users.isEmpty()) {
            users.add(new User("user", "123"));
            saveUsers();
        }
    }

    public List<User> getUsers() {
        return new ArrayList<>(users);
    }

    public void addUser(User user) {
        users.add(user);
        saveUsers();
    }

    public void removeUser(User user) {
        // 删除用户对应的联系人文件
        File contactsFile = new File(user.getContactsFile());
        if (contactsFile.exists()) {
            contactsFile.delete();
        }

        users.remove(user);
        saveUsers();
    }

    public User authenticateUser(String username, String password) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    @SuppressWarnings("unchecked")
    private void loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USER_DATA_FILE))) {
            users = (List<User>) ois.readObject();
        } catch (FileNotFoundException e) {
            // 首次运行，文件不存在是正常的
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_DATA_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}