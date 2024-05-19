package org.javierherrera.system.Controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
 *
 * @author Javier
 */
public class ProveedorController implements Initializable {

    private ObservableList<Proveedor> listaProveedores;
    private Main escenarioPrincipal;

    private enum operador {
        AGREGRAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO
    }
    private operador tipoDeOperador = operador.NINGUNO;
    @FXML
    private Button btn_EliminarC;

    @FXML
    private Button btn_Volver;

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
    private TableColumn col_emailProveedor;

    @FXML
    private TableColumn col_nitProveedor;

    @FXML
    private TableColumn col_nombreProveedor;

    @FXML
    private TableColumn col_paginaWeb;

    @FXML
    private TableColumn col_razonSocial;

    @FXML
    private TableColumn col_telefonoProveedor;

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
    private TextField txt_emailProveedor;

    @FXML
    private TextField txt_nitP;

    @FXML
    private TextField txt_nombreP;

    @FXML
    private TextField txt_paginaW;

    @FXML
    private TextField txt_razonS;

    @FXML
    private TextField txt_telefonoProveedor;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        cargarDatos();
        desactivarControles();
    }

    public void cargarDatos() {
        tv_Cliente.setItems(getProveedores());
        col_codigoProveedor.setCellValueFactory(new PropertyValueFactory<Proveedor, Integer>("codigoProveedor"));
        col_contactoPrincipal.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("contactoPrincipal"));
        col_direccionProveedor.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("direccionProveedor"));
        col_emailProveedor.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("emailProveedor"));
        col_nitProveedor.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("NITProveedor"));
        col_nombreProveedor.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("nombresProveedor"));
        col_paginaWeb.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("paginaWeb"));
        col_razonSocial.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("razonSocial"));
        col_telefonoProveedor.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("telefonoProveedor"));
        col_apellidosProveedor.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("apellidosProveedor"));
    }

    public void seleccionar() {
        try {
            txt_codP.setText(String.valueOf(((Proveedor) tv_Cliente.getSelectionModel().getSelectedItem()).getCodigoProveedor()));
            txt_nitP.setText((((Proveedor) tv_Cliente.getSelectionModel().getSelectedItem()).getNITProveedor()));
            txt_nombreP.setText((((Proveedor) tv_Cliente.getSelectionModel().getSelectedItem()).getNombresProveedor()));
            txt_apellidoP.setText((((Proveedor) tv_Cliente.getSelectionModel().getSelectedItem()).getApellidosProveedor()));
            txt_direccionP.setText((((Proveedor) tv_Cliente.getSelectionModel().getSelectedItem()).getDireccionProveedor()));
            txt_razonS.setText((((Proveedor) tv_Cliente.getSelectionModel().getSelectedItem()).getRazonSocial()));
            txt_contactoP.setText((((Proveedor) tv_Cliente.getSelectionModel().getSelectedItem()).getContactoPrincipal()));
            txt_paginaW.setText((((Proveedor) tv_Cliente.getSelectionModel().getSelectedItem()).getPaginaWeb()));
            txt_telefonoProveedor.setText((((Proveedor) tv_Cliente.getSelectionModel().getSelectedItem()).getTelefonoProveedor()));
            txt_emailProveedor.setText((((Proveedor) tv_Cliente.getSelectionModel().getSelectedItem()).getEmailProveedor()));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Por favor selecciona una fila válida", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public ObservableList<Proveedor> getProveedores() {
        ArrayList<Proveedor> listaPro = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_listarProveedores()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                listaPro.add(new Proveedor(
                        resultado.getInt("codigoProveedor"),
                        resultado.getString("NITProveedor"),
                        resultado.getString("nombresProveedor"),
                        resultado.getString("apellidosProveedor"),
                        resultado.getString("direccionProveedor"),
                        resultado.getString("razonSocial"),
                        resultado.getString("contactoPrincipal"),
                        resultado.getString("paginaWeb"),
                        resultado.getString("telefonoProveedor"),
                        resultado.getString("emailProveedor")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaProveedores = FXCollections.observableList(listaPro);
    }

    public void Agregar() {
        switch (tipoDeOperador) {
            case NINGUNO:
                limpiarControles();
                activarControles();
                btn_agregarC.setText("Guardar");
                btn_EliminarC.setText("Cancelar");
                btn_editarC.setDisable(true);
                btn_reportesC.setDisable(true);
                tipoDeOperador = operador.ACTUALIZAR;
                break;
            case ACTUALIZAR:
                guardar();
                limpiarControles();

                desactivarControles();
                btn_agregarC.setText("Agregar");
                btn_EliminarC.setText("Eliminar");
                btn_editarC.setDisable(false);
                btn_reportesC.setDisable(false);
                tipoDeOperador = operador.NINGUNO;
                break;
        }
    }

    public void guardar() {
        Proveedor registro = new Proveedor();
        registro.setCodigoProveedor(Integer.parseInt(txt_codP.getText()));
        registro.setNITProveedor(txt_nitP.getText());
        registro.setNombresProveedor(txt_nombreP.getText());
        registro.setApellidosProveedor(txt_apellidoP.getText());
        registro.setDireccionProveedor(txt_direccionP.getText());
        registro.setRazonSocial(txt_razonS.getText());
        registro.setContactoPrincipal(txt_contactoP.getText());
        registro.setPaginaWeb(txt_paginaW.getText());
        registro.setTelefonoProveedor(txt_telefonoProveedor.getText());
        registro.setEmailProveedor(txt_emailProveedor.getText());

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_crearProveedor(?,?,?,?,?,?,?,?,?,?)}");
            procedimiento.setInt(1, registro.getCodigoProveedor());
            procedimiento.setString(2, registro.getNITProveedor());
            procedimiento.setString(3, registro.getNombresProveedor());
            procedimiento.setString(4, registro.getApellidosProveedor());
            procedimiento.setString(5, registro.getDireccionProveedor());
            procedimiento.setString(6, registro.getRazonSocial());
            procedimiento.setString(7, registro.getContactoPrincipal());
            procedimiento.setString(8, registro.getPaginaWeb());
            procedimiento.setString(9, registro.getTelefonoProveedor());
            procedimiento.setString(10, registro.getEmailProveedor());
            listaProveedores.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminar() {
        switch (tipoDeOperador) {
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btn_agregarC.setText("Agregar");
                btn_EliminarC.setText("Eliminar");
                btn_editarC.setDisable(false);
                btn_reportesC.setDisable(false);
                tipoDeOperador = operador.NINGUNO;
                break;
            default:
                if (tv_Cliente.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmas la eliminacion del registro?", "Eliminar Proveedores", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                    if (respuesta == JOptionPane.YES_NO_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_eliminarProveedor(?)}");
                            procedimiento.setInt(1, ((Proveedor) tv_Cliente.getSelectionModel().getSelectedItem()).getCodigoProveedor());
                            procedimiento.execute();
                            listaProveedores.remove(tv_Cliente.getSelectionModel().getSelectedItem());
                            limpiarControles();
                            cargarDatos();
                            JOptionPane.showMessageDialog(null, "Proveedor eliminado correctamente");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar una fila para eliminar");
                }

                break;
        }
    }

    public void editar() {
        switch (tipoDeOperador) {
            case NINGUNO:
                if (tv_Cliente.getSelectionModel().getSelectedItem() != null) {
                    btn_editarC.setText("Actualizar");
                    btn_reportesC.setText("Cancelar");
                    btn_EliminarC.setDisable(true);
                    btn_agregarC.setDisable(true);
                    activarControles();
                    txt_codP.setEditable(false);
                    tipoDeOperador = operador.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar una fila para editar");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                btn_editarC.setText("Editar");
                btn_reportesC.setText("Reportes");
                btn_agregarC.setDisable(false);
                btn_EliminarC.setDisable(false);
                desactivarControles();
                limpiarControles();
                tipoDeOperador = operador.NINGUNO;
                cargarDatos();
                break;
        }
    }

    public void reportes() {
        switch (tipoDeOperador) {
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btn_editarC.setText("Editar");
                btn_reportesC.setText("Reportes");
                btn_agregarC.setDisable(false);
                btn_EliminarC.setDisable(false);
                tipoDeOperador = operador.NINGUNO;

        }
    }

    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_actualizarProveedor(?,?,?,?,?,?,?,?,?,?)}");
            Proveedor registro = (Proveedor) tv_Cliente.getSelectionModel().getSelectedItem();
            registro.setNITProveedor(txt_nitP.getText());
            registro.setNombresProveedor(txt_nombreP.getText());
            registro.setApellidosProveedor(txt_apellidoP.getText());
            registro.setDireccionProveedor(txt_direccionP.getText());
            registro.setRazonSocial(txt_razonS.getText());
            registro.setContactoPrincipal(txt_contactoP.getText());
            registro.setPaginaWeb(txt_paginaW.getText());
            registro.setTelefonoProveedor(txt_telefonoProveedor.getText());
            registro.setEmailProveedor(txt_emailProveedor.getText());
            procedimiento.setInt(1, registro.getCodigoProveedor());
            procedimiento.setString(2, registro.getNITProveedor());
            procedimiento.setString(3, registro.getNombresProveedor());
            procedimiento.setString(4, registro.getApellidosProveedor());
            procedimiento.setString(5, registro.getDireccionProveedor());
            procedimiento.setString(6, registro.getRazonSocial());
            procedimiento.setString(7, registro.getContactoPrincipal());
            procedimiento.setString(8, registro.getPaginaWeb());
            procedimiento.setString(9, registro.getTelefonoProveedor());
            procedimiento.setString(10, registro.getEmailProveedor());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void desactivarControles() {
        txt_nitP.setEditable(false);
        txt_nitP.setEditable(false);
        txt_nombreP.setEditable(false);
        txt_apellidoP.setEditable(false);
        txt_direccionP.setEditable(false);
        txt_razonS.setEditable(false);
        txt_contactoP.setEditable(false);
        txt_paginaW.setEditable(false);
        txt_telefonoProveedor.setEditable(false);
        txt_emailProveedor.setEditable(false);
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
        txt_telefonoProveedor.setEditable(true);
        txt_emailProveedor.setEditable(true);
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
        txt_telefonoProveedor.clear();
        txt_emailProveedor.clear();
    }

    public void MenuPrincipalView() {
        escenarioPrincipal.MainMenuView();
    }

    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btn_Volver) {
            escenarioPrincipal.MainMenuView();
        }
    }

    public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

}
