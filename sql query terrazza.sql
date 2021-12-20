create database skycuritibacostumers;

USE skycuritibacostumers;

/** Tabela de clientes com reserva **/

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
    Situacao varchar(40) NOT NULL,	
    Pagamento double NOT NULL,
    IdExterno varchar(30) NOT NULL,
    PRIMARY KEY (Id)
);

ALTER TABLE terrazzacostumers
ADD COLUMN Observacao text AFTER Situacao;

ALTER TABLE terrazzacostumers
ADD COLUMN Aguardando boolean AFTER Observacao;

/** Tabela de clientes da filda de espera **/

create table waitingcostumers (
	Id int(11) NOT NULL AUTO_INCREMENT,
    Nome varchar(60) NOT NULL,
    Sobrenome varchar(80) NOT NULL,
    Telefone varchar(20) NOT NULL,
    Salao varchar(30) NOT NULL,
    Pessoas int(2) NOT NULL,
    Data date NOT NULL,
    HoraChegada time NOT NULL,
    HoraSentada time,
    Mesa varchar(50),
    Situacao varchar(40),
    Observacao text,
    PRIMARY KEY (Id)
);

/** Não vamos usar mais duas tabelas. Apenas uma **/
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

insert into terrazzacostumers (Nome, Sobrenome, Telefone, Email, Salao, Pessoas, Data, Hora, Mesa, Situacao, Pagamento, IdExterno) values
('Lohane', 'Vêkanandre Sthephany Smith Bueno de HA HA HA de Raio Laser bala de Icekiss', '5541995655618', 'robert@hotmail.com', 'Terrazza Almoço', 3, '2021-09-16', '12:00', '1', 'Confirmado', 0.00, '2876444');

insert into terrazzacostumers (Nome, Sobrenome, Telefone, Email, Salao, Pessoas, Data, Hora, Mesa, Situacao, Pagamento, IdExterno) values
('Robert', 'Smith', '5541995655618', 'robert@hotmail.com', 'Terrazza Almoço', 3, '2021-11-16', '12:00', '1', 'Confirmado', 0.00, '2876444');

insert into terrazzacostumers (Nome, Sobrenome, Telefone, Email, Salao, Pessoas, Data, Hora, Mesa, Situacao, Pagamento, IdExterno) values
('Robert', 'Smith', '5541995655618', 'robert@hotmail.com', 'Terrazza Almoço', 3, '2021-11-16', '12:00', '1', 'Cancelado por solicitação do cliente', 0.00, '2876444');

/** inserção com o novo campo de texto **/

insert into terrazzacostumers (Nome, Sobrenome, Telefone, Email, Salao, Pessoas, Data, Hora, Mesa, Situacao, Observacao, Pagamento, IdExterno) values
('Cabeleleila', 'Leila', '5541995655618', 'cabeleleilaleila@hotmail.com', 'Terrazza Almoço', 3, CURDATE(), '12:00', '1', 'Cancelado por solicitação do cliente', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent nec augue id lectus lacinia tristique quis sed elit. In lorem diam, posuere vel nisl nec, sodales ullamcorper diam. Nulla sollicitudin tincidunt massa, a vehicula leo aliquet et. In in iaculis ipsum. Donec iaculis ante eget nibh efficitur rhoncus. Donec ipsum metus, porta ut eleifend sit amet, commodo vel nisl. Donec sit amet tortor sed ipsum sagittis faucibus. Integer tincidunt, erat eget volutpat lobortis, eros erat accumsan lorem, et volutpat est ex a turpis. Fusce at ante a est iaculis rutrum. Ut nunc est, maximus id tellus ut, semper consequat mi. Vivamus est massa, maximus vel orci sit amet, laoreet finibus massa. Morbi a urna vitae leo eleifend iaculis et ac ante. ', 0.00, '2876444');

/** inserção com o novo campo booleano de aguardando mesa **/

insert into terrazzacostumers (Nome, Sobrenome, Telefone, Email, Salao, Pessoas, Data, Hora, Mesa, Situacao, Observacao, Aguardando, Pagamento, IdExterno) values
('Felipe', 'Smith', '5541995655618', 'cabeleleilaleila@hotmail.com', 'Terrazza Almoço', 3, CURDATE(), '12:00', '1', 'Cancelado por solicitação do cliente', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent nec augue id lectus lacinia tristique quis sed elit. In lorem diam, posuere vel nisl nec, sodales ullamcorper diam. Nulla sollicitudin tincidunt massa, a vehicula leo aliquet et. In in iaculis ipsum. Donec iaculis ante eget nibh efficitur rhoncus. Donec ipsum metus, porta ut eleifend sit amet, commodo vel nisl. Donec sit amet tortor sed ipsum sagittis faucibus. Integer tincidunt, erat eget volutpat lobortis, eros erat accumsan lorem, et volutpat est ex a turpis. Fusce at ante a est iaculis rutrum. Ut nunc est, maximus id tellus ut, semper consequat mi. Vivamus est massa, maximus vel orci sit amet, laoreet finibus massa. Morbi a urna vitae leo eleifend iaculis et ac ante. ', true, 0.00, '2876444');

/* testes de select */

SELECT * FROM skycuritibacostumers.terrazzacostumers;

SELECT * FROM skycuritibacostumers.terrazzacostumers
WHERE Id = 2;

SELECT * FROM skycuritibacostumers.terrazzacostumers
WHERE IdExterno= '61894688acfaa40012fd6306';

select CURDATE();

SELECT Id, Nome, Sobrenome, Telefone, Email, Salao, Pessoas, Data, Hora, Mesa, Situacao, Pagamento, IdExterno
FROM skycuritibacostumers.terrazzacostumers
WHERE DATE(Data) = CURDATE();

DELETE FROM skycuritibacostumers.terrazzacostumers WHERE Data < now() - interval 30 DAY;

SELECT Id, Nome, Sobrenome, Telefone, Email, Salao, Pessoas, Data, Hora, Mesa, Situacao, Pagamento, IdExterno
FROM skycuritibacostumers.terrazzacostumers
WHERE DATE(Data) = CURDATE() AND Situacao != "Cancelado pelo cliente" AND Situacao != "Cancelado por solicitação do cliente"
AND Situacao != "Cancelado por no-show do cliente" AND Situacao != "Cancelado por erro de cadastro";

/** updates a serem usados no nos dao.impl **/
UPDATE terrazzacostumers
SET Nome = ?, Sobrenome = ?, Telefone = ?, Email = ?, Salao = ?, Pessoas = ?, Data = ?, Hora = ?, Mesa = ?, Situacao = ?, Observacao = ?, Pagamento = ?
WHERE IdExterno = '2876444' AND Situacao !=  'Cancelado por no-show' AND Situacao != 'Sentado';

UPDATE terrazzacostumers
SET Nome = 'vamos ver se mudou', Sobrenome = 'a', Telefone = 'a', Email = 'a', Salao = 'Jantar no Terrazza 40', Pessoas = 1, Data = CURDATE(), Hora = '19:00', Mesa = '1', Situacao = 'Cancelado por no-show', Observacao = 'a', Pagamento = 0.00
WHERE IdExterno = '2876444' AND Situacao !=  'Cancelado por no-show' AND Situacao != 'Sentado';

/** update para colocar cientes na database no domingo para poder fazer testes **/
update terrazzacostumers
set Data = CURDATE()
where DATE(Data) = '2021-12-18';

/** só para limpar os dados para testes **/
DELETE FROM skycuritibacostumers.terrazzacostumers;

DROP TABLE skycuritibacostumers.terrazzacostumers;