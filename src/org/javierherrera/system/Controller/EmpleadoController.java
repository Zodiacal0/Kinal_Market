/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.javierherrera.system.bin.CargoEmpleado;
import org.javierherrera.system.bin.Empleado;
import org.javierherrera.system.dao.Conexion;
import org.javierherrera.system.main.Main;

/**
 *
 * @author Javier
 */
public class EmpleadoController implements Initializable {
    
    private ObservableList<Empleado> listaEmpleado;
    private ObservableList<CargoEmpleado> listaCargoEmpleado;
    
    private Main escenarioPrincipal;

    private enum operaciones {
        AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NULL
    }
    private operaciones tipoDeOperaciones = operaciones.NULL;
    
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
    private ComboBox cmb_codigoCargoEmpleado;

    @FXML
    private TableColumn col_apellidosEmpleado;

    @FXML
    private TableColumn col_codigoCargoEmpleado;

    @FXML
    private TableColumn col_codigoEmpleado;

    @FXML
    private TableColumn col_direccion;

    @FXML
    private TableColumn col_nombresEmpleado;

    @FXML
    private TableColumn col_sueldo;

    @FXML
    private TableColumn col_turno;

    @FXML
    private TableView<Empleado> tv_Empleados;
    

    @FXML
    private TextField txt_apellidosEmpleado;

    @FXML
    private TextField txt_codigoEmpleado;

    @FXML
    private TextField txt_direccion;

    @FXML
    private TextField txt_nombresEmpleado;

    @FXML
    private TextField txt_sueldo;

