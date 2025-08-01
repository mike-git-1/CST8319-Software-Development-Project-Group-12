package model.beans;
import java.sql.Date;

public class User {
    
    private int id;
    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private Date date_created;
    private Date date_modified;
    private String verification_token;
    private boolean verified;

    public User() {}

    // Builder for User bean
    public User(int id, String first_name, String last_name, String email, String password, Date date_created, Date date_modified, String verification_token, boolean verified) {
    	this.id = id;
    	this.first_name = first_name;
    	this.last_name = last_name;
    	this.email = email;
    	this.password = password;
    	this.date_created = date_created;
    	this.date_modified = date_modified;
    	this.verification_token = verification_token;
    	this.verified = verified;
    }

    // Getters and setters for each field
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }
    public void setLast_name(String last_name) {
        this.last_name = last_name;
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

    public Date getDate_created() {
        return date_created;
    }
    public void setDate_created(Date date_created) {
        this.date_created = date_created;
    }

    public Date getDate_modified() {
        return date_modified;
    }
    public void setDate_modified(Date date_modified) {
        this.date_modified = date_modified;
    }

    public String getVerification_token() {
        return verification_token;
    }
    public void setVerification_token(String verification_token) {
        this.verification_token = verification_token;
    }

    public boolean isVerified() {
        return verified;
    }
    public void setVerified(boolean verified) {
        this.verified = verified;
    }
    private String salt;

public String getSalt() { return salt; }
public void setSalt(String salt) { this.salt = salt; }    
    
}