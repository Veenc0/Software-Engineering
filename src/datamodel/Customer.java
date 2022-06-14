package datamodel;

import java.util.ArrayList;
import java.util.List;
/**
 * Class for entity type <i>Customer</i>.
 * <p>
 * Customer is an individual (a person, not a business) who creates and holds (owns) orders in the system.
 * </p>
 * 
 * @version <code style=color:green>{@value application.package_info#Version}</code>
 * @author <code style=color:blue>{@value application.package_info#Author}</code>
 */

public class Customer {

    /**
     * Unique Customer id attribute, {@code id < 0} is invalid, id can only be set once.
     */
    private long id;

    /**
     * Customer's surname attribute, never null.
     */
    private String lastName;

    /**
     * None-surname name parts, never null.
     */
    private String firstName;

    /**
     * Customer contact information with multiple contacts.
     */
    private final List<String> contacts;

    /**
     * Default constructor
     */
    public Customer() {
    	this.id = -1;
    	this.lastName = "";
    	this.firstName = "";
    	this.contacts = new ArrayList<String>();
    }

    /**
     * Constructor with single-String name argument.
     * @param name single-String Customer name, e.g. "Eric Meyer".
     * @throws IllegalArgumentException if name argument is null.
     */
    public Customer(String name) throws IllegalArgumentException {
    	this.id = -1;
    	if (name == "") {
    		throw new IllegalArgumentException("name empty.");
    	}
    	if (name == null) {
    		throw new IllegalArgumentException("name null.");
    	}
    	this.lastName = "";
    	this.firstName = "";
		splitName(name);
    	contacts = new ArrayList<String>();  
    }

    /**
     * Id getter.
     * @return customer id, returns {@code null}, if id is unassigned.
     */
    public Long getId() {
    	if (id == -1 ) {
    		return null;
    	}
        return id;
    }

    /**
     * Id setter. Id can only be set once with valid id, id is immutable after assignment.
     * @param id value to assign if this.id attribute is still unassigned {@code id < 0} and id argument is valid.
     * @throws IllegalArgumentException if id argument is invalid ({@code id < 0}).
     * @return chainable self-reference.
     */
    public Customer setId(long id)  throws IllegalArgumentException{
    	if(id < 0) {
    		throw new IllegalArgumentException("invalid id (negative).");
    	}
    	if(this.id > 0) {
    		return this;
    	}
    	this.id = id;
        return this;
    }

    /**
     * LastName getter.
     * @return value of lastName attribute, never null, mapped to "".
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * FirstName getter.
     * @return value of firstName attribute, never null, mapped to "".
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Setter that splits a single-String name (for example, "Eric Meyer") into first-
     * ("Eric") and lastName ("Meyer") parts and assigns parts to corresponding attributes.
     * @param first value assigned to firstName attribute, null is ignored.
     * @param last value assigned to lastName attribute, null is ignored.
     * @return chainable self-reference.
     */
    public Customer setName(String first, String last) {
    	this.firstName = first;
    	this.lastName = last;
        return this;
    }

    /**
     * Setter that splits a single-String name (e.g. "Eric Meyer") into first- and
     * lastName parts and assigns parts to corresponding attributes. This method references to the Method splitName.
     * @param name single-String name to split into first- and lastName parts.
     * @throws IllegalArgumentException if name argument is null.
     * @return chainable self-reference.
     */
    public Customer setName(String name){
    	this.splitName(name);
        return this;
    }

    /**
     * Return number of contacts.
     * @return number of contacts.
     */
    public int contactsCount() {
    	int sum = contacts.size();
    	return sum;
    }

    /**
     * Contacts getter (as {@code String[]}).
     * @return contacts (as {@code String[]}).
     */
    public String[] getContacts() {
    	if(contacts.size() == 0) {
    		return new String[0];
    	}
    	String[] contacts = new String[this.contacts.size()];
    	for (int i = 0; i < this.contacts.size(); i++) {
    		contacts[i] = this.contacts.get(i);
    	}
    	
        return contacts;
    } 
    
    /**
     * Add new contact for Customer. Only valid contacts(not null, "" or duplicates) are added.
     * @param contact valid contact (not null or "" nor duplicate), invalid contacts are ignored
     * @throws IllegalArgumentException if contact argument is null or empty "" String
     * @return chainable self-reference.
     */
    public Customer addContact(String contact) throws IllegalArgumentException{
    	if (contact == null || contact == "") {
    		throw new IllegalArgumentException("Invalid or empty name");
    	}	
    	if(contact.length() < 6) {
    		throw new IllegalArgumentException("contact less than 6 characters: \"" + contact + "\".");
    		}
    	if(contacts.contains(contact)) {
    		return this;
    	}
    	contact = contact.replaceAll(";", "").replaceAll(",", "").replaceAll( "[\']" , "");
    	if(contact.replaceAll("\"", "").trim().length() < 6) {    		
    		throw new IllegalArgumentException("contact less than 6 characters: \"" + contact + "\".");
    		}
    	contact = contact.replaceAll("\"", "").trim();
        contacts.add(contact);
        return this;
    }
    
    /**
     * Deletes the i-th contact with {@code i >= 0} and {@code i < contactsCount()}, otherwise method has no effect.
     * @param i index of contact to delete.
     */
    public void deleteContact(int i) {
    	if(i >= 0 && i < contactsCount()) {
        contacts.remove(i);
    	}
    }

    /**
     * Delete all contacts.
     */
    public void deleteAllContacts() {
        contacts.clear();
    }

    /**
     * Split single-String name into last- and first name parts.
     * @param name single-String name to split into first- and last name parts.
     * @return chainable self-reference.
     */
    private Customer splitName(String name) {
    	String[] customerName;
    	if(name != null) {
    		if(name.contains(",") == false) {
    			this.lastName = name;
    		}
			for (int i=0; i< name.length(); i++) {
    		if(name.contains(",")) {
    			customerName = name.trim().split("\\s*,\\s*");
    	    	this.firstName = customerName[1];
    	    	this.lastName = customerName[0];
    			continue;
    		}
    			if(name.contains(";")) {
    			customerName = name.trim().split("\\s*;\\s*");
    	    	this.firstName = customerName[1];
    	    	this.lastName = customerName[0];
    	    	continue;
    		}
    			if(name.contains(" ")) {
    			if (name.split(" ").length > 2) {
    				for (int x=0; x< name.length(); x++) {
    					customerName = name.split(" ");
        				String a = name.substring(0, name.lastIndexOf(' '));
            			String b = name.substring(name.lastIndexOf(" ") + 1);
            			this.firstName = a;
            			this.lastName = b;
            			customerName[0] = a;
    					customerName[1] = b;
    					}
    				continue;
    			}else{
    			customerName = name.split(" ");
    	    	this.firstName = customerName[0];
    	    	this.lastName = customerName[1];
    				}	
    			}
			}
    	}
        return this;
    }  
}