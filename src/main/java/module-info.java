module dev.brianmiller.javafxchallengecontactsapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;


    opens dev.brianmiller.javafxchallengecontactsapp to javafx.fxml;
    exports dev.brianmiller.javafxchallengecontactsapp;

    opens dev.brianmiller.javafxchallengecontactsapp.datamodel to javafx.fxml;
    exports dev.brianmiller.javafxchallengecontactsapp.datamodel;

}