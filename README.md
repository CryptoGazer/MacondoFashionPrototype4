# Macondo Fashion

A full-stack fashion e-commerce web application built with Spring Boot and Thymeleaf. Users can browse clothing and accessories by gender and category, manage a shopping cart, and complete purchases. An admin panel provides full product and user management.

---

## Tech Stack

- **Backend:** Java 17, Spring Boot 3.2.4, Spring Security, Spring Data JPA
- **Frontend:** Thymeleaf, HTML5, CSS
- **Database:** MySQL 8.x
- **Build:** Maven

---

## Features

### For users

- Register, log in, and manage your account
- Browse products by gender (Men / Women) and category
- View detailed product pages with images
- Add items to a shopping cart and complete checkout
- Track purchase history (items bought, total spent)

### For admins

- Dashboard with product and user management
- Create, edit, and delete products with image uploads
- View all users and their order history
- Manage product inventory and sales data

### Product categories

Clothes and Jackets, Pullovers, Shirts, T-Shirts, Hoodies, Jeans, Trousers, Sports, Watches, Bracelets, Souvenirs

---

## Prerequisites

- Java 17
- Maven
- MySQL 8.x running locally

---

## Local Setup

### 1. Create the database

```sql
CREATE DATABASE macondo_fashion_prototype4;
```

### 2. Configure database credentials

The default connection in `src/main/resources/application.properties` expects:

```text
Host:     localhost
Port:     3306
Database: macondo_fashion_prototype4
User:     root
Password: root
```

If your MySQL setup differs, update `application.properties` accordingly:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/macondo_fashion_prototype4
spring.datasource.username=your_username
spring.datasource.password=your_password
```

> Note: the default config uses port `8889` (MAMP default). Change to `3306` if using a standard MySQL installation.

### 3. Build and run

```bash
./mvnw spring-boot:run
```

Or with Maven installed globally:

```bash
mvn spring-boot:run
```

### 4. Open the app

Navigate to `http://localhost:8080` in your browser.

Hibernate will auto-create all tables on first run via `spring.jpa.hibernate.ddl-auto=update`.

---

## Creating an Admin Account

After registering a regular user, update their role directly in the database:

```sql
UPDATE users SET roles = 'ROLE_ADMIN' WHERE name = 'your_username';
```

The admin panel is then accessible at `http://localhost:8080/admin`.

---

## Project Structure

```text
src/main/java/com/macondo_cs/MacondoFashionPrototype4/
├── config/          # Security config, user details, global advice
├── controllers/     # MainController, UserController, PurchasingProcessController,
│                    # AdminController, DatabasesController, ImageController
├── models/          # User, Product, Cart, Image entities and DTOs
├── services/        # UserService, ProductService, NewUserDetailsService
└── repo/            # JPA repositories for all entities

src/main/resources/
├── templates/       # Thymeleaf HTML templates
│   └── blocks/      # Reusable header, footer, sidebar, product card fragments
└── static/          # CSS, JS, images
```

---

## Key Endpoints

| Path | Access | Description |
| --- | --- | --- |
| `/` | Public | Landing page |
| `/men`, `/women` | Public | Browse by gender |
| `/register`, `/login` | Public | Authentication |
| `/cart/{userName}` | Authenticated | Shopping cart |
| `/buy/{userName}` | Authenticated | Checkout |
| `/profile/{userName}` | Authenticated | User profile |
| `/admin` | Admin only | Admin dashboard |
| `/admin/databases/**` | Admin only | Product and user management |

---

## License

This project was developed as an IB Computer Science Internal Assessment.
