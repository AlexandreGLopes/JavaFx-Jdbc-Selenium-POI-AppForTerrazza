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
    Data date NOT NULL,
    Hora time NOT NULL,
    Mesa varchar(50) NOT NULL,
    Situacao varchar(35) NOT NULL,
    Pagamento double NOT NULL,
    IdExterno varchar(30) NOT NULL,
    PRIMARY KEY (Id)
);

/** Não vamos usar mais duas tabelas. Apenas uma
/*
create table bistrocostumers (
	Id int(11) NOT NULL AUTO_INCREMENT,
    Nome varchar(60) NOT NULL,
    Sobrenome varchar(80) NOT NULL,
    Telefone varchar(20) NOT NULL,
    Email varchar(100) NOT NULL,
    Salao varchar(30) NOT NULL,
    Pessoas int(2) NOT NULL,
    Data date NOT NULL,
    Hora time NOT NULL,
    Mesa varchar(50) NOT NULL,
    Situacao varchar(35) NOT NULL,
	Pagamento double NOT NULL,
	IdExterno varchar(30) NOT NULL,
    PRIMARY KEY (Id)
);
*/

insert into terrazzacostumers (Nome, Sobrenome, Telefone, Email, Salao, Pessoas, Data, Hora, Mesa, Situacao, Pagamento, IdExterno) values
('Thiago', 'Guimaraes', '5541995655618', 'thiagoaguimaraes@hotmail.com', 'Terrazza 40', 3, '2021-11-13', '12:00', '1', 'Confirmado', 300.00, '61894688acfaa40012fd6306');

insert into terrazzacostumers (Nome, Sobrenome, Telefone, Email, Salao, Pessoas, Data, Hora, Mesa, Situacao, Pagamento, IdExterno) values
('Alex', 'Lopes', '5541995655618', 'alex@hotmail.com', '38 Floor', 3, '2021-11-13', '12:00', '1', 'Confirmado', 300.00, '61894688acfaa40012fd6306');

insert into terrazzacostumers (Nome, Sobrenome, Telefone, Email, Salao, Pessoas, Data, Hora, Mesa, Situacao, Pagamento, IdExterno) values
('Helena', 'Lopes', '5541995655618', 'helena@hotmail.com', 'Terrazza Almoço', 3, '2021-11-13', '12:00', '1', 'Confirmado', 0.00, '267345367');