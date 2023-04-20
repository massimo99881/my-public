
--create utente
CREATE SEQUENCE utente_id_utente_seq;

CREATE TABLE utente (
  id_utente bigint UNIQUE NOT NULL DEFAULT nextval('utente_id_utente_seq'),
  nome VARCHAR (25) NOT NULL,
  cognome VARCHAR (25) NOT NULL,
  attivo BOOLEAN DEFAULT TRUE,
  is_admin BOOLEAN DEFAULT FALSE,
  /*login parameters*/
  account VARCHAR (30) UNIQUE NOT NULL, -- è indirizzo email
  password CHAR (8) NOT NULL
);

ALTER SEQUENCE utente_id_utente_seq OWNED BY utente.id_utente;
--
-- insert into utente
/*un admin puo disattivare gli utenti che si sono registrati
  un admin puo essere registrato solo dal DBA
  Il DBA puo disattivare un admin
*/
INSERT INTO utente (nome,cognome,attivo,is_admin,account,password)
VALUES ('massimo','galasi',false,true,'admin@elezioni.it','admin');
INSERT INTO utente (nome,cognome,attivo,is_admin,account,password)
VALUES ('utente13','cognome13',true,true,'admin13@elezioni.it','utente13');


-- create carica
CREATE SEQUENCE carica_id_carica_seq;
CREATE TABLE carica (
  id_carica bigint UNIQUE NOT NULL DEFAULT nextval('carica_id_carica_seq'),
  titolo VARCHAR (45) UNIQUE NOT NULL,
  durata INT NOT NULL CHECK (durata>0 AND durata<=7) -- in anni
);
ALTER SEQUENCE carica_id_carica_seq OWNED BY carica.id_carica;
-- 


--create elezione

/* Deve sempre passare almeno un giorno dalla data di inizio candidature
   alla data di inzio votazioni 
   a alla data di inizio carica del candidato che è stato eletto
 */
CREATE SEQUENCE elezione_id_elezione_seq;

CREATE TABLE elezione (
  id_elezione bigint UNIQUE NOT NULL DEFAULT nextval('elezione_id_elezione_seq'),
  data_crea DATE DEFAULT current_date 
  	CHECK(data_crea = current_date), --data creazione elezione e inizio candidature 
  data_ini DATE NOT NULL 
  	CHECK(data_ini > current_date ), --data inizio votazioni 
  data_fine DATE NOT NULL
  	CHECK(data_fine > data_ini ),  --data inizio carica 
  descr VARCHAR (45), -- responsabilità
  carica INT UNIQUE NOT NULL,
  FOREIGN KEY (carica) REFERENCES carica (id_carica)
);

ALTER SEQUENCE elezione_id_elezione_seq OWNED BY elezione.id_elezione;
--NOTICE:  CREATE TABLE will create implicit sequence "elezione_id_elezione_seq" for serial column "elezione.id_elezione"


-- create candidatura


CREATE TABLE candidatura (
  id_elezione bigint ,
  id_utente bigint ,
  nr_voti INT DEFAULT 0,
  PRIMARY KEY (id_elezione, id_utente),
  FOREIGN KEY (id_elezione) REFERENCES elezione (id_elezione),
  FOREIGN KEY (id_utente) REFERENCES utente (id_utente)
); 

--NOTICE:  CREATE TABLE / PRIMARY KEY will create implicit index "candidatura_pkey" for table "candidatura"

-- create votazione
CREATE TABLE votazione (
  id_elezione bigint ,
  id_utente bigint ,
  ha_votato BOOLEAN NOT NULL, -- T = votato, F=scheda nulla
  PRIMARY KEY (id_elezione, id_utente),
  FOREIGN KEY (id_elezione) REFERENCES elezione (id_elezione),
  FOREIGN KEY (id_utente) REFERENCES utente (id_utente)
  -- ATTENZIONE:
  -- se si aggiorna un voto perche l'utente ha cambiato idea e 
  -- vuole votare un altro candidato, bisogna necessariamente verificare che
  -- la data per le votazioni non sia scaduta.
);

--NOTICE:  CREATE TABLE / PRIMARY KEY will create implicit index "votazione_pkey" for table "votazione"


------------------------------------------------------------------------
/*Alcune query*/
SET DATESTYLE TO SQL;

--calculate postgreSQL database size in disk
--SELECT pg_size_pretty(pg_database_size('geekdb'));

--calculate postgreSQL table size in disk:
--SELECT pg_size_pretty(pg_total_relation_size('big_table'));
