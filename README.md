# CodeInsight – AI-Powered Coding Learning Assistant 🚀

[![Java](https://img.shields.io/badge/Java-17%2B-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-19-blue.svg)](https://react.dev/)
[![Vite](https://img.shields.io/badge/Vite-8.x-purple.svg)](https://vitejs.dev/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

An intelligent, full-stack educational coding assistant designed for computer science students to master programming fundamentals: **Loops**, **Arrays**, **Functions**, and **Recursion**. CodeInsight delivers real-time static code diagnostics, beginner-friendly mistake categorization, persistent attempt tracking in **MySQL**, weak-topic detection, and personalized practice recommendations across **Java**, **Python**, **C**, and **C++**.

---

## 📌 Problem Statement & Objectives

### Problem
Beginner programmers frequently encounter conceptual roadblocks when learning algorithmic logic—such as subtle **off-by-one errors**, **infinite loop traps**, **array index out-of-bounds**, and **missing recursion base cases**. Traditional automated judges simply return cryptic `Wrong Answer` or `Runtime Error` messages without explaining *why* the logic failed, leading to frustration and inefficient learning.

### Objectives
1. **Explain Rather Than Punish:** Provide beginner-friendly, actionable diagnostic explanations for algorithmic mistakes instead of plain pass/fail flags.
2. **Safe Code Evaluation:** Safely analyze submitted code using deterministic AST and pattern analysis without executing untrusted arbitrary code on the server (preventing Remote Code Execution vulnerabilities).
3. **Multi-Language Fluency:** Enable students to solve problems interchangeably in **Java**, **Python**, **C**, and **C++** with language-tailored templates, file names, and syntax feedback.
4. **Data-Driven Personalized Guidance:** Persist all student submissions in **MySQL** to dynamically evaluate topic mastery, identify weak topics, and recommend targeted practice problems.

---

## 🌟 Features & Modules

### 1. 📊 Student Learning Dashboard
- **Progress Statistics:** Solved problems ratio, overall accuracy rate, persistence streaks, and identified focus areas.
- **Topic Mastery Cards:** Visual progress bars for Loops, Arrays, Functions, and Recursion with completion status.
- **AI Recommendation Banner:** Personalized recommendations suggesting the next best problem to solve based on past mistakes.
- **Recent Submissions Log:** Timestamped audit trail of recent attempts with scores and diagnostic results.

### 2. 🧪 Interactive Practice Lab
- **Multi-Language Selector:** Instant switching between **Java** (`Solution.java`), **Python** (`solution.py`), **C** (`solution.c`), and **C++** (`solution.cpp`).
- **Dynamic Templates & Instructions:** Problem statements and function signatures automatically adapt to the selected language.
- **In-Progress Work Preservation:** Caches code per language so you never lose work when toggling between languages.
- **Collapsible Hints:** Provides staged hints without spoiling solutions immediately.

### 3. 🔍 Algorithmic Diagnostic Engine
Safe, static rule-based analyzer that detects common beginner pitfalls across languages:
- **Off-By-One Errors:** Detects `i <= length` in 0-indexed languages and Python `range(1, n)` upper-bound exclusions.
- **Infinite Loop Risk:** Flags loop counter omission, unprogressed while conditions, and premature semicolons (`for(...);`).
- **Recursion Flaws:** Identifies missing termination `if` base cases, missing recursive calls, and unreduced arguments (`fib(n)`).
- **Missing Return Statements:** Ensures non-void methods properly return results on all execution paths.
- **Structural Syntax Integrity:** Identifies unbalanced braces, missing colons in Python, and malformed blocks.

### 4. 📈 Diagnostic & Weak-Topic Analysis
- **Dynamic Weakest Topic Detection:** Pinpoints the domain where failure rates cluster.
- **Error Distribution Breakdown:** Visual distribution of mistakes (percentage of off-by-one errors, recursion base-case missing, etc.).
- **Beginner Remediation Strategy:** Provides concrete rules of thumb to prevent recurring bugs.

### 5. 🗄️ Attempt History & Audit Log
- Persistently records every attempt in **MySQL** with student ID, language, score, timestamp, and diagnostic feedback.
- Detailed inspection modal to review previously submitted code.

---

## 🛠️ Technology Stack

| Layer | Technologies Used |
|---|---|
| **Frontend** | React 19, Vite 8, Lucide React (Icons), Modern Responsive CSS |
| **Backend** | Java 17+, Spring Boot 3.4.x, Spring Web, Spring Data JPA, Hibernate, HikariCP |
| **Database** | MySQL 8.0 (Relational schema: `topics`, `questions`, `submissions`) |
| **Build & Tooling** | Maven Wrapper (`mvnw`), npm, Node.js v20+ |

---

## 📁 Project Folder Structure

```
CodeInsight/
├── backend/                              # Spring Boot REST API
│   ├── src/main/java/com/codeinsight/
│   │   ├── config/                      # CORS & WebMvc configurations
│   │   ├── controller/                  # REST Controllers (Topics, Questions, Submissions, Analytics)
│   │   ├── dto/                         # Data Transfer Objects
│   │   ├── entity/                      # Relational JPA Entities (Topic, Question, Submission, etc.)
│   │   ├── exception/                   # Global exception handling & error responses
│   │   ├── init/                        # DataInitializer (auto-seeds 12 questions on startup)
│   │   ├── repository/                  # Spring Data JPA Repositories
│   │   └── service/                     # Business logic, CodeAnalysisService, AnalyticsService
│   ├── src/main/resources/
│   │   └── application.properties       # Datasource config with environment-variable placeholders
│   ├── src/test/java/                   # Unit & integration test suites
│   ├── pom.xml                          # Maven build dependencies
│   └── mvnw.cmd                         # Maven wrapper executable for Windows
├── frontend/                             # React.js SPA (Vite)
│   ├── src/
│   │   ├── components/                  # Dashboard, PracticeView, WeakTopicAnalysis, HistoryView, Navbar
│   │   ├── services/                    # Centralized API client (api.js)
│   │   ├── App.jsx                      # Main application shell
│   │   └── index.css                    # Responsive CSS design system
│   ├── package.json                     # Frontend dependencies & scripts
│   └── vite.config.js                   # Vite config with backend proxy and host settings
├── .env.example                         # Safe template for local environment variables
├── .gitignore                           # Excludes target, node_modules, dist, and secrets
└── README.md                            # Project documentation
```

---

## ⚙️ Prerequisites for Windows

Before running the project, ensure you have the following installed on your machine:
1. **JDK 17 or JDK 21**: Verify with `java -version`.
2. **Node.js (v18+) & npm**: Verify with `node -v` and `npm -v`.
3. **MySQL 8.0 Server**: Verify service is running via Windows Services or `mysqladmin ping`.
4. **Git**: Verify with `git --version`.

---

## 🚀 Step-by-Step Local Setup & Run Guide (Windows)

### Step 1: Set Up MySQL Database

1. Open PowerShell or Command Prompt.
2. Log into MySQL and create the database:
   ```powershell
   mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS codeinsight_db;"
   ```
   *(Enter your MySQL password when prompted).*

---

### Step 2: Configure Environment Variables

Create your local `.env` file from the provided template:
```powershell
# Copy the example file
Copy-Item .env.example .env
```
Open `.env` in any text editor and fill in your MySQL credentials:
```env
DB_HOST=localhost
DB_PORT=3306
DB_NAME=codeinsight_db
DB_USERNAME=root
DB_PASSWORD=your_mysql_password
```

---

### Step 3: Start the Spring Boot Backend (Terminal 1)

1. Open a **PowerShell** window.
2. Navigate to the `backend` directory:
   ```powershell
   cd E:\OneDrive\Documents\CodeInsight\backend
   ```
3. Set your `JAVA_HOME` (pointing to your JDK 17/21 installation) and launch the server:
   ```powershell
   $env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
   $env:DB_PASSWORD = "your_mysql_password"   # (Only if you have a password)
   .\mvnw.cmd spring-boot:run
   ```
4. Wait until you see:
   ```text
   Started BackendApplication in X.XXX seconds
   ```
   > The backend is now active at: **`http://localhost:8080`**  
   > *(On initial startup, it automatically creates the database tables and seeds 12 practice questions).*

---

### Step 4: Start the React Frontend (Terminal 2)

1. Open a **second, separate PowerShell** window.
2. Navigate to the `frontend` directory:
   ```powershell
   cd E:\OneDrive\Documents\CodeInsight\frontend
   ```
3. Install frontend dependencies:
   ```powershell
   npm install
   ```
4. Start the Vite development server:
   ```powershell
   npm run dev
   ```
5. You will see:
   ```text
   VITE v8.x.x  ready in XXX ms

   ➜  Local:   http://localhost:5173/
   ```

---

### Step 5: Open in Your Web Browser

Open your browser and navigate to:  
👉 **[http://localhost:5173](http://localhost:5173)**

---

## 📸 Screenshots

*(To add screenshots to your GitHub repo, take a screenshot of each tab and place the images in a `docs/screenshots/` folder)*

| Dashboard Overview | Practice Lab with Language Dropdown |
|---|---|
| ![Dashboard](docs/screenshots/dashboard.png) | ![Practice Lab](docs/screenshots/practice.png) |

| Diagnostic Mistake Analysis | Attempt History & Audit Log |
|---|---|
| ![Diagnostics](docs/screenshots/diagnostics.png) | ![History](docs/screenshots/history.png) |

---

## 🧪 Running Backend Automated Tests

To run the automated integration and diagnostic tests:
```powershell
cd E:\OneDrive\Documents\CodeInsight\backend
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
.\mvnw.cmd test
```
**Verification Scope:**
- `BackendApplicationTests`: Verifies full Spring context, MySQL connection, relational persistence, multi-language submissions (Java, Python, C, C++), and analytics calculations.
- `CodeAnalysisServiceTest`: Validates detection of off-by-one errors, recursion base case gaps, premature semicolons, and syntax imbalances.

---

## 🔮 Future Enhancements

- 🤖 **LLM API Integration:** Plug in Google Gemini API or OpenAI API via a configurable toggle for advanced natural-language tutoring dialogues.
- ⏱️ **Timer & Mock Contests:** Timed coding challenge mode to simulate college placement technical rounds.
- 👥 **Multi-User Authentication:** Spring Security with JWT tokens for multi-tenant student accounts and classroom leaderboards.
- 📦 **Containerization:** Docker & Docker Compose setup for one-command containerized deployment.

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
