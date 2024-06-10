use DB_KinalShop	;

DELIMITER $$
CREATE PROCEDURE sp_crearCliente(in codigoCliente int, in nitCliente varchar(10), in nombreCliente varchar(50), in apellidoCliente varchar (50), in direccionCliente varchar(150), in telefonoCliente varchar(45), in correoCliente varchar(45))
BEGIN

INSERT INTO Cliente(codigoCliente,nitCliente,nombreCliente,apellidoCliente,direccionCliente,telefonoCliente,correoCliente)
	VALUES(codigoCliente,nitCliente,nombreCliente,apellidoCliente,direccionCliente,telefonoCliente,correoCliente);

END$$
DELIMITER ;

call sp_crearCliente('1', '12456789-9', 'Rodrigo', 'Herrera','pamplona', '32547654', 'rh@gmail.com');

DELIMITER $$
CREATE PROCEDURE sp_listarCliente()
BEGIN

	SELECT * FROM Cliente; 

END$$
DELIMITER ;

CALL sp_listarCliente();

DELIMITER $$
CREATE PROCEDURE sp_actualizarCliente(in codigoCliente int, in nitClienteActualizado varchar(10), in nombreClienteActualizado varchar(50), in apellidoClienteActualizado varchar (50), in direccionClienteActualizado varchar(150), in telefonoClienteActualizado varchar(45), in correoClienteActualizado varchar(45))
BEGIN

	UPDATE Cliente
    SET nitCliente = nitClienteActualizado, nombreCliente = nombreClienteActualizado, apellidoCliente = apellidoClienteActualizado, direccionCliente = direccionClienteActualizado, telefonoCliente = telefonoClienteActualizado, correoCliente = correoClienteActualizado
    WHERE codigoCliente = codigoCliente;

END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_eliminarCliente(in codigoCliente int)
BEGIN

	DELETE FROM Cliente
    WHERE codigoCliente = codigoCliente;

END$$
DELIMITER ;

CALL sp_eliminarCliente(1);

-- CRUD DE TipoProducto

DELIMITER $$
CREATE PROCEDURE sp_crearTipoProducto(in codigoTipoProducto int, in descripcion varchar(15))
BEGIN

	INSERT INTO TipoProducto(codigoTipoProducto,descripcion)
		VALUES(codigoTipoProducto,descripcion);

END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_listarTipoProducto()
BEGIN

	SELECT * FROM TipoProducto;

END$$
DELIMITER ;

DELIMITER $$

CREATE PROCEDURE sp_actualizarTipoProducto(
    IN p_codigoTipoProducto INT,
    IN p_nuevaDescripcion VARCHAR(45)
)
BEGIN
    UPDATE TipoProducto
    SET descripcion = p_nuevaDescripcion
    WHERE codigoTipoProducto = p_codigoTipoProducto;
END$$

DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_eliminarTipoProducto(in codigoTipoProducto int)
BEGIN

	DELETE FROM TipoProducto
    WHERE codigoTipoProducto = codigoTipoProducto;

END$$
DELIMITER ;

CALL sp_eliminarTipoProducto(1);

DELIMITER $$
CREATE PROCEDURE sp_buscarTipoDeProducto(
	IN codigoTipoProducto INT
)
BEGIN

	SELECT * FROM TipoProducto
    WHERE codigoTipoProducto = codigoTipoProducto;

END$$
DELIMITER ;

CALL sp_buscarTipoDeProducto(1234);


DELIMITER $$
CREATE PROCEDURE sp_crearCompras(
    IN numeroDocumento INT,
    IN fechaDocumento DATE,
    IN descripcion VARCHAR(60),
    IN totalDocumento DECIMAL(10,2)
)
BEGIN
    INSERT INTO Compras(numeroDocumento, fechaDocumento, descripcion, totalDocumento)
    VALUES(numeroDocumento, fechaDocumento, descripcion, totalDocumento);
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_listarCompras()
BEGIN
    SELECT * FROM Compras;
END$$
DELIMITER ;

CALL sp_listarCompras();

DELIMITER $$
CREATE PROCEDURE sp_buscarCompras(IN numeroDocumento INT)
BEGIN
    SELECT * FROM Compras
    WHERE numeroDocumento;
END$$
DELIMITER ;

CALL sp_buscarCompras(1234);

