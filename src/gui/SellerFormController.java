package gui;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.DepartmentService;
import model.services.SellerService;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class SellerFormController implements Initializable {

    private Seller entity;
    private SellerService sellerService;
    private DepartmentService departmentService;
    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    @FXML
    private TextField txtId;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtEmail;
    @FXML
    private DatePicker dpBirthDate;
    @FXML
    private TextField txtBaseSalary;
    @FXML
    ComboBox<Department> comboBoxDepartment;
    @FXML
    private Label labelErrorName;
    @FXML
    private Label labelErrorEmail;
    @FXML
    private Label labelErrorBirthDate;
    @FXML
    private Label labelErrorBaseSalary;

    @FXML
    private Button btSave;
    @FXML
    private Button btCancel;

    private ObservableList<Department> observableList;

    public void setSeller(Seller entity) {
        this.entity = entity;
    }

    public void setSellerServices(SellerService sellerService, DepartmentService departmentService) {
        this.sellerService = sellerService;
        this.departmentService = departmentService;
    }

    public void subscribeDataChangeListener(DataChangeListener listener) {
        dataChangeListeners.add(listener);
    }

    @FXML
    public void onBtSaveAction(ActionEvent event) {
        if (sellerService == null) throw new IllegalStateException("SellerService are not was injected.");
        if (entity == null) throw new IllegalStateException("Entity are not was injected.");
        try {
            entity = getFormData();
            sellerService.saveOrUpdate(entity);
            notifyDataChangeListeners();
            Utils.currentStage(event).close();
        } catch (ValidationException e) {
            setErrorMessages(e.getErrors());
        } catch (DbException e) {
            Alerts.showAlert("Error saving Object", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void notifyDataChangeListeners() {
        for (DataChangeListener listener : dataChangeListeners) {
            listener.onDataChanged();
        }
    }

    private Seller getFormData() {
        Seller seller = new Seller();
        ValidationException exception = new ValidationException("Validation error.");
        seller.setId(Utils.tryParseToInteger(txtId.getText()));
        if (txtName == null || txtName.getText().trim().equals("")) exception.addError("name", "Field can`n be empty.");
        seller.setName(txtName.getText());
        if (txtEmail == null || txtEmail.getText().trim().equals("")) exception.addError("email", "Field can`n be empty.");
        seller.setEmail(txtEmail.getText());

        if (dpBirthDate.getValue() == null) exception.addError("birthDate", "Field can`n be empty.");
        else {
            Instant instant = Instant.from(dpBirthDate.getValue().atStartOfDay(ZoneId.systemDefault()));
            seller.setBirthDate(Date.from(instant));
        }

        if (txtBaseSalary == null || txtBaseSalary.getText().trim().equals("")) exception.addError("baseSalary", "Field can`n be empty.");
        seller.setBaseSalary(Utils.tryParseToDouble(txtBaseSalary.getText()));

        seller.setDepartment(comboBoxDepartment.getValue());

        if (exception.getErrors().size() > 0) throw exception;
        return seller;
    }

    @FXML
    public void onBtCancelAction(ActionEvent event) {
        Utils.currentStage(event).close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private void initializeNodes() {
        Constraints.setTextFieldInteger(txtId);
        Constraints.setTextFieldMaxLength(txtName, 70);
        Constraints.setTextFieldDouble(txtBaseSalary);
        Constraints.setTextFieldMaxLength(txtEmail, 40);
        Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");

        initializeComboBoxDepartment();
    }

    public void updateFormData() {
        if (entity == null) throw new IllegalStateException("Entity was null.");
        txtId.setText(String.valueOf(entity.getId()));
        txtName.setText(String.valueOf(entity.getName()));
        txtEmail.setText(entity.getEmail());
        Locale.setDefault(Locale.US);
        txtBaseSalary.setText(String.format("%.2f", entity.getBaseSalary()));
        if (entity.getBirthDate() != null)
            dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));
        if (entity.getDepartment() == null) comboBoxDepartment.getSelectionModel().selectFirst();
        else comboBoxDepartment.setValue(entity.getDepartment());
    }

    public void loadAssociatedObjects() {
        if (departmentService == null) throw new IllegalStateException("Department Service was null.");
        List<Department> list = departmentService.findAll();
        observableList = FXCollections.observableArrayList(list);
        comboBoxDepartment.setItems(observableList);
    }

    private void setErrorMessages(Map<String, String> errors) {
        Set<String> fields = errors.keySet();
        labelErrorName.setText((fields.contains("name") ? errors.get("name") : ""));
        labelErrorEmail.setText(fields.contains("email") ? errors.get("email") : "");
        labelErrorBaseSalary.setText((fields.contains("baseSalary")) ? errors.get("baseSalary") : "");
        labelErrorBirthDate.setText(fields.contains("birthDate") ? errors.get("birthDate") : "");
    }

    private void initializeComboBoxDepartment() {
        Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
            @Override
            protected void updateItem(Department item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName());
            }
        };
        comboBoxDepartment.setCellFactory(factory);
        comboBoxDepartment.setButtonCell(factory.call(null));
    }
}
