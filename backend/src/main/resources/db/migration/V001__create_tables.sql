-- Crear tabla de juegos
CREATE TABLE juegos (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  min_jugadores INT NOT NULL,
  max_jugadores INT NOT NULL,
  tiempo_estimado INT NOT NULL -- Duración estimada en minutos
);

-- Crear tabla de usuarios
CREATE TABLE usuarios (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nombre_usuario VARCHAR(100) NOT NULL UNIQUE,
  correo_electronico VARCHAR(100) NOT NULL UNIQUE,
  contrasena VARCHAR(255) NOT NULL,
  latitud_hogar DECIMAL(9,6),
  longitud_hogar DECIMAL(9,6)
);

-- Crear tabla de usuarios siguiendo a otros usuarios (relación n:n)
CREATE TABLE usuarios_siguiendo (
  id_usuario INT,
  id_usuario_siguiendo INT,
  fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id_usuario, id_usuario_siguiendo),
  FOREIGN KEY (id_usuario) REFERENCES usuarios(id),
  FOREIGN KEY (id_usuario_siguiendo) REFERENCES usuarios(id)
);

-- Crear tabla de eventos
CREATE TABLE eventos (
  id INT AUTO_INCREMENT PRIMARY KEY,
  titulo VARCHAR(100) NOT NULL,
  descripcion TEXT,
  juego_id INT NOT NULL,
  latitud DECIMAL(9,6),
  longitud DECIMAL(9,6),
  creador INT NOT NULL,
  min_personas INT NOT NULL,
  max_personas INT NOT NULL,
  fecha_inicio_inscripciones DATETIME NOT NULL,
  fecha_fin_inscripciones DATETIME,
  fecha_evento DATETIME NOT NULL,
  estado enum('pendiente', 'lleno', 'finalizado') NOT NULL,
  FOREIGN KEY (creador) REFERENCES usuarios(id),
  FOREIGN KEY (juego_id) REFERENCES juegos(id),
  CHECK (min_personas <= max_personas)
);

-- Crear tabla de relación entre eventos y usuarios
CREATE TABLE eventos_usuarios (
  id_evento INT,
  id_usuario INT,
  fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id_evento, id_usuario),
  FOREIGN KEY (id_evento) REFERENCES eventos(id),
  FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
);
