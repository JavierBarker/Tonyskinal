create database DBTonysKinal2018501;
use dbtonyskinal2018501;
#Drop database if exists  DBTonysKinal2018501;
#ALTER USER 'root'@'localhost' identified WITH mysql_native_password BY 'root';
delimiter $$
	create procedure sp_Productos()
		begin
			create table Productos(
				codigoProducto int not null auto_increment,
				nombreProducto varchar(150) not null,
				cantidad int not null,
				primary key PK_codigoProducto(codigoProducto)
			);
		end $$
delimiter ;


delimiter $$
	create procedure sp_TipoPlato()
		begin
			create table TipoPlato(
				codigoTipoPlato int not null auto_increment,
				descripcionTipo varchar(100) not null,
				primary key PK_codigoTipoPlato(codigoTipoPlato)
			);
		end $$
delimiter ;


delimiter $$
	create procedure sp_Platos()
		begin
			create table Platos(
				codigoPlato int not null auto_increment,
				cantidad int not null,
				nombrePlato varchar(50) not null,
				descripcionPlato varchar(150) not null,
				precioPlato decimal(10,2) not null,
				tipoPlato_codigoTipoPlato int not null,

				primary key PK_codigoPlato(codigoPlato),
				constraint FK_Platos_TipoPlato foreign key (tipoPlato_codigoTipoPlato) references TipoPlato(codigoTipoPlato) on delete cascade
			);
		end $$
delimiter;

delimiter $$
	create procedure sp_ProductosHasPlatos()
		begin
			create table Productos_has_Platos(
				productos_codigoProducto int not null,
				platos_codigoPlato int not null,
				codigoProducto int not null,

				primary key PK_Productos_codigoProducto(productos_codigoProducto),
				constraint FK_Productos_has_Platos_Productos1 foreign key(codigoProducto) references Productos(codigoProducto) on delete cascade,
				constraint FK_Productos_has_Platos_Platos1 foreign key(platos_codigoPlato) references Platos(codigoPlato) on delete cascade
			);
		end $$
delimiter ;


delimiter $$
	create procedure sp_Empresas()
		begin 
			create table Empresas(
				codigoEmpresa int not null auto_increment,
				nombreEmpresa varchar(150) not null,
				direccion varchar(150) not null,
				telefono varchar(10) not null,
				primary key PK_codigoEmpresa(codigoEmpresa)
			);
		end $$
delimiter ;


delimiter $$
	create procedure sp_Presupuestos()
		begin
			create table Presupuestos(
				codigoPresupuesto int not null auto_increment,
				fechaSolicitud date not null,
				cantidadPresupuesto decimal(10,2) not null,
				empresas_codigoEmpresa int not null,
				primary key PK_codigoPresupuesto(codigoPresupuesto),
				constraint FK_Presupuestos_Empresas foreign key (empresas_codigoEmpresa) references Empresas(codigoEmpresa) on delete cascade
			);
		end $$
delimiter ;


delimiter $$
	create procedure sp_Servicios()
		begin 
			create table Servicios(
				codigoServicio int not null auto_increment,
				fechaServicio date not null,
				tipoServicio varchar(100) not null,
				horaServicio time not null,
				lugarServicio varchar(100) not null,
				telefonoContacto varchar(10) not null,
				empresas_codigoEmpresa int not null,
				primary key PK_codigoServicio (codigoServicio),
				constraint FK_Servicios_Empresas foreign key(empresas_codigoEmpresa) references Empresas(codigoEmpresa) on delete cascade
			);
		end $$
delimiter ;


delimiter $$
	create procedure sp_ServiciosHasPlatos()
		begin
			create table Servicios_has_Platos(
				servicios_codigoServicio int not null,
				platos_codigoPlato int not null,
				codigoServicio int not null,
				primary key PK_Servicios_codigoServicio (servicios_codigoServicio),
				constraint FK_Servicios_has_Platos_Servicios1 foreign key(codigoServicio) references Servicios(codigoServicio) on delete cascade,
				constraint FK_Servicios_has_Platos_Platos1 foreign key(platos_codigoPlato) references Platos(codigoPlato) on delete cascade
			);
		end $$
