/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package org.javierherrera.system.Controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.javierherrera.system.bin.CargoEmpleado;
import org.javierherrera.system.dao.Conexion;
import org.javierherrera.system.main.Main;

/**
 *
 * @author Javier
 */
public class CargoEmpleadoController implements Initializable {

    private ObservableList<CargoEmpleado> listaClientes;
    private Main escenarioPrincipal;

    private enum operaciones {
        AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NULL
    }

    private operaciones tipoDeOperaciones = operaciones.NULL;

    @FXML
    private Button btn_Volver;
    
    @FXML
    private Button btn_EliminarC;

    @FXML
    private Button btn_agregarC;

    @FXML
    private Button btn_editarC;

    @FXML
    private Button btn_reportesC;

    @FXML
    private TableColumn col_codigoCargo;

    @FXML
    private TableColumn col_descripcionC;

    @FXML
    private TableColumn col_nombreC;

    @FXML
    private TableView tv_Cargo;

    @FXML
    private TextField txt_idC;

    @FXML
    private TextField txt_nitC;

    @FXML
    private TextField txt_nombreC;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
    }

    public void cargarDatos() {
        tv_Cargo.setItems(getCargoEmpleado());
        col_codigoCargo.setCellValueFactory(new PropertyValueFactory<CargoEmpleado, Integer>("codigoCargoEmpleado"));
        col_nombreC.setCellValueFactory(new PropertyValueFactory<CargoEmpleado, String>("nombreCargo"));
        col_descripcionC.setCellValueFactory(new PropertyValueFactory<CargoEmpleado, String>("descripcionCargo"));
        
    }

    public ObservableList<CargoEmpleado> getCargoEmpleado() {
        ArrayList<CargoEmpleado> lista = new ArrayList<>();
        try {
            Connection conexion = Conexion.getInstance().getConexion();
            if (conexion != null) {
                try (PreparedStatement procedimiento = conexion.prepareCall("{CALL sp_listarCargoEmpleado()}");
                        ResultSet resultado = procedimiento.executeQuery()) {
                    while (resultado.next()) {
                        lista.add(new CargoEmpleado(
                          resultado.getInt("codigoCargoEmpleado"),
                                resultado.getString("nombreCargo"),
                            resultado.getString("descripcionCargo")));
                    }
                }
            } else {
                System.out.println("No se pudo establecer conexión con la base de datos.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaClientes = FXCollections.observableList(lista);
    }

    public void Agregar() throws SQLException {
        switch (tipoDeOperaciones) {
            case NULL:
                activarControles();
                btn_agregarC.setText("Guardar");
                btn_EliminarC.setText("Cancelar");
                btn_editarC.setDisable(true);
                btn_reportesC.setDisable(true);
                tipoDeOperaciones = operaciones.ACTUALIZAR;
                break;
            case ACTUALIZAR:
                guardar();
                desactivarControles();
                cargarDatos();
                limpiarControles();
                btn_agregarC.setText("Agregar");
                btn_EliminarC.setText("Eliminar");
                btn_editarC.setDisable(false);
                btn_reportesC.setDisable(false);
                tipoDeOperaciones = operaciones.ACTUALIZAR;
                tipoDeOperaciones = operaciones.NULL;
                break;

        }
    }

    public void guardar() throws SQLException {
        CargoEmpleado reg = new CargoEmpleado();
        reg.setCodigoCargoEmpleado(Integer.parseInt(txt_idC.getText()));
        reg.setNombreCargo(txt_nombreC.getText());
        reg.setDescripcionCargo(txt_nitC.getText());
        PreparedStatement procedimiento = Conexion.getInstance().getConexion()
                .prepareCall("{CALL sp_crearCargoEmpleado(?, ?, ?)}");
        procedimiento.setInt(1, reg.getCodigoCargoEmpleado());
        procedimiento.setString(2, reg.getNombreCargo());
        procedimiento.setString(3, reg.getDescripcionCargo());
        procedimiento.execute();
        listaClientes.add(reg);
    }

    public void editar() throws Exception {
        switch (tipoDeOperaciones) {
            case NULL:
                if (tv_Cargo.getSelectionModel().getSelectedItem() != null) {
                    btn_editarC.setText("Actualizar");
                    btn_reportesC.setText("Cancelar");
                    btn_EliminarC.setDisable(true);
                    btn_agregarC.setDisable(true);
                    tipoDeOperaciones = operaciones.NULL;
                    activarControles();
                    txt_idC.setEditable(false);
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Selecciones un Cliente");
                }
                break;

            case ACTUALIZAR:
                actualizar();
                btn_editarC.setText("Editar");
                btn_reportesC.setText("Cancelar");
                btn_EliminarC.setDisable(false);
                btn_agregarC.setDisable(false);
                desactivarControles();
                limpiarControles();
                tipoDeOperaciones = operaciones.NULL;
                cargarDatos();
                break;
        }
    }

    public void reportes() {
        switch (tipoDeOperaciones) {
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btn_editarC.setText("Editar");
                btn_reportesC.setText("Reportes");
                btn_agregarC.setDisable(false);
                btn_EliminarC.setDisable(false);
                tipoDeOperaciones = operaciones.NULL;
        }
    }

    public void actualizar() throws Exception {
        CargoEmpleado reg = new CargoEmpleado();
        reg.setCodigoCargoEmpleado(Integer.parseInt(txt_idC.getText()));
        reg.setNombreCargo(txt_nombreC.getText());
        reg.setDescripcionCargo(txt_nitC.getText());
        PreparedStatement procedimiento = Conexion.getInstance().getConexion()
                .prepareCall("{CALL sp_actualizarCargoEmpleado(?, ?, ?)}");
        procedimiento.setInt(1, reg.getCodigoCargoEmpleado());
        procedimiento.setString(2, reg.getNombreCargo());
        procedimiento.setString(3, reg.getDescripcionCargo());
        procedimiento.execute();

    }

    public void seleccionar() {
        txt_idC.setText(
                String.valueOf(((CargoEmpleado) tv_Cargo.getSelectionModel().getSelectedItem()).getCodigoCargoEmpleado()));
        txt_nombreC.setText(((CargoEmpleado) tv_Cargo.getSelectionModel().getSelectedItem()).getNombreCargo());
        txt_nitC.setText(((CargoEmpleado) tv_Cargo.getSelectionModel().getSelectedItem()).getDescripcionCargo());
    }

    public void eliminar() throws SQLException {
        switch (tipoDeOperaciones) {
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btn_agregarC.setText("Agregar");
                btn_EliminarC.setText("Eliminar");
                btn_editarC.setDisable(false);
                btn_reportesC.setDisable(false);
                tipoDeOperaciones = operaciones.NULL;
                break;
            default:
                if (tv_Cargo.getSelectionModel().getSelectedItem() != null) {
                    int ans = JOptionPane.showConfirmDialog(null, "Confirma esta Accion", "Verificacion",
                            JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (ans == JOptionPane.YES_NO_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion()
                                    .prepareCall("{CALL sp_eliminarCargoEmpleado(?)}");
                            procedimiento.setInt(1,
                                    ((CargoEmpleado) tv_Cargo.getSelectionModel().getSelectedItem()).getCodigoCargoEmpleado());
                            procedimiento.execute();
                            listaClientes.remove(tv_Cargo.getSelectionModel().getSelectedItem());
                            limpiarControles();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar Cliente para eliminar");
                }
        }
    }

    public void desactivarControles() {
        txt_idC.setEditable(false);
        txt_nombreC.setEditable(false);
        txt_nitC.setEditable(false);
    }

    public void activarControles() {
        txt_idC.setEditable(true);
        txt_nombreC.setEditable(true);
        txt_nitC.setEditable(true);
    }

    public void limpiarControles() {
        txt_idC.clear();
        txt_nombreC.clear();
        txt_nitC.clear();
    }

    public void MenuPrincipalView() {
        escenarioPrincipal.MainMenuView();
    }

    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    @FXML
    public void handleButtonAction(ActionEvent event) {
        if(event.getSource() == btn_Volver){
            escenarioPrincipal.MainMenuView();
        }
    }

}