DELIMITER $$
CREATE PROCEDURE sp_actualizarCompra(
    IN numeroDocumento INT,
    IN nuevaFechaDocumento DATE,
    IN nuevaDescripcion VARCHAR(60),
    IN nuevoTotalDocumento DECIMAL(10,2)
)
BEGIN
    UPDATE Compras
    SET fechaDocumento = nuevaFechaDocumento,
        descripcion = nuevaDescripcion,
        totalDocumento = nuevoTotalDocumento
    WHERE numeroDocumento = numeroDocumento;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_eliminarCompra(
    IN numeroDocumento INT
)
BEGIN
    DELETE FROM Compras
    WHERE numeroDocumento = numeroDocumento;
END$$
DELIMITER ;

CALL sp_eliminarCompra(1234);

DELIMITER $$

CREATE PROCEDURE sp_crearCargoEmpleado(
    IN p_codigoCargoEmpleado INT,
    IN p_nombreCargo VARCHAR(45),
    IN p_descripcionCargo VARCHAR(45)
)
BEGIN
    INSERT INTO CargoEmpleado(codigoCargoEmpleado, nombreCargo, descripcionCargo)
    VALUES(p_codigoCargoEmpleado, p_nombreCargo, p_descripcionCargo);
END$$

CREATE PROCEDURE sp_listarCargoEmpleado()
BEGIN
    SELECT * FROM CargoEmpleado;
END$$

CALL sp_listarCargoEmpleado();

CREATE PROCEDURE sp_actualizarCargoEmpleado(
    IN p_codigoCargoEmpleado INT,
    IN p_nuevoNombreCargo VARCHAR(45),
    IN p_nuevaDescripcionCargo VARCHAR(45)
)
BEGIN
    UPDATE CargoEmpleado
    SET nombreCargo = p_nuevoNombreCargo,
        descripcionCargo = p_nuevaDescripcionCargo
    WHERE codigoCargoEmpleado = p_codigoCargoEmpleado;
END$$

CREATE PROCEDURE sp_eliminarCargoEmpleado(
    IN p_codigoCargoEmpleado INT
)
BEGIN
    DELETE FROM CargoEmpleado
    WHERE codigoCargoEmpleado = p_codigoCargoEmpleado;
END$$
DELIMITER ;

CALL sp_eliminarCargoEmpleado(1234);

DELIMITER $$

CREATE PROCEDURE sp_crearProveedor(
    IN p_codigoProveedor INT,
    IN p_NITProveedor VARCHAR(13),
    IN p_nombresProveedor VARCHAR(60),
    IN p_apellidosProveedor VARCHAR(60),
    IN p_direccionProveedor VARCHAR(150),
    IN p_razonSocial VARCHAR(60),
    IN p_contactoPrincipal VARCHAR(100),
    IN p_paginaWeb VARCHAR(50),
    IN p_telefonoProveedor VARCHAR(13),
    IN p_emailProveedor VARCHAR(50)
)
BEGIN
    INSERT INTO proveedores (
        codigoProveedor,
        NITProveedor,
        nombresProveedor,
        apellidosProveedor,
        direccionProveedor,
        razonSocial,
        contactoPrincipal,
        paginaWeb,
        telefonoProveedor,
        emailProveedor
    ) VALUES (
        p_codigoProveedor,
        p_NITProveedor,
        p_nombresProveedor,
        p_apellidosProveedor,
        p_direccionProveedor,
        p_razonSocial,
        p_contactoPrincipal,
        p_paginaWeb,
        p_telefonoProveedor,
        p_emailProveedor
    );
END$$

DELIMITER ;


DELIMITER $$

CREATE PROCEDURE sp_listarProveedores()
BEGIN
    SELECT * FROM proveedores;
END$$

DELIMITER ;


DELIMITER $$

