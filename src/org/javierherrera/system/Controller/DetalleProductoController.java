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
import org.javierherrera.system.bin.Compras;
import org.javierherrera.system.bin.DetalleProducto;
import org.javierherrera.system.bin.Producto;
import org.javierherrera.system.bin.TipoDeProducto;
import org.javierherrera.system.dao.Conexion;
import org.javierherrera.system.main.Main;

/**
 *
 * @author Javier
 */
public class DetalleProductoController implements Initializable {
    private ObservableList<DetalleProducto> listaDetalleProducto;
    private ObservableList<Producto> listaProducto;
    private ObservableList<Compras> ListaCompras;
    private Main escenarioPrincipal;

   

    private enum operaciones {
        AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NULL
    }
    private operaciones tipoDeOperaciones = operaciones.NULL;
    
    @FXML private Button btn_EliminarC;
    @FXML private Button btn_Volver;
    @FXML private Button btn_agregarC;
    @FXML private Button btn_editarC;
    @FXML private Button btn_reportesC;
    @FXML private ComboBox cmb_codigoProducto;
    @FXML private ComboBox cmb_numeroDocumento;
    @FXML private TableColumn<DetalleProducto, Integer> col_cantidad;
    @FXML private TableColumn<DetalleProducto, Double> col_codigoDetalleCompra;
    @FXML private TableColumn<DetalleProducto, Integer> col_codigoProducto;
    @FXML private TableColumn<DetalleProducto, String> col_costoUnitario;
    @FXML private TableColumn<DetalleProducto, Integer> col_numeroDocumento;
    @FXML private TableView<DetalleProducto> tv_DetalleProducto;
    @FXML private TextField txt_cantidad;
    @FXML private TextField txt_costoUnitario;
    @FXML private TextField txt_codigoDetalleCompra;
   
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            cargarDatos();
            cmb_codigoProducto.setItems(getProducto());
            cmb_numeroDocumento.setItems(getCompras());
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al cargar los datos: " + e.getMessage());
        }
    }

    
    public void cargarDatos() throws SQLException {
        tv_DetalleProducto.setItems(getDetalleProducto());
        col_codigoDetalleCompra.setCellValueFactory(new PropertyValueFactory<>("codigoDetalleCompra"));
        col_costoUnitario.setCellValueFactory(new PropertyValueFactory<>("costoUnitario"));
        col_cantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        col_codigoProducto.setCellValueFactory(new PropertyValueFactory<>("codigoProducto"));
        col_numeroDocumento.setCellValueFactory(new PropertyValueFactory<>("numeroDocumento"));
    }
    
    public ObservableList<DetalleProducto> getDetalleProducto() throws SQLException {
        ArrayList<DetalleProducto> lista = new ArrayList<>();
        Connection conexion = Conexion.getInstance().getConexion();
        if (conexion != null) {
            try (PreparedStatement procedimiento = conexion.prepareCall("{CALL sp_listarDetallesCompra()}");
                 ResultSet resultado = procedimiento.executeQuery()) {
                while (resultado.next()) {
                    lista.add(new DetalleProducto(
                            resultado.getInt("codigoDetalleCompra"),
                            resultado.getDouble("costoUnitario"),
                            resultado.getInt("cantidad"),
                            resultado.getString("codigoProducto"),
                            resultado.getInt("numeroDocumento")
                    ));
                }
            }
        }
        listaDetalleProducto = FXCollections.observableList(lista);
        return listaDetalleProducto;
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
    
    public ObservableList<Compras> getCompras() {
        ArrayList<Compras> lista = new ArrayList<>();
        try {
            Connection conexion = Conexion.getInstance().getConexion();
            if (conexion != null) {
                try (PreparedStatement procedimiento = conexion.prepareCall("{CALL sp_listarCompras()}")) {
                    try (ResultSet resultado = procedimiento.executeQuery()) {
                        while (resultado.next()) {
                            lista.add(new Compras(
                                    resultado.getInt("numeroDocumento"),
                                    resultado.getString("fechaDocumento"),
                                    resultado.getString("descripcion"),
                                    resultado.getDouble("totalDocumento")));
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo establecer conexión con la base de datos.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los datos: " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error inesperado: " + e.getMessage());
        }
        return ListaCompras = FXCollections.observableList(lista);
    }
    
    public void Agregar() throws SQLException {
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
        DetalleProducto reg = new DetalleProducto();
        Object codigoProductoObj = cmb_codigoProducto.getSelectionModel().getSelectedItem();
        System.out.println("Tipo de objeto seleccionado en cmb_codigoProducto: " + codigoProductoObj.getClass().getName());
        if (codigoProductoObj instanceof Producto) {
            Producto codigoProducto = (Producto) codigoProductoObj;
            reg.setCodigoProducto(codigoProducto.getCodigoProducto());
        } else {
            System.err.println("Error: Debe seleccionar un tipo de producto válido.");
            return;
        }
        Object numeroDocumentoObj = cmb_numeroDocumento.getSelectionModel().getSelectedItem();
        if (numeroDocumentoObj instanceof Compras) {
            Compras numeroDocumento = (Compras) numeroDocumentoObj;
            reg.setNumeroDocumento(numeroDocumento.getNumeroDocumento());
        } else {
            System.err.println("Error: Debe seleccionar un proveedor válido.");
            return;
        }
        reg.setCodigoDetalleCompra(Integer.parseInt(txt_codigoDetalleCompra.getText()));
        reg.setCostoUnitario(Double.parseDouble(txt_costoUnitario.getText()));
        reg.setCantidad(Integer.parseInt(txt_cantidad.getText()));
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_crearDetalleCompra(?, ?, ?, ?, ?)}");
            procedimiento.setInt(1, reg.getCodigoDetalleCompra());
            procedimiento.setDouble(2, reg.getCostoUnitario());
            procedimiento.setInt(3, reg.getCantidad());
            procedimiento.setString(4, reg.getCodigoProducto());
            procedimiento.setInt(5, reg.getNumeroDocumento());
            procedimiento.execute();
            
            listaDetalleProducto.add(reg);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al guardar el producto: " + e.getMessage());
        }
    }
    
    public void seleccionar() {
        if (tv_DetalleProducto.getSelectionModel().getSelectedItem() != null) {
            DetalleProducto productoSeleccionado = tv_DetalleProducto.getSelectionModel().getSelectedItem();
            txt_codigoDetalleCompra.setText(String.valueOf(productoSeleccionado.getCodigoDetalleCompra()));
            txt_costoUnitario.setText(String.valueOf(productoSeleccionado.getCostoUnitario()));
            txt_cantidad.setText(String.valueOf(productoSeleccionado.getCantidad()));
            cmb_codigoProducto.getSelectionModel().select(buscarProducto(productoSeleccionado.getCodigoProducto()));
            cmb_numeroDocumento.getSelectionModel().select(buscarCompras(productoSeleccionado.getNumeroDocumento()));
        }
    }
    
    private Producto buscarProducto(String codigoProducto) {
    for (Producto tipo : listaProducto) {
        if (tipo.getCodigoProducto().equals(codigoProducto)) {
            return tipo;
        }
    }
    return null;
}

    private Compras buscarCompras(int numeroDocumento) {
        for (Compras tipo : ListaCompras) {
            if (tipo.getNumeroDocumento() == numeroDocumento) {
                return tipo;
            }
        }
        return null;
    }
    
    public void editar() {
        switch (tipoDeOperaciones) {
            case NULL:
                if (tv_DetalleProducto.getSelectionModel().getSelectedItem() != null) {
                    DetalleProducto productoSeleccionado = tv_DetalleProducto.getSelectionModel().getSelectedItem();
                    txt_codigoDetalleCompra.setText(String.valueOf(productoSeleccionado.getCodigoDetalleCompra()));
                    txt_costoUnitario.setText(String.valueOf(productoSeleccionado.getCostoUnitario()));
                    txt_cantidad.setText(String.valueOf(productoSeleccionado.getCantidad()));
                    cmb_codigoProducto.getSelectionModel().select(buscarProducto(productoSeleccionado.getCodigoProducto()));
                    cmb_numeroDocumento.getSelectionModel().select(buscarCompras(productoSeleccionado.getNumeroDocumento()));

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
                try {
                    cargarDatos();
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.err.println("Error al cargar los datos: " + e.getMessage());
                }
                limpiarControles();
                tipoDeOperaciones = operaciones.NULL;
                break;
        }
    }

    
    public void actualizar() {
        DetalleProducto productoSeleccionado = tv_DetalleProducto.getSelectionModel().getSelectedItem();
        productoSeleccionado.setCodigoDetalleCompra(Integer.parseInt(txt_codigoDetalleCompra.getText()));
        productoSeleccionado.setCostoUnitario(Double.parseDouble(txt_costoUnitario.getText()));
        productoSeleccionado.setCantidad(Integer.parseInt(txt_cantidad.getText()));

        Object codigoProductoObj = cmb_codigoProducto.getSelectionModel().getSelectedItem();
        if (codigoProductoObj instanceof Producto) {
            Producto codigoProducto = (Producto) codigoProductoObj;
            productoSeleccionado.setCodigoProducto(codigoProducto.getCodigoProducto());
        } else {
            System.err.println("Error: Debe seleccionar un tipo de producto válido.");
            return;
        }

        // Verificar y asignar el proveedor seleccionado
        Object numeroDocumentoObj = cmb_numeroDocumento.getSelectionModel().getSelectedItem();
        if (numeroDocumentoObj instanceof Compras) {
            Compras numeroDocumento = (Compras) numeroDocumentoObj;
            productoSeleccionado.setNumeroDocumento(numeroDocumento.getNumeroDocumento());
        } else {
            System.err.println("Error: Debe seleccionar un proveedor válido.");
            return;
        }

        // Intentar ejecutar el procedimiento almacenado
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_actualizarDetalleCompra(?, ?, ?, ?, ?)}");
            procedimiento.setInt(1, productoSeleccionado.getCodigoDetalleCompra());
            procedimiento.setDouble(2, productoSeleccionado.getCostoUnitario());
            procedimiento.setInt(3, productoSeleccionado.getCantidad());
            procedimiento.setString(4, productoSeleccionado.getCodigoProducto());
            procedimiento.setInt(5, productoSeleccionado.getNumeroDocumento());
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
            DetalleProducto productoSeleccionado = tv_DetalleProducto.getSelectionModel().getSelectedItem();
            if (productoSeleccionado != null) {
                int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar Producto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (respuesta == JOptionPane.YES_OPTION) {
                    try {
                        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{CALL sp_eliminarDetalleProducto(?)}");
                        procedimiento.setInt(1, productoSeleccionado.getCodigoDetalleCompra());
                        procedimiento.execute();
                        listaProducto.remove(productoSeleccionado);
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
        txt_codigoDetalleCompra.setEditable(true);
        txt_costoUnitario.setEditable(true);
        txt_cantidad.setEditable(true);
        cmb_codigoProducto.setDisable(false);
        cmb_numeroDocumento.setDisable(false);
    }

    public void desactivarControles() {
        txt_codigoDetalleCompra.setEditable(false);
        txt_costoUnitario.setEditable(false);
        txt_cantidad.setEditable(false);
        cmb_codigoProducto.setDisable(true);
        cmb_numeroDocumento.setDisable(true);
    }

    public void limpiarControles() {
        txt_codigoDetalleCompra.clear();
        txt_costoUnitario.clear();
        txt_cantidad.clear();
        cmb_codigoProducto.getSelectionModel().clearSelection();
        cmb_numeroDocumento.getSelectionModel().clearSelection();
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
    
}
