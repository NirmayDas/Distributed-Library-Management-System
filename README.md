<!--
  README.md for Distributed Library Management System
-->

# 📚 Distributed Library Management System  
*A Java-based client-server library system with cloud hosting and a JavaFX GUI*

[Demo Video Of the Project](https://drive.google.com/file/d/1ICSt50dcIt96hCFgptS1_YsAi0bUzOmh/view?usp=sharing)

![Java](https://img.shields.io/badge/Java-8-orange.svg)  
![Maven](https://img.shields.io/badge/Maven-3.8.1-blue.svg)  
![MongoDB](https://img.shields.io/badge/MongoDB-4.6.1-green.svg)  
![JavaFX](https://img.shields.io/badge/JavaFX-17-lightgrey.svg)  
![Azure ACI](https://img.shields.io/badge/Azure%20ACI-blue.svg)  

---

## 📖 Table of Contents
1. [Overview](#-overview)  
2. [Features](#-features)  
3. [Tech Stack](#-tech-stack)  
4. [Prerequisites](#-prerequisites)  
5. [Project Structure](#-project-structure)  
6. [Setup & Running](#-setup--running)  
   - [Server](#server)  
   - [Client](#client)  
7. [Usage](#-usage)  
8. [Contact & Acknowledgments](#-contact--acknowledgments)  
9. [License](#-license)  

---

## 🔍 Overview
A distributed client-server application built to showcase object-oriented programming skills. Library members can:
- Browse a catalog  
- Borrow and return items  
- Leave reviews  

All data is stored in Azure Cosmos DB (MongoDB API) and served via an Azure Container Instance. The client uses a JavaFX GUI with search, filtering, and audio feedback.

---

## ✨ Features

**Required**  
- **Catalog Browsing**: Titles, authors, genres, availability  
- **Borrow/Return**: `BORROW <id>` / `RETURN <id>`  
- **Secure Login**: Username/password authentication  

**Additional**  
- 🔒 **Password Encryption**  
- 🗄️ **MongoDB (Azure Cosmos DB)**  
- ☁️ **Cloud Server on Azure ACI**  
- ⚙️ **Maven** build & dependency management  
- 🔊 **Sound Effects** for UI actions  
- 🔍 **Search** by title or author  
- 🏷️ **Tagging & Filtering** by genre  
- 💬 **Review System** with TextArea feedback  

---

## 🛠️ Tech Stack
- **Language**: Java 8  
- **Build**: Maven 3.8.1  
- **Database**: Azure Cosmos DB (MongoDB API)  
- **GUI**: JavaFX 17  
- **Cloud**: Azure Container Instances  

---

## ⚙️ Prerequisites
- **Java 8** (JDK)  
- **Maven 3.8.1**  
- **MongoDB**: An Azure Cosmos DB instance (or local MongoDB).  
  - _Tip_: Replace the hard-coded connection strings in `AccountManager.java`, `Catalog.java`, and `ClientHandler.java` with your own.

---

## 📁 Project Structure
```
Distributed-Library-Management-System/
├── ServerSide/
│   ├── pom.xml
│   └── src/main/java/com/nirmaydas/serverside/
│       ├── Server.java
│       ├── ClientHandler.java
│       ├── AccountManager.java
│       ├── Catalog.java
│       ├── PasswordEncrypter.java
│       └── …  
└── ClientSide/
    ├── pom.xml
    └── src/main/java/com/nirmaydas/clientside/
        ├── Client.java
        ├── Controller.java
        ├── Command.java
        └── Response.java
    └── src/main/resources/com/nirmaydas/clientside/
        ├── *.fxml
        └── sounds/*.wav|*.mp3
```

---

## 🚀 Setup & Running

### Server
1. **Cloud (Azure ACI)**   
   - Verify with:  
     ```bash
     az container show        --resource-group myResourceGroup        --name library-server
     ```
2. **Local**  
   ```bash
   cd ServerSide
   # update MongoDB URI in the Java classes
   mvn exec:java@run-server
   ```

### Client
```bash
cd ClientSide
# if local server: edit Controller.java → "localhost:5000"
mvn clean package
mvn exec:java@run-client
```

---

## 🎮 Usage
1. **Create Account or Login with:**  
   - Username: `newuser`
   - Password: `newpass`  
2. **Browse** catalog items  
3. **Borrow/Return** via commands  
4. **Reviews**: click “Reviews” and submit  
5. **Feedback** displayed in a TextArea  

---

## 📬 Contact & Acknowledgments
- **Made by:**: Nirmay Das  
- **Email**: [nirmaydas99@gmail.com](mailto:nirmaydas99@gmail.com)  
- Utilizes JavaFX, GSON, MongoDB Java Driver, Azure ACI  

---

## 📄 License
_For educational purposes only. Not licensed for public distribution._
