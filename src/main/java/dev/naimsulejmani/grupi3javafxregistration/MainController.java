package dev.naimsulejmani.grupi3javafxregistration;

import dev.naimsulejmani.grupi3javafxregistration.models.Person;
import dev.naimsulejmani.grupi3javafxregistration.repositories.PersonRepository;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public class MainController {

    private Person person = null;
    private PersonRepository repository;
    private List<Person> persons = null;
    private ObservableList<Person> observablePersons = null;

    public void initialize() {
        // qe na e thirr JAVAFX pasi ta krijon objektin MainController  => new MainController()
        repository = new PersonRepository();
        persons = repository.findAll();
        observablePersons = FXCollections.observableArrayList();
        observablePersons.addAll(persons);
        lvPersons.setItems(observablePersons);
        initializeTable();
        refreshForm();
    }

    private void initializeTable() {
        TableColumn<Person, Integer> idColumn = new TableColumn<>("Id");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Person, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Person, String> surnameColumn = new TableColumn<>("Surname");
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));

        TableColumn<Person, Boolean> passiveColumn = new TableColumn<>("IsPasive");
        passiveColumn.setCellValueFactory(new PropertyValueFactory<>("passive"));


        TableColumn<Person, LocalDate> birthdateColumn = new TableColumn<>("Birthdate");
        birthdateColumn.setCellValueFactory(new PropertyValueFactory<>("birthdate"));

        TableColumn<Person, Button> deleteColumn = new TableColumn<>("Delete");
        deleteColumn.setPrefWidth(80); // madhesia minimale per buton eshte opsionale e mund ta hjekim edhe ta ngjyrosim me dite qe eshte deleted
        deleteColumn.setCellValueFactory(personButtonCellDataFeatures -> {
            Person person = personButtonCellDataFeatures.getValue();

            Button deleteButton = new Button("Delete");
            // Set the button style
            deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");


            //Shtimi i onAction eventit per butonin per rreshtin ne fjale! me lambda expression
            deleteButton.setOnAction(event -> {
                // Logica me fshi nihere ne databaze, nese eshte e suksesshme e fshijme edhe nga observable, perndryshe e shfaqim alert
                Alert prompt = new Alert(Alert.AlertType.CONFIRMATION);
                prompt.setTitle("Confirmation");
                prompt.setHeaderText("Are you sure you want to delete this person?");
                prompt.setContentText(person.toString());
                Optional<ButtonType> result = prompt.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    boolean deleted = repository.removeById(person.getId());
                    if (deleted)
                        observablePersons.remove(person);
                    else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Warning");
                        alert.setHeaderText(null);
                        alert.setContentText("Failed to delete person");
                        alert.showAndWait();
                    }

                }

                // Optional: Update the table UI after deletion (consider calling tvPersons.refresh())
            });

            return new SimpleObjectProperty<>(deleteButton);
        });

        tvPersons.getColumns().addAll(nameColumn, surnameColumn, passiveColumn, birthdateColumn, deleteColumn);

        tvPersons.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Person selectedStudent = tvPersons.getSelectionModel().getSelectedItem();
                if (selectedStudent != null) {
                    System.out.println("Selected student: " + selectedStudent.toString());
                    person = repository.findById(selectedStudent.getId());
                    if (person != null) {
                        setStudent(person);
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Warning");
                        alert.setHeaderText(null);
                        alert.setContentText("Failed to find person");
                        alert.showAndWait();
                    }
                }
            }
        });
        tvPersons.setItems(observablePersons);
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
    @FXML
    private TableView<Person> tvPersons;


    public void resetForm(ActionEvent actionEvent) {
        tfName.clear();
        tfSurname.clear();
        dpBirthdate.setValue(null);
        chbIsPassive.setSelected(false);
    }

    public void registerForm(ActionEvent actionEvent) {
        Person person = getPerson();
        if (person != null) {
            boolean success = false;
            boolean created = person.getId() == 0;
            if (created)
                success = repository.add(person);
            else
                success = repository.modify(person.getId(), person);

            if (success) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText(created ? "Person registered successfully" : "Person updated successfully");
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
        observablePersons.clear();
        observablePersons.addAll(persons);
    }

    private Person getPerson() {
        if (tfName.getText().isBlank()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a name for the person");
            alert.showAndWait();
            return null;
        }

        if (person == null) person = new Person();

        person.setName(tfName.getText());
        person.setSurname(tfSurname.getText());
        person.setBirthdate(dpBirthdate.getValue());
        person.setPassive(chbIsPassive.isSelected());

        return person;
    }

    private void setStudent(Person person) {
        if (this.person == null || this.person != person) {
            this.person = person;
        }
        tfName.setText(person.getName());
        tfSurname.setText(person.getSurname());
        dpBirthdate.setValue(person.getBirthdate());
        chbIsPassive.setSelected(person.isPassive());
    }
}