delimiter ;


delimiter $$
	create procedure sp_TipoEmpleado()
		begin
			create table TipoEmpleado(
				codigoTipoEmpleado int not null auto_increment,
				descripcion varchar(100) not null,
				primary key PK_codigoTipoEmpleado(codigoTipoEmpleado)
			);
		end $$
delimiter ;


delimiter $$
	create procedure sp_Empleados()
		begin
			create table Empleados(
				codigoEmpleado int not null auto_increment,
				numeroEmpleado int not null,
				apellidosEmpleado varchar(150) not null,
				nombresEmpleado varchar(150) not null,
				direccionEmpleado varchar(150) not null,
				telefonoContacto varchar(10) not null,
				gradoCocinero varchar(50),
				tipoEmpleado_codigoTipoEmpleado int not null,
				primary key PK_codigoEmpleado(codigoEmpleado),
				constraint FK_Empleados_TipoEmpleado foreign key(tipoEmpleado_codigoTipoEmpleado) references TipoEmpleado(codigoTipoEmpleado) on delete cascade
			);
		end $$
delimiter ;


delimiter $$
	create procedure sp_ServiciosHasEmpleados()
		begin
			create table Servicios_has_Empleados(
				servicios_codigoServicio int not null auto_increment,
				codigoServicio int not null,
				empleados_codigoEmpleado int not null,

				fechaEvento date not null,
				horaEvento time not null,
				lugarEvento varchar(150) not null,
				primary key PK_Servicios_codigoServicio(servicios_codigoServicio),
				constraint FK_Servicios_has_Empleados_Servicios1 foreign key(codigoServicio) references Servicios(codigoServicio) on delete cascade,
				constraint FK_Servicios_has_Empleados_Empreados1 foreign key(empleados_codigoEmpleado) references Empleados(codigoEmpleado) on delete cascade
			);
		end $$
delimiter ;

/********************************************************************************************************************************************/
/*LLAMADOS*/
call sp_Productos();
call sp_TipoPlato();
call sp_Platos();
call sp_ProductosHasPlatos();
call sp_Empresas();
call sp_Presupuestos();
call sp_Servicios();
call sp_ServiciosHasPlatos();
call sp_TipoEmpleado();
call sp_Empleados();
call sp_ServiciosHasEmpleados();













/*CRUD de la base de datos */
use DBTonysKinal2018501;

/*PRODUCTOS***********************************
/*listar****************/
delimiter $$
	create procedure sp_ListarProductos()
		begin 
			select 
				productos.codigoProducto,
				productos.nombreProducto,
				productos.cantidad 
			from productos;
		end $$
delimiter ;

call sp_ListarProductos();
/********************************/

/*Agregar**************/
delimiter $$
	create procedure sp_AgregarProducto(in nom varchar(150), cant int)
		begin
			insert into productos(nombreProducto, cantidad) 
				values (nom,cant);
		end $$
delimiter ;

call sp_AgregarProducto("huevos",90);
call sp_AgregarProducto("cebollas",100);
call sp_AgregarProducto("tomates",60);
call sp_AgregarProducto("bananos",25);
call sp_AgregarProducto("manzanas",85);


/****************************************/

/*Actualizar*****************************/
delimiter $$
	create procedure sp_ActualizarProducto(in id int, nom varchar(150), cant int)
		begin
			update productos set nombreProducto=nom, cantidad=cant 
				where codigoProducto=id;
		end $$
delimiter ;

/*call sp_ActualizarProducto(1,"huevos1",85);*/
/*********************************************/

/*Eliminar*******************************/
delimiter $$
	create procedure sp_EliminarProducto(in id int)
		begin
			delete from productos 
				where codigoProducto=id;
		end $$
delimiter ;

/*call sp_EliminarProducto(3);*/
/***************************************************/

/*Buscar************************************/
delimiter $$
	create procedure sp_BuscarProducto(in id int)
		begin
			select 
				productos.codigoProducto,
				productos.nombreProducto,
				productos.cantidad 
			from productos where codigoProducto=id;
		end $$
