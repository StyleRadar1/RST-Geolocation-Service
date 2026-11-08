CREATE TABLE geolocalizacion.categoria_almacen (
    id     UUID PRIMARY KEY,
    nombre VARCHAR(60) NOT NULL
);

CREATE TABLE geolocalizacion.almacen (
    id           UUID PRIMARY KEY,
    categoria_id UUID           NOT NULL REFERENCES geolocalizacion.categoria_almacen (id),
    nombre       VARCHAR(120)   NOT NULL,
    latitud      NUMERIC(9, 6)  NOT NULL,
    longitud     NUMERIC(9, 6)  NOT NULL
);

CREATE TABLE geolocalizacion.fundacion (
    id       UUID PRIMARY KEY,
    nombre   VARCHAR(120)  NOT NULL,
    latitud  NUMERIC(9, 6) NOT NULL,
    longitud NUMERIC(9, 6) NOT NULL
);
