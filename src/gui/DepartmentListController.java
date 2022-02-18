package gui;

import application.Program;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;

import java.net.URL;
import java.util.ResourceBundle;

public class DepartmentListController implements Initializable {

    @FXML
    private TableView<Department> tableViewDepartments;
    @FXML
    private TableColumn<Department, Integer> tableColumnId;
    @FXML
    private TableColumn<Department, String> tableColumnName;
    @FXML
    private Button btNew;

    @FXML
    public void btNewAction() {
        System.out.println("OK");
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    private void initializeNodes() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("Id"));
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("Name"));
        Stage stage = (Stage) Program.getMainStage().getWindow();
        tableViewDepartments.prefHeightProperty().bind(stage.heightProperty());
    }
}
