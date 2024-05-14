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
import org.javierherrera.system.bin.TipoDeProducto;
import org.javierherrera.system.dao.Conexion;
import org.javierherrera.system.main.Main;

/**
 *
 * @author Javier
 */
public class TipoDeProductoController implements Initializable {

    private ObservableList<TipoDeProducto> listaTipoDeProducto;
    private Main escenarioPrincipal;

    private enum operaciones {
        AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NULL
    }

    private operaciones tipoDeOperaciones = operaciones.NULL;

    @FXML
    private TableView tv_TipoDeProducto;

    @FXML
    private TableColumn col_codigoTipoProducto;

    @FXML
    private TableColumn col_descripcion;

    @FXML
    private Button btn_agregar;

    @FXML
    private Button btn_editar;

    @FXML
    private Button btn_Eliminar;

    @FXML
    private Button btn_reportes;
    
    @FXML
    private Button btn_Volver;

    @FXML
    private TextField txt_codigoTipoProducto;

    @FXML
    private TextField txt_descripcion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
    }

    public void cargarDatos() {
        tv_TipoDeProducto.setItems(getTipoDeProducto());
        col_codigoTipoProducto.setCellValueFactory(new PropertyValueFactory<Cliente, Integer>("codigoTipoProducto"));
        col_descripcion.setCellValueFactory(new PropertyValueFactory<Cliente, String>("descripcion"));

    }

    public ObservableList<TipoDeProducto> getTipoDeProducto() {
        ArrayList<TipoDeProducto> lista = new ArrayList<>();
        try {
            Connection conexion = Conexion.getInstance().getConexion();
            if (conexion != null) {
                try (PreparedStatement procedimiento = conexion.prepareCall("{CALL sp_listarTipoProducto()}");
                        ResultSet resultado = procedimiento.executeQuery()) {
                    while (resultado.next()) {
                        lista.add(new TipoDeProducto(
                                resultado.getInt("codigoTipoProducto"),
                                resultado.getString("descripcion")));
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
        return listaTipoDeProducto = FXCollections.observableList(lista);
    }

    public void Agregar() throws SQLException {
        switch (tipoDeOperaciones) {
            case NULL:
                activarControles();
                btn_agregar.setText("Guardar");
                btn_Eliminar.setText("Cancelar");
                btn_editar.setDisable(true);
                btn_reportes.setDisable(true);
                tipoDeOperaciones = operaciones.ACTUALIZAR;
                break;
            case ACTUALIZAR:
                guardar();
                desactivarControles();
                cargarDatos();
                limpiarControles();
                btn_agregar.setText("Agregar");
                btn_Eliminar.setText("Eliminar");
                btn_editar.setDisable(false);
                btn_reportes.setDisable(false);
                tipoDeOperaciones = operaciones.ACTUALIZAR;
                tipoDeOperaciones = operaciones.NULL;
                break;

        }
    }

    public void guardar() throws SQLException {
        TipoDeProducto reg = new TipoDeProducto();
        reg.setCodigoTipoProducto(Integer.parseInt(txt_codigoTipoProducto.getText()));
        reg.setDescripcion(txt_descripcion.getText());
        PreparedStatement procedimiento = Conexion.getInstance().getConexion()
                .prepareCall("{CALL sp_crearTipoProducto(?, ?)}");
        procedimiento.setInt(1, reg.getCodigoTipoProducto());
        procedimiento.setString(2, reg.getDescripcion());
        procedimiento.execute();
        listaTipoDeProducto.add(reg);
    }

    public void editar() throws Exception {
        switch (tipoDeOperaciones) {
            case NULL:
                if (tv_TipoDeProducto.getSelectionModel().getSelectedItem() != null) {
                    btn_editar.setText("Actualizar");
                    btn_reportes.setText("Cancelar");
                    btn_Eliminar.setDisable(true);
                    btn_agregar.setDisable(true);
                    tipoDeOperaciones = operaciones.NULL;
                    activarControles();
                    txt_codigoTipoProducto.setEditable(false);
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Selecciones un Cliente");
                }
                break;

            case ACTUALIZAR:
                actualizar();
                btn_editar.setText("Editar");
                btn_reportes.setText("Cancelar");
                btn_Eliminar.setDisable(false);
                btn_agregar.setDisable(false);
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
                btn_editar.setText("Editar");
                btn_reportes.setText("Reportes");
                btn_agregar.setDisable(false);
                btn_Eliminar.setDisable(false);
                tipoDeOperaciones = operaciones.NULL;
        }
    }

    public void actualizar() throws Exception {
        TipoDeProducto reg = new TipoDeProducto();
        reg.setCodigoTipoProducto(Integer.parseInt(txt_codigoTipoProducto.getText()));
        reg.setDescripcion(txt_descripcion.getText());
        PreparedStatement procedimiento = Conexion.getInstance().getConexion()
                .prepareCall("{CALL sp_actualizarTipoProducto(?, ?)}");
        procedimiento.setInt(1, reg.getCodigoTipoProducto());
        procedimiento.setString(2, reg.getDescripcion());
        procedimiento.execute();

    }

    public void seleccionar() {
        txt_codigoTipoProducto.setText(String.valueOf(
                ((TipoDeProducto) tv_TipoDeProducto.getSelectionModel().getSelectedItem()).getCodigoTipoProducto()));
        txt_descripcion
                .setText(((TipoDeProducto) tv_TipoDeProducto.getSelectionModel().getSelectedItem()).getDescripcion());
    }

    public void eliminar() throws SQLException {
        switch (tipoDeOperaciones) {
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btn_agregar.setText("Agregar");
                btn_Eliminar.setText("Eliminar");
                btn_editar.setDisable(false);
                btn_reportes.setDisable(false);
                tipoDeOperaciones = operaciones.NULL;
                break;
            default:
                if (tv_TipoDeProducto.getSelectionModel().getSelectedItem() != null) {
                    int ans = JOptionPane.showConfirmDialog(null, "Confirma esta Accion", "Verificacion",
                            JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (ans == JOptionPane.YES_NO_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion()
                                    .prepareCall("{CALL sp_eliminarCliente(?)}");
                            procedimiento.setInt(1,
                                    ((TipoDeProducto) tv_TipoDeProducto.getSelectionModel().getSelectedItem())
                                            .getCodigoTipoProducto());
                            procedimiento.execute();
                            listaTipoDeProducto.remove(tv_TipoDeProducto.getSelectionModel().getSelectedItem());
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
        txt_codigoTipoProducto.setEditable(false);
        txt_descripcion.setEditable(false);
    }

    public void activarControles() {
        txt_codigoTipoProducto.setEditable(true);
        txt_descripcion.setEditable(true);
    }

    public void limpiarControles() {
        txt_codigoTipoProducto.clear();
        txt_descripcion.clear();
    }

    public void MenuPrincipalView() {
        escenarioPrincipal.MainMenuView();
    }

    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    @FXML
    public void handleButtonAction(ActionEvent event) {
        if(event.getSource() == btn_Volver ){
            escenarioPrincipal.MainMenuView();
        }
    }

}
