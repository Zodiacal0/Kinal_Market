/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import org.javierherrera.system.bin.Producto;
import org.javierherrera.system.bin.Proveedor;
import org.javierherrera.system.bin.TipoDeProducto;
import org.javierherrera.system.dao.Conexion;
import org.javierherrera.system.main.Main;

/**
 *
 * @author Javier
 */
public class ProductoController implements Initializable {

    private ObservableList<Producto> listaProducto;
    private ObservableList<Proveedor> listaProveedor;
    private ObservableList<TipoDeProducto> listaTipoDeProducto;
    private Main escenarioPrincipal;

    private enum operaciones {
        AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NULL
    }

    private operaciones tipoDeOperaciones = operaciones.NULL;

    @FXML
    private TableView<Producto> tv_Producto;
    @FXML
    private TableColumn<Producto, String> col_codigoProducto;
    @FXML
    private TableColumn<Producto, String> col_descripcionProducto;
    @FXML
    private TableColumn<Producto, Double> col_precioUnitario;
    @FXML
    private TableColumn<Producto, Double> col_precioDocena;
    @FXML
    private TableColumn<Producto, Double> col_precioMayor;
    @FXML
    private TableColumn<Producto, Integer> col_existencia;
    @FXML
    private TableColumn<Producto, Integer> col_codigoTipoProducto;
    @FXML
    private TableColumn<Producto, Integer> col_codigoProveedor;
    @FXML
    private Button btn_agregarC;
    @FXML
    private Button btn_editarC;
    @FXML
    private Button btn_EliminarC;
    @FXML
    private Button btn_reportesC;
    @FXML
    private TextField txt_codigoProducto;
    @FXML
    private TextField txt_descripcionProducto;
    @FXML
    private TextField txt_precioUnitario;
    @FXML
    private TextField txt_precioDocena;
    @FXML
    private TextField txt_precioMayor;
    @FXML
    private TextField txt_existencia;
    @FXML
    private ComboBox<TipoDeProducto> cmb_codigoTipoProducto;
    @FXML
    private ComboBox<Proveedor> cmb_codigoProveedor;
    @FXML
    private Button btn_Volver;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmb_codigoProveedor.setItems(getProveedor());
        cmb_codigoTipoProducto.setItems(getTipoDeProducto());
    }

    public void cargarDatos() {
        tv_Producto.setItems(getProducto());
        col_codigoProducto.setCellValueFactory(new PropertyValueFactory<>("codigoProducto"));
        col_descripcionProducto.setCellValueFactory(new PropertyValueFactory<>("descripcionProducto"));
        col_precioUnitario.setCellValueFactory(new PropertyValueFactory<>("precioUnitario"));
        col_precioDocena.setCellValueFactory(new PropertyValueFactory<>("precioDocena"));
        col_precioMayor.setCellValueFactory(new PropertyValueFactory<>("precioMayor"));
        col_existencia.setCellValueFactory(new PropertyValueFactory<>("existencia"));
        col_codigoTipoProducto.setCellValueFactory(new PropertyValueFactory<>("codigoTipoProducto"));
        col_codigoProveedor.setCellValueFactory(new PropertyValueFactory<>("codigoProveedor"));
    }

    public ObservableList<Producto> getProducto() {
        ArrayList<Producto> lista = new ArrayList<>();
        try {
            Connection conexion = Conexion.getInstance().getConexion();
            if (conexion != null) {
                try (PreparedStatement procedimiento = conexion.prepareCall("{CALL sp_listarProductos()}");
                     ResultSet resultado = procedimiento.executeQuery()) {
                    while (resultado.next()) {
                        lista.add(new Producto(
                                resultado.getString("codigoProducto"),
                                resultado.getString("descripcionProducto"),
                                resultado.getDouble("precioUnitario"),
                                resultado.getDouble("precioDocena"),
                                resultado.getDouble("precioMayor"),
                                resultado.getInt("existencia"),
                                resultado.getInt("codigoTipoProducto"),
                                resultado.getInt("codigoProveedor")));
                    }
                }
            } else {
                System.out.println("No se pudo establecer conexión con la base de datos.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaProducto = FXCollections.observableList(lista);
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
        }
        return listaTipoDeProducto = FXCollections.observableList(lista);
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
        }
        return listaProveedor = FXCollections.observableList(lista);
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
        Producto reg = new Producto();
        Object tipoProductoSeleccionadoObj = cmb_codigoTipoProducto.getSelectionModel().getSelectedItem();
        if (tipoProductoSeleccionadoObj instanceof TipoDeProducto) {
            TipoDeProducto tipoProductoSeleccionado = (TipoDeProducto) tipoProductoSeleccionadoObj;
            reg.setCodigoTipoProducto(tipoProductoSeleccionado.getCodigoTipoProducto());
        } else {
            System.err.println("Error: Debe seleccionar un tipo de producto válido.");
            return;
        }
        Object proveedorSeleccionadoObj = cmb_codigoProveedor.getSelectionModel().getSelectedItem();
        if (proveedorSeleccionadoObj instanceof Proveedor) {
            Proveedor proveedorSeleccionado = (Proveedor) proveedorSeleccionadoObj;
            reg.setCodigoProveedor(proveedorSeleccionado.getCodigoProveedor());
        } else {
            System.err.println("Error: Debe seleccionar un proveedor válido.");
            return;
        }
        reg.setCodigoProducto(txt_codigoProducto.getText());
        reg.setDescripcionProducto(txt_descripcionProducto.getText());
        reg.setPrecioUnitario(Double.parseDouble(txt_precioUnitario.getText()));
        reg.setPrecioDocena(Double.parseDouble(txt_precioDocena.getText()));
        reg.setPrecioMayor(Double.parseDouble(txt_precioMayor.getText()));
        reg.setExistencia(Integer.parseInt(txt_existencia.getText()));
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_crearProducto(?, ?, ?, ?, ?, ?, ?, ?)}");
            procedimiento.setString(1, reg.getCodigoProducto());
            procedimiento.setString(2, reg.getDescripcionProducto());
            procedimiento.setDouble(3, reg.getPrecioUnitario());
            procedimiento.setDouble(4, reg.getPrecioDocena());
            procedimiento.setDouble(5, reg.getPrecioMayor());
            procedimiento.setInt(6, reg.getExistencia());
            procedimiento.setInt(7, reg.getCodigoTipoProducto());
            procedimiento.setInt(8, reg.getCodigoProveedor());
            procedimiento.execute();
            listaProducto.add(reg);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al guardar el producto: " + e.getMessage());
        }
    }
    
    public void seleccionar() {
    if (tv_Producto.getSelectionModel().getSelectedItem() != null) {
        Producto productoSeleccionado = tv_Producto.getSelectionModel().getSelectedItem();
        txt_codigoProducto.setText(productoSeleccionado.getCodigoProducto());
        txt_descripcionProducto.setText(productoSeleccionado.getDescripcionProducto());
        txt_precioUnitario.setText(String.valueOf(productoSeleccionado.getPrecioUnitario()));
        txt_precioDocena.setText(String.valueOf(productoSeleccionado.getPrecioDocena()));
        txt_precioMayor.setText(String.valueOf(productoSeleccionado.getPrecioMayor()));
        txt_existencia.setText(String.valueOf(productoSeleccionado.getExistencia()));
        cmb_codigoTipoProducto.getSelectionModel().select(buscarTipoDeProducto(productoSeleccionado.getCodigoTipoProducto()));
        cmb_codigoProveedor.getSelectionModel().select(buscarProveedor(productoSeleccionado.getCodigoProveedor()));
    }
}

    public void editar() {
        switch (tipoDeOperaciones) {
            case NULL:
                if (tv_Producto.getSelectionModel().getSelectedItem() != null) {
                    Producto productoSeleccionado = tv_Producto.getSelectionModel().getSelectedItem();
                    txt_codigoProducto.setText(productoSeleccionado.getCodigoProducto());
                    txt_descripcionProducto.setText(productoSeleccionado.getDescripcionProducto());
                    txt_precioUnitario.setText(String.valueOf(productoSeleccionado.getPrecioUnitario()));
                    txt_precioDocena.setText(String.valueOf(productoSeleccionado.getPrecioDocena()));
                    txt_precioMayor.setText(String.valueOf(productoSeleccionado.getPrecioMayor()));
                    txt_existencia.setText(String.valueOf(productoSeleccionado.getExistencia()));
                    cmb_codigoTipoProducto.getSelectionModel().select(buscarTipoDeProducto(productoSeleccionado.getCodigoTipoProducto()));
                    cmb_codigoProveedor.getSelectionModel().select(buscarProveedor(productoSeleccionado.getCodigoProveedor()));

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
        Producto productoSeleccionado = tv_Producto.getSelectionModel().getSelectedItem();
        productoSeleccionado.setCodigoProducto(txt_codigoProducto.getText());
        productoSeleccionado.setDescripcionProducto(txt_descripcionProducto.getText());
        productoSeleccionado.setPrecioUnitario(Double.parseDouble(txt_precioUnitario.getText()));
        productoSeleccionado.setPrecioDocena(Double.parseDouble(txt_precioDocena.getText()));
        productoSeleccionado.setPrecioMayor(Double.parseDouble(txt_precioMayor.getText()));
        productoSeleccionado.setExistencia(Integer.parseInt(txt_existencia.getText()));

        // Verificar y asignar el tipo de producto seleccionado
        Object tipoProductoSeleccionadoObj = cmb_codigoTipoProducto.getSelectionModel().getSelectedItem();
        if (tipoProductoSeleccionadoObj instanceof TipoDeProducto) {
            TipoDeProducto tipoProductoSeleccionado = (TipoDeProducto) tipoProductoSeleccionadoObj;
            productoSeleccionado.setCodigoTipoProducto(tipoProductoSeleccionado.getCodigoTipoProducto());
        } else {
            System.err.println("Error: Debe seleccionar un tipo de producto válido.");
            return;
        }

        // Verificar y asignar el proveedor seleccionado
        Object proveedorSeleccionadoObj = cmb_codigoProveedor.getSelectionModel().getSelectedItem();
        if (proveedorSeleccionadoObj instanceof Proveedor) {
            Proveedor proveedorSeleccionado = (Proveedor) proveedorSeleccionadoObj;
            productoSeleccionado.setCodigoProveedor(proveedorSeleccionado.getCodigoProveedor());
        } else {
            System.err.println("Error: Debe seleccionar un proveedor válido.");
            return;
        }

        // Intentar ejecutar el procedimiento almacenado
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_actualizarProducto(?, ?, ?, ?, ?, ?, ?, ?)}");
            procedimiento.setString(1, productoSeleccionado.getCodigoProducto());
            procedimiento.setString(2, productoSeleccionado.getDescripcionProducto());
            procedimiento.setDouble(3, productoSeleccionado.getPrecioUnitario());
            procedimiento.setDouble(4, productoSeleccionado.getPrecioDocena());
            procedimiento.setDouble(5, productoSeleccionado.getPrecioMayor());
            procedimiento.setInt(6, productoSeleccionado.getExistencia());
            procedimiento.setInt(7, productoSeleccionado.getCodigoTipoProducto());
            procedimiento.setInt(8, productoSeleccionado.getCodigoProveedor());
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
            if (tv_Producto.getSelectionModel().getSelectedItem() != null) {
                int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar Producto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (respuesta == JOptionPane.YES_OPTION) {
                    try {
                        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_eliminarProducto(?)}");
                        procedimiento.setString(1, tv_Producto.getSelectionModel().getSelectedItem().getCodigoProducto());
                        procedimiento.execute();
                        listaProducto.remove(tv_Producto.getSelectionModel().getSelectedIndex());
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

    // Métodos de activación, desactivación y limpieza de controles
    public void activarControles() {
        txt_codigoProducto.setEditable(true);
        txt_descripcionProducto.setEditable(true);
        txt_precioUnitario.setEditable(true);
        txt_precioDocena.setEditable(true);
        txt_precioMayor.setEditable(true);
        txt_existencia.setEditable(true);
        cmb_codigoTipoProducto.setDisable(false);
        cmb_codigoProveedor.setDisable(false);
    }

    public void desactivarControles() {
        txt_codigoProducto.setEditable(false);
        txt_descripcionProducto.setEditable(false);
        txt_precioUnitario.setEditable(false);
        txt_precioDocena.setEditable(false);
        txt_precioMayor.setEditable(false);
        txt_existencia.setEditable(false);
        cmb_codigoTipoProducto.setDisable(true);
        cmb_codigoProveedor.setDisable(true);
    }

    public void limpiarControles() {
        txt_codigoProducto.clear();
        txt_descripcionProducto.clear();
        txt_precioUnitario.clear();
        txt_precioDocena.clear();
        txt_precioMayor.clear();
        txt_existencia.clear();
        cmb_codigoTipoProducto.getSelectionModel().clearSelection();
        cmb_codigoProveedor.getSelectionModel().clearSelection();
    }
    
    public void reportes(){
    
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        escenarioPrincipal.MainMenuView();
    }

    public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    private TipoDeProducto buscarTipoDeProducto(int codigoTipoProducto) {
        for (TipoDeProducto tipo : listaTipoDeProducto) {
            if (tipo.getCodigoTipoProducto() == codigoTipoProducto) {
                return tipo;
            }
        }
        return null;
    }

    private Proveedor buscarProveedor(int codigoProveedor) {
        for (Proveedor proveedor : listaProveedor) {
            if (proveedor.getCodigoProveedor() == codigoProveedor) {
                return proveedor;
            }
        }
        return null;
    }
}

