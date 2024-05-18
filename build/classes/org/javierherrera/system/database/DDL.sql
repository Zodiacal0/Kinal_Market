DROP DATABASE IF EXISTS DB_KinalShop;
CREATE DATABASE DB_KinalShop;
use DB_KinalShop;

set global time_zone = '-6:00';

CREATE TABLE Cliente(
    
    codigoCliente int,
    nitCliente varchar(10),
    nombreCliente varchar(50),
    apellidoCliente varchar(50),
    direccionCliente varchar(150),
    telefonoCliente varchar(45),
    correoCliente varchar(45),
    PRIMARY KEY PK_codigoCliente (codigoCliente)

)Engine InnoDB;

CREATE TABLE TipoProducto(
	
    codigoTipoProducto int,
    descripcion varchar(45),
    PRIMARY KEY PK_codigoTipoProducto (codigoTipoProducto)
    
)Engine InnoDB;

CREATE TABLE Compras(

	numeroDocumento int,
    fechaDocumento date,
    descripcion varchar(60),
    totalDocumento decimal(10,2),
    PRIMARY KEY PK_numeroDocumento (numeroDocumento)

)Engine InnoDB;

CREATE TABLE CargoEmpleado(

	codigoCargoEmpleado int,
    nombreCargo varchar(45),
    descripcionCargo varchar(45),
    PRIMARY KEY PK_codigoCargoEmpleado (codigoCargoEmpleado)

)Engine InnoDB;

CREATE TABLE Proveedores(

codigoProveedor int,
nitProveedor varchar(10),
nombreProveedor varchar(60),
apellidosProveedor varchar(60),
direccionProveedor varchar(150),
razonSocial varchar(60),
contactoPrincipal varchar(100),
paginaWeb varchar(50),
PRIMARY KEY PK_codigoProveedor (codigoProveedor)

)Engine InnoDB;

CREATE TABLE TelefonoProveedor(

codigoTelefonoProveedor int,
numeroPincipal varchar(8),
numeroSecundario varchar(8),
observaciones varchar(45),
codigoProveedor int,
PRIMARY KEY PK_codigoTelefonoProveedor (codigoTelefonoProveedor),
FOREIGN KEY (codigoProveedor) REFERENCES Proveedores (codigoProveedor)

);

CREATE TABLE EmailProveedor(

codigoEmailProveedor int,
emailproveedor varchar(50),
descripcion varchar(100),
codigoProveedor int,
PRIMARY KEY PK_codigoEmailProveedor (codigoEmailProveedor),
FOREIGN KEY (codigoProveedor) REFERENCES Proveedores (codigoProveedor)

);

CREATE TABLE Productos(
		
codigoProducto varchar(15),
descripcionProducto varchar(15),
precioUnitario decimal(10,2),
precioDocena decimal(10,2),
precioMayor decimal(10,2),
-- imagenProducto varchar(45),
existencia int,
codigoTipoProducto int,
codigoProveedor int,
PRIMARY KEY PK_codigoProducto (codigoProducto),
FOREIGN KEY (codigoTipoProducto) REFERENCES TipoProducto(codigoTipoProducto),
FOREIGN KEY (codigoProveedor) REFERENCES Proveedores(codigoProveedor)
    
);

CREATE TABLE DetalleCompra(

codigoDetalleCompra int,
costoUnitario decimal(10,2),
cantidad int,
codigoProducto varchar(15),
numeroDocumento int,
PRIMARY KEY PK_codigoDetalleCompra (codigoDetalleCompra),
FOREIGN KEY (codigoProducto) REFERENCES Productos(codigoProducto),
FOREIGN KEY (numeroDocumento) REFERENCES Compras(numeroDocumento)
    
);

CREATE TABLE Empleados(

codigoEmpleado int,
nombresEmpleado varchar(50),
apellidosEmpleado varchar(50),
sueldo decimal(10,2),
direccion varchar(150),
turno varchar(15),
codigoCargoEmpleado int,
PRIMARY KEY PK_codigoEmpleado (codigoEmpleado),
FOREIGN KEY (codigoCargoEmpleado) REFERENCES CargoEmpleado(codigoCargoEmpleado)

);

CREATE TABLE Factura(

numeroDeFactura int,
estado varchar(50),
totalFactura decimal(10,2),
fechaFactura varchar(45),
codigoCliente int,
codigoEmpleado int,
PRIMARY KEY PK_numeroDeFactura (numeroDeFactura),
FOREIGN KEY (codigoCliente) REFERENCES Cliente(codigoCliente),
FOREIGN KEY (codigoEmpleado) REFERENCES Empleados(codigoEmpleado)

);

CREATE TABLE DetalleFactura(

codigoDetalleFactura int,
precioUnitario decimal(10,2),
cantidad int,
numeroDeFactura int,
codigoProducto varchar(15),
PRIMARY KEY PK_codigoDetalleFactura (codigoDetalleFactura),
FOREIGN KEY (numeroDeFactura) REFERENCES Factura(numeroDeFactura),
FOREIGN KEY (codigoProducto) REFERENCES Productos(codigoProducto)

);