delimiter ;

call sp_BuscarProducto(2);
/******************************************************/







/*TIPO PLATO********************************************/
/*Listar**********************************/
delimiter $$
	create procedure sp_ListarTiposPlatos()
		begin 
			select 
				TipoPlato.codigoTipoPlato,
				TipoPlato.descripcionTipo 
			from TipoPlato;
		end $$
delimiter ;

call sp_ListarTiposPlatos();
/********************************************/

/*Agregar***********************************/
delimiter $$
	create procedure sp_AgregarTipoPlato(in des varchar(100))
		begin
			insert into TipoPlato(descripcionTipo) 
				values (des);
		end $$
delimiter ;

call sp_AgregarTipoPlato("comida china");
call sp_AgregarTipoPlato("comida italiana");
call sp_AgregarTipoPlato("comida tipica");
call sp_AgregarTipoPlato("comida alemana");
call sp_AgregarTipoPlato("comida indú");
/***************************************************/

/*Actualizar*************************************/
delimiter $$
	create procedure sp_ActualizarTipoPlato(in id int, des varchar(100))
		begin 
			update TipoPlato set descripcionTipo=des 
				where codigoTipoPlato=id;
		end $$
delimiter ;

/*call sp_ActualizarTipoPlato(1,"comida china 4");*/
/**************************************************************/

/*Eliminar*****************************************************/
delimiter $$
	create procedure sp_EliminarTipoPlato(in id int)
		begin
			delete from TipoPlato 
				where codigoTipoPlato=id;
		end $$
delimiter ;

/*call sp_EliminarTipoPlato(2);*/
/******************************************************************/

/*Buscar**********************************************************/
delimiter $$
	create procedure sp_BuscarTipoPlato(in id int)
		begin
			select 
				TipoPlato.codigoTipoPlato,
				TipoPlato.descripcionTipo 
			from TipoPlato where codigoTipoPlato=id;
		end $$
delimiter ;

call sp_BuscarTipoPlato(3);
/*********************************************************************/





/*PLATOS******************************/
/*Listar******************************/
delimiter $$
	create procedure sp_ListarPlatos()
		begin
			select 
				platos.codigoPlato,
				platos.cantidad,
				platos.nombrePlato,
				platos.descripcionPlato,
				platos.precioPlato,
				platos.tipoPlato_codigoTipoPlato 
			from platos;
		end $$
delimiter ;

call sp_ListarPlatos();
/******************************************/

/*Agregar*********************************/
delimiter $$
	create procedure sp_AgregarPlato(in cant int, nom varchar(50), des varchar(150), precio decimal(10,2), tipo int)
		begin
			insert into platos(cantidad, nombrePlato, descripcionPlato, precioPlato, TipoPlato_codigoTipoPlato) 
				values(cant, nom, des, precio, tipo);
		end $$
delimiter ;

call sp_AgregarPlato(25, "wantan", "masa con camaron en el interior", 6.50, 1);
call sp_AgregarPlato(40, "pasta", "masa en tiras con salsa", 10.50, 2);
call sp_AgregarPlato(51, "paches", "masa de papa con salsa", 20.50, 3);
call sp_AgregarPlato(48, "kassler", "filete de carne de cerdo ahumado y salado", 5.50, 4);
call sp_AgregarPlato(50, "samosa", "masa con relleno de patata acompañado de verduras", 7.50, 5);
/********************************************************************************************/

/*Actualizar****************************************/
delimiter $$
	create procedure sp_ActualizarPlato(in id int, cant int, nom varchar(50), des varchar(150), precio decimal(10,2))
		begin
			update platos set cantidad=cant, nombrePlato=nom, descripcionPlato=des, precioPlato=precio 
				where codigoPlato=id;
		end $$
delimiter ;

/*call sp_ActualizarPlato(1, 80, "wantan22", "masa con camaron en el interior55555", 7.65);*/
/*******************************************************************************************************************/

/*Eliminar*****************************************/
delimiter $$
	create procedure sp_EliminarPlato(in id int)
		begin
			delete from platos 
				where codigoPlato=id;
		end $$
