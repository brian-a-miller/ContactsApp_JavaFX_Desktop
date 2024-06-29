package dev.brianmiller.javafxchallengecontactsapp;

import dev.brianmiller.javafxchallengecontactsapp.datamodel.ContactData;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author Brian A. Miller
 * @date Friday, June 28, 2024
 */
public class ContactsAppMain extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ContactsAppMain.class.getResource("contacts.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void init() {
        ContactData.getInstance().loadContacts();
    }

    @Override
    public void stop() {
        ContactData.getInstance().saveContacts();
    }

    public static void main(String[] args) {
        launch();
    }
}