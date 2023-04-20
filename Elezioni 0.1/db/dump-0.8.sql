--
-- PostgreSQL database dump
--

-- Dumped from database version 9.1.9
-- Dumped by pg_dump version 9.1.9
-- Started on 2013-08-20 01:32:20 CEST

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 169 (class 3079 OID 11681)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 1955 (class 0 OID 0)
-- Dependencies: 169
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 167 (class 1259 OID 49516)
-- Dependencies: 1918 5
-- Name: candidatura; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE candidatura (
    id_elezione integer NOT NULL,
    id_utente integer NOT NULL,
    nr_voti integer DEFAULT 0
);


ALTER TABLE public.candidatura OWNER TO postgres;

--
-- TOC entry 164 (class 1259 OID 49488)
-- Dependencies: 1912 5
-- Name: carica; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE carica (
    id_carica bigint NOT NULL,
    titolo character varying(45) NOT NULL,
    durata integer NOT NULL,
    CONSTRAINT carica_durata_check CHECK (((durata > 0) AND (durata <= 7)))
);


ALTER TABLE public.carica OWNER TO postgres;

--
-- TOC entry 163 (class 1259 OID 49486)
-- Dependencies: 164 5
-- Name: carica_id_carica_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE carica_id_carica_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.carica_id_carica_seq OWNER TO postgres;

--
-- TOC entry 1956 (class 0 OID 0)
-- Dependencies: 163
-- Name: carica_id_carica_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE carica_id_carica_seq OWNED BY carica.id_carica;


--
-- TOC entry 166 (class 1259 OID 49499)
-- Dependencies: 1914 1915 1916 1917 5
-- Name: elezione; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE elezione (
    id_elezione bigint NOT NULL,
    data_crea date DEFAULT ('now'::text)::date,
    data_ini date NOT NULL,
    data_fine date NOT NULL,
    descr character varying(45),
    stato boolean DEFAULT true, --si disattiva quando scade il periodo candidature e non ci sono candidati 
    carica integer NOT NULL,
    --remove comment after test:
    --CONSTRAINT elezione_data_crea_check CHECK ((data_crea = ('now'::text)::date)),
    CONSTRAINT elezione_check CHECK ((data_fine > data_ini)),        
    CONSTRAINT elezione_data_ini_check CHECK ((data_ini > data_crea))
);


ALTER TABLE public.elezione OWNER TO postgres;

--
-- TOC entry 165 (class 1259 OID 49497)
-- Dependencies: 5 166
-- Name: elezione_id_elezione_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE elezione_id_elezione_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.elezione_id_elezione_seq OWNER TO postgres;

--
-- TOC entry 1957 (class 0 OID 0)
-- Dependencies: 165
-- Name: elezione_id_elezione_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE elezione_id_elezione_seq OWNED BY elezione.id_elezione;


--
-- TOC entry 162 (class 1259 OID 49476)
-- Dependencies: 1909 1910 5
-- Name: utente; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE utente (
    id_utente bigint NOT NULL,
    nome character varying(25) NOT NULL,
    cognome character varying(25) NOT NULL,
    attivo boolean DEFAULT true,
    is_admin boolean DEFAULT false,
    account character varying(30) NOT NULL,
    password character(8) NOT NULL
);


ALTER TABLE public.utente OWNER TO postgres;

--
-- TOC entry 161 (class 1259 OID 49474)
-- Dependencies: 162 5
-- Name: utente_id_utente_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE utente_id_utente_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.utente_id_utente_seq OWNER TO postgres;

--
-- TOC entry 1958 (class 0 OID 0)
-- Dependencies: 161
-- Name: utente_id_utente_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE utente_id_utente_seq OWNED BY utente.id_utente;


--
-- TOC entry 168 (class 1259 OID 49532)
-- Dependencies: 5
-- Name: votazione; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE votazione (
    id_elezione bigint NOT NULL,
    id_utente bigint NOT NULL,
    ha_votato boolean NOT NULL
);


ALTER TABLE public.votazione OWNER TO postgres;

--
-- TOC entry 1911 (class 2604 OID 49491)
-- Dependencies: 164 163 164
-- Name: id_carica; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY carica ALTER COLUMN id_carica SET DEFAULT nextval('carica_id_carica_seq'::regclass);


--
-- TOC entry 1913 (class 2604 OID 49502)
-- Dependencies: 165 166 166
-- Name: id_elezione; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY elezione ALTER COLUMN id_elezione SET DEFAULT nextval('elezione_id_elezione_seq'::regclass);


