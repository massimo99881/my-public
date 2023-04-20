CREATE TABLE pda (  
  id_pda SERIAL PRIMARY KEY ,
  id_cliente BIGINT NULL ,
  data DATE NULL ,
  luogo VARCHAR(45) NULL ,
  sped_indirizzo VARCHAR(145) NULL ,
  sped_n_civico VARCHAR(10) NULL 
); 

CREATE TABLE pda_cliente_sede (  
  id_sede SERIAL PRIMARY KEY ,
  ragsoc VARCHAR(100) NULL ,
  indirizzo VARCHAR(145) NULL ,
  cap VARCHAR(5) NULL ,
  comune VARCHAR(45) NULL ,
  n_civico VARCHAR(5) NULL ,
  id_pda BIGINT NOT NULL
); 