delimiter ;

/*call sp_EliminarPlato(2);*/
/**************************************************/

/*Buscar*******************************************/
delimiter $$
	create procedure sp_BuscarPlato(in id int)
		begin
			select 
				platos.codigoPlato,
				platos.cantidad,
				platos.nombrePlato,
				platos.descripcionPlato,
				platos.precioPlato,
				platos.tipoPlato_codigoTipoPlato 
			from platos where codigoPlato=id;
		end $$
delimiter ;

call sp_BuscarPlato(3);
/****************************************************/











/*PRODUCTOS_HAS_PLATOS********************************************************/
/*Listar****************************************************************************/
delimiter $$
	create procedure sp_ListarProductosHasPlatos()
		begin
			select 
				platos.codigoPlato,
				productos.codigoProducto
			from productos, platos order by platos.codigoPlato asc;
		end $$
delimiter ;

call sp_ListarProductosHasPlatos();
/************************************************************************************/











/*EMPRESAS********************************************************/
/*Listar**********************************************************/
delimiter $$
	create procedure sp_ListarEmpresas()
		begin
			select 
				empresas.codigoEmpresa,
				empresas.nombreEmpresa,
				empresas.direccion,
				empresas.telefono 
			from empresas;
		end $$
delimiter ;

call sp_ListarEmpresas();
/****************************************************************/

/*Agregar*********************************************************/
delimiter $$
	create procedure sp_AgregarEmpresa(in nom varchar(150), direc varchar(150), tel varchar(10))
		begin
			insert into empresas(nombreEmpresa, direccion, telefono) 
				values (nom, direc, tel);
		end $$
delimiter ;

call sp_AgregarEmpresa("Claro", "5ta av 14-58 zona 5", "51855421");
call sp_AgregarEmpresa("Pepsi", "6ta av 20-12 zona 8", "45321587");
call sp_AgregarEmpresa("Distelsa", "8ta av 16-45 zona 4", "54123589");
call sp_AgregarEmpresa("My Tec", "9ta av 09-15 zona 7", "45889164");
call sp_AgregarEmpresa("Imeqmo", "7ta av 10-16 zona 2", "41947532");
call sp_AgregarEmpresa("Portinova", "5ta av 09-15 zona 7", "55234411");
call sp_AgregarEmpresa("Walmart", "4ta av 04-50 zona 7", "59417824");

/*********************************************************************************/

/*Actualizar***********************************************************/
delimiter $$
	create procedure sp_ActualizarEmpresa(in id int, nom varchar(150), direc varchar(150), tel varchar(10))
		begin
			update empresas set nombreEmpresa=nom, direccion=direc, telefono=tel 
				where codigoEmpresa=id;
		end $$
delimiter ;

/*call sp_ActualizarEmpresa(1, "Claro1", "5ta av 14-60 zona 5", "22222222");*/
/*******************************************************************************/

/*Eliminar***********************************************************************/
delimiter $$
	create procedure sp_EliminarEmpresa(in id int)
		begin 
			delete from empresas 
				where codigoEmpresa=id;
		end $$
delimiter ;

/*call sp_EliminarEmpresa(2);*/
/**********************************************************************************/

/*Buscar**************************************************************************/
delimiter $$
	create procedure sp_BuscarEmpresa(in id int)
		begin
			select 
				empresas.codigoEmpresa,
				empresas.nombreEmpresa,
				empresas.direccion,
				empresas.telefono 
			from Empresas where codigoEmpresa=id;
		end $$
delimiter ;

call sp_BuscarEmpresa(1);
/************************************************************************************/





/*PRESUPUESTO***********************************************************************/
/*Listar*****************************************************************************/
delimiter $$
	create procedure sp_ListarPresupuestos()
		begin
			select 
				presupuestos.codigoPresupuesto,
				presupuestos.fechaSolicitud,
				presupuestos.cantidadPresupuesto,
				presupuestos.Empresas_codigoEmpresa 
			from presupuestos;
		end $$
delimiter ;

call sp_ListarPresupuestos();
/*************************************************************************************/

