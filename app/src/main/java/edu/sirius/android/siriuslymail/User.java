package edu.sirius.android.siriuslymail;


public class User {
    private String email;
    private String password;
    private String host;
    private Long active;


    User() {
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

    public Long getActive() {
        return active;
    }

    public void setActive(Long active) {
        this.active = active;
    }

    public User(String email, String password, String host, Long active) {
        this.email = email;
        this.password = password;
        this.host = host;
        this.active = active;
    }

}
