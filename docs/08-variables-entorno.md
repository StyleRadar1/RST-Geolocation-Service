## <img src="https://img.shields.io/badge/08-5600EF?style=for-the-badge" height="20" align="absmiddle" /> 8. VARIABLES DE ENTORNO

Las variables se definen en `.env`, que no se versiona. La plantilla `.env.example` indica cuáles se requieren.

| Variable | Descripción | Valor por Defecto |
| :--- | :--- | :--- |
| `SERVER_PORT` | Puerto del servicio | `8240` |
| `DB_HOST` | Host de PostgreSQL | `localhost` |
| `DB_PORT` | Puerto de PostgreSQL | `5432` |
| `DB_NAME` | Nombre de la base de datos | `styleradar_geolocation` |
| `DB_USER` | Usuario de PostgreSQL | `postgres` |
| `DB_PASSWORD` | Contraseña de PostgreSQL | — |
| `TIENDAS_URL` | URL de `ms-tiendas` con `/api/v1` | `http://localhost:8140/api/v1` (QA y PROD: `styleradar-tiendas-<ambiente>.azurewebsites.net/api/v1`) |
| `PAGOS_FILTRO_HABILITADO` | Muestra en el mapa solo las tiendas con suscripción al día | `false` (QA: `true`) |
| `PAGOS_URL` | URL de `ms-pagos` con `/api/v1` | `http://localhost:8400/api/v1` (QA: la del Container App de pagos) |
| `TIENDAS_CARGA_HABILITADA` | Activa la carga periódica desde `ms-tiendas` | `true` |
| `TIENDAS_CARGA_INTERVALO` | Cada cuánto se carga desde `ms-tiendas` | `PT30M` |
| `TIENDAS_CARGA_REINTENTO` | Cada cuánto se reintenta mientras la última carga haya fallado | `PT2M` |

---

<p align="center"><a href="../README.md">⬅ Volver al índice</a></p>
