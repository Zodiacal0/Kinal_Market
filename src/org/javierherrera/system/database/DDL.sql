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

create table proveedores(
	codigoProveedor int,
    NITProveedor varchar(13),
    nombresProveedor varchar(60),
    apellidosProveedor varchar (60),
    direccionProveedor varchar (150),
    razonSocial varchar (60),
    contactoPrincipal varchar (100),
    paginaWeb varchar(50),
    telefonoProveedor varchar (13),
    emailProveedor varchar(50),
    primary key PK_codigoProveedor(codigoProveedor)
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
FOREIGN KEY (codigoTipoProducto) REFERENCES TipoProducto(codigoTipoProducto) on delete cascade,
FOREIGN KEY (codigoProveedor) REFERENCES Proveedores(codigoProveedor)on delete cascade
    
);

CREATE TABLE DetalleCompra(

codigoDetalleCompra int,
costoUnitario decimal(10,2),
cantidad int,
codigoProducto varchar(15),
numeroDocumento int,
PRIMARY KEY PK_codigoDetalleCompra (codigoDetalleCompra),
FOREIGN KEY (codigoProducto) REFERENCES Productos(codigoProducto)on delete cascade,
FOREIGN KEY (numeroDocumento) REFERENCES Compras(numeroDocumento)on delete cascade
    
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
FOREIGN KEY (codigoCliente) REFERENCES Cliente(codigoCliente)on delete cascade,
FOREIGN KEY (codigoEmpleado) REFERENCES Empleados(codigoEmpleado)on delete cascade

);

CREATE TABLE DetalleFactura(

codigoDetalleFactura int,
precioUnitario decimal(10,2),
cantidad int,
numeroDeFactura int,
codigoProducto varchar(15),
PRIMARY KEY PK_codigoDetalleFactura (codigoDetalleFactura),
FOREIGN KEY (numeroDeFactura) REFERENCES Factura(numeroDeFactura)on delete cascade,
FOREIGN KEY (codigoProducto) REFERENCES Productos(codigoProducto)on delete cascade

);

