# To-Do Voodoo

This project is containerized with Docker.

It uses PostgreSQL via Docker Compose, configured as temporary storage `tmpfs` so database data is NOT persisted (for now) across container restarts.
https://docs.docker.com/engine/storage/tmpfs/

Once the project has a framework like Spring Boot, an ORM like Hibernate, and Flyway for migrations, it can move to a code-first setup with versioned schema changes and persistent data across restarts to avoid dirty data and schema drift.

Run the project with:

```bash
docker compose up --build
```

# ToDo Voodoo

This is a To Do application intended as an ongoing collaborative project to gain experience with professional developer workflows, open source projects, and various technical skills as well as software architecture and design. 

