# nafpl.io
Accelerating Software Assessments with LLMs and RAG

## Table of Contents
1. [Prerequisites](#prerequisites)
2. [Cloning the Repository](#cloning-the-repository)
3. [Building the Application]()
4. [Running the Application for Local Development](#-running-the-application-for-local-development)
 
## 📋 Prerequisites
Ensure the following is installed on your machine:

- [JDK 21](https://www.oracle.com/java/technologies/downloads/#java21)
- [Docker](https://docs.docker.com/engine/install/)

💡 For the Java part of the application, [IntelliJ IDEA](https://www.jetbrains.com/idea/download/) is highly
recommended - this guide uses it as default.

Verify your local installations by running:
```bash
echo "Java version: $(java -version 2>&1 | awk -F '"' '/version/ {print $2}')"
echo "Docker version: $(docker --version | awk '{print $3}' | tr -d ',')"
```

## 💻 Cloning the Repository
Follow these steps to clone the repository to your local machine:

1. **Open a terminal**
2. **Navigate to the directory where you want to clone the repository**:
   ```bash
   cd /path/to/your/desired/directory
   ```
3. **Clone the repository using `git`**:
   ```bash
   git clone https://github.com/xtsekes/nafpl.io.git
   ```

## 🛠️ Building the Application

To simplify the process of building and running the application, we use Docker Compose. This ensures that all services (e.g., the application, database, etc.) are started with the correct configuration.

1. **Navigate to the project directory**:
   Open a terminal and navigate to the root directory of the project:
2. **Build the application**:
   1. Run the following commands to install all its dependencies:
      1. Install ollama
          ```bash
          docker compose -f ./docker-compose.ollama.yaml up -d
          ```
      2. Install PostgresSQL
          ```bash
          docker compose -f ./docker-compose.yaml up -d
          ```
   2. Run the following command to build the application:
      ```bash
      ./mvnw package
      ```

## 🚀 Running the Application for Local Development

### Java - `nafplio`
1. **Navigate to nafplio directory**:
   ```bash
   cd /path/to/nafplio
   ```
2. **Run the following command to install all dependencies, compile the source code, run tests and verify the build's
   success:**
   ```bash
   ./mvnw clean verify
   ```
 3. **Run the application in dev mode**:
    ```bash
    ./mvnw compile quarkus:dev
    ```
    The application will be available at [http://localhost:8080/](http://localhost:8080/)