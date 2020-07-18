DROP TABLE people_b IF EXISTS;

CREATE TABLE people_b  (
    person_id BIGINT IDENTITY NOT NULL PRIMARY KEY,
    first_name VARCHAR(20),
    last_name VARCHAR(20)
);
