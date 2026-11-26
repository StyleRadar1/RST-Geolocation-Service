-- NIT del almacén en ms-tiendas; la carga periódica lo completa en los almacenes que ya existen
ALTER TABLE geolocalizacion.almacen ADD COLUMN IF NOT EXISTS nit VARCHAR(30);