/*Agregar****************************************************/
delimiter $$
	create procedure sp_AgregarPresupuesto(in fecha date, pres decimal(10,2), empresa int)
		begin
			insert into presupuestos(fechaSolicitud, cantidadPresupuesto, Empresas_codigoEmpresa) 
				values (fecha, pres, empresa);
		end $$
delimiter ;

call sp_AgregarPresupuesto("2020/04/02", 960.50, 1);
call sp_AgregarPresupuesto("2020/05/07", 78.41, 2);
call sp_AgregarPresupuesto("2020/06/03", 952.41, 3);
call sp_AgregarPresupuesto("2020/07/05", 7550.65, 4);
call sp_AgregarPresupuesto("2020/08/06", 500.41, 5);


/*********************************************************************/

/*Actualizar********************************************************/
delimiter $$
	create procedure sp_ActualizarPresupuesto(in id int, fecha date, pres decimal(10,2))
		begin
			update presupuestos set fechaSolicitud=fecha, cantidadPresupuesto=pres 
				where codigoPresupuesto=id;
		end $$
delimiter ;

/*call sp_ActualizarPresupuesto(1, "2021/04/03", 1000.41);*/
/**********************************************************************/

/*Eliminar*************************************************************/
delimiter $$
	create procedure sp_EliminarPresupuesto(in id int)
		begin
			delete from presupuestos 
				where codigoPresupuesto=id;
		end $$
delimiter ;

/*call sp_EliminarPresupuesto(1);*/
/********************************************************************/

/*Buscar**********************************************************/
delimiter $$
	create procedure sp_BuscarPresupuesto(in id int)
		begin
			select 
				presupuestos.codigoPresupuesto,
				presupuestos.fechaSolicitud,
				presupuestos.cantidadPresupuesto,
				presupuestos.Empresas_codigoEmpresa 
			from presupuestos where codigoPresupuesto=id;
		end $$
delimiter ;

call sp_BuscarPresupuesto(3);
/*******************************************************************/





/*SERVICIOS*************************************************************/
/*Listar****************************************************************/
delimiter $$
	create procedure sp_ListarServicios()
		begin
			select 
				servicios.codigoServicio,
				servicios.fechaServicio,
				servicios.tipoServicio,
				servicios.horaServicio,
				servicios.lugarServicio,
				servicios.telefonoContacto,
				servicios.empresas_codigoEmpresa 
			from servicios;
		end $$
delimiter ;

call sp_ListarServicios();
/***********************************************************************/

/*Agregar**************************************************************/
delimiter $$
	create procedure sp_AgregarServicio(in fecha date, tipo varchar(100), hora time, lugar varchar(100), contacto varchar(10), empresa int )
		begin
			insert into servicios(fechaServicio, tipoServicio, horaServicio, lugarServicio, telefonoContacto, empresas_codigoEmpresa)
				values (fecha, tipo, hora, lugar, contacto, empresa);
		end $$
delimiter ;

call sp_AgregarServicio("2020/04/04", "conferencia", "18:34", "Miraflores", 54854641, 1);
call sp_AgregarServicio("2020/02/03", "buffet", "08:34", "tikal futura", 54545632, 2);
call sp_AgregarServicio("2020/08/05", "convivencia", "19:20", "Mixco", 45632654, 3);
call sp_AgregarServicio("2020/12/06", "fiesta", "11:05", "Ciudad de Guatemala", 51248652, 4);
call sp_AgregarServicio("2020/11/06", "desayuno", "12:35", "eskala", 44111110, 5);
/***************************************************************************************************/

/*Actualizar*********************************************************************/
delimiter $$
	create procedure sp_ActualizarServicio(in id int, fecha date, tipo varchar(100), hora time, lugar varchar(100), contacto varchar(10))
		begin
			update servicios set fechaServicio=fecha, tipoServicio=tipo, horaServicio=hora, lugarServicio=lugar, telefonoContacto=contacto 
				where codigoServicio=id;
		end $$
delimiter ;

/*call sp_ActualizarServicio(1, "2020/04/03", "buffet4854854", "19:00", "miraflores", 11111111);*/
/*******************************************************************************************************/

