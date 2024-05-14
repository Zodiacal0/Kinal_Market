/*
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
import org.javierherrera.system.bin.Cliente;
import org.javierherrera.system.dao.Conexion;
import org.javierherrera.system.main.Main;

/**
 *
 * @author Javier
 */
public class ClienteController implements Initializable {

    private ObservableList<Cliente> listaClientes;
    private Main escenarioPrincipal;

    private enum operaciones {
        AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NULL
    }

    private operaciones tipoDeOperaciones = operaciones.NULL;

    @FXML
    private TableView<Cliente> tv_Cliente;

    @FXML
    private TableColumn col_codigoCliente;
    @FXML
    private TableColumn col_nombreCliente;
    @FXML
    private TableColumn col_apellidoCliente;
    @FXML
    private TableColumn col_direccionCliente;
    @FXML
    private TableColumn col_nitCliente;
    @FXML
    private TableColumn col_telefonoCliente;
    @FXML
    private TableColumn col_correoCliente;

    @FXML
    private Button btn_agregarC;
    @FXML
    private Button btn_editarC;
    @FXML
    private Button btn_EliminarC;
    @FXML
    private Button btn_reportesC;

    @FXML
    private TextField txt_idC;
    @FXML
    private TextField txt_nombreC;
    @FXML
    private TextField txt_apellidoC;
    @FXML
    private TextField txt_direccionC;
    @FXML
    private TextField txt_nitC;
    @FXML
    private TextField txt_telefonoC;
    @FXML
    private TextField txt_correoC;
    @FXML
    private Button btn_Volver;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
    }

    public void cargarDatos() {
        tv_Cliente.setItems(getCliente());
        col_codigoCliente.setCellValueFactory(new PropertyValueFactory<Cliente, Integer>("codigoCliente"));
        col_nitCliente.setCellValueFactory(new PropertyValueFactory<Cliente, String>("nitCliente"));
        col_nombreCliente.setCellValueFactory(new PropertyValueFactory<Cliente, String>("nombreCliente"));
        col_apellidoCliente.setCellValueFactory(new PropertyValueFactory<Cliente, String>("apellidoCliente"));
        col_direccionCliente.setCellValueFactory(new PropertyValueFactory<Cliente, String>("direccionCliente"));
        col_telefonoCliente.setCellValueFactory(new PropertyValueFactory<Cliente, String>("telefonoCliente"));
        col_correoCliente.setCellValueFactory(new PropertyValueFactory<Cliente, String>("correoCliente"));

    }

    public ObservableList<Cliente> getCliente() {
        ArrayList<Cliente> lista = new ArrayList<>();
        try {
            Connection conexion = Conexion.getInstance().getConexion();
            if (conexion != null) {
                try (PreparedStatement procedimiento = conexion.prepareCall("{CALL sp_listarCliente()}");
                        ResultSet resultado = procedimiento.executeQuery()) {
                    while (resultado.next()) {
                        lista.add(new Cliente(
                                resultado.getInt("codigoCliente"),
                                resultado.getString("nitCliente"),
                                resultado.getString("nombreCliente"),
                                resultado.getString("apellidoCliente"),
                                resultado.getString("direccionCliente"),
                                resultado.getString("telefonoCliente"),
                                resultado.getString("correoCliente")));
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
        Cliente reg = new Cliente();
        reg.setCodigoCliente(Integer.parseInt(txt_idC.getText()));
        reg.setNombreCliente(txt_nombreC.getText());
        reg.setApellidoCliente(txt_apellidoC.getText());
        reg.setDireccionCliente(txt_direccionC.getText());
        reg.setNitCliente(txt_nitC.getText());
        reg.setTelefonoCliente(txt_telefonoC.getText());
        reg.setCorreoCliente(txt_correoC.getText());
        PreparedStatement procedimiento = Conexion.getInstance().getConexion()
                .prepareCall("{CALL sp_crearCliente(?, ?, ?, ?, ?, ?, ?)}");
        procedimiento.setInt(1, reg.getCodigoCliente());
        procedimiento.setString(2, reg.getNombreCliente());
        procedimiento.setString(3, reg.getApellidoCliente());
        procedimiento.setString(4, reg.getDireccionCliente());
        procedimiento.setString(5, reg.getNitCliente());
        procedimiento.setString(6, reg.getTelefonoCliente());
        procedimiento.setString(7, reg.getCorreoCliente());
        procedimiento.execute();
        listaClientes.add(reg);
    }

    public void editar() throws Exception {
        switch (tipoDeOperaciones) {
            case NULL:
                if (tv_Cliente.getSelectionModel().getSelectedItem() != null) {
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
        Cliente reg = new Cliente();
        reg.setCodigoCliente(Integer.parseInt(txt_idC.getText()));
        reg.setNombreCliente(txt_nombreC.getText());
        reg.setApellidoCliente(txt_apellidoC.getText());
        reg.setDireccionCliente(txt_direccionC.getText());
        reg.setNitCliente(txt_nitC.getText());
        reg.setTelefonoCliente(txt_telefonoC.getText());
        reg.setCorreoCliente(txt_correoC.getText());
        PreparedStatement procedimiento = Conexion.getInstance().getConexion()
                .prepareCall("{CALL sp_actualizarCliente(?, ?, ?, ?, ?, ?, ?)}");
        procedimiento.setInt(1, reg.getCodigoCliente());
        procedimiento.setString(2, reg.getNombreCliente());
        procedimiento.setString(3, reg.getApellidoCliente());
        procedimiento.setString(4, reg.getDireccionCliente());
        procedimiento.setString(5, reg.getNitCliente());
        procedimiento.setString(6, reg.getTelefonoCliente());
        procedimiento.setString(7, reg.getCorreoCliente());
        procedimiento.execute();

    }

    public void seleccionar() {
        txt_idC.setText(
                String.valueOf(((Cliente) tv_Cliente.getSelectionModel().getSelectedItem()).getCodigoCliente()));
        txt_nombreC.setText(((Cliente) tv_Cliente.getSelectionModel().getSelectedItem()).getNombreCliente());
        txt_apellidoC.setText(((Cliente) tv_Cliente.getSelectionModel().getSelectedItem()).getApellidoCliente());
        txt_direccionC.setText(((Cliente) tv_Cliente.getSelectionModel().getSelectedItem()).getDireccionCliente());
        txt_nitC.setText(((Cliente) tv_Cliente.getSelectionModel().getSelectedItem()).getNitCliente());
        txt_telefonoC.setText(((Cliente) tv_Cliente.getSelectionModel().getSelectedItem()).getTelefonoCliente());
        txt_correoC.setText(((Cliente) tv_Cliente.getSelectionModel().getSelectedItem()).getCorreoCliente());
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
                if (tv_Cliente.getSelectionModel().getSelectedItem() != null) {
                    int ans = JOptionPane.showConfirmDialog(null, "Confirma esta Accion", "Verificacion",
                            JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (ans == JOptionPane.YES_NO_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion()
                                    .prepareCall("{CALL sp_eliminarCliente(?)}");
                            procedimiento.setInt(1,
                                    ((Cliente) tv_Cliente.getSelectionModel().getSelectedItem()).getCodigoCliente());
                            procedimiento.execute();
                            listaClientes.remove(tv_Cliente.getSelectionModel().getSelectedItem());
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
        txt_apellidoC.setEditable(false);
        txt_direccionC.setEditable(false);
        txt_nitC.setEditable(false);
        txt_telefonoC.setEditable(false);
        txt_correoC.setEditable(false);
    }

    public void activarControles() {
        txt_idC.setEditable(true);
        txt_nombreC.setEditable(true);
        txt_apellidoC.setEditable(true);
        txt_direccionC.setEditable(true);
        txt_nitC.setEditable(true);
        txt_telefonoC.setEditable(true);
        txt_correoC.setEditable(true);
    }

    public void limpiarControles() {
        txt_idC.clear();
        txt_nombreC.clear();
        txt_apellidoC.clear();
        txt_direccionC.clear();
        txt_nitC.clear();
        txt_telefonoC.clear();
        txt_correoC.clear();
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
