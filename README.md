# AI Interview Question Evaluator

A full-stack system for practicing interview answers and getting an automated similarity-based score against a reference answer. Built as three cooperating services: a lightweight Java HTTP server, a Python/Flask NLP microservice, and a static HTML/JS frontend.

## How it works

1. The user fills in a question, a reference (expected) answer, and their own answer in the browser UI.
2. The frontend (`frontend/index.html`) POSTs this to the Java server at `http://localhost:8080/submit`.
3. The Java server (`InterviewServer`) saves the user and their answer to MySQL, then calls the Flask microservice at `http://127.0.0.1:5000/evaluate`.
4. Flask (`ai-service-flask/app.py`) computes TF-IDF cosine similarity between the user's answer and the reference answer, derives a 0–100 score, and returns a feedback string.
5. The Java server stores the evaluation result and returns `{ score, feedback }` to the browser, which renders it.

```
Browser (frontend/index.html)
        │  POST /submit  (JSON: name, question, modelAnswer, answer)
        ▼
Java HTTP server – InterviewServer.java  (port 8080)
        │  • saves user + answer to MySQL
        │  • calls Flask for scoring
        ▼
Flask microservice – app.py  (port 5000)
        │  TF-IDF + cosine similarity → score, feedback
        ▼
Java server saves evaluation → returns JSON to browser
```

## Tech stack

- **Backend (Java)** — `com.sun.net.httpserver.HttpServer`, JDBC (MySQL Connector/J), Gson for JSON
- **AI microservice (Python)** — Flask, scikit-learn (`TfidfVectorizer`, `cosine_similarity`)
- **Database** — MySQL
- **Frontend** — Plain HTML, JS (`fetch`), Bootstrap 5

## Project structure

```
backend-java/
  src/
    server/InterviewServer.java     # HTTP server, /submit endpoint, orchestration
    service/FlaskClient.java        # calls the Flask /evaluate endpoint
    dao/                            # UserDAO, AnswerDAO, EvaluationDAO, QuestionDAO (JDBC)
    db/DBConnection.java            # MySQL connection
    model/                          # Answer, User, Question, EvaluationRequest, EvaluationResult
  lib/                              # gson, mysql-connector-j jars
ai-service-flask/
  app.py                            # Flask app, /evaluate endpoint, TF-IDF scoring
  requirements.txt
frontend/
  index.html                        # submission form
  result.html                       # static result view
```

## Setup & running locally

### 1. Database
Create a MySQL database (e.g. `ai_interview`) with `users`, `answers`, and `evaluations` tables matching the fields used in the DAO classes (`users.name`; `answers.user_id, question, answer`; `evaluations.answer_id, score, feedback`). Update credentials in `backend-java/src/db/DBConnection.java` if needed.

### 2. Flask AI service
```bash
cd ai-service-flask
pip install -r requirements.txt
python app.py
```
Runs on `http://127.0.0.1:5000`.

### 3. Java backend
Compile the classes in `backend-java/src` against the jars in `backend-java/lib` (gson, mysql-connector-j), then run `server.InterviewServer`.
```
Java Server running on http://localhost:8080
```

### 4. Frontend
Open `frontend/index.html` in a browser (or serve it statically). Fill in name, question, reference answer, and your answer, then submit.

## API

**POST** `/submit` (Java server, port 8080)
```json
{
  "name": "Archit",
  "question": "What is a HashMap in Java?",
  "modelAnswer": "HashMap stores key-value pairs and is not synchronized.",
  "answer": "It's a data structure that stores key value pairs, not thread safe."
}
```
Returns:
```json
{ "score": 74, "feedback": "Good answer but missing some internal details." }
```

**POST** `/evaluate` (Flask service, port 5000) — called internally by the Java server; not meant to be hit directly by the frontend.

## Known limitations / next steps

- `Main.java` (a standalone CLI test runner) is currently commented out — `InterviewServer` is the real entry point.
- No authentication on the `/submit` endpoint.
- Question bank (`QuestionDAO`) is wired up but not yet exposed through an endpoint — questions/reference answers are currently entered manually in the form rather than pulled from the `questions` table.
- Scoring is pure TF-IDF cosine similarity (no semantic/embedding-based matching yet).

## Author
Archit Mahajan — [github.com/architmhajn](https://github.com/architmhajn)