CREATE PROCEDURE sp_actualizarProveedor(
    IN p_codigoProveedor INT,
    IN p_nuevosNombresProveedor VARCHAR(60),
    IN p_nuevosApellidosProveedor VARCHAR(60),
    IN p_nuevaDireccionProveedor VARCHAR(150),
    IN p_nuevaRazonSocial VARCHAR(60),
    IN p_nuevoContactoPrincipal VARCHAR(100),
    IN p_nuevaPaginaWeb VARCHAR(50),
    IN p_nuevoTelefonoProveedor VARCHAR(13),
    IN p_nuevoEmailProveedor VARCHAR(50)
)
BEGIN
    UPDATE proveedores
    SET nombresProveedor = p_nuevosNombresProveedor,
        apellidosProveedor = p_nuevosApellidosProveedor,
        direccionProveedor = p_nuevaDireccionProveedor,
        razonSocial = p_nuevaRazonSocial,
        contactoPrincipal = p_nuevoContactoPrincipal,
        paginaWeb = p_nuevaPaginaWeb,
        telefonoProveedor = p_nuevoTelefonoProveedor,
        emailProveedor = p_nuevoEmailProveedor
    WHERE codigoProveedor = p_codigoProveedor;
    
END$$

DELIMITER ;

DELIMITER $$

CREATE PROCEDURE sp_eliminarProveedor(
    IN p_codigoProveedor INT
)
BEGIN
    DELETE FROM proveedores WHERE codigoProveedor = p_codigoProveedor;
END$$

DELIMITER ;


DELIMITER $$
CREATE PROCEDURE sp_buscarProveedor(
	IN codigoProveedor INT
)
BEGIN

	SELECT * FROM Proveedores
    WHERE codigoProveedor = codigoProveedor;
END$$
DELIMITER ;

CALL sp_buscarProveedor(1);

-- CRUD de Productos

DELIMITER $$

CREATE PROCEDURE sp_crearProducto(
    IN p_codigoProducto VARCHAR(15),
    IN p_descripcionProducto VARCHAR(15),
    IN p_precioUnitario DECIMAL(10,2),
    IN p_precioDocena DECIMAL(10,2),
    IN p_precioMayor DECIMAL(10,2),
    -- IN p_imagenProducto VARCHAR(45),
    IN p_existencia INT,
    IN p_codigoTipoProducto INT,
    IN p_codigoProveedor INT
)
BEGIN
    INSERT INTO Productos(codigoProducto, descripcionProducto, precioUnitario, precioDocena, precioMayor,existencia, codigoTipoProducto, codigoProveedor)
    VALUES(p_codigoProducto, p_descripcionProducto, p_precioUnitario, p_precioDocena, p_precioMayor, p_existencia, p_codigoTipoProducto, p_codigoProveedor);
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_listarProductos()
BEGIN
    SELECT * FROM Productos;
END$$
DELIMITER ;

CALL sp_listarProductos();

DELIMITER $$
CREATE PROCEDURE sp_actualizarProducto(
    IN p_codigoProducto VARCHAR(15),
    IN p_nuevaDescripcionProducto VARCHAR(15),
    IN p_nuevoPrecioUnitario DECIMAL(10,2),
    IN p_nuevoPrecioDocena DECIMAL(10,2),
    IN p_nuevoPrecioMayor DECIMAL(10,2),
    -- IN p_nuevaImagenProducto VARCHAR(45),
    IN p_nuevaExistencia INT,
    IN p_nuevoCodigoTipoProducto INT,
    IN p_nuevoCodigoProveedor INT
)
BEGIN
    UPDATE Productos
    SET descripcionProducto = p_nuevaDescripcionProducto,
        precioUnitario = p_nuevoPrecioUnitario,
        precioDocena = p_nuevoPrecioDocena,
        precioMayor = p_nuevoPrecioMayor,
        -- imagenProducto = p_nuevaImagenProducto,
        existencia = p_nuevaExistencia,
        codigoTipoProducto = p_nuevoCodigoTipoProducto,
        codigoProveedor = p_nuevoCodigoProveedor
    WHERE codigoProducto = p_codigoProducto;
END$$
DELIMITER ;


DELIMITER $$
CREATE PROCEDURE sp_eliminarProducto(
    IN codigoProducto VARCHAR(15)
)
BEGIN
    DELETE FROM Productos
    WHERE codigoProducto = codigoProducto;
END$$
DELIMITER ;


-- CRUD de DetalleCompra 
DELIMITER $$

CREATE PROCEDURE sp_crearDetalleCompra(
    IN p_codigoDetalleCompra INT,
    IN p_costoUnitario DECIMAL(10,2),
    IN p_cantidad INT,
    IN p_codigoProducto VARCHAR(15),
    IN p_numeroDocumento INT
)
BEGIN
    INSERT INTO DetalleCompra(codigoDetalleCompra, costoUnitario, cantidad, codigoProducto, numeroDocumento)
    VALUES(p_codigoDetalleCompra, p_costoUnitario, p_cantidad, p_codigoProducto, p_numeroDocumento);
