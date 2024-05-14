/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package org.javierherrera.system.Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import org.javierherrera.system.main.Main;

/**
 *
 * @author Javier
 */
public class MainFrameController implements Initializable {
    private Main escenarioPrincipal;

    @FXML
    MenuItem btn_Clientes;
    @FXML
    MenuItem btnProgramador;
    @FXML
    MenuItem btn_aboutUs;
    @FXML
    MenuItem btn_TipoDeProductos;
    @FXML 
    MenuItem btn_Compras;
    @FXML 
    MenuItem btn_CargoEmpleado;
    @FXML
    MenuItem btn_Proveedores;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public MenuItem getBtn_clientes() {
        return btn_Clientes;
    }

    public void setBtn_clientes(MenuItem btn_clientes) {
        this.btn_Clientes = btn_clientes;
    }

    public MenuItem getBtnProgramador() {
        return btnProgramador;
    }

    public void setBtnProgramador(MenuItem btnProgramador) {
        this.btnProgramador = btnProgramador;
    }

    @FXML
    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btn_Clientes) {
            escenarioPrincipal.menuClienteView();
        }
        if (event.getSource() == btn_aboutUs) {
            escenarioPrincipal.AboutUsView();
        }
        if (event.getSource() == btn_TipoDeProductos) {
            escenarioPrincipal.TipoDeProductoView();
        }
        if (event.getSource() == btn_Compras) {
            escenarioPrincipal.comprasView();
        }
        if (event.getSource() == btn_CargoEmpleado) {
            escenarioPrincipal.cargoEmpleadoView();
        }
        if (event.getSource() == btn_Proveedores) {
            escenarioPrincipal.Proveedor();
        }
    }
}
