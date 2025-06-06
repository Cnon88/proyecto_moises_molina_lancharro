DELIMITER //

CREATE TRIGGER after_insert_eventos_usuarios
AFTER INSERT ON eventos_usuarios
FOR EACH ROW
BEGIN
    DECLARE usuarios_inscritos INT;
    DECLARE max_personas_evento INT;

    -- Obtener el número actual de usuarios inscritos en el evento
    SELECT COUNT(*) INTO usuarios_inscritos
    FROM eventos_usuarios
    WHERE id_evento = NEW.id_evento;

    -- Obtener el número máximo de personas permitidas para el evento
    SELECT max_personas INTO max_personas_evento
    FROM eventos
    WHERE id = NEW.id_evento;

    -- Si el número de usuarios inscritos iguala el máximo permitido, marcar el evento como lleno
    IF usuarios_inscritos >= max_personas_evento THEN
        UPDATE eventos
        SET estado = 'lleno'
        WHERE id = NEW.id_evento AND estado = 'pendiente';
    END IF;
END //

DELIMITER ;