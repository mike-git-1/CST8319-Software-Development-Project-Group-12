/*

Ignore the note that was here in the previous iteration; I had a brainfart and forgot what a builder pattern was LOL
Should all be working with the builder being used to the fullest! :)

 */

package model.builders;
import java.sql.Date;

import model.beans.User;

public class UserBuilder {
	
    int id;
    String first_name;
    String last_name;
    String email;
    String password;
    Date date_created;
    Date date_modified;
    String verification_token;
    boolean verified;
    
    public UserBuilder() {
    	
    }

    public User build() {
        return new User(id, first_name, last_name, email, password, date_created, date_modified, verification_token, verified);
    }
    
    public UserBuilder setID(int id) {
    	this.id = id;
    	return this;
    }
    
    public UserBuilder setFirstName(String first_name) {
    	this.first_name = first_name;
    	return this;
    }
    
    public UserBuilder setLastName(String last_name) {
    	this.last_name = last_name;
    	return this;
    }
    
    public UserBuilder setEmail(String email) {
    	this.email = email;
    	return this;
    }
    
    public UserBuilder setPassword(String password) {
    	this.password = password;
    	return this;
    }
    
    public UserBuilder setDateCreated(Date created) {
    	this.date_created = created;
    	return this;
    }
    
    public UserBuilder setDateModified(Date modified) {
    	this.date_modified = modified;
    	return this;
    }
    
    public UserBuilder setVerificationToken(String token) {
    	this.verification_token = token;
    	return this;
    }
    
    public UserBuilder setVerified(boolean verified) {
    	this.verified = verified;
    	return this;
    }
	
    public UserBuilder setSalt(String salt) {
    this.salt = salt;
    return this;
}
}