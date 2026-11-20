INSERT INTO geolocalizacion.categoria_almacen (id, nombre)
SELECT v.id, v.nombre
FROM (VALUES
    ('44444444-4444-4444-4444-444444444444'::uuid, 'Casual'),
    ('11111111-1111-1111-1111-111111111111'::uuid, 'Formal'),
    ('55555555-5555-5555-5555-555555555555'::uuid, 'Deportivo'),
    ('22222222-2222-2222-2222-222222222222'::uuid, 'Urbano'),
    ('33333333-3333-3333-3333-333333333333'::uuid, 'Vintage'),
    ('66666666-6666-6666-6666-666666666666'::uuid, 'Accesorios'),
    ('77777777-7777-7777-7777-777777777777'::uuid, 'Calzado')
) AS v (id, nombre)
WHERE NOT EXISTS (SELECT 1 FROM geolocalizacion.categoria_almacen c WHERE lower(c.nombre) = lower(v.nombre))
  AND NOT EXISTS (SELECT 1 FROM geolocalizacion.categoria_almacen c WHERE c.id = v.id);
