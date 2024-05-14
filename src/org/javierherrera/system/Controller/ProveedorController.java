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
import org.javierherrera.system.bin.Proveedor;
import org.javierherrera.system.dao.Conexion;
import org.javierherrera.system.main.Main;

/**
 *
 * @author Javier
 */
public class ProveedorController implements Initializable {

    private ObservableList<Proveedor> listaProveedor;
    private Main escenarioPrincipal;

    private enum operaciones {
        AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NULL
    }

    private operaciones tipoDeOperaciones = operaciones.NULL;
            
    @FXML
    private Button btn_EliminarC;

    @FXML
    private Button btn_agregarC;

    @FXML
    private Button btn_editarC;

    @FXML
    private Button btn_reportesC;

    @FXML
    private TableColumn col_apellidosProveedor;

    @FXML
    private TableColumn col_codigoProveedor;

    @FXML
    private TableColumn col_contactoPrincipal;

    @FXML
    private TableColumn col_direccionProveedor;

    @FXML
    private TableColumn col_nitProveedor;

    @FXML
    private TableColumn col_nombreProveedor;

    @FXML
    private TableColumn col_paginaWeb;

    @FXML
    private TableColumn col_razonSocial;

    @FXML
    private TableView tv_Cliente;

    @FXML
    private TextField txt_apellidoP;

    @FXML
    private TextField txt_codP;

    @FXML
    private TextField txt_contactoP;

    @FXML
    private TextField txt_direccionP;

    @FXML
    private TextField txt_nitP;

    @FXML
    private TextField txt_nombreP;

    @FXML
    private TextField txt_paginaW;

    @FXML
    private TextField txt_razonS;
    
