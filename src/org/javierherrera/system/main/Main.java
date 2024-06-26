/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML.java to edit this template
 */
package org.javierherrera.system.main;

import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.javierherrera.system.Controller.AboutUsController;
import org.javierherrera.system.Controller.CargoEmpleadoController;
import org.javierherrera.system.Controller.ClienteController;
import org.javierherrera.system.Controller.ComprasController;
import org.javierherrera.system.Controller.DetalleFacturaController;
import org.javierherrera.system.Controller.DetalleProductoController;
import org.javierherrera.system.Controller.EmpleadoController;
import org.javierherrera.system.Controller.ProveedorController;
import org.javierherrera.system.Controller.MainFrameController;
import org.javierherrera.system.Controller.ProductoController;
import org.javierherrera.system.Controller.TipoDeProductoController;
import org.javierherrera.system.bin.Factura;

/**
 *
 * @author Javier
 */
public class Main extends Application {

    private Stage escenarioPrincipal;
    private Scene escena;

    @Override
    public void start(Stage escenarioPrincipal) throws Exception {

        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("Kinal Shop");
        MainMenuView();
        escenarioPrincipal.show();

        // Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        // Scene scene = new Scene(root);

    }

    public Initializable cambiarEscena(String fxmlname, double width, double height) throws Exception {
        Initializable resultado;
        FXMLLoader loader = new FXMLLoader();
        InputStream file = Main.class.getResourceAsStream("/org/javierherrera/system/View/" + fxmlname);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Main.class.getResource("/org/javierherrera/system/View/" + fxmlname));

        escena = new Scene((Parent) loader.load(file), width, height);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable) loader.getController();

        return resultado;
    }

    public void MainMenuView() {
        try {
            MainFrameController mainFrame;
            mainFrame = (MainFrameController) cambiarEscena("MainFrame.fxml", 479.0, 481.0);
            mainFrame.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void menuClienteView() {
    try {
        ClienteController clienteControllerView = (ClienteController) cambiarEscena("ClienteFrame.fxml", 837.0,
                464.0);
        clienteControllerView.setEscenarioPrincipal(this);
    } catch (Exception e) {
        e.printStackTrace();
    }
}


    public void AboutUsView() {
        try {
            AboutUsController aboutUsView = (AboutUsController) cambiarEscena("AboutUs.fxml", 698.0, 398.0);
            aboutUsView.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void TipoDeProductoView() {
        try {
            TipoDeProductoController tipoDeProducto = (TipoDeProductoController) cambiarEscena(
                    "TipoDeProductoFrame.fxml", 837.0, 464.0);
            tipoDeProducto.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void comprasView() {
        try {
            ComprasController compras = (ComprasController) cambiarEscena("ComprasFrame.fxml", 837.0,464.0);
            compras.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void cargoEmpleadoView() {
        try {
            CargoEmpleadoController cargo = (CargoEmpleadoController) cambiarEscena("CargoEmpleado.fxml", 837.0,464.0);
            cargo.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void Proveedor() {
        try {
            ProveedorController prov = (ProveedorController) cambiarEscena("ProveedorFrame.fxml", 1070.0,590.0);
            prov.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void Productos() {
        try {
            ProductoController prod = (ProductoController) cambiarEscena("ProductoFrame.fxml", 979.0,558.0);
            prod.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void DetalleProductos() {
        try {
            DetalleProductoController prod = (DetalleProductoController) cambiarEscena("DetalleProducto.fxml", 979.0,558.0);
            prod.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void Empleado() {
        try {
            EmpleadoController prod = (EmpleadoController) cambiarEscena("EmpleadosFrame.fxml", 979.0,558.0);
            prod.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void Factura() {
        try {
            // Asegúrate de que cambiarEscena devuelva un objeto del tipo Factura
            Object obj = cambiarEscena("FacturaFrame.fxml", 979.0, 558.0);
            if (obj instanceof Factura) {
                Factura factura = (Factura) obj;
                factura.setEscenarioPrincipal(this);
            } else {
                System.err.println("Error: El objeto devuelto no es una instancia de Factura.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void DetalleFactura() {
        try {
            DetalleFacturaController detalleFac = (DetalleFacturaController) cambiarEscena("DetalleFactura.fxml", 979.0,558.0);
            detalleFac.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    public static void main(String[] args) {
        launch(args);
    }

}
