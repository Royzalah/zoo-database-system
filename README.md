<a id="readme-top"></a>

<h1 align="center">🦁 Zoo Database System 🐧</h1>

<p align="center">
	<b>A full stack zoo management system backed by PostgreSQL, with a Spring Boot JDBC API and a React dashboard.</b>
</p>
<p align="center">
	<a href="https://github.com/Royzalah/zoo-database-system/issues">Report Bug</a>
	•
	<a href="https://github.com/Royzalah/zoo-database-system/issues">Request Feature</a>
</p>

---

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-ED8B00?style=flat&logo=openjdk&logoColor=white" alt="Java" />
  <img src="https://img.shields.io/badge/Spring%20Boot-3.1.1-6DB33F?style=flat&logo=springboot&logoColor=white" alt="Spring Boot" />
  <img src="https://img.shields.io/badge/PostgreSQL-336791?style=flat&logo=postgresql&logoColor=white" alt="PostgreSQL" />
  <img src="https://img.shields.io/badge/React-18.2-61DAFB?style=flat&logo=react&logoColor=black" alt="React" />
  <img src="https://img.shields.io/badge/Vite-4.5-646CFF?style=flat&logo=vite&logoColor=white" alt="Vite" />
  <img src="https://img.shields.io/badge/TailwindCSS-3.3-06B6D4?style=flat&logo=tailwindcss&logoColor=white" alt="TailwindCSS" />
  <img src="https://img.shields.io/badge/Maven-C71A36?style=flat&logo=apachemaven&logoColor=white" alt="Maven" />
</p>

<!-- ABOUT THE PROJECT -->
## 1 About The Project
Zoo Database System manages animals in a zoo (predators, fish and penguins), their zoo assignment, feeding, medical treatments and happiness levels. The project started as a Java OOP model and was migrated to a full PostgreSQL backed application: a hand written JDBC DAO layer talks to Postgres, a Spring Boot REST API exposes that data, and a React client renders it as an interactive dashboard.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- KEY FEATURES -->
## 2 Key Features
- **PostgreSQL persistence**: Animals, predators, fish, penguins, veterinary clinics and medical treatments are modeled as real relational tables with foreign keys, replacing the original file based (`ObjectOutputStream`) storage.
- **Custom JDBC DAO layer**: Every table has its own DAO (`AnimalDAO`, `PredatorDAO`, `FishDAO`, `PenguinDAO`, `MedicalTreatmentDAO`, `ZooDAO`) built on `PreparedStatement`, with no ORM in between.
- **Database trigger**: An `AFTER INSERT` trigger on `MEDICAL_TREATMENT` automatically raises an animal's happiness whenever a treatment is logged, with no extra Java code involved.
- **REST API**: A Spring Boot controller exposes the zoo's data and actions (load data, feed all animals, play all sounds, list penguins sorted by height or name, compute dominant fish colors, and more).
- **React dashboard**: A Vite + Tailwind client consumes the API to show predators, fish and penguins, add new animals, and trigger zoo wide actions.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- ARCHITECTURE -->
## 3 Architecture

```
├── src/main/java/com/example/zoo
│   ├── ass3
│   │   ├── exceptions        # Domain validation exceptions (age, weight, color, gender, pattern...)
│   │   ├── general           # Address, DataUtils, FoodSummary and shared enums (Color, Gender, Pattern...)
│   │   ├── manage            # Manage: orchestrates the in memory Zoo model
│   │   └── models            # Animal, Predator, Fish, Penguin and their concrete subtypes (Lion, Tiger, GoldFish...)
│   ├── db                    # JDBC layer: DBConnection plus one DAO per table
│   └── wrapper
│       ├── controller        # ZooController: REST endpoints under /api
│       └── interfaces        # IZoo, IVeterinaryClinic
├── src/main/resources/static # Built React app served by Spring Boot, plus images and sound effects
├── sql
│   ├── 01_schema.sql         # Table definitions (ZooTable, Animals, Predator, Fish, Penguin...)
│   ├── 02_seed_data.sql      # Sample data
│   ├── 03_queries.sql        # Reference queries against the schema
│   └── 04_trigger.sql        # Happiness-on-treatment trigger, with a before/after demonstration
├── client                    # React + Vite + Tailwind frontend
│   └── src                   # ZooApp, ZooOverview, PredatorsList, FishList, PenguinList, AddAnimalForm...
└── docs                      # Project presentation
```

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- CLASSES OVERVIEW -->
## 4 Classes Overview

