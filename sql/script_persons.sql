-- Crear tabla
DROP TABLE persons CASCADE CONSTRAINTS;

CREATE TABLE persons (
    id NUMBER PRIMARY KEY,
    name VARCHAR2(100) NOT NULL,
    birth_date DATE NOT NULL
);

COMMIT;

-- Secuencia
DROP SEQUENCE seq_persons;

CREATE SEQUENCE seq_persons
START WITH 1
INCREMENT BY 1
NOCACHE
NOCYCLE;

-- Trigger de autoincremento
CREATE OR REPLACE TRIGGER trg_persons_id
BEFORE INSERT ON persons
FOR EACH ROW
WHEN (new.id IS NULL)
BEGIN
    :new.id := seq_persons.NEXTVAL;
END;
/

-- Procedimiento almacenado para insertar
CREATE OR REPLACE PROCEDURE insert_person (
    p_name       IN VARCHAR2,
    p_birth_date IN DATE
) AS
BEGIN
    INSERT INTO persons (id, name, birth_date)
    VALUES (seq_persons.NEXTVAL, p_name, p_birth_date);
    COMMIT;
END;
/

-- Procedimiento almacenado para listar (opcional)
CREATE OR REPLACE PROCEDURE list_persons (
    p_cursor OUT SYS_REFCURSOR
) AS
BEGIN
    OPEN p_cursor FOR
        SELECT id, name, birth_date FROM persons ORDER BY id;
END;
/

-- Insertar datos de ejemplo
BEGIN
    insert_person('Carlos Pérez', TO_DATE('1990-05-21','YYYY-MM-DD'));
    insert_person('María Gómez', TO_DATE('1985-10-15','YYYY-MM-DD'));
    insert_person('Andrés Torres', TO_DATE('2000-03-02','YYYY-MM-DD'));
END;
/

COMMIT;

SELECT * FROM PERSONS;