--
-- TOC entry 1908 (class 2604 OID 49479)
-- Dependencies: 161 162 162
-- Name: id_utente; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY utente ALTER COLUMN id_utente SET DEFAULT nextval('utente_id_utente_seq'::regclass);


--
-- TOC entry 1946 (class 0 OID 49516)
-- Dependencies: 167 1948
-- Data for Name: candidatura; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 1943 (class 0 OID 49488)
-- Dependencies: 164 1948
-- Data for Name: carica; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO carica (id_carica, titolo, durata) VALUES (1, 'Carica1', 3);
INSERT INTO carica (id_carica, titolo, durata) VALUES (2, 'Cariica2', 1);
INSERT INTO carica (id_carica, titolo, durata) VALUES (3, 'Carica3', 4);
INSERT INTO carica (id_carica, titolo, durata) VALUES (4, 'Carica4', 1);
INSERT INTO carica (id_carica, titolo, durata) VALUES (5, 'Carica5', 1);
INSERT INTO carica (id_carica, titolo, durata) VALUES (6, 'Carica6', 3);
INSERT INTO carica (id_carica, titolo, durata) VALUES (7, 'CaricaA', 2);
INSERT INTO carica (id_carica, titolo, durata) VALUES (8, 'CaricaB', 2);
INSERT INTO carica (id_carica, titolo, durata) VALUES (9, 'CaricaEE', 4);


--
-- TOC entry 1959 (class 0 OID 0)
-- Dependencies: 163
-- Name: carica_id_carica_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('carica_id_carica_seq', 9, true);


--
-- TOC entry 1945 (class 0 OID 49499)
-- Dependencies: 166 1948
-- Data for Name: elezione; Type: TABLE DATA; Schema: public; Owner: postgres
--

/*
 for test

INSERT INTO elezione (id_elezione, data_crea, data_ini, data_fine, descr, carica) VALUES (1, CURRENT_DATE, CURRENT_DATE + INTERVAL '1 day', CURRENT_DATE + INTERVAL '2 day', 'Elezione1', 8);
INSERT INTO elezione (id_elezione, data_crea, data_ini, data_fine, descr, carica) VALUES (2, CURRENT_DATE, CURRENT_DATE + INTERVAL '2 day', CURRENT_DATE + INTERVAL '3 day', 'Elezione2', 7);
INSERT INTO elezione (id_elezione, data_crea, data_ini, data_fine, descr, carica) VALUES (3, CURRENT_DATE, CURRENT_DATE + INTERVAL '3 day', CURRENT_DATE + INTERVAL '4 day', 'Elezione3', 6);
INSERT INTO elezione (id_elezione, data_crea, data_ini, data_fine, descr, carica) VALUES (4, CURRENT_DATE, CURRENT_DATE + INTERVAL '4 day', CURRENT_DATE + INTERVAL '5 day', 'Elezione4', 5);
INSERT INTO elezione (id_elezione, data_crea, data_ini, data_fine, descr, carica) VALUES (5, CURRENT_DATE, CURRENT_DATE + INTERVAL '5 day', CURRENT_DATE + INTERVAL '6 day', 'Elezione5', 4);
INSERT INTO elezione (id_elezione, data_crea, data_ini, data_fine, descr, carica) VALUES (6, CURRENT_DATE, CURRENT_DATE + INTERVAL '6 day', CURRENT_DATE + INTERVAL '7 day', 'Elezione4', 3);
INSERT INTO elezione (id_elezione, data_crea, data_ini, data_fine, descr, carica) VALUES (7, CURRENT_DATE, CURRENT_DATE + INTERVAL '7 day', CURRENT_DATE + INTERVAL '8 day', 'Elezione7', 2);
INSERT INTO elezione (id_elezione, data_crea, data_ini, data_fine, descr, carica) VALUES (8, CURRENT_DATE, CURRENT_DATE + INTERVAL '8 day', CURRENT_DATE + INTERVAL '9 day', 'Elezione8', 1);

*/



--
-- TOC entry 1960 (class 0 OID 0)
-- Dependencies: 165
-- Name: elezione_id_elezione_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('elezione_id_elezione_seq', 7, true);


