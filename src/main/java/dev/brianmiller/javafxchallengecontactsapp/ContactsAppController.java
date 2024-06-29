package dev.brianmiller.javafxchallengecontactsapp;

import dev.brianmiller.javafxchallengecontactsapp.datamodel.Contact;
import dev.brianmiller.javafxchallengecontactsapp.datamodel.ContactData;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

import java.io.IOException;
import java.util.Optional;

/**
 * @author Brian A. Miller
 * @date Friday, June 28, 2024
 */
public class ContactsAppController {

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private TableView<Contact> tableView;

    @FXML
    private ContextMenu tableContextMenu;

    public void initialize() {

        tableContextMenu = new ContextMenu();
        MenuItem editMenuItem = new MenuItem("Edit");
        MenuItem deleteMenuItem = new MenuItem("Delete");
        editMenuItem.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Contact contact = tableView.getSelectionModel().getSelectedItem();
                String firstName = contact.getFirstName();
                String lastName = contact.getLastName();
                System.out.printf("User asked to EDIT contact: %s %s %n",
                        firstName, lastName);
                editContact(contact);
            }
        });
        deleteMenuItem.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Contact contact = tableView.getSelectionModel().getSelectedItem();
                String firstName = contact.getFirstName();
                String lastName = contact.getLastName();
                System.out.printf("User asked to DELETE contact: %s %s %n",
                        firstName, lastName);
                deleteContact(contact);
            }
        });

        tableContextMenu.getItems().addAll(editMenuItem, deleteMenuItem);

        TableColumn<Contact, String> firstNameColumn = new TableColumn<>("First Name");
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<Contact, String> lastNameColumn = new TableColumn<>("Last Name");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<Contact, String> phoneNumberColumn = new TableColumn<>("Phone Number");
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        TableColumn<Contact, String> emailAddressColumn = new TableColumn<>("E-Mail Address");
        emailAddressColumn.setCellValueFactory(new PropertyValueFactory<>("emailAddress"));

        TableColumn<Contact, String> notesColumn = new TableColumn<>("Notes");
        notesColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));

        tableView.setItems(ContactData.getInstance().getContacts());
        tableView.getColumns().addAll(firstNameColumn, lastNameColumn, phoneNumberColumn, emailAddressColumn, notesColumn);
        

        tableView.setRowFactory(new Callback<>() {
            @Override
            public TableRow<Contact> call(TableView<Contact> contactTableView) {
                TableRow<Contact> row = new TableRow<Contact>() {
                    @Override
                    protected void updateItem(Contact contact, boolean b) {
                        super.updateItem(contact, b);
                        // TODO
                    }
                };
                row.emptyProperty().addListener(
                        (obs, wasEmpty, isNowEmpty) -> {
                            if (isNowEmpty) {
                                row.setContextMenu(null);
                            } else {
                                row.setContextMenu(tableContextMenu);
                            }
                        }
                );
                return row;
            }
        });
    }

    @FXML
    public void handleAddNewContact() {
        System.out.println("User selected Add New Contact menu item");

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Add New Contact");
        dialog.setHeaderText("Use this dialog to add a new contact.");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("addNewContact.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException ioe) {
            System.err.println("Couldn't load the dialog for adding a new contact");
            ioe.printStackTrace(System.err);
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            NewContactDialogController controller = fxmlLoader.getController();
            Contact newContact = controller.processResults();
            tableView.getSelectionModel().select(newContact);
        }
    }

    private void deleteContact(Contact contactToDelete) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Contact");
        alert.setHeaderText(String.format("Delete contact: %s %s",
                contactToDelete.getFirstName(), contactToDelete.getLastName()));
        alert.setContentText("Are you sure?  Press OK to confirm, or cancel to Back out.");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && (result.get() == ButtonType.OK)) {
            ContactData.getInstance().deleteContact(contactToDelete);
        }
    }

    private void editContact(Contact contactToEdit) {
        System.out.println("User selected Add New Contact menu item");

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Edit Contact");
        dialog.setHeaderText("Use this dialog to edit a contact.");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("editContact.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException ioe) {
            System.err.println("Couldn't load the dialog for editing a contact");
            ioe.printStackTrace(System.err);
            return;
        }
        EditContactDialogController controller = fxmlLoader.getController();
        controller.populateData(contactToEdit);

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Contact editedContact = controller.processResults();
            tableView.getSelectionModel().select(editedContact);
        }

    }

    @FXML
    public void handleExit() {
        System.out.println("Goodbye!");
        Platform.exit();
    }
}