| Class Name | Layer/Type | Description |
| :--- | :--- | :--- |
| `DBConnection` | **DB** | Singleton that loads credentials from `db.properties` and hands out a shared PostgreSQL `Connection`. |
| `ZooDAO`, `AnimalDAO`, `PredatorDAO`, `FishDAO`, `PenguinDAO`, `MedicalTreatmentDAO` | **DB** | One DAO per table, each wrapping insert / update / delete / search with `PreparedStatement`. |
| `Manage` | **Domain** | Central orchestrator implementing `IZoo`, holding the in memory `Zoo` and coordinating actions like feeding and adding animals. |
| `Zoo`, `Animal`, `Predator`, `Fish`, `Penguin` | **Domain** | Core OOP model of the zoo and its inhabitants, with concrete subtypes such as `Lion`, `Tiger`, `GoldFish` and `ClownFish`. |
| `VeterinaryClinic`, `MedicalTreatment` | **Domain** | Model the clinic and the treatments logged against animals, which drive the happiness trigger. |
| `ZooController` | **API** | Spring Boot `@RestController` exposing `/api/zoo`, `/api/predators`, `/api/fish`, `/api/penguins`, `/api/add-animal`, `/api/feedAll`, `/api/soundAll`, `/api/dominantColors`, `/api/increasingAgeOneYear` and `/api/veterinaryClinic`. |
| `ZooApplication` | **API** | Spring Boot entry point that boots the app and initializes the shared `Manage` instance. |
| `ZooApp` | **Client** | Root React component tying together the overview, lists and forms. |

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- DEMONSTRATION -->
## 5 Demonstration
A full walkthrough of the project, including the schema design and the JDBC integration, is available in the presentation at [`docs/Zoo_Project_Presentation.pdf`](docs/Zoo_Project_Presentation.pdf).

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- GETTING STARTED -->
## 6 Getting Started

### 6.1 Prerequisites

* **Java**: 17
* **Maven**: for building and running the Spring Boot backend
* **PostgreSQL**: a running instance with a `zoo_db` database (create it from `sql/01_schema.sql`)
* **Node.js**: for the React client (Vite 4)

### 6.2 Installation

1. Clone the repo
```
git clone https://github.com/Royzalah/zoo-database-system.git
cd zoo-database-system
```

2. Create the database and load the schema, seed data and trigger
```
psql -U postgres -c "CREATE DATABASE zoo_db"
psql -U postgres -d zoo_db -f sql/01_schema.sql
psql -U postgres -d zoo_db -f sql/02_seed_data.sql
psql -U postgres -d zoo_db -f sql/04_trigger.sql
```

3. Add your local database credentials. Create `src/main/resources/db.properties` (this file is gitignored and never committed):
```
db.url=jdbc:postgresql://localhost:5432/zoo_db
db.user=your_username
db.password=your_password
```

4. Run the backend from the project root
```
mvn spring-boot:run
```

5. (Optional) Run the React client in dev mode, in a separate terminal
```
cd client
npm install
npm run dev
```
The client proxies API calls to `http://localhost:8080`, where the Spring Boot backend runs.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- USAGE -->
## 7 Usage

**How to Use:**
* **Load the zoo**: call `GET /api/loadData` or open the dashboard, which triggers the initial load.
* **Browse animals**: `GET /api/predators`, `GET /api/fish` and `GET /api/penguins` return the current animals grouped by type.
* **Add an animal**: `POST /api/add-animal` with a plain text, comma separated payload describing the new animal.
* **Zoo wide actions**: `GET /api/feedAll` feeds every animal, `GET /api/soundAll` plays each animal's sound, `GET /api/dominantColors` reports the most common fish colors, and `GET /api/increasingAgeOneYear` ages every animal by a year.
* **Log a treatment**: inserting into `MEDICAL_TREATMENT` (via `MedicalTreatmentDAO`) automatically raises the treated animal's happiness through the database trigger.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- CONTRIBUTORS -->
## 8 Author

<div align="center">
Roei Zalah &nbsp;
</div>

<p align="right">(<a href="#readme-top">back to top</a>)</p>
