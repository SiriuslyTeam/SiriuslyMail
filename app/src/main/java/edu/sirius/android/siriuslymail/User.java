package edu.sirius.android.siriuslymail;


public class User {
    private String email;
    private String password;
    private String host;
    private static final User ourInstance = new User();

    public static User getInstance() {
        return ourInstance;
    }

    private User() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public static User getOurInstance() {
        return ourInstance;
    }

    public void update(String email, String password, String host) {
        this.email = email;
        this.password = password;
        this.host = host;
    }

    public void clear() {
        update(null, null, null);
    }
}
