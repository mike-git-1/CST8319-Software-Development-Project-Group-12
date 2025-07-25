/*

This is a holdover from a previous project that I took the skeleton of to help develop the bones of Invetra's
database stuff, specifically the user DAO.

I don't know why this exists alongside the plain "User" bean, but I figured I'd configure both to work together for now and will
come back to figure out why it's set up this way in a day or two once I have more time.

If it can be removed I will 100% do that before our submission period - otherwise I'll add more comments throughout to explain
why both beans are necessary.

Cheers!
Rowan

 */


package database.beans;

import java.sql.Date;

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
    
    public void UserBuilder() {
    	
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
	
}