    @FXML
    private TextField txt_turno;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmb_codigoCargoEmpleado.setItems(getCargoEmpleado());
    }
    
    public void cargarDatos() {
        try{
            tv_Empleados.setItems(getEmpleado());
            col_codigoEmpleado.setCellValueFactory(new PropertyValueFactory<>("codigoEmpleado"));
            col_nombresEmpleado.setCellValueFactory(new PropertyValueFactory<>("nombresEmpleado"));
            col_apellidosEmpleado.setCellValueFactory(new PropertyValueFactory<>("apellidosEmpleado"));
            col_sueldo.setCellValueFactory(new PropertyValueFactory<>("sueldo"));
            col_direccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
            col_turno.setCellValueFactory(new PropertyValueFactory<>("turno"));
            col_codigoCargoEmpleado.setCellValueFactory(new PropertyValueFactory<>("codigoCargoEmpleado"));
        }catch(Exception  e){
            e.printStackTrace();
        }
            
    }
    
    
    public ObservableList<Empleado> getEmpleado() {
        ArrayList<Empleado> lista = new ArrayList<>();
        try {
            Connection conexion = Conexion.getInstance().getConexion();
            if (conexion != null) {
                try (PreparedStatement procedimiento = conexion.prepareCall("{CALL sp_listarEmpleados()}");
                     ResultSet resultado = procedimiento.executeQuery()) {
                    while (resultado.next()) {
                        lista.add(new Empleado(
                                resultado.getInt("codigoEmpleado"),
                                resultado.getString("nombresEmpleado"),
                                resultado.getString("apellidosEmpleado"),
                                resultado.getDouble("sueldo"),
                                resultado.getString("direccion"),
                                resultado.getString("turno"),
                                resultado.getInt("codigoCargoEmpleado")));
                    }
                }
            } else {
                System.out.println("No se pudo establecer conexión con la base de datos.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return FXCollections.observableList(lista); 
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
        return listaCargoEmpleado = FXCollections.observableList(lista);
    }

    public void Agregar() {
        switch (tipoDeOperaciones) {
            case NULL:
                activarControles();
                btn_agregarC.setText("Guardar");
                btn_EliminarC.setText("Cancelar");
                btn_editarC.setDisable(true);
                btn_reportesC.setDisable(true);
                tipoDeOperaciones = operaciones.AGREGAR;
                break;
            case AGREGAR:
                guardar();
                desactivarControles();
                cargarDatos();
                limpiarControles();
                btn_agregarC.setText("Agregar");
                btn_EliminarC.setText("Eliminar");
                btn_editarC.setDisable(false);
                btn_reportesC.setDisable(false);
                tipoDeOperaciones = operaciones.NULL;
                break;
        }
    }
    
    public void guardar() {
        Empleado reg = new Empleado();
        Object codigoCargoEmpleadoObj = cmb_codigoCargoEmpleado.getSelectionModel().getSelectedItem();
        if (codigoCargoEmpleadoObj instanceof CargoEmpleado) {
            CargoEmpleado cargoEmpleado = (CargoEmpleado) codigoCargoEmpleadoObj;
            reg.setCodigoCargoEmpleado(cargoEmpleado.getCodigoCargoEmpleado());
        } else {
            System.err.println("Error: Debe seleccionar un tipo de producto válido.");
            return;
        }

        reg.setCodigoEmpleado(Integer.parseInt(txt_codigoEmpleado.getText()));
        reg.setNombresEmpleado(txt_nombresEmpleado.getText());
        reg.setApellidosEmpleado(txt_apellidosEmpleado.getText());
        reg.setSueldo(Double.parseDouble(txt_sueldo.getText()));
        reg.setDireccion(txt_direccion.getText());
        reg.setTurno(txt_turno.getText());

        /*Depuración*/
        System.out.println("Codigo Cargo Empleado: " + reg.getCodigoCargoEmpleado());
        System.out.println("Codigo Empleado: " + reg.getCodigoEmpleado());
        System.out.println("Nombres Empleado: " + reg.getNombresEmpleado());
        System.out.println("Apellidos Empleado: " + reg.getApellidosEmpleado());
        System.out.println("Sueldo: " + reg.getSueldo());
        System.out.println("Dirección: " + reg.getDireccion());
        System.out.println("Turno: " + reg.getTurno());

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_crearEmpleado(?, ?, ?, ?, ?, ?, ?)}");
            procedimiento.setInt(1, reg.getCodigoEmpleado());
            procedimiento.setString(2, reg.getNombresEmpleado());
            procedimiento.setString(3, reg.getApellidosEmpleado());
            procedimiento.setDouble(4, reg.getSueldo());
            procedimiento.setString(5, reg.getDireccion());
            procedimiento.setString(6, reg.getTurno());
            procedimiento.setInt(7, reg.getCodigoCargoEmpleado());
            procedimiento.execute();
            listaEmpleado.add(reg);
       
            
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al guardar el empleado: " + e.getMessage());
        }

    }

    
    public void seleccionar() {
    Empleado empleadoSeleccionado = tv_Empleados.getSelectionModel().getSelectedItem();
    if (empleadoSeleccionado != null) {
        txt_codigoEmpleado.setText(String.valueOf(empleadoSeleccionado.getCodigoEmpleado()));
        txt_nombresEmpleado.setText(empleadoSeleccionado.getNombresEmpleado());
        txt_apellidosEmpleado.setText(String.valueOf(empleadoSeleccionado.getApellidosEmpleado()));
        txt_sueldo.setText(String.valueOf(empleadoSeleccionado.getSueldo()));
        txt_direccion.setText(String.valueOf(empleadoSeleccionado.getDireccion()));
        txt_turno.setText(String.valueOf(empleadoSeleccionado.getDireccion()));
        cmb_codigoCargoEmpleado.getSelectionModel().select(buscarCodigoCargoEmpleado(empleadoSeleccionado.getCodigoCargoEmpleado()));
    }
}

    
    private CargoEmpleado buscarCodigoCargoEmpleado(int codigoCargoEmpleado) {
        for (CargoEmpleado cargo : listaCargoEmpleado) {
            if (cargo.getCodigoCargoEmpleado() == codigoCargoEmpleado) {
                return cargo;
            }
        }
        return null;
    }

    
    public void editar() {
        switch (tipoDeOperaciones) {
            case NULL:
                if (tv_Empleados.getSelectionModel().getSelectedItem() != null) {
                    Empleado empleadoSeleccionado = tv_Empleados.getSelectionModel().getSelectedItem();
                    txt_codigoEmpleado.setText(String.valueOf(empleadoSeleccionado.getCodigoEmpleado()));
                    txt_nombresEmpleado.setText(empleadoSeleccionado.getNombresEmpleado());
                    txt_apellidosEmpleado.setText(empleadoSeleccionado.getApellidosEmpleado());
                    txt_sueldo.setText(String.valueOf(empleadoSeleccionado.getSueldo()));
                    txt_direccion.setText(empleadoSeleccionado.getDireccion());
                    txt_turno.setText(empleadoSeleccionado.getTurno());
                    cmb_codigoCargoEmpleado.getSelectionModel().select(buscarCodigoCargoEmpleado(empleadoSeleccionado.getCodigoCargoEmpleado()));

                    btn_editarC.setText("Actualizar");
                    btn_reportesC.setText("Cancelar");
                    btn_agregarC.setDisable(true);
                    btn_EliminarC.setDisable(true);
                    activarControles();
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
                } else {
                    System.out.println("Debe seleccionar un elemento.");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                btn_editarC.setText("Editar");
                btn_reportesC.setText("Reportes");
                btn_agregarC.setDisable(false);
                btn_EliminarC.setDisable(false);
                desactivarControles();
                cargarDatos();
                limpiarControles();
                tipoDeOperaciones = operaciones.NULL;
                break;
        }
    }
    
    public void actualizar() {
        Empleado productoSeleccionado = tv_Empleados.getSelectionModel().getSelectedItem();
        productoSeleccionado.setCodigoEmpleado(Integer.parseInt(txt_codigoEmpleado.getText()));
        productoSeleccionado.setNombresEmpleado(txt_nombresEmpleado.getText());
        productoSeleccionado.setApellidosEmpleado(txt_apellidosEmpleado.getText());
        productoSeleccionado.setSueldo(Double.parseDouble(txt_sueldo.getText()));
        productoSeleccionado.setDireccion(txt_direccion.getText());
        productoSeleccionado.setTurno(txt_turno.getText());

        Object codigoCargoEmpleadoObj = cmb_codigoCargoEmpleado.getSelectionModel().getSelectedItem();
        if (codigoCargoEmpleadoObj instanceof CargoEmpleado) {
            CargoEmpleado codigoCargoEmpleado = (CargoEmpleado) codigoCargoEmpleadoObj;
            productoSeleccionado.setCodigoCargoEmpleado(codigoCargoEmpleado.getCodigoCargoEmpleado());
        } else {
            System.err.println("Error: Debe seleccionar un tipo de producto válido.");
            return;
        }
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_actualizarEmpleado(?, ?, ?, ?, ?, ?, ?)}");
            procedimiento.setInt(1, productoSeleccionado.getCodigoEmpleado());
            procedimiento.setString(2, productoSeleccionado.getNombresEmpleado());
            procedimiento.setString(3, productoSeleccionado.getApellidosEmpleado());
            procedimiento.setDouble(4, productoSeleccionado.getSueldo());
            procedimiento.setString(5, productoSeleccionado.getDireccion());
            procedimiento.setString(6, productoSeleccionado.getTurno());
            procedimiento.setInt(7, productoSeleccionado.getCodigoCargoEmpleado());
            procedimiento.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al actualizar el producto: " + e.getMessage());
        }
    }
    
    public void eliminar() {
        if (tipoDeOperaciones == operaciones.AGREGAR) {
            desactivarControles();
            limpiarControles();
            btn_agregarC.setText("Agregar");
            btn_EliminarC.setText("Eliminar");
            btn_editarC.setDisable(false);
            btn_reportesC.setDisable(false);
            tipoDeOperaciones = operaciones.NULL;
        } else {
            if (tv_Empleados.getSelectionModel().getSelectedItem() != null) {
                int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar Producto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (respuesta == JOptionPane.YES_OPTION) {
                    try {
                        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_eliminarEmpleado(?)}");
                        procedimiento.setInt(1, tv_Empleados.getSelectionModel().getSelectedItem().getCodigoEmpleado());
                        procedimiento.execute();
                        listaEmpleado.remove(tv_Empleados.getSelectionModel().getSelectedIndex());
                        limpiarControles();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        System.err.println("Error al eliminar el producto: " + e.getMessage());
                    }
                }
            } else {
                System.out.println("Debe seleccionar un elemento.");
            }
        }
    }
    
    public void activarControles() {
        txt_codigoEmpleado.setEditable(true);
        txt_nombresEmpleado.setEditable(true);
        txt_apellidosEmpleado.setEditable(true);
        txt_direccion.setEditable(true);
        txt_turno.setEditable(true);
        cmb_codigoCargoEmpleado.setDisable(false);
    }

    public void desactivarControles() {
        txt_codigoEmpleado.setEditable(false);
        txt_nombresEmpleado.setEditable(false);
        txt_apellidosEmpleado.setEditable(false);
        txt_direccion.setEditable(false);
        txt_turno.setEditable(false);
        cmb_codigoCargoEmpleado.setDisable(false);
    }

    public void limpiarControles() {
        txt_codigoEmpleado.clear();
        txt_nombresEmpleado.clear();
        txt_apellidosEmpleado.clear();
        txt_direccion.clear();
        txt_turno.clear();
        cmb_codigoCargoEmpleado.getSelectionModel().clearSelection();
    }
    
    public void reportes(){
    
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
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
