CREATE DATABASE movies;

USE movies;

CREATE TABLE imdb (
	imdb_id varchar(16) NOT NULL,
	vote_average float DEFAULT 0.0 NOT NULL,
	vote_count int DEFAULT 0 NOT NULL,
	release_date date NOT NULL,
	revenue decimal(15,2) DEFAULT 1000000 NOT NULL,
	budget decimal(15,2) DEFAULT 1000000 NOT NULL,
	runtime int DEFAULT 90 NOT NULL,
	CONSTRAINT pk_imdb_id PRIMARY KEY (imdb_id)
);