    @FXML
    private Button btn_Volver;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
    }

    public void cargarDatos() {
        tv_Cliente.setItems(getProveedor());
        col_codigoProveedor.setCellValueFactory(new PropertyValueFactory<Proveedor, Integer>("codigoProveedor"));
        col_nitProveedor.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("nitProveedor"));
        col_nombreProveedor.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("nombreProveedor"));
        col_apellidosProveedor.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("apellidosProveedor"));
        col_direccionProveedor.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("direccionProveedor"));
        col_razonSocial.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("razonSocial"));
        col_contactoPrincipal.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("contactoPrincipal"));
        col_paginaWeb.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("paginaWeb"));

    }

    public ObservableList<Proveedor> getProveedor() {
        ArrayList<Proveedor> lista = new ArrayList<>();
        try {
            Connection conexion = Conexion.getInstance().getConexion();
            if (conexion != null) {
                try (PreparedStatement procedimiento = conexion.prepareCall("{CALL sp_listarProveedores()}");
                        ResultSet resultado = procedimiento.executeQuery()) {
                    while (resultado.next()) {
                        lista.add(new Proveedor(
                                resultado.getInt("codigoProveedor"),
                                resultado.getString("nitProveedor"),
                                resultado.getString("nombreProveedor"),
                                resultado.getString("apellidosProveedor"),
                                resultado.getString("direccionProveedor"),
                                resultado.getString("razonSocial"),
                                resultado.getString("contactoPrincipal"),
                                resultado.getString("paginaWeb")));

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
        return listaProveedor = FXCollections.observableList(lista);
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
        Proveedor reg = new Proveedor();
        reg.setCodigoProveedor(Integer.parseInt(txt_codP.getText()));
        reg.setNitProveedor(txt_nitP.getText());
        reg.setNombreProveedor(txt_nombreP.getText());
        reg.setApellidosProveedor(txt_apellidoP.getText());
        reg.setDireccionProveedor(txt_direccionP.getText());
        reg.setRazonSocial(txt_razonS.getText());
        reg.setContactoPrincipal(txt_contactoP.getText());
        reg.setPaginaWeb(txt_paginaW.getText());

        PreparedStatement procedimiento = Conexion.getInstance().getConexion()
                .prepareCall("{CALL sp_crearProveedor(?, ?, ?, ?, ?, ?, ?,?)}");
        procedimiento.setInt(1, reg.getCodigoProveedor());
        procedimiento.setString(2, reg.getNitProveedor());
        procedimiento.setString(3, reg.getNombreProveedor());
        procedimiento.setString(4, reg.getApellidosProveedor());
        procedimiento.setString(5, reg.getDireccionProveedor());
        procedimiento.setString(6, reg.getRazonSocial());
        procedimiento.setString(7, reg.getContactoPrincipal());
        procedimiento.setString(8, reg.getPaginaWeb());

        procedimiento.execute();
        listaProveedor.add(reg);
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
                    txt_codP.setEditable(false);
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
        Proveedor reg = new Proveedor();
        reg.setCodigoProveedor(Integer.parseInt(txt_codP.getText()));
        reg.setNitProveedor(txt_nitP.getText());
        reg.setNombreProveedor(txt_nombreP.getText());
        reg.setApellidosProveedor(txt_apellidoP.getText());
        reg.setDireccionProveedor(txt_direccionP.getText());
        reg.setRazonSocial(txt_razonS.getText());
        reg.setContactoPrincipal(txt_contactoP.getText());
        reg.setPaginaWeb(txt_paginaW.getText());
        PreparedStatement procedimiento = Conexion.getInstance().getConexion()
                .prepareCall("{CALL sp_actualizarProveedor(?, ?, ?, ?, ?, ?, ?,?)}");
        procedimiento.setInt(1, reg.getCodigoProveedor());
        procedimiento.setString(2, reg.getNitProveedor());
        procedimiento.setString(3, reg.getNombreProveedor());
        procedimiento.setString(4, reg.getApellidosProveedor());
        procedimiento.setString(5, reg.getDireccionProveedor());
        procedimiento.setString(6, reg.getRazonSocial());
        procedimiento.setString(7, reg.getContactoPrincipal());
        procedimiento.setString(8, reg.getPaginaWeb());
        procedimiento.execute();

    }

    public void seleccionar() {
        txt_codP.setText(
                String.valueOf(((Proveedor) tv_Cliente.getSelectionModel().getSelectedItem()).getCodigoProveedor()));
        txt_nitP.setText(((Proveedor) tv_Cliente.getSelectionModel().getSelectedItem()).getNitProveedor());
        txt_nombreP.setText(((Proveedor) tv_Cliente.getSelectionModel().getSelectedItem()).getNombreProveedor());
        txt_apellidoP.setText(((Proveedor) tv_Cliente.getSelectionModel().getSelectedItem()).getApellidosProveedor());
        txt_direccionP.setText(((Proveedor) tv_Cliente.getSelectionModel().getSelectedItem()).getDireccionProveedor());
        txt_razonS.setText(((Proveedor) tv_Cliente.getSelectionModel().getSelectedItem()).getRazonSocial());
        txt_contactoP.setText(((Proveedor) tv_Cliente.getSelectionModel().getSelectedItem()).getContactoPrincipal());
        txt_paginaW.setText(((Proveedor) tv_Cliente.getSelectionModel().getSelectedItem()).getPaginaWeb());

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
                                    .prepareCall("{CALL sp_eliminarProveedor(?)}");
                            procedimiento.setInt(1,
                                    ((Proveedor) tv_Cliente.getSelectionModel().getSelectedItem()).getCodigoProveedor());
                            procedimiento.execute();
                            listaProveedor.remove(tv_Cliente.getSelectionModel().getSelectedItem());
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
        txt_codP.setEditable(false);
        txt_nitP.setEditable(false);
        txt_nombreP.setEditable(false);
        txt_apellidoP.setEditable(false);
        txt_direccionP.setEditable(false);
        txt_razonS.setEditable(false);
        txt_contactoP.setEditable(false);
        txt_paginaW.setEditable(false);
    }

    public void activarControles() {
        txt_codP.setEditable(true);
        txt_nitP.setEditable(true);
        txt_nombreP.setEditable(true);
        txt_apellidoP.setEditable(true);
        txt_direccionP.setEditable(true);
        txt_razonS.setEditable(true);
        txt_contactoP.setEditable(true);
        txt_paginaW.setEditable(true);
    }

    public void limpiarControles() {
        txt_codP.clear();
        txt_nitP.clear();
        txt_nombreP.clear();
        txt_apellidoP.clear();
        txt_direccionP.clear();
        txt_razonS.clear();
        txt_contactoP.clear();
        txt_paginaW.clear();
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
