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
import org.javierherrera.system.bin.DetalleFactura;
import org.javierherrera.system.bin.Factura;
import org.javierherrera.system.bin.Producto;
import org.javierherrera.system.dao.Conexion;
import org.javierherrera.system.main.Main;

public class DetalleFacturaController implements Initializable {

    private ObservableList<DetalleFactura> listaDetalleFactura;
    private ObservableList<Factura> listaFactura;
    private ObservableList<Producto> listaProducto;
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
    private ComboBox<Producto> cmb_codigoProducto;

    @FXML
    private ComboBox<Factura> cmb_numeroDeFactura;

    @FXML
    private TableColumn<DetalleFactura, Integer> col_cantidad;

    @FXML
    private TableColumn<DetalleFactura, Integer> col_codigoDetalleFactura;

    @FXML
    private TableColumn<DetalleFactura, String> col_codigoProducto;

    @FXML
    private TableColumn<DetalleFactura, Integer> col_numeroDeFactura;

    @FXML
    private TableColumn<DetalleFactura, Double> col_precioUnitario;

    @FXML
    private TableView<DetalleFactura> tv_DetalleProducto;

    @FXML
    private TextField txt_cantidad;

    @FXML
    private TextField txt_precioUnitario;

    @FXML
    private TextField txt_codigoDetalleFactura;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmb_codigoProducto.setItems(getProducto());
        cmb_numeroDeFactura.setItems(getFactura());
    }

    public void cargarDatos() {
        tv_DetalleProducto.setItems(getDetalleFactura());
        col_codigoProducto.setCellValueFactory(new PropertyValueFactory<>("codigoProducto"));
        col_precioUnitario.setCellValueFactory(new PropertyValueFactory<>("precioUnitario"));
        col_cantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        col_codigoDetalleFactura.setCellValueFactory(new PropertyValueFactory<>("codigoDetalleFactura"));
        col_numeroDeFactura.setCellValueFactory(new PropertyValueFactory<>("numeroDeFactura"));
    }

    public ObservableList<DetalleFactura> getDetalleFactura() {
        ArrayList<DetalleFactura> lista = new ArrayList<>();
        try (Connection conexion = Conexion.getInstance().getConexion();
             PreparedStatement procedimiento = conexion.prepareCall("{CALL sp_listarDetallesFactura()}");
             ResultSet resultado = procedimiento.executeQuery()) {
            while (resultado.next()) {
                lista.add(new DetalleFactura(
                        resultado.getInt("codigoDetalleFactura"),
                        resultado.getDouble("precioUnitario"),
                        resultado.getInt("cantidad"),
                        resultado.getInt("numeroDeFactura"),
                        resultado.getString("codigoProducto")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaDetalleFactura = FXCollections.observableList(lista);
    }

    public ObservableList<Factura> getFactura() {
        ArrayList<Factura> lista = new ArrayList<>();
        try (Connection conexion = Conexion.getInstance().getConexion();
             PreparedStatement procedimiento = conexion.prepareCall("{CALL sp_listarFacturas()}");
             ResultSet resultado = procedimiento.executeQuery()) {
            while (resultado.next()) {
                lista.add(new Factura(
                        resultado.getInt("numeroDeFactura"),
                        resultado.getString("estado"),
                        resultado.getDouble("totalFactura"),
                        resultado.getString("fechaFactura"),
                        resultado.getInt("codigoCliente"),
                        resultado.getInt("codigoEmpleado")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaFactura = FXCollections.observableList(lista);
    }

    public ObservableList<Producto> getProducto() {
        ArrayList<Producto> lista = new ArrayList<>();
        try (Connection conexion = Conexion.getInstance().getConexion();
             PreparedStatement procedimiento = conexion.prepareCall("{CALL sp_listarProductos()}");
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaProducto = FXCollections.observableList(lista);
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
        DetalleFactura reg = new DetalleFactura(
                Integer.parseInt(txt_codigoDetalleFactura.getText()),
                Double.parseDouble(txt_precioUnitario.getText()),
                Integer.parseInt(txt_cantidad.getText()),
                ((Factura) cmb_numeroDeFactura.getSelectionModel().getSelectedItem()).getNumeroDeFactura(),
                ((Producto) cmb_codigoProducto.getSelectionModel().getSelectedItem()).getCodigoProducto()
        );

        try (Connection conexion = Conexion.getInstance().getConexion();
             PreparedStatement procedimiento = conexion.prepareCall("{CALL sp_crearDetalleFactura(?, ?, ?, ?, ?)}")) {
            procedimiento.setInt(1, reg.getCodigoDetalleFactura());
            procedimiento.setDouble(2, reg.getPrecioUnitario());
            procedimiento.setInt(3, reg.getCantidad());
            procedimiento.setInt(4, reg.getNumeroDeFactura());
            procedimiento.setString(5, reg.getCodigoProducto());
            procedimiento.execute();
            listaDetalleFactura.add(reg);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al guardar el producto: " + e.getMessage());
        }
    }

    public void seleccionar() {
        if (tv_DetalleProducto.getSelectionModel().getSelectedItem() != null) {
            DetalleFactura productoSeleccionado = tv_DetalleProducto.getSelectionModel().getSelectedItem();
            txt_codigoDetalleFactura.setText(String.valueOf(productoSeleccionado.getCodigoDetalleFactura()));
            txt_cantidad.setText(String.valueOf(productoSeleccionado.getCantidad()));
            txt_precioUnitario.setText(String.valueOf(productoSeleccionado.getPrecioUnitario()));
            cmb_codigoProducto.getSelectionModel().select(buscarCodigoProducto(productoSeleccionado.getCodigoProducto()));
            cmb_numeroDeFactura.getSelectionModel().select(buscarNumeroFactura(productoSeleccionado.getNumeroDeFactura()));
        }
    }

    public void editar() {
        switch (tipoDeOperaciones) {
            case NULL:
                if (tv_DetalleProducto.getSelectionModel().getSelectedItem() != null) {
                    DetalleFactura productoSeleccionado = tv_DetalleProducto.getSelectionModel().getSelectedItem();
                    txt_cantidad.setText(String.valueOf(productoSeleccionado.getCantidad()));
                    txt_precioUnitario.setText(String.valueOf(productoSeleccionado.getPrecioUnitario()));
                    txt_codigoDetalleFactura.setText(String.valueOf(productoSeleccionado.getCodigoDetalleFactura()));
                    cmb_codigoProducto.getSelectionModel().select(buscarCodigoProducto(productoSeleccionado.getCodigoProducto()));
                    cmb_numeroDeFactura.getSelectionModel().select(buscarNumeroFactura(productoSeleccionado.getNumeroDeFactura()));

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
        DetalleFactura productoSeleccionado = tv_DetalleProducto.getSelectionModel().getSelectedItem();
        productoSeleccionado.setCantidad(Integer.parseInt(txt_cantidad.getText()));
        productoSeleccionado.setPrecioUnitario(Double.parseDouble(txt_precioUnitario.getText()));
        productoSeleccionado.setCodigoDetalleFactura(Integer.parseInt(txt_codigoDetalleFactura.getText()));
        productoSeleccionado.setCodigoProducto(((Producto) cmb_codigoProducto.getSelectionModel().getSelectedItem()).getCodigoProducto());
        productoSeleccionado.setNumeroDeFactura(((Factura) cmb_numeroDeFactura.getSelectionModel().getSelectedItem()).getNumeroDeFactura());

        try (Connection conexion = Conexion.getInstance().getConexion();
             PreparedStatement procedimiento = conexion.prepareCall("{CALL sp_actualizarDetalleFactura(?, ?, ?, ?, ?)}")) {
            procedimiento.setInt(1, productoSeleccionado.getCodigoDetalleFactura());
            procedimiento.setDouble(2, productoSeleccionado.getPrecioUnitario());
            procedimiento.setInt(3, productoSeleccionado.getCantidad());
            procedimiento.setInt(4, productoSeleccionado.getNumeroDeFactura());
            procedimiento.setString(5, productoSeleccionado.getCodigoProducto());
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
            if (tv_DetalleProducto.getSelectionModel().getSelectedItem() != null) {
                int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar Producto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (respuesta == JOptionPane.YES_OPTION) {
                    try (Connection conexion = Conexion.getInstance().getConexion();
                         PreparedStatement procedimiento = conexion.prepareCall("{CALL sp_eliminarDetalleFactura(?)}")) {
                        procedimiento.setInt(1, tv_DetalleProducto.getSelectionModel().getSelectedItem().getCodigoDetalleFactura());
                        procedimiento.execute();
                        listaDetalleFactura.remove(tv_DetalleProducto.getSelectionModel().getSelectedIndex());
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
        txt_cantidad.setEditable(true);
        txt_precioUnitario.setEditable(true);
        txt_codigoDetalleFactura.setEditable(true);
        cmb_codigoProducto.setDisable(false);
        cmb_numeroDeFactura.setDisable(false);
    }

    public void desactivarControles() {
        txt_cantidad.setEditable(false);
        txt_codigoDetalleFactura.setEditable(false);
        txt_precioUnitario.setEditable(false);
        cmb_codigoProducto.setDisable(true);
        cmb_numeroDeFactura.setDisable(true);
    }

    public void limpiarControles() {
        txt_cantidad.clear();
        txt_codigoDetalleFactura.clear();
        txt_precioUnitario.clear();
        cmb_codigoProducto.getSelectionModel().clearSelection();
        cmb_numeroDeFactura.getSelectionModel().clearSelection();
    }

    public void reportes() {
        // Implement report generation functionality here
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

    private Producto buscarCodigoProducto(String codigoProducto) {
        for (Producto producto : listaProducto) {
            if (producto.getCodigoProducto().equals(codigoProducto)) {
                return producto;
            }
        }
        return null;
    }

    private Factura buscarNumeroFactura(int numeroDeFactura) {
        for (Factura factura : listaFactura) {
            if (factura.getNumeroDeFactura() == numeroDeFactura) {
                return factura;
            }
        }
        return null;
    }
}