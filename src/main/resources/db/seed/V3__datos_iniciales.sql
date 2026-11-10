-- Datos de ejemplo en Bogotá para probar el mapa en QA
INSERT INTO geolocalizacion.categoria_almacen (id, nombre) VALUES
    ('11111111-1111-1111-1111-111111111111', 'Formal'),
    ('22222222-2222-2222-2222-222222222222', 'Urbano'),
    ('33333333-3333-3333-3333-333333333333', 'Vintage');

INSERT INTO geolocalizacion.almacen (id, categoria_id, nombre, latitud, longitud) VALUES
    ('a1111111-1111-1111-1111-111111111111', '11111111-1111-1111-1111-111111111111', 'Sastrería Antonia', 4.676200, -74.048100),
    ('a2222222-2222-2222-2222-222222222222', '22222222-2222-2222-2222-222222222222', 'Calle 85 Store', 4.668300, -74.054600),
    ('a3333333-3333-3333-3333-333333333333', '33333333-3333-3333-3333-333333333333', 'Nora Vintage', 4.648600, -74.063500);

INSERT INTO geolocalizacion.fundacion (id, nombre, latitud, longitud) VALUES
    ('f1111111-1111-1111-1111-111111111111', 'Fundación Ropa Solidaria', 4.660000, -74.060000),
    ('f2222222-2222-2222-2222-222222222222', 'Fundación Abrigo Norte', 4.782700, -74.042700);