/*Eliminar*********************************************************************************************/
delimiter $$
	create procedure sp_EliminarServicio(in id int)
		begin
			delete from servicios 
				where codigoServicio=id;
		end $$
delimiter ;

/*call sp_EliminarServicio(1);*/
/******************************************************************************************************/

/*Buscar******************************************************************/
delimiter $$
	create procedure sp_BuscarServicio(in id int)
		begin
			select 
				servicios.codigoServicio,
				servicios.fechaServicio,
				servicios.tipoServicio,
				servicios.horaServicio,
				servicios.lugarServicio,
				servicios.telefonoContacto,
				servicios.empresas_codigoEmpresa 
			from servicios where codigoServicio=id;
		end $$
delimiter ;

call sp_BuscarServicio(2);
/*****************************************************************************/










/*SERVICIOS_HAS_PLATOS**********************************************************/
/*Listar************************************************************************/

delimiter $$
	create procedure sp_ListarServiciosHasPlatos()
		begin
			select 
				servicios.codigoServicio, 
				platos.codigoPlato 
            from servicios, platos order by servicios.codigoServicio asc;
        end $$
delimiter ;

call sp_ListarServiciosHasPlatos();

/********************************************************************************/










/*TIPO EMPLEADO**************************************/
/*Listar*********************************************/
delimiter $$
	create procedure sp_ListarTiposEmpleados()
		begin
			select 
				TipoEmpleado.codigoTipoEmpleado,
				TipoEmpleado.descripcion 
			from TipoEmpleado;
		end $$
delimiter ;

call sp_ListarTiposEmpleados();
/******************************************************/

/*Agregar********************************************/
delimiter $$
	create procedure sp_AgregarTipoEmpleado(in des varchar(100))
		begin
			insert into tipoempleado(descripcion) 
				values(des);
		end $$
delimiter ;

call sp_AgregarTipoEmpleado("Empleado temporal");
call sp_AgregarTipoEmpleado("Ayudante");
call sp_AgregarTipoEmpleado("Chef");
call sp_AgregarTipoEmpleado("Conserje");
call sp_AgregarTipoEmpleado("Mesero");
/**********************************************************/

/*Actualizar***********************************************/
delimiter $$
	create procedure sp_ActualizarTipoEmpleado(in id int, des varchar(100))
		begin
			update tipoempleado set descripcion=des 
				where codigoTipoEmpleado=id;
		end $$
delimiter ;

/*call sp_ActualizarTipoEmpleado(2, "Empleado temporal222");*/
/****************************************************************/

/*Eliminar************************************************/
delimiter $$
	create procedure sp_EliminarTipoEmpleado(in id int)
		begin
			delete from tipoempleado 
				where codigoTipoEmpleado=id;
		end $$
delimiter ;

/*call sp_EliminarTipoEmpleado(1);*/
/***************************************************************/

/*Buscar********************************************************/
delimiter $$
	create procedure sp_BuscarTipoEmpleado(in id int)
		begin
			select 
				TipoEmpleado.codigoTipoEmpleado,
				TipoEmpleado.descripcion 
			from tipoempleado where codigoTipoEmpleado=id;
		end $$
delimiter ;

call sp_BuscarTipoEmpleado(2);
/****************************************************************/







/*EMPLEADOS*******************************************************/
/*Listar**********************************************************/
delimiter $$
	create procedure sp_ListarEmpleados()
		begin
			select 
				empleados.codigoEmpleado,
				empleados.numeroEmpleado,
				empleados.apellidosEmpleado,
				empleados.nombresEmpleado,
				empleados.direccionEmpleado,
				empleados.telefonoContacto,
				empleados.gradoCocinero,
				empleados.tipoEmpleado_codigoTipoEmpleado 
			from empleados;
		end $$
delimiter ;

call sp_ListarEmpleados();
/*****************************************************/

