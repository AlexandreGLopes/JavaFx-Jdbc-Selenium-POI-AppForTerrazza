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
    Situacao varchar(40) NOT NULL,	
    Pagamento double NOT NULL,
    IdExterno varchar(30) NOT NULL,
    PRIMARY KEY (Id)
);

ALTER TABLE terrazzacostumers
ADD COLUMN Observacao text AFTER Situacao;

ALTER TABLE terrazzacostumers
ADD COLUMN Aguardando boolean AFTER Observacao;

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

insert into standardmessages (Titulo, Mensagem, MensagemPadrao)
values ('Cliente adicionado à espera', 'Olá, %s. Aqui é do restaurante Terrazza 40. Você entrou em nossa fila de espera. Estamos agendando a chegada das pessoas da fila no restaurante. Seu horário de chegada é às %s. Ficaremos aguardando. Lembrando que pode ser que aguarde um pouco a mais por uma mesa quando você chegar.', 'Olá, %s. Aqui é do restaurante Terrazza 40. Você entrou em nossa fila de espera. Estamos agendando a chegada das pessoas da fila no restaurante. Seu horário de chegada é às %s. Ficaremos aguardando. Lembrando que pode ser que aguarde um pouco a mais por uma mesa quando você chegar.');

insert into standardmessages (Titulo, Mensagem, MensagemPadrao)
values ('Confirmação de reserva', 'Olá, %s. Aqui é do restaurante Terrazza 40. Temos uma reserva sua hoje às %s para %d pessoas. Você confirma a reserva? Confirma também o númeo de pessoas? Ficamos no aguardo de sua resposta.', 'Olá, %s. Aqui é do restaurante Terrazza 40. Temos uma reserva sua hoje às %s para %d pessoas. Você confirma a reserva? Confirma também o númeo de pessoas? Ficamos no aguardo de sua resposta.');

UPDATE skycuritibacostumers.standardmessages
SET Mensagem = 'Olá, %s. Aqui é do restaurante Terrazza 40.\nTemos uma reserva sua hoje às %s para %d pessoas.\nVocê confirma a reserva? Confirma também o número de pessoas? Ficamos no aguardo de sua resposta.\n\nAcesse o link para mais informações sobre nosso estabelecimento:\nhttps://linktr.ee/terrazza40', MensagemPadrao = 'Olá, %s. Aqui é do restaurante Terrazza 40.\nTemos uma reserva sua hoje às %s para %d pessoas.\nVocê confirma a reserva? Confirma também o número de pessoas? Ficamos no aguardo de sua resposta.\n\nAcesse o link para mais informações sobre nosso estabelecimento:\nhttps://linktr.ee/terrazza40'
WHERE Id = 2;


DELETE FROM skycuritibacostumers.terrazzacostumers;

CREATE USER 'developer'@'localhost' IDENTIFIED BY '123456';
GRANT ALL PRIVILEGES ON * . * TO 'developer'@'localhost';
FLUSH PRIVILEGES;