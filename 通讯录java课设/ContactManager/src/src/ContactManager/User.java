package src.ContactManager;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String password;
    private String contactsFile;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.contactsFile = "contacts_" + username + ".dat";
    }

    // Getters and Setters
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getContactsFile() { return contactsFile; }

    public void setPassword(String password) { this.password = password; }
    public void setUsername(String username) {
        this.username = username;
        this.contactsFile = "contacts_" + username + ".dat";
    }
    @Override
    public String toString() {
        return username;
    }
}