package dev.brianmiller.javafxchallengecontactsapp;

import dev.brianmiller.javafxchallengecontactsapp.datamodel.Contact;
import dev.brianmiller.javafxchallengecontactsapp.datamodel.ContactData;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class NewContactDialogController {

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

    public Contact processResults() {
        String firstName = firstNameField.getText().strip();
        String lastName = lastNameField.getText().strip();
        String phoneNumber = phoneNumberField.getText().strip();
        String emailAddress = emailAddressField.getText().strip();
        String notes = notesField.getText().strip();

        Contact newContact = new Contact(firstName, lastName, phoneNumber, emailAddress, notes);

        ContactData.getInstance().addContact(newContact);

        return newContact;
    }
}
