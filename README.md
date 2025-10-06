# to-do-task

## Overview
Full-stack to-do app: React frontend (TypeScript/Vite/CSS), Spring Boot backend (Java 21), PostgreSQL DB.

**Features:**
- Add tasks (title/description)
- View top 5 recent uncompleted tasks
- Mark complete (hide from list)

---

## Prerequisites
- Docker & Docker Compose
- Git (clone repo)
- Linux/Bash

---

## Quick Start

1. **Clone repository:**
    ```sh
    git clone <repo-url> && cd to-do-task
    ```
2. **Set environment variables:**
    ```sh
    cp .env.example .env
    ```
    (edit `DB_PASSWORD` in `.env`)

3. **Run containers:**
    ```sh
    docker compose up --build
    ```
    - Builds backend JAR, frontend dist (~2-3 min first time)
    - Starts DB → Backend → Frontend

---

## Access

- **UI:** [http://localhost:3000](http://localhost:3000)  
  (add tasks, toggle "Done")
- **API:** [http://localhost:8080/api/v1/task](http://localhost:8080/api/v1/task)  
  (GET/POST/PUT test with curl/Postman)
- **Database:** `localhost:5432`  
  (pgAdmin/psql: DB=`todo`, User=`postgres`, Pass=`from .env`)

**Expected:**  
Add task → Shows in list (top 5 recent incomplete).  
Toggle "Done" → Disappears.

---

## API Endpoints

- `POST /api/v1/task` &mdash; Add task (body: `{title, description}`)
- `GET /api/v1/task` &mdash; Top 5 recent incomplete tasks
- `PUT /api/v1/task/{id}?completed=true` &mdash; Mark complete
