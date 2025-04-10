```markdown
## Running the Project with Docker

To run this project using Docker, follow these steps:

### Prerequisites

Ensure you have Docker and Docker Compose installed on your system. Verify the installation by running:

```bash
docker --version
docker-compose --version
```

### Build and Run

1. Build the Docker image:

   ```bash
   docker-compose build
   ```

2. Start the services:

   ```bash
   docker-compose up
   ```

   This will start the application and its dependencies.

### Configuration

- The application service is exposed on port `8086`.
- The database service uses the default PostgreSQL port `5432`.

### Environment Variables

The following environment variables are used by the services:

- `POSTGRES_USER`: Database username (default: `user`)
- `POSTGRES_PASSWORD`: Database password (default: `password`)

### Additional Commands

To stop the services, use:

```bash
docker-compose down
```

For a clean start, remove the volumes:

```bash
docker-compose down --volumes
```

Refer to the `docker-compose.yml` file for more details on the configuration.
```