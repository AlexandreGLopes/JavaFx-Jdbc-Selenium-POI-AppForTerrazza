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

ALTER TABLE waitingcostumers
ADD COLUMN Aguardando boolean AFTER Observacao;

create table standardmessages (
	Id int(11) NOT NULL AUTO_INCREMENT,
    Titulo varchar(60) NOT NULL,
    Mensagem text,
    MensagemPadrao text,
    PRIMARY KEY (Id)
);

create table costumer_x_standardmessage (
	FK_idcostumer int,
    FK_idstandardmessage int,
    foreign key(FK_idcostumer)
    references terrazzacostumers(Id),
    foreign key(FK_idstandardmessage)
    references standardmessages(Id)
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
('Felipe', 'Smith', '5541998642881', 'cabeleleilaleila@hotmail.com', 'Almoço Terrazza 40', 3, CURDATE(), '12:00', '1', 'Cancelado por solicitação do cliente', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent nec augue id lectus lacinia tristique quis sed elit. In lorem diam, posuere vel nisl nec, sodales ullamcorper diam. Nulla sollicitudin tincidunt massa, a vehicula leo aliquet et. In in iaculis ipsum. Donec iaculis ante eget nibh efficitur rhoncus. Donec ipsum metus, porta ut eleifend sit amet, commodo vel nisl. Donec sit amet tortor sed ipsum sagittis faucibus. Integer tincidunt, erat eget volutpat lobortis, eros erat accumsan lorem, et volutpat est ex a turpis. Fusce at ante a est iaculis rutrum. Ut nunc est, maximus id tellus ut, semper consequat mi. Vivamus est massa, maximus vel orci sit amet, laoreet finibus massa. Morbi a urna vitae leo eleifend iaculis et ac ante. ', true, 0.00, '2876444');

insert into skycuritibacostumers.terrazzacostumers (Nome, Sobrenome, Telefone, Email, Salao, Pessoas, Data, Hora, Mesa, Situacao, Observacao, Aguardando, Pagamento, IdExterno) values
('Cabeleleila', 'Leila', '5541998642881', 'cabeleleilaleila@hotmail.com', 'Almoço Terrazza 40', 3, CURDATE(), '12:00', '1', 'Novo', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent nec augue id lectus lacinia tristique quis sed elit. In lorem diam, posuere vel nisl nec, sodales ullamcorper diam. Nulla sollicitudin tincidunt massa, a vehicula leo aliquet et. In in iaculis ipsum. Donec iaculis ante eget nibh efficitur rhoncus. Donec ipsum metus, porta ut eleifend sit amet, commodo vel nisl. Donec sit amet tortor sed ipsum sagittis faucibus. Integer tincidunt, erat eget volutpat lobortis, eros erat accumsan lorem, et volutpat est ex a turpis. Fusce at ante a est iaculis rutrum. Ut nunc est, maximus id tellus ut, semper consequat mi. Vivamus est massa, maximus vel orci sit amet, laoreet finibus massa. Morbi a urna vitae leo eleifend iaculis et ac ante. ', true, 0.00, '2876444');

insert into terrazzacostumers (Nome, Sobrenome, Telefone, Email, Salao, Pessoas, Data, Hora, Mesa, Situacao, Observacao, Aguardando, Pagamento, IdExterno) values
('Lohane', 'Vêkanandre Sthephany Smith Bueno de HA HA HA de Raio Laser bala de Icekiss', '5541998642881', 'cabeleleilaleila@hotmail.com', 'Almoço Terrazza 40', 3, CURDATE(), '12:00', '1', 'Novo', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent nec augue id lectus lacinia tristique quis sed elit. In lorem diam, posuere vel nisl nec, sodales ullamcorper diam. Nulla sollicitudin tincidunt massa, a vehicula leo aliquet et. In in iaculis ipsum. Donec iaculis ante eget nibh efficitur rhoncus. Donec ipsum metus, porta ut eleifend sit amet, commodo vel nisl. Donec sit amet tortor sed ipsum sagittis faucibus. Integer tincidunt, erat eget volutpat lobortis, eros erat accumsan lorem, et volutpat est ex a turpis. Fusce at ante a est iaculis rutrum. Ut nunc est, maximus id tellus ut, semper consequat mi. Vivamus est massa, maximus vel orci sit amet, laoreet finibus massa. Morbi a urna vitae leo eleifend iaculis et ac ante. ', true, 0.00, '2876444');

insert into terrazzacostumers (Nome, Sobrenome, Telefone, Email, Salao, Pessoas, Data, Hora, Mesa, Situacao, Observacao, Aguardando, Pagamento, IdExterno) values
('Policial', 'Desfarçado', '5541998642881', 'cabeleleilaleila@hotmail.com', 'Jantar no Terrazza 40', 3, CURDATE(), '12:00', '1', 'Novo', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent nec augue id lectus lacinia tristique quis sed elit. In lorem diam, posuere vel nisl nec, sodales ullamcorper diam. Nulla sollicitudin tincidunt massa, a vehicula leo aliquet et. In in iaculis ipsum. Donec iaculis ante eget nibh efficitur rhoncus. Donec ipsum metus, porta ut eleifend sit amet, commodo vel nisl. Donec sit amet tortor sed ipsum sagittis faucibus. Integer tincidunt, erat eget volutpat lobortis, eros erat accumsan lorem, et volutpat est ex a turpis. Fusce at ante a est iaculis rutrum. Ut nunc est, maximus id tellus ut, semper consequat mi. Vivamus est massa, maximus vel orci sit amet, laoreet finibus massa. Morbi a urna vitae leo eleifend iaculis et ac ante. ', true, 0.00, '2876444');

/** testes de insert na waitingcostumers **/

insert into waitingcostumers (Nome, Sobrenome, Telefone, Salao, Pessoas, Data, HoraChegada, HoraSentada, Mesa, Situacao, Observacao)
values ('Felipe', 'Smith', '5541995655618', 'Terrazza Jantar', 2, CURDATE(), '19:00', null, null, 'Novo', 'nadegas a declaras');

insert into waitingcostumers (Nome, Sobrenome, Telefone, Salao, Pessoas, Data, HoraChegada, HoraSentada, Mesa, Situacao, Observacao)
values ('Cabeleleila', 'Leila', '5541995655618', 'Terrazza Jantar', 2, CURDATE(), '19:00', null, null, 'Novo', 'nadegas a declaras');

insert into waitingcostumers (Nome, Sobrenome, Telefone, Salao, Pessoas, Data, HoraChegada, HoraSentada, Mesa, Situacao, Observacao)
values ('Lohane', 'Vêkanandre Sthephany Smith Bueno de HA HA HA de Raio Laser bala de Icekiss', '5541998642881', 'Terrazza Jantar', 2, CURDATE(), '19:00', null, null, 'Novo', 'nadegas a declaras');

/** testes de insert do standardmessages **/

insert into standardmessages (Titulo, Mensagem, MensagemPadrao)
values ('Cliente adicionado à espera', 'Olá, %s. Aqui é do restaurante Terrazza 40. Você entrou em nossa fila de espera. Estamos agendando a chegada das pessoas da fila no restaurante. Seu horário de chegada é às %s. Ficaremos aguardando. Lembrando que pode ser que aguarde um pouco a mais por uma mesa quando você chegar.', 'Olá, %s. Aqui é do restaurante Terrazza 40. Você entrou em nossa fila de espera. Estamos agendando a chegada das pessoas da fila no restaurante. Seu horário de chegada é às %s. Ficaremos aguardando. Lembrando que pode ser que aguarde um pouco a mais por uma mesa quando você chegar.');

insert into standardmessages (Titulo, Mensagem, MensagemPadrao)
values ('Confirmação de reserva', 'Olá, %s. Aqui é do restaurante Terrazza 40. Temos uma reserva sua hoje às %s para %d pessoas. Você confirma a reserva? Confirma também o númeo de pessoas? Ficamos no aguardo de sua resposta.', 'Olá, %s. Aqui é do restaurante Terrazza 40. Temos uma reserva sua hoje às %s para %d pessoas. Você confirma a reserva? Confirma também o númeo de pessoas? Ficamos no aguardo de sua resposta.');

/* testes de select */

SELECT * FROM skycuritibacostumers.terrazzacostumers;

SELECT * FROM skycuritibacostumers.terrazzacostumers
WHERE Id = 2;

SELECT * FROM skycuritibacostumers.terrazzacostumers
WHERE Data = CURDATE();

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

SELECT * FROM skycuritibacostumers.waitingcostumers;

SELECT * FROM skycuritibacostumers.waitingcostumers
WHERE DATE(Data) = CURDATE();

SELECT * FROM skycuritibacostumers.standardmessages;

SELECT * FROM skycuritibacostumers.standardmessages
WHERE Titulo = 'Cliente adicionado à espera';

/** updates a serem usados no nos costumer.dao.impl **/
UPDATE terrazzacostumers
SET Nome = ?, Sobrenome = ?, Telefone = ?, Email = ?, Salao = ?, Pessoas = ?, Data = ?, Hora = ?, Mesa = ?, Situacao = ?, Observacao = ?, Pagamento = ?
WHERE IdExterno = ? AND Situacao !=  'Cancelado por no-show' AND Situacao != 'Sentado';

UPDATE terrazzacostumers
SET Pessoas = ?, Mesa = ?, Situacao = ?, Observacao = ?, Aguardando = ?
WHERE IdExterno = ?;

/** updates a serem usados no nos waitingcostumer.dao.impl **/
UPDATE waitingcostumers
SET Nome = ?, Sobrenome = ?, Telefone = ?, Salao = ?, Pessoas = ?, Data = ?, HoraChegada = ?, HoraSentada = ?, Mesa = ?, Situacao = ?, Observacao = ?, Aguardando = ?
WHERE Id = ?;

/** teste
UPDATE terrazzacostumers
SET Nome = 'vamos ver se mudou', Sobrenome = 'a', Telefone = 'a', Email = 'a', Salao = 'Jantar no Terrazza 40', Pessoas = 1, Data = CURDATE(), Hora = '19:00', Mesa = '1', Situacao = 'Cancelado por no-show', Observacao = 'a', Pagamento = 0.00
WHERE IdExterno = '2876444' AND Situacao !=  'Cancelado por no-show' AND Situacao != 'Sentado';
**/

/** update da mensagem padrão **/

UPDATE skycuritibacostumers.standardmessages
SET Mensagem = 'Olá, %s. Aqui é do restaurante Terrazza 40.\nTemos uma reserva sua hoje às %s para %d pessoas.\nVocê confirma a reserva? Confirma também o número de pessoas? Ficamos no aguardo de sua resposta.\n\nAcesse o link para mais informações sobre nosso estabelecimento:\nhttps://linktr.ee/terrazza40'
WHERE Id = 2;

/** update para colocar cientes na database no domingo para poder fazer testes **/
update terrazzacostumers
set Data = CURDATE()
where DATE(Data) = '2021-12-18';

/** só para limpar os dados para testes **/
DELETE FROM skycuritibacostumers.terrazzacostumers;

DELETE FROM skycuritibacostumers.waitingcostumers
where Date(Data) = curdate();

DELETE FROM skycuritibacostumers.costumer_x_standardmessage;

DROP TABLE skycuritibacostumers.terrazzacostumers;

drop table skycuritibacostumers.costumer_x_standardmessage;