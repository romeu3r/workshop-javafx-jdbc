package gui;

import db.DbException;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.services.DepartmentService;

import java.net.URL;
import java.util.ResourceBundle;

public class DepartmentFormController implements Initializable {

    private Department entity;
    private DepartmentService departmentService;

    @FXML
    private TextField txtId;
    @FXML
    private TextField txtName;
    @FXML
    private Label labelErrorName;

    @FXML
    private Button btSave;
    @FXML
    private Button btCancel;

    public void setDepartment(Department entity) {
        this.entity = entity;
    }

    public void setDepartmentService(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @FXML
    public void onBtSaveAction(ActionEvent event) {
        if (departmentService == null) throw new IllegalStateException("DepartmentService are not was injected.");
        if (entity == null) throw new IllegalStateException("Entity are not was injected.");
        try {
            entity = getFormData();
            departmentService.saveOrUpdate(entity);
            Utils.currentStageStage(event).close();
        } catch (DbException e) {
            Alerts.showAlert("Error saving Object", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private Department getFormData() {
        Department department = new Department();
        department.setId(Utils.tryParseToInteger(txtId.getText()));
        department.setName(txtName.getText());
        return department;
    }

    @FXML
    public void onBtCancelAction(ActionEvent event) {
        Utils.currentStageStage(event).close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private void initializeNodes() {
        Constraints.setTextFieldInteger(txtId);
        Constraints.setTextFieldMaxLength(txtName, 30);
    }

    public void updateFormData() {
        if (entity == null) throw new IllegalStateException("Entity was null.");
        txtId.setText(String.valueOf(entity.getId()));
        txtName.setText(String.valueOf(entity.getName()));
    }
}