END$$

CREATE PROCEDURE sp_listarDetallesCompra()
BEGIN
    SELECT * FROM DetalleCompra;
END$$

CREATE PROCEDURE sp_actualizarDetalleCompra(
    IN p_codigoDetalleCompra INT,
    IN p_nuevoCostoUnitario DECIMAL(10,2),
    IN p_nuevaCantidad INT,
    IN p_nuevoCodigoProducto VARCHAR(15),
    IN p_nuevoNumeroDocumento INT
)
BEGIN
    UPDATE DetalleCompra
    SET costoUnitario = p_nuevoCostoUnitario,
        cantidad = p_nuevaCantidad,
        codigoProducto = p_nuevoCodigoProducto,
        numeroDocumento = p_nuevoNumeroDocumento
    WHERE codigoDetalleCompra = p_codigoDetalleCompra;
END$$




DELIMITER $$

CREATE PROCEDURE sp_eliminarDetalleCompra(
    IN p_codigoDetalleCompra INT
)
BEGIN
    DELETE FROM DetalleCompra
    WHERE codigoDetalleCompra = p_codigoDetalleCompra;
END$$

DELIMITER ;

-- CRUD de Empleados

DELIMITER $$

CREATE PROCEDURE sp_crearEmpleado(
    IN p_codigoEmpleado INT,
    IN p_nombresEmpleado VARCHAR(50),
    IN p_apellidosEmpleado VARCHAR(50),
    IN p_sueldo DECIMAL(10,2),
    IN p_direccion VARCHAR(150),
    IN p_turno VARCHAR(15),
    IN p_codigoCargoEmpleado INT
)
BEGIN
    INSERT INTO Empleados(codigoEmpleado, nombresEmpleado, apellidosEmpleado, sueldo, direccion, turno, codigoCargoEmpleado)
    VALUES(p_codigoEmpleado, p_nombresEmpleado, p_apellidosEmpleado, p_sueldo, p_direccion, p_turno, p_codigoCargoEmpleado);
END$$

CREATE PROCEDURE sp_listarEmpleados()
BEGIN
    SELECT * FROM Empleados;
END$$

CREATE PROCEDURE sp_actualizarEmpleado(
    IN p_codigoEmpleado INT,
    IN p_nuevosNombresEmpleado VARCHAR(50),
    IN p_nuevosApellidosEmpleado VARCHAR(50),
    IN p_nuevoSueldo DECIMAL(10,2),
    IN p_nuevaDireccion VARCHAR(150),
    IN p_nuevoTurno VARCHAR(15),
    IN p_nuevoCodigoCargoEmpleado INT
)
BEGIN
    UPDATE Empleados
    SET nombresEmpleado = p_nuevosNombresEmpleado,
        apellidosEmpleado = p_nuevosApellidosEmpleado,
        sueldo = p_nuevoSueldo,
        direccion = p_nuevaDireccion,
        turno = p_nuevoTurno,
        codigoCargoEmpleado = p_nuevoCodigoCargoEmpleado
    WHERE codigoEmpleado = p_codigoEmpleado;
END$$

CREATE PROCEDURE sp_eliminarEmpleado(
    IN p_codigoEmpleado INT
)
BEGIN
    DELETE FROM Empleados
    WHERE codigoEmpleado = p_codigoEmpleado;
END$$

DELIMITER ;

-- CRUD de Factura
DELIMITER $$

CREATE PROCEDURE sp_crearFactura(
    IN p_numeroDeFactura INT,
    IN p_estado VARCHAR(50),
    IN p_totalFactura DECIMAL(10,2),
    IN p_fechaFactura VARCHAR(45),
    IN p_codigoCliente INT,
    IN p_codigoEmpleado INT
)
BEGIN
    INSERT INTO Factura(numeroDeFactura, estado, totalFactura, fechaFactura, codigoCliente, codigoEmpleado)
    VALUES(p_numeroDeFactura, p_estado, p_totalFactura, p_fechaFactura, p_codigoCliente, p_codigoEmpleado);
END$$

CREATE PROCEDURE sp_listarFacturas()
BEGIN
    SELECT * FROM Factura;
END$$