--
-- TOC entry 1941 (class 0 OID 49476)
-- Dependencies: 162 1948
-- Data for Name: utente; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO utente (id_utente, nome, cognome, attivo, is_admin, account, password) VALUES (2, 'utente1', 'cognome1', true, false, 'utente1@elezioni.it', 'utente1 ');
INSERT INTO utente (id_utente, nome, cognome, attivo, is_admin, account, password) VALUES (3, 'utente2', 'cognome2', true, false, 'utente2@elezioni.it', 'utente2 ');
INSERT INTO utente (id_utente, nome, cognome, attivo, is_admin, account, password) VALUES (4, 'utente3', 'cognome3', true, false, 'utente3@elezioni.it', 'utente3 ');
INSERT INTO utente (id_utente, nome, cognome, attivo, is_admin, account, password) VALUES (5, 'utente4', 'cognome4', true, false, 'utente4@elezioni.it', 'utente4 ');
INSERT INTO utente (id_utente, nome, cognome, attivo, is_admin, account, password) VALUES (7, 'utente6', 'cognome6', true, false, 'utente6@elezioni.it', 'utente6 ');
INSERT INTO utente (id_utente, nome, cognome, attivo, is_admin, account, password) VALUES (6, 'utente5', 'cognome5', true, false, 'utente5@elezioni.it', 'utente5 ');
INSERT INTO utente (id_utente, nome, cognome, attivo, is_admin, account, password) VALUES (9, 'utente8', 'cognome8', true, false, 'utente8@elezioni.it', 'utente8 ');
INSERT INTO utente (id_utente, nome, cognome, attivo, is_admin, account, password) VALUES (13, 'utente12', 'cognome12', true, false, 'utente12@elezioni.it', 'utente12');
INSERT INTO utente (id_utente, nome, cognome, attivo, is_admin, account, password) VALUES (11, 'utente10', 'cognome10', true, false, 'utente10@elezioni.it', 'utente10');
INSERT INTO utente (id_utente, nome, cognome, attivo, is_admin, account, password) VALUES (10, 'utente9', 'cognome9', true, false, 'utente9@elezioni.it', 'utente9 ');
INSERT INTO utente (id_utente, nome, cognome, attivo, is_admin, account, password) VALUES (14, 'utente13', 'cognome13', true, true, 'admin13@elezioni.it', 'utente13');
INSERT INTO utente (id_utente, nome, cognome, attivo, is_admin, account, password) VALUES (8, 'utente7', 'cognome7', false, false, 'utente7@elezioni.it', 'utente7 ');
INSERT INTO utente (id_utente, nome, cognome, attivo, is_admin, account, password) VALUES (1, 'admin', 'cognomeA', false, true, 'admin@elezioni.it', 'admin   ');
INSERT INTO utente (id_utente, nome, cognome, attivo, is_admin, account, password) VALUES (12, 'utente11', 'cognome11', false, false, 'utente11@elezioni.it', 'utente11');


--
-- TOC entry 1961 (class 0 OID 0)
-- Dependencies: 161
-- Name: utente_id_utente_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('utente_id_utente_seq', 14, true);


--
-- TOC entry 1947 (class 0 OID 49532)
-- Dependencies: 168 1948
-- Data for Name: votazione; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 1932 (class 2606 OID 49521)
-- Dependencies: 167 167 167 1949
-- Name: candidatura_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY candidatura
    ADD CONSTRAINT candidatura_pkey PRIMARY KEY (id_elezione, id_utente);


--
-- TOC entry 1924 (class 2606 OID 49494)
-- Dependencies: 164 164 1949
-- Name: carica_id_carica_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY carica
    ADD CONSTRAINT carica_id_carica_key UNIQUE (id_carica);


--
-- TOC entry 1926 (class 2606 OID 49496)
-- Dependencies: 164 164 1949
-- Name: carica_titolo_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY carica
    ADD CONSTRAINT carica_titolo_key UNIQUE (titolo);


--
-- TOC entry 1928 (class 2606 OID 49510)
-- Dependencies: 166 166 1949
-- Name: elezione_carica_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY elezione
    ADD CONSTRAINT elezione_carica_key UNIQUE (carica);


--
-- TOC entry 1930 (class 2606 OID 49508)
-- Dependencies: 166 166 1949
-- Name: elezione_id_elezione_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY elezione
    ADD CONSTRAINT elezione_id_elezione_key UNIQUE (id_elezione);


--
-- TOC entry 1920 (class 2606 OID 49485)
-- Dependencies: 162 162 1949
-- Name: utente_account_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY utente
    ADD CONSTRAINT utente_account_key UNIQUE (account);


