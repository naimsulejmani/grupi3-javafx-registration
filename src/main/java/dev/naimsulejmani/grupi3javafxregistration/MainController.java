package dev.naimsulejmani.grupi3javafxregistration;

import dev.naimsulejmani.grupi3javafxregistration.models.Person;
import dev.naimsulejmani.grupi3javafxregistration.repositories.PersonRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;


public class MainController {

    private PersonRepository repository;
    private List<Person> persons = null;
    private ObservableList<Person> observablePersons = null;

    public void initialize() {
        // qe na e thirr JAVAFX pasi ta krijon objektin MainController  => new MainController()
        repository = new PersonRepository();
      refreshForm();
    }

    @FXML
    private TextField tfName;
    @FXML
    private TextField tfSurname;
    @FXML
    private DatePicker dpBirthdate;
    @FXML
    private CheckBox chbIsPassive;

    @FXML
    private ListView<Person> lvPersons;


    public void resetForm(ActionEvent actionEvent) {
        tfName.clear();
        tfSurname.clear();
        dpBirthdate.setValue(null);
        chbIsPassive.setSelected(false);
    }

    public void registerForm(ActionEvent actionEvent) {
        Person person = getPerson();
        if(person != null) {
            boolean registered = repository.add(person);
            if(registered) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Person registered successfully");
                alert.showAndWait();
                resetForm(null);
                refreshForm();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Something wrong happened!");
                alert.showAndWait();
            }
        }
    }

    private void refreshForm() {
        persons = repository.findAll();
        observablePersons = FXCollections.observableArrayList();
        observablePersons.addAll(persons);
        lvPersons.setItems(observablePersons);
    }

    private Person getPerson() {
        if(tfName.getText().isBlank()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a name for the person");
            alert.showAndWait();
            return null;
        }

        Person person = new Person();
        person.setName(tfName.getText());
        person.setSurname(tfSurname.getText());
        person.setBirthdate(dpBirthdate.getValue());
        person.setPassive(chbIsPassive.isSelected());

        return person;
    }
}
