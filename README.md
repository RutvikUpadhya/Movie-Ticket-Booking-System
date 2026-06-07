# 🎬 Movie Ticket Booking System
A beginner-friendly **Java Swing + MySQL + JDBC + Maven** mini project.

---

## 📁 Project Structure

```
Ticket Booking System/
├── src/main/java/com/movieticket/
│   ├── Main.java           ← Entry point
│   ├── DBConnection.java   ← JDBC database connection
│   ├── HomePage.java       ← Home screen with 3 movies
│   └── SeatSelection.java  ← Seat grid, booking & DB save
├── pom.xml                 ← Maven config (MySQL driver)
└── README.md
```

---

## 🛠️ Step 1 – Prerequisites

| Tool | Version | Download |
|------|---------|----------|
| JDK | 17 (or 11) | https://adoptium.net |
| Maven | 3.8+ | https://maven.apache.org/download.cgi |
| MySQL | 8.x | https://dev.mysql.com/downloads/ |
| VS Code | Latest | https://code.visualstudio.com |

**VS Code Extensions needed:**
- `Extension Pack for Java` (Microsoft) — installs Java + Maven support

---

## 🗄️ Step 2 – Create the Database in MySQL Workbench

Open **MySQL Workbench** and run the following SQL:

```sql
-- 1. Create the database
CREATE DATABASE IF NOT EXISTS movie_booking;

-- 2. Switch to it
USE movie_booking;

-- 3. Create the bookings table
CREATE TABLE IF NOT EXISTS bookings (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    movie_name VARCHAR(50),
    seat_no    VARCHAR(10)
);
```

---

## 🔑 Step 3 – Update Database Credentials

Open `src/main/java/com/movieticket/DBConnection.java` and change:

```java
private static final String DB_URL   = "jdbc:mysql://localhost:3306/movie_booking";
private static final String USER     = "root";      // ← your MySQL username
private static final String PASSWORD = "root";      // ← your MySQL password
```

---

## ▶️ Step 4 – Run the Project

### Option A – Using VS Code terminal

```bash
# 1. Install dependencies and compile
mvn clean compile

# 2. Run the application
mvn exec:java
```

### Option B – Using VS Code's built-in Run button

1. Open `Main.java`
2. Click the **▶ Run** button that appears above `public static void main`

---

## 🎮 How to Use

1. **Home Page** – Three movies are displayed. Click **Book Ticket** on any movie.
2. **Seat Selection** – A 5×5 grid opens.
   - 🟢 **Green** = Available (click to select)
   - 🔵 **Blue** = Selected by you (click again to deselect)
   - 🔴 **Red** = Already booked (disabled)
   - The total amount updates dynamically (₹200 per seat).
3. Click **Confirm Booking ✔** to save seats to the database.
4. A confirmation popup shows the movie name, seats, and total.
5. Click **← Back** to return to the Home Page.

---

## 💡 Tips for Beginners

- If you see a **Database Warning** when opening Seat Selection, make sure:
  - MySQL server is running
  - Credentials in `DBConnection.java` are correct
  - The `movie_booking` database and `bookings` table exist
- You can verify bookings by running `SELECT * FROM bookings;` in MySQL Workbench.

---

## 📦 Maven Dependency (pom.xml)

Only one external dependency is used:

```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <version>8.3.0</version>
</dependency>
```

Maven downloads this automatically on first `mvn compile`.