--
-- TOC entry 1922 (class 2606 OID 49483)
-- Dependencies: 162 162 1949
-- Name: utente_id_utente_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY utente
    ADD CONSTRAINT utente_id_utente_key UNIQUE (id_utente);


--
-- TOC entry 1934 (class 2606 OID 49536)
-- Dependencies: 168 168 168 1949
-- Name: votazione_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY votazione
    ADD CONSTRAINT votazione_pkey PRIMARY KEY (id_elezione, id_utente);


--
-- TOC entry 1936 (class 2606 OID 49522)
-- Dependencies: 1929 166 167 1949
-- Name: candidatura_id_elezione_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY candidatura
    ADD CONSTRAINT candidatura_id_elezione_fkey FOREIGN KEY (id_elezione) REFERENCES elezione(id_elezione);


--
-- TOC entry 1937 (class 2606 OID 49527)
-- Dependencies: 167 162 1921 1949
-- Name: candidatura_id_utente_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY candidatura
    ADD CONSTRAINT candidatura_id_utente_fkey FOREIGN KEY (id_utente) REFERENCES utente(id_utente);


--
-- TOC entry 1935 (class 2606 OID 49511)
-- Dependencies: 164 166 1923 1949
-- Name: elezione_carica_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY elezione
    ADD CONSTRAINT elezione_carica_fkey FOREIGN KEY (carica) REFERENCES carica(id_carica);


--
-- TOC entry 1938 (class 2606 OID 49537)
-- Dependencies: 166 168 1929 1949
-- Name: votazione_id_elezione_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY votazione
    ADD CONSTRAINT votazione_id_elezione_fkey FOREIGN KEY (id_elezione) REFERENCES elezione(id_elezione);


--
-- TOC entry 1939 (class 2606 OID 49542)
-- Dependencies: 168 162 1921 1949
-- Name: votazione_id_utente_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY votazione
    ADD CONSTRAINT votazione_id_utente_fkey FOREIGN KEY (id_utente) REFERENCES utente(id_utente);


--
-- TOC entry 1954 (class 0 OID 0)
-- Dependencies: 5
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;




/* Trigger che annulla inserimento di una candidatura di un utente che si è già candidato per una 
 * elezione in corso. 
 * Una elezione è in corso in uno dei due casi:
 * -è in attesa di registrazione di candidati
 * -è in attesa di registrazione voti
 * Si blocca facilmente il tentativo di un utente di inserire la stessa candidatura per la stessa elezione
 * per mezzo della chiave composta (id_elezione, id_utente).
 * Diventa un po piu difficile gestire il tentativo da parte di un utente di registrarsi ad
 * un'altra elezione nonostante la presenza di una sua candidatura per altra elezione in corso.
 * Per risolvere uso questo trigger.
 * */
DROP TRIGGER IF EXISTS utenteGiaCandidatoTrigger ON candidatura CASCADE;

CREATE OR REPLACE FUNCTION utenteGiaCandidato() 
RETURNS TRIGGER AS $$
DECLARE
	count_user INTEGER;
BEGIN
	SELECT COUNT(*) 
	
	INTO count_user 
           	
	FROM
		(SELECT id_utente,
	       id_elezione,
	       ha_votato ,
	       NULL AS nr_voti
		FROM votazione
		WHERE id_utente = NEW.id_utente
		  UNION
		SELECT id_utente,
		       id_elezione ,
		       NULL AS ha_votato,
		       nr_voti 
		FROM candidatura
		WHERE id_utente = NEW.id_utente
		) as t1


		WHERE t1.id_elezione IN (
		--elezione in corso : dalla data di creazione (fase candidati) alla data_fine-1 (ultimo giorno per votare)
        	SELECT e.id_elezione
		  	FROM   elezione e
		  	WHERE  data_fine > current_date 
		  	AND data_crea <= current_date
		) 
		
		AND id_utente = NEW.id_utente;
		
  IF count_user > 0 THEN
	RAISE EXCEPTION 'Errore di inserimento in candidatura : id_elezione % e id_utente %. Utente gia candidato per una elezione in corso', NEW.id_elezione, NEW.id_utente;
  END IF;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER utenteGiaCandidatoTrigger
BEFORE INSERT OR UPDATE ON candidatura
FOR EACH ROW EXECUTE PROCEDURE utenteGiaCandidato();

-- Completed on 2013-08-20 01:32:20 CEST

--
-- PostgreSQL database dump complete
--