CREATE PROCEDURE sp_actualizarFactura(
    IN p_numeroDeFactura INT,
    IN p_nuevoEstado VARCHAR(50),
    IN p_nuevoTotalFactura DECIMAL(10,2),
    IN p_nuevaFechaFactura VARCHAR(45),
    IN p_nuevoCodigoCliente INT,
    IN p_nuevoCodigoEmpleado INT
)
BEGIN
    UPDATE Factura
    SET estado = p_nuevoEstado,
        totalFactura = p_nuevoTotalFactura,
        fechaFactura = p_nuevaFechaFactura,
        codigoCliente = p_nuevoCodigoCliente,
        codigoEmpleado = p_nuevoCodigoEmpleado
    WHERE numeroDeFactura = p_numeroDeFactura;
END$$

CREATE PROCEDURE sp_eliminarFactura(
    IN p_numeroDeFactura INT
)
BEGIN
    DELETE FROM Factura
    WHERE numeroDeFactura = p_numeroDeFactura;
END$$

DELIMITER ;

DELIMITER $$

CREATE PROCEDURE sp_crearDetalleFactura(
    IN p_codigoDetalleFactura INT,
    IN p_precioUnitario DECIMAL(10,2),
    IN p_cantidad INT,
    IN p_numeroDeFactura INT,
    IN p_codigoProducto VARCHAR(15)
)
BEGIN
    INSERT INTO DetalleFactura(codigoDetalleFactura, precioUnitario, cantidad, numeroDeFactura, codigoProducto)
    VALUES(p_codigoDetalleFactura, p_precioUnitario, p_cantidad, p_numeroDeFactura, p_codigoProducto);
END$$

CREATE PROCEDURE sp_listarDetallesFactura()
BEGIN
    SELECT * FROM DetalleFactura;
END$$

CREATE PROCEDURE sp_actualizarDetalleFactura(
    IN p_codigoDetalleFactura INT,
    IN p_nuevoPrecioUnitario DECIMAL(10,2),
    IN p_nuevaCantidad INT,
    IN p_nuevoNumeroDeFactura INT,
    IN p_nuevoCodigoProducto VARCHAR(15)
)
BEGIN
    UPDATE DetalleFactura
    SET precioUnitario = p_nuevoPrecioUnitario,
        cantidad = p_nuevaCantidad,
        numeroDeFactura = p_nuevoNumeroDeFactura,
        codigoProducto = p_nuevoCodigoProducto
    WHERE codigoDetalleFactura = p_codigoDetalleFactura;
END$$

CREATE PROCEDURE sp_eliminarDetalleFactura(
    IN p_codigoDetalleFactura INT
)
BEGIN
    DELETE FROM DetalleFactura
    WHERE codigoDetalleFactura = p_codigoDetalleFactura;
END$$

DELIMITER ;

DELIMITER $$

CREATE TRIGGER AfterInsertDetalleCompra
AFTER INSERT ON DetalleCompra
FOR EACH ROW
BEGIN
    DECLARE precioProveedor DECIMAL(10,2);
    DECLARE precioDocena DECIMAL(10,2);
    DECLARE precioMayor DECIMAL(10,2);

    SET precioProveedor = NEW.costoUnitario * 1.40;
    SET precioDocena = precioProveedor * 1.35;
    SET precioMayor = precioProveedor * 1.25;

    UPDATE Productos
    SET precioUnitario = precioProveedor,
        precioDocena = precioDocena,
        precioMayor = precioMayor
    WHERE codigoProducto = NEW.codigoProducto;
END $$

DELIMITER ;

CALL sp_crearCliente(1,'123456-8','prueba','pruab1','5ta Calle Z.2 Mixco','+502 12345678','pruabcorreo@gmail.com');
CALL sp_crearCliente(2,'888029-8','Javier','Herrera','5ta Calle Z.2 Mixco','+502 55885335','javierherrera5513@gmail.com');
CALL sp_crearCliente(3,'123545-3','Ludwin','Xocoy','Barrio Chino','+502 12653498','ludwinxocoy6@gmail.com');
CALL sp_crearCliente(4,'652375-8','Luis','Flores','Direccion ejemp1','+502 12345678','luispedro@gmail.com');
CALL sp_listarCliente();


CALL sp_crearTipoProducto(1,"Proteínas");
CALL sp_crearTipoProducto(2,"aaaaa");
CALL sp_listarTipoProducto();
CALL sp_actualizarTipoProducto(1, 'Proteina Sintetica');
-- CALL sp_eliminarTipoProducto(1);

