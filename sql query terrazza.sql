create database skycuritibacostumers;

USE skycuritibacostumers;

create table terrazzacostumers (
	Id int(11) NOT NULL AUTO_INCREMENT,
    Nome varchar(60) NOT NULL,
    Sobrenome varchar(80) NOT NULL,
    Telefone varchar(20) NOT NULL,
    Email varchar(100) NOT NULL,
    Salao varchar(30) NOT NULL,
    Pessoas int(2) NOT NULL,
    Dia datetime NOT NULL,
    Hora time NOT NULL,
    Mesa varchar(50) NOT NULL,
    Situacao varchar(35) NOT NULL,
    Pagamento double NOT NULL,
    PRIMARY KEY (Id)
);

create table bistrocostumers (
	Id int(11) NOT NULL AUTO_INCREMENT,
    Nome varchar(60) NOT NULL,
    Sobrenome varchar(80) NOT NULL,
    Telefone varchar(20) NOT NULL,
    Email varchar(100) NOT NULL,
    Salao varchar(30) NOT NULL,
    Pessoas int(2) NOT NULL,
    Dia datetime NOT NULL,
    Hora time NOT NULL,
    Mesa varchar(50) NOT NULL,
    Situacao varchar(35) NOT NULL,
	Pagamento double NOT NULL,
    PRIMARY KEY (Id)
);