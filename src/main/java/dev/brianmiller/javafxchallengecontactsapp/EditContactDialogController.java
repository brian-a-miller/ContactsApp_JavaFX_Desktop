package dev.brianmiller.javafxchallengecontactsapp;

import dev.brianmiller.javafxchallengecontactsapp.datamodel.Contact;
import dev.brianmiller.javafxchallengecontactsapp.datamodel.ContactData;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class EditContactDialogController {

    private Contact contact;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private TextField emailAddressField;

    @FXML
    private TextField notesField;

    public void populateData(Contact contact) {

        this.contact = contact;

        firstNameField.setText(contact.getFirstName());
        lastNameField.setText(contact.getLastName());
        phoneNumberField.setText(contact.getPhoneNumber());
        emailAddressField.setText(contact.getEmailAddress());
        notesField.setText(contact.getNotes());
    }

    public Contact processResults() {
        contact.setFirstName(firstNameField.getText().strip());
        contact.setLastName(lastNameField.getText().strip());
        contact.setPhoneNumber(phoneNumberField.getText().strip());
        contact.setEmailAddress(emailAddressField.getText().strip());
        contact.setNotes(notesField.getText().strip());

        ContactData.getInstance().updateContact(contact);

        return contact;
    }
}
