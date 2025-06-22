# University Management System

This is a Spring Boot–based backend system designed to manage university operations such as users, students, instructors, courses, sections, grades, and time slots. The system includes robust role-based access control, AI-based academic planning with Gemini, and supports PostgreSQL as its primary database.

---

## Technologies

- Java 17
- Spring Boot 3
- Spring Security
- JWT, OAuth2 , (with 2FA support)
- PostgreSQL
- Hibernate (JPA)
- Lombok
- Google Gemini API (via HTTP client)
- Maven

---

## Authentication and Roles

| Role        | Authentication Method     | Capabilities                                |
|-------------|----------------------------|---------------------------------------------|
| ADMIN       | Session + 2FA (email/SMS)  | Full administrative access                  |
| MODERATOR   | Session + 2FA              | Create and manage users and faculties       |
| REGISTRAR   | Session + 2FA              | Manage sections and student enrollments     |
| INSTRUCTOR  | JWT                        | View and manage own courses and sections    |
| STUDENT     | JWT                        | View own data, enroll in sections, get AI plan |

---

## Features

### Security
- Combined JWT, OAuth2
- 2FA for privileged roles (Admin, Moderator, Registrar)
- Fine-grained role-based method access via `@PreAuthorize`

### User Management
- CRUD operations on users, with role assignment
- Search users by criteria (e.g., username, email)

### Academic Structure
- Course creation with prerequisite relationships
- Section scheduling, maximum seats, and instructors
- Time slots assigned per section
- Student enrollment via authenticated context
- Instructors retrieve their course tree

### Gemini AI Planning
- Generates course plans for students based on their preferences and available offerings
- Powered by Google Gemini 1.5 Flash API

---

## Directory Structure