CALL sp_crearCompras(1234, '2024-02-06', "ejemplo1", 1.00);
CALL sp_listarCompras();
CALL sp_actualizarCompra(1234, '2024-05-06', "ejemplo2", 1.00);
-- CALL sp_eliminarCompra(1234);

CALL sp_crearCargoEmpleado(1234,"Gerente","administtrar tienda");
CALL sp_listarCargoEmpleado();
CALL sp_actualizarCargoEmpleado(1234,"JEFE","Mandar");
-- CALL sp_eliminarCargoEmpleado(1234);

call sp_crearProveedor(1, '0614000000011', 'Gasolinera Express', 'S.A.', 'Av. Principal 123, Zona 1', 'Gasolinera Express S.A.', 'Juan Pérez', 'www.gasolineraexpress.com', '1234567890', 'info@gasolineraexpress.com');
call sp_crearProveedor(2, '0614000000024', 'Distribuidora de Alimentos', 'Dialiment S.A.', 'Av. Comercial 456, Zona 2', 'Dialiment S.A.', 'María Gómez', 'www.dialiment.com', '2345678901', 'info@dialiment.com');
call sp_crearProveedor(3, '0614000000037', 'Bebidas Refrescantes', 'Refrescos del Sur S.A.', 'Calle Refrescante 789, Zona 3', 'Refrescos del Sur S.A.', 'Pedro Martínez', 'www.refrescosdelsur.com', '3456789012', 'info@refrescosdelsur.com');
call sp_crearProveedor(4, '0614000000040', 'Lubricantes y Aceites', 'Lubriaceites Ltda.', 'Carrera Lubricante 101, Zona 4', 'Lubriaceites Ltda.', 'Luis Rodríguez', 'www.lubriaceites.com', '4567890123', 'info@lubriaceites.com');
call sp_crearProveedor(5, '0614000000053', 'Productos de Limpieza', 'Limpiafacil S.A.', 'Pasaje Limpio 202, Zona 5', 'Limpiafacil S.A.', 'Ana López', 'www.limpiafacil.com', '5678901234', 'info@limpiafacil.com');
call sp_actualizarProveedor(1,'Juan', 'Perez', 'Calle 123', 'Razón Social', 'Contacto', 'www.proveedor.com', '1234567890', 'proveedor@correo.com');
CALL sp_listarProveedores();
CALL sp_actualizarProveedor(1, 'Nuevo Nombre', 'Nuevos Apellidos', 'Nueva Dirección', 'Nueva Razón Social', 'Nuevo Contacto Principal', 'www.nuevaweb.com',1234567,'NUEVOCORREGO@GMAIL.COM');
-- CALL sp_eliminarProveedor(2);


CALL sp_crearProducto('ABC123', 'Producto', 10.99, 99.99, 199.99, 100, 1, 1);
CALL sp_listarProductos();
CALL sp_actualizarProducto('ABC123', 'Nuevo', 20.99, 199.99, 299.99, 200, 1, 1);
-- CALL sp_eliminarProducto('ABC123');

CALL sp_crearDetalleCompra(1, 10.99, 5, 'ABC123', 1234);
CALL sp_listarDetallesCompra();
CALL sp_actualizarDetalleCompra(1, 15.99, 7, 'ABC123', 1234);
-- CALL sp_eliminarDetalleCompra(1);

CALL sp_crearEmpleado(1, 'Juan', 'Pérez', 2000.00, 'Dirección del empleado', 'Matutino', 1234);
CALL sp_listarEmpleados();
CALL sp_actualizarEmpleado(1, 'Juan', 'López', 2200.00, 'Nueva Dirección del empleado', 'Vespertino', 1234);

CALL sp_crearFactura(1, 'Pagado', 1000.00, '2024-05-06', 1, 1);
CALL sp_listarFacturas();
CALL sp_actualizarFactura(1, 'Pendiente', 1200.00, '2024-05-07', 1, 1);
-- CALL sp_eliminarFactura(1);

CALL sp_crearDetalleFactura(1, 10.99, 5, 1, 'ABC123');
CALL sp_listarDetallesFactura();
CALL sp_actualizarDetalleFactura(1, 15.99, 7, 1, 'ABC123');
-- CALL sp_eliminarDetalleFactura(1);
