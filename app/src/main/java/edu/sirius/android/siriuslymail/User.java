package edu.sirius.android.siriuslymail;


public class User {
    private String email;
    private String password;
    private String imapHost;
    private String smtpHost;
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

    public String getSmtpHost() {
        return smtpHost;
    }

    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    public String getImapHost() {
        return imapHost;
    }

    public void setImapHost(String imapHost) {
        this.imapHost = imapHost;
    }

    public Long getActive() {
        return active;
    }

    public void setActive(Long active) {
        this.active = active;
    }

    public User(String email, String password, String imapHost, String smtpHost, long active) {
        this.email = email;
        this.password = password;
        this.imapHost = imapHost;
        this.smtpHost = smtpHost;
        this.active = active;
    }

    public void clear() {
        update(null, null, null);
    }
}
