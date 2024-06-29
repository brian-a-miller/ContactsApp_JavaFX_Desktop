package dev.brianmiller.javafxchallengecontactsapp.datamodel;

import java.util.Objects;

/**
 * @author Brian A. Miller
 * @date Friday, June 28, 2024
 */
public class Contact implements Comparable<Contact> {

    private static Long nextId = 1L;

    public void setId(Long id) {
        this.id = id;
    }

    private Long id;

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String emailAddress;
    private String notes;

    /**
     * Default constructor
     */
    public Contact() {
        this.id = nextId++;
    }

    public Contact(String firstName, String lastName, String phoneNumber,
                   String emailAddress, String notes) {
        this(null, firstName, lastName, phoneNumber, emailAddress, notes);
    }

    public Contact(Long id, String firstName, String lastName, String phoneNumber,
                   String emailAddress, String notes) {

        this.id = (id == null ? (nextId++) : id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.notes = notes;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getId() {
        return id;
    }

    @Override
    public int compareTo(Contact o) {
        if (o == null) {
            return 1;
        }
        if ((id != null) && (o.id != null)) {
            return Long.compare(id, o.id);
        }

        String myFirstLast = String.format("%s %s", firstName, lastName);
        String theirFirstLast = String.format("%s %s", o.firstName, o.lastName);
        return myFirstLast.compareTo(theirFirstLast);
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}

