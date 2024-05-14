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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.javierherrera.system.bin.Compras;
import org.javierherrera.system.dao.Conexion;
import org.javierherrera.system.main.Main;

/**
 *
 * @author Javier
 */
public class ComprasController implements Initializable {

    private ObservableList<Compras> ListaCompras;
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
    private TableColumn col_descripcion;

    @FXML
    private TableColumn col_fechaDocumento;

    @FXML
    private TableColumn col_numeroDocumento;

    @FXML
    private TableColumn col_totalDocumento;

    @FXML
    private DatePicker datePickerDocumento;

    @FXML
    private TableView tv_Compras;

    @FXML
    private TextField txt_Desc;

    @FXML
    private TextField txt_Total;

    @FXML
    private TextField txt_numDocumento;
    
    @FXML 
    private Button btn_Volver;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
    }

    public void cargarDatos() {
        tv_Compras.setItems(getCompras());
        col_numeroDocumento.setCellValueFactory(new PropertyValueFactory<Compras, Integer>("numeroDocumento"));
        col_fechaDocumento.setCellValueFactory(new PropertyValueFactory<Compras, String>("fechaDocumento"));
        col_descripcion.setCellValueFactory(new PropertyValueFactory<Compras, String>("descripcion"));
        col_totalDocumento.setCellValueFactory(new PropertyValueFactory<Compras, Double>("totalDocumento"));

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

    @FXML
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
                try {
                    guardar();
                    desactivarControles();
                    cargarDatos();
                    limpiarControles();
                    btn_agregarC.setText("Agregar");
                    btn_EliminarC.setText("Eliminar");
                    btn_editarC.setDisable(false);
                    btn_reportesC.setDisable(false);
                    tipoDeOperaciones = operaciones.NULL;
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Error al guardar los datos: " + e.getMessage());
                }
                break;
        }
    }

    public void guardar() throws SQLException {
    Compras reg = new Compras();
    reg.setNumeroDocumento(Integer.parseInt(txt_numDocumento.getText()));
    LocalDate date = datePickerDocumento.getValue();
    String dateFormat = date.toString();
    reg.setFechaDocumento(dateFormat);
    reg.setDescripcion(txt_Desc.getText());
    reg.setTotalDocumento(Double.parseDouble(txt_Total.getText()));
    try (PreparedStatement procedimiento = Conexion.getInstance().getConexion()
            .prepareCall("{CALL sp_crearCompras(?, ?, ?, ?)}")) {
        procedimiento.setInt(1, reg.getNumeroDocumento());
        procedimiento.setString(2, reg.getFechaDocumento());
        procedimiento.setString(3, reg.getDescripcion());
        procedimiento.setDouble(4, reg.getTotalDocumento());
        procedimiento.execute();
    }
    ListaCompras.add(reg);
}

    public void actualizar() throws SQLException {
        Compras reg = new Compras();
        reg.setNumeroDocumento(Integer.parseInt(txt_numDocumento.getText()));

        LocalDate date = datePickerDocumento.getValue();
        String dateFormat = date.toString();

        reg.setFechaDocumento(dateFormat);
        reg.setDescripcion(txt_Desc.getText());
        reg.setTotalDocumento(Double.parseDouble(txt_Total.getText()));

        try (PreparedStatement procedimiento = Conexion.getInstance().getConexion()
                .prepareCall("{CALL sp_actualizarCompra(?, ?, ?, ?)}")) {
            procedimiento.setInt(1, reg.getNumeroDocumento());
            procedimiento.setString(2, reg.getFechaDocumento());
            procedimiento.setString(3, reg.getDescripcion());
            procedimiento.setDouble(4, reg.getTotalDocumento());
            procedimiento.execute();
        }
    }


    public void editar() throws Exception {
        switch (tipoDeOperaciones) {
            case NULL:
                if (tv_Compras.getSelectionModel().getSelectedItem() != null) {
                    btn_editarC.setText("Actualizar");
                    btn_reportesC.setText("Cancelar");
                    btn_EliminarC.setDisable(true);
                    btn_agregarC.setDisable(true);
                    tipoDeOperaciones = operaciones.NULL;
                    activarControles();
                    txt_numDocumento.setEditable(false);
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

  
    
    public void seleccionar() {
    Compras seleccionado = (Compras) tv_Compras.getSelectionModel().getSelectedItem();
    if (seleccionado != null) {
        txt_numDocumento.setText(String.valueOf(seleccionado.getNumeroDocumento()));
        String date = seleccionado.getFechaDocumento();
        datePickerDocumento.setValue(LocalDate.parse(date));
        txt_Desc.setText(seleccionado.getDescripcion());
        txt_Total.setText(String.valueOf(seleccionado.getTotalDocumento()));
    } else {
        JOptionPane.showMessageDialog(null, "No se ha seleccionado ningún registro.");
    }
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
                if (tv_Compras.getSelectionModel().getSelectedItem() != null) {
                    int ans = JOptionPane.showConfirmDialog(null, "Confirma esta Accion", "Verificacion",
                            JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (ans == JOptionPane.YES_NO_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion()
                                    .prepareCall("{CALL sp_eliminarCompra(?)}");
                            procedimiento.setInt(1,
                                    ((Compras) tv_Compras.getSelectionModel().getSelectedItem()).getNumeroDocumento());
                            procedimiento.execute();
                            ListaCompras.remove(tv_Compras.getSelectionModel().getSelectedItem());
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
        txt_numDocumento.setEditable(false);
        datePickerDocumento.setEditable(false);
        txt_Desc.setEditable(false);
        txt_Total.setEditable(false);
    }

    public void activarControles() {
        txt_numDocumento.setEditable(true);
        datePickerDocumento.setEditable(true);
        txt_Desc.setEditable(true);
        txt_Total.setEditable(true);
    }

    public void limpiarControles() {
    txt_numDocumento.clear();
    datePickerDocumento.setValue(null);
    txt_Desc.clear();
    txt_Total.clear();
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
