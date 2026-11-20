## <img src="https://img.shields.io/badge/05-5600EF?style=for-the-badge" height="20" align="absmiddle" /> 5. EVENTOS QUE PUBLICA Y CONSUME

El servicio no publica eventos. Consume por **RabbitMQ** (exchange `styleradar.exchange`) los almacenes y fundaciones que registra `ms-tiendas`, para mantener actualizados el mapa, las distancias y las fundaciones cercanas. El Front (por el API Gateway) y `ms-item-catalogo` (por Feign, para la distancia) consumen sus endpoints por HTTP.

### Carga desde ms-tiendas por Feign

Como `ms-tiendas` no publica eventos de almacenes, el servicio los carga por HTTP con `TiendasClient` (Feign) 20 segundos después de arrancar y luego cada 30 minutos:

| Endpoint de ms-tiendas | Qué se toma |
| :--- | :--- |
| `GET /api/v1/stores?page&size` | Almacenes aprobados (todas las páginas): NIT, nombre comercial, categoría y ubicación. |
| `GET /api/v1/foundations?latitude&longitude` | Fundaciones activas: id, nombre y ubicación. |

El id de cada almacén y fundación se calcula como un UUID fijo a partir del NIT o del id de `ms-tiendas`, así que cargar varias veces actualiza los mismos registros. Las categorías se ajustan a las del análisis unificado (`DEPORTIVA` pasa a `Deportivo`). Los registros sin nombre, categoría o ubicación válida se descartan. Si `ms-tiendas` no responde, se registra un aviso y se reintenta en la siguiente carga.

### Matriz de Eventos AMQP

| Tipo | Routing key | Emisor / Receptor | Qué hace |
| :---: | :--- | :--- | :--- |
| **CONSUME** | `styleradar.tiendas.almacen.registrado` · `.almacen.actualizado` | `ms-tiendas` &rarr; `ms-geolocalizacion` | Crea o actualiza el almacén con su nombre, categoría y ubicación. |
| **CONSUME** | `styleradar.tiendas.fundacion.registrada` · `.fundacion.actualizada` | `ms-tiendas` &rarr; `ms-geolocalizacion` | Crea o actualiza la fundación con su nombre y ubicación. |

Contenido esperado: `almacenId` o `fundacionId` (UUID), `nombre`, `categoria` (solo almacenes) y `latitud`/`longitud`. También se aceptan dentro de `data`. Si la categoría no existe se crea.

### Colas y errores

| Cola | Para qué |
| :--- | :--- |
| `styleradar.geolocalizacion.almacen.q` | Recibe los eventos de almacenes. |
| `styleradar.geolocalizacion.fundacion.q` | Recibe los eventos de fundaciones. |
| `styleradar.geolocalizacion.dlq` | Cola de errores: por el DLX `styleradar.dlx` llegan los eventos inválidos (sin id, nombre o ubicación válidos), tras 3 intentos. |

El consumidor está apagado por defecto (`RABBITMQ_LISTENER_ENABLED=false`) hasta que el servicio tenga su usuario en el broker; se enciende por ambiente junto con `RABBITMQ_HOST`, `RABBITMQ_USER` y `RABBITMQ_PASSWORD`.

---

<p align="center"><a href="../README.md">⬅ Volver al índice</a></p>
