# INVENTRA
_A modern inventory management system for big and small businesses alike_

---

## Description
Inventra is a web-based inventory management platform that helps companies manage stock across multiple locations.
It has a wide variety tools built-in to aid companies, including multi-user support and an in-depth permission system, advanced auditing, inventory management at the click of a button, and much more!

## Installation
Follow these steps to set up the project locally with **Tomcat 11.0.9**, **Java 21**, and **Maven**.

### 1. Prerequisites
Make sure the following are installed:
- [Java 21 (JDK)](https://jdk.java.net/21/)
- [Apache Maven](https://maven.apache.org/download.cgi)
- [Apache Tomcat 11.0.9](https://tomcat.apache.org/download-11.cgi)

To confirm:
```bash
java -version        # should show java 21
mvn -version         # should show Apache Maven
```

### 2. Clone and Build
Clone the repository with:
```bash
git clone https://github.com/mike-git-1/CST8319-Software-Development-Project-Group-12.git
cd CST8319-Software-Development-Project-Group-12
```
Then, build the project with Maven:
```bash
mvn clean install
```
This will generate a .war file in the target/ directory.

### 3. Deploy to Tomcat
Copy the generated .war file to your Tomcat webapps directory:
```bash
cp target/inventra /path/to/tomcat/webapps/
```
Then, start Tomcat:
```bash
/path/to/tomcat/bin/startup.bat  # Windows
/path/to/tomcat/bin/startup.sh   # Mac/Linux
```
...and open your browser to:
```bash
http://localhost:8080/inventra/
