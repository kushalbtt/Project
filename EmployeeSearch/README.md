# Employee Search (Java Swing + MySQL)

A simple Java Swing desktop app that connects to a MySQL database (the typical `COMPANY` schema) and lets you:
- Load **Departments** and **Projects** from the database
- Select a **Department** and click **Search** to list employees in that department


---

## Project Structure

```
Project/
└── EmployeeSearch/
    ├── example.properties
    └── src/
        ├── Database.java
        └── EmployeeSearchFrame.java
```

---

## Features

- **JDBC connection** using a `.properties` config file
- **DB Fill / Load** button loads:
  - `Dname` values from `DEPARTMENT`
  - `Pname` values from `PROJECT`
- **Search** lists employee first/last names for the selected department
- Basic UI actions (clear selections, error popups)

> Current behavior: the employee search is driven by the selected **department** (projects are loaded in the UI but are not currently used in the search query).

---

## Requirements

- **Java (JDK 15+)**  
  (The code uses Java text blocks `""" ... """`, which require Java 15+.)
- **MySQL** database with tables matching the classic `COMPANY` schema:
  - `DEPARTMENT(Dnumber, Dname, ...)`
  - `PROJECT(Pname, ...)`
  - `EMPLOYEE(Fname, Lname, Dno, ...)`
- **MySQL Connector/J** on your classpath  
  (driver class: `com.mysql.cj.jdbc.Driver`)

---

## Setup

### 1) Clone the repo
```bash
git clone https://github.com/kushalbtt/Project.git
cd Project/EmployeeSearch
```

### 2) Configure the database connection
This project reads DB settings from `example.properties` (same folder as the app).

`example.properties` must define:
- `db.url`
- `db.user`
- `db.password`
- `db.driver`

> Security tip: don’t commit real credentials. For a real project, copy this to something like `db.properties` and add it to `.gitignore`.

### 3) Add the MySQL Connector/J driver
**If you’re using an IDE (recommended):**
- IntelliJ: *Project Structure → Libraries → + → Java* (add the Connector/J jar)
- Eclipse: *Build Path → Add External Archives…*

---

## Run

### Option A: Run in an IDE (recommended)
1. Open the `EmployeeSearch/` folder as a project
2. Ensure MySQL Connector/J is added
3. Run:
   - `EmployeeSearch/src/EmployeeSearchFrame.java`

### Option B: Run from terminal (manual classpath)
Place the Connector/J jar somewhere (example: `EmployeeSearch/lib/mysql-connector-j.jar`), then:

**macOS/Linux**
```bash
cd EmployeeSearch
mkdir -p out
javac -cp "lib/mysql-connector-j.jar" -d out src/*.java
java  -cp "out:lib/mysql-connector-j.jar" EmployeeSearchFrame
```

**Windows (PowerShell/CMD)**
```bat
cd EmployeeSearch
mkdir out
javac -cp "lib\mysql-connector-j.jar" -d out src\*.java
java  -cp "out;lib\mysql-connector-j.jar" EmployeeSearchFrame
```

---

## How To Use

1. Click the button that loads DB data (departments/projects)
2. Select a **Department**
3. Click **Search**
4. Employee names will appear in the output text area

---

## Troubleshooting

- **“Could not open database.”**
  - Check `example.properties` values
  - Confirm the DB host is reachable and the user has permissions
  - Confirm Connector/J is on the classpath

- **No results**
  - Confirm the `COMPANY` schema has data in `DEPARTMENT` and `EMPLOYEE`
  - Confirm employees have a matching `Dno` that exists in `DEPARTMENT.Dnumber`

- **Java version errors**
  - Ensure you’re using **JDK 15+**

---

## Notes / Improvements (Ideas)

- Use the selected **Project** in the employee search as well
- Add a `.gitignore` (at minimum: `.DS_Store` and any local secrets files)
- Convert to Maven/Gradle for easier dependency management

---

## Contributors

- Repository contributors listed on GitHub (see repo Insights / Contributors)
