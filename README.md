# dataProject

Running
-------

To compile and run the GUI application (requires the MySQL Connector/J jar):

1. There's a MySQL connector JAR in the project at `mysql-connector-j-9.5.0/mysql-connector-j-9.5.0.jar` (or `src/mysql-connector-j-9.5.0.jar`).
2. Use the provided `run.sh` script which prepares a `lib/` folder, copies the JAR there, compiles the sources to `bin/`, and runs the app.

From the project root (zsh/macOS):
```bash
./run.sh
```

Or run manually:
```bash
mkdir -p bin
javac -d bin src/*.java
java -cp "bin:/path/to/EmployeeSearch/mysql-connector-j-9.5.0/mysql-connector-j-9.5.0.jar" EmployeeSearchFrame
```

VS Code
-------

There is a `.vscode/launch.json` config named `Run EmployeeSearchFrame` that sets the classpath to `bin` and `lib/*`. Ensure you run `./run.sh` first (to place the connector JAR into `lib/`) or copy the JAR to `lib/` manually before launching from the editor.

Notes
-----

- Consider moving the connector JAR permanently into `lib/` and removing the copy from `src/` to keep dependencies separated from source files.
- If you still get "Could not open database.", open a terminal and check the stack trace printed by the app — it will show whether the driver class was missing (ClassNotFoundException) or the DB connection failed (SQLException). 