/*Agregar**********************************************/
delimiter $$
	create procedure sp_AgregarEmpleado(in numemple int, apell varchar(150), nom varchar(150), direccion varchar(150), contacto varchar(10), grado varchar(50), tipo int)
		begin
			insert into empleados(numeroEmpleado, apellidosEmpleado, nombresEmpleado, direccionEmpleado, telefonoContacto, gradoCocinero, TipoEmpleado_codigoTipoEmpleado) 
				values (numemple, apell, nom, direccion, contacto, grado, tipo);
		end $$
delimiter ;

call sp_agregarEmpleado(124, "Barker", "Javier", "12 av 21-41", "1234565789", "0", 1);
call sp_agregarEmpleado(125, "Hernandez", "Raul", "13 av 22-32", "2222222223", "3", 2);
call sp_agregarEmpleado(126, "Castillo", "Sergio", "5 av 5-42", "5556566655", "1", 3);
call sp_agregarEmpleado(127, "Alvarez", "Manuel", "6 av 5-20", "4445551111", "0", 4);
call sp_agregarEmpleado(128, "Gomez", "Antonio", "2 av 20-50", "4464666666", "0", 5);
/******************************************************************************************************/

/*Actualizar*************************************************************/
delimiter $$
	create procedure sp_ActualizarEmpleado(in id int, numemple int, apell varchar(150), nom varchar(150), direccion varchar(150), contacto varchar(10), grado varchar(50))
		begin
			update empleados set numeroEmpleado=numemple, apellidosEmpleado=apell, nombresEmpleado=nom, direccionEmpleado=direccion, telefonoContacto=contacto, gradoCocinero=grado
				where codigoEmpleado=id;
		end $$
delimiter ;

/*call sp_ActualizarEmpleado(1, 854, "Barker5555", "Javier5555", "12 a", "11111144", "ayudante");*/
/************************************************************************************************************/

/*Eliminar*******************************************************************************************/
delimiter $$
	create procedure sp_EliminarEmpleado(in id int)
		begin
			delete from empleados 
				where codigoEmpleado=id;
		end $$
delimiter ;

/*call sp_EliminarEmpleados(1);*/
/*****************************************************************************************************/

/*Buscar************************************************************************/
delimiter $$
	create procedure sp_BuscarEmpleado(in id int)
		begin
			select 
				empleados.codigoEmpleado,
				empleados.numeroEmpleado,
				empleados.apellidosEmpleado,
				empleados.nombresEmpleado,
				empleados.direccionEmpleado,
				empleados.telefonoContacto,
				empleados.gradoCocinero,
				empleados.tipoEmpleado_codigoTipoEmpleado 
			from empleados where codigoEmpleado=id;
		end $$
delimiter ;

call sp_BuscarEmpleado(2);
/********************************************************************************/






/*SERVICIOS HAS EMPLEADOS****************************************************/
/*Listar*********************************************************************/
delimiter $$
	create procedure sp_ListarServiciosHasEmpleados()
		begin
			select 
				servicios_has_empleados.servicios_codigoServicio,
				servicios_has_empleados.codigoServicio,
				servicios_has_empleados.empleados_codigoEmpleado,
				servicios_has_empleados.fechaEvento,
				servicios_has_empleados.horaEvento,
				servicios_has_empleados.lugarEvento 
			from servicios_has_empleados;
		end $$
delimiter ;

call sp_ListarServiciosHasEmpleados();
/*****************************************************************************/

/*Agregar*********************************************************************/
delimiter $$
	create procedure sp_AgregarServicioHasEmpleado(in servicio int, empleado int, fecha date, hora time, lugar varchar(150))
		begin
			insert into servicios_has_empleados (codigoServicio, empleados_codigoEmpleado, fechaEvento, horaEvento, lugarEvento)
				values(servicio, empleado, fecha, hora, lugar);
		end $$
delimiter ;

call sp_AgregarServicioHasEmpleado(1, 1, "2020/05/03", "22:52", "miraflores");
call sp_AgregarServicioHasEmpleado(2, 2, "2020/04/03", "16:50", "tikal futura");
call sp_AgregarServicioHasEmpleado(3, 3, "2020/06/03", "20:40", "Mixco");
call sp_AgregarServicioHasEmpleado(4, 4, "2020/07/03", "18:35", "Ciudad de Guatemala");
call sp_AgregarServicioHasEmpleado(5, 5, "2020/07/03", "13:15", "eskala");
/*******************************************************************************************/

