# to-do-task

# Overview
Full-stack to-do app: React frontend (TypeScript/Vite/CSS), Spring Boot backend (Java 21), PostgreSQL DB. Features: Add tasks (title/description), view top 5 recent uncompleted tasks, mark complete (hides from UI). Matches assessment UI mockup.

# Prerequisites
Docker & Docker Compose
Git (clone repo)
Linux/Bash

# Quick Start
Clone: git clone <repo-url> && cd to-do-task
Env: cp .env.example .env (edit DB_PASSWORD)
Run: docker compose up --build
Builds backend JAR, frontend dist (~2-3 min first time).
Starts DB → Backend → Frontend.

# Access:
UI: http://localhost:3000 (add tasks, toggle "Done").
API: http://localhost:8080/api/v1/task (GET/POST/PUT test with curl/Postman).
DB: localhost:5432 (pgAdmin/psql: DB=todo, User=postgres, Pass=from .env).
Expected: Add task → Shows in list (top 5 recent incomplete). Toggle "Done" → Disappears.


# API Endpoints
POST /api/v1/task: Add task (body: {title, description}).
GET /api/v1/task: Top 5 recent incomplete tasks.
PUT /api/v1/task/{id}?completed=true: Mark complete.
