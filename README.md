# 🏫 Smart Complaint & Grievance Redressal System

A JavaFX-based desktop application for managing student complaints from **submission to resolution** through a centralized system for Students, Admins, and Workers.

## 📌 Problem

Managing college complaints manually can make it difficult to track issues, assign responsibilities, monitor progress, and maintain complaint history.

This system provides a digital platform to **submit, assign, track, resolve, reopen, and close complaints** efficiently.

## ✨ Key Features

- 👨‍🎓 Student registration and login
- 📝 Submit complaints with unique Complaint IDs
- 🗂️ Categorize complaints
- 🏢 Assign complaints to departments
- 👷 Assign complaints to specific workers
- 📊 Admin dashboard with complaint statistics
- 🔍 Search student complaints using **username/registration number**
- 📋 View and track personal complaints
- 🔄 Update complaint status
- 🔁 Reopen complaints when required
- ✅ Add resolution details
- 📚 View complete complaint history
- 💾 Persistent SQLite database storage
- 🔐 Role-based access for Student, Admin, and Worker
- 🎨 Modern JavaFX graphical interface

## 🔄 Complaint Workflow

```text
Student
   ↓
Submit Complaint
   ↓
Submitted
   ↓
Admin Assigns Department & Worker
   ↓
Assigned
   ↓
Worker Processes Complaint
   ↓
In Progress
   ↓
Resolution Added
   ↓
Resolved
   ↓
Closed / Reopened
```

## 👥 User Roles

### 👨‍🎓 Student
- Register and login
- Submit complaints
- View own complaints
- Track complaint status
- View assigned worker/department
- View resolution
- Reopen complaints when necessary

### 👨‍💼 Admin
- Monitor complaint statistics
- View and manage active complaints
- Assign departments and workers
- Search complaints by student registration number
- View student complaint history
- Monitor reopened complaints
- View resolved and closed complaints

### 👷 Worker
- Login with department
- View assigned complaints
- Update complaint progress
- Add resolution details
- Mark complaints as resolved

## 🛠️ Tech Stack

| Technology | Purpose |
|---|---|
| Java | Application logic |
| JavaFX | Graphical User Interface |
| SQLite | Database |
| JDBC | Database connectivity |
| Maven | Dependency management |
| Eclipse | Development |
| Git & GitHub | Version control |

## 🏗️ Project Structure

```text
SmartComplaintSystem/
│
├── src/main/java/com/smartcomplaint/
│   ├── Main.java
│   ├── Database.java
│   └── Complaint.java
│
├── complaints.db
├── pom.xml
└── README.md
```

### Main Components

**Main.java**  
Handles the JavaFX interface, navigation, login, registration, dashboards, complaint management, and user interactions.

**Database.java**  
Handles SQLite connection, user authentication, registration, complaint storage, assignment, status updates, and resolution updates.

**Complaint.java**  
Model class representing complaint information.

## 🗄️ Database

The application uses **SQLite** for persistent storage.

The database stores:

- User accounts and roles
- Student registration numbers
- Departments
- Complaints
- Categories
- Assigned workers
- Complaint status
- Resolution details
- Complaint creation time

Data remains available after closing and reopening the application.

## 🚀 How to Run

### Requirements

- JDK
- Eclipse IDE
- Maven
- JavaFX

### Run

Open the project in Eclipse and execute:

```bash
mvn javafx:run
```

The application automatically initializes the SQLite database when started.

## 🔮 Future Enhancements

- Email/SMS notifications
- Complaint priority levels
- File/image attachments
- Advanced analytics and reports
- Password hashing
- AI-assisted complaint categorization
- Web/mobile version
- Cloud deployment

## 🎓 Project Information

**Project:** Smart Complaint & Grievance Redressal System  
**Problem Statement:** PS-19  
**Type:** Desktop Application  
**Language:** Java  
**GUI:** JavaFX  
**Database:** SQLite  
**Connectivity:** JDBC  
**Build Tool:** Maven  

---

### ⭐ Project Goal

To provide a simple and centralized system that improves **complaint tracking, accountability, transparency, and resolution management** within an educational institution.