/*Agregar***********************************************************************************/
delimiter $$
	create procedure sp_ActualizarServicioHasEmpleado(in id int, fecha date, hora time, lugar varchar(150))
		begin
			update Servicios_Has_Empleados set fechaEvento=fecha, horaEvento=hora, lugarEvento=lugar 
				where servicios_codigoServicio=id;
		end $$
delimiter ;

/*call sp_ActualizarServicioHasEmpleado(1, "2020-06-04", "23:30:00", "miraflores 2");*/
/************************************************************************************************/

/*Eliminar***************************************************************************************/
delimiter $$
	create procedure sp_EliminarServicioHasEmpleado(in id int)
		begin
			delete from Servicios_Has_Empleados 
				where servicios_codigoServicio=id;
		end $$
delimiter ;

/*call sp_EliminarServicioHasEmpleado(6);*/
/************************************************************************************************/

/*Buscar*****************************************************************************************/
delimiter $$
	create procedure sp_BuscarServicioHasEmpleado(in id int)
		begin
			select 
				servicios_has_empleados.servicios_codigoServicio,
				servicios_has_empleados.codigoServicio,
				servicios_has_empleados.empleados_codigoEmpleado,
				servicios_has_empleados.fechaEvento,
				servicios_has_empleados.horaEvento,
				servicios_has_empleados.lugarEvento 
			from servicios_has_empleados where servicios_codigoServicio= id;
		end $$
delimiter ;

call sp_BuscarServicioHasEmpleado(3);
/**************************************************************************************************/






















/*REPORTES **********************************************************************/
/*REPORTE PRESUPUESTO************************************************************/

/*SUB REPORTE PRESUPUESTO FINAL**************************************************/
delimiter $$
	create procedure sp_SubReportePresupuestoFinal(in codEmpresa int)
		begin 
			select 
				P.fechaSolicitud,
				P.cantidadPresupuesto
			from  Empresas E inner join Presupuestos P on
				E.codigoEmpresa = P.empresas_codigoEmpresa where E.codigoEmpresa = codEmpresa 
					group by P.cantidadPresupuesto;
		end $$
delimiter ;

call sp_SubReportePresupuestoFinal(1);
/**********************************************************************************/

/*REPORTE PRESUPUESTO FINAL*********************************************************/
delimiter $$
	create procedure sp_ReportePresupuestoFinal(in codEmpresa int)
		begin
			select 
				E.nombreEmpresa, 
				E.direccion,
				E.telefono,
                S.fechaServicio,
				S.horaServicio,
                S.lugarServicio,
                S.telefonoContacto 
			from Empresas E inner join Servicios S on 
				E.codigoEmpresa = S.empresas_codigoEmpresa where E.codigoEmpresa = codEmpresa;
		end $$
delimiter ;

call sp_ReportePresupuestoFinal(1);
/************************************************************************************/













/*SUB REPORTE SERVICIOS FINAL******************************************************************************/
delimiter $$
	create procedure sp_SubReporteServicioFinal()
		begin
			select 
				pro.nombreProducto 
			from platos pla inner Join productos pro 
				group by pro.nombreProducto;
		end $$
delimiter ;

call sp_SubReporteServicioFinal();
/***********************************************************************************************************/




/*REPORTE SERVICIO FINAL***************************************************************************************/
delimiter $$
	create procedure sp_ReporteServicioFinal(in codServicio int)
		begin
			select 
				S.tipoServicio,
				S.fechaServicio,
				S.horaServicio,
                S.lugarServicio,
                S.telefonoContacto,
				p.cantidad, 
				p.nombrePlato, 
				tp.descripcionTipo 
			from servicios s inner join platos p, TipoPlato tp 
				where s.codigoServicio = codServicio and p.tipoPlato_codigoTipoPlato = tp.codigoTipoPlato;
		end $$
delimiter ;

call sp_ReporteServicioFinal(1);
/******************************************************************************************************************/




