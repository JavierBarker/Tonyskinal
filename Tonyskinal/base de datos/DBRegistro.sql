Drop database if exists DBRegistro;
Create database DBRegistro;

use DBRegistro;
create table persona(
	codigoPersona int not null auto_increment,
    DPI varchar(15) not null,
    nombrePersona varchar(200) not null,
    primary key PK_codigoPersona(codigoPersona)
);

create table usuario(
	usuario varchar(50) not null,
    password varchar(50) not null,
    rol varchar(50) not null
);

select * from persona;

insert into persona(DPI, nombrePersona) values("1245632562147","Juan Perez");
insert into persona(DPI, nombrePersona) values("1242642214411","Javier Barker");
insert into persona(DPI, nombrePersona) values("1242665478415","Miguel Estrada");

select * from persona;

select * from usuario;