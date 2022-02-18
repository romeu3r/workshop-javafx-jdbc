package gui;

import application.Program;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DepartmentListController implements Initializable {

    private DepartmentService departmentService;

    @FXML
    private TableView<Department> tableViewDepartments;
    @FXML
    private TableColumn<Department, Integer> tableColumnId;
    @FXML
    private TableColumn<Department, String> tableColumnName;
    @FXML
    private Button btNew;
    private ObservableList<Department> observableList;

    @FXML
    public void btNewAction(ActionEvent event) {
        Stage parentStage = Utils.currentStageStage(event);
        Department obj = new Department();
        createDialogForm(obj, "/gui/DepartmentForm.fxml", parentStage);
    }

    public void setDepartmentService(DepartmentService service) {
        this.departmentService = service;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    private void initializeNodes() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("Id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        Stage stage = (Stage) Program.getMainStage().getWindow();
        tableViewDepartments.prefHeightProperty().bind(stage.heightProperty());
    }

    public void updateTableView() {
        if (departmentService == null) throw new IllegalStateException("The service was null.");
        List<Department> list = departmentService.findAll();
        observableList = FXCollections.observableArrayList(list);
        tableViewDepartments.setItems(observableList);
    }

    private void createDialogForm(Department obj, String absoluteName, Stage parentStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = loader.load();

            DepartmentFormController controller = loader.getController();
            controller.setDepartment(obj);
            controller.setDepartmentService(new DepartmentService());
            controller.updateFormData();


            Stage dialogStage = new Stage();
            dialogStage.setTitle("Enter department data");
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(parentStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);/*Modality.WINDOW_MODAL (Responsible for block interaction with other windows. )*/
            dialogStage.showAndWait();
        } catch (IOException e) {
            Alerts.showAlert("IOException", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}
