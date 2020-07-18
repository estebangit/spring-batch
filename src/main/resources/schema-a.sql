DROP TABLE people_a IF EXISTS;

CREATE TABLE people_a  (
    person_id BIGINT IDENTITY NOT NULL PRIMARY KEY,
    first_name VARCHAR(20),
    last_name VARCHAR(20)
);
