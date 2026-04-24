# Smart Parking Slot Booking System

Full-stack Java EE web application for booking parking slots.

## Tech Stack

| Layer       | Technology                              |
|-------------|------------------------------------------|
| Frontend    | HTML, CSS, JavaScript, JSP               |
| Backend     | Java Servlets (Jakarta EE 6 / Servlet 6) |
| Database    | MySQL 8                                  |
| Server      | Apache Tomcat 10.1+                      |
| Connectivity| JDBC                                     |
| Architecture| MVC (Servlet=Controller, JSP=View, DAO=Model) |

> NOTE: Tomcat 10+ uses the `jakarta.*` package namespace. If you are stuck on Tomcat 9, replace every `jakarta.servlet.*` import with `javax.servlet.*`.

---

## Project Structure (Eclipse Dynamic Web Project)

```
SmartParking/
├── src/main/java/com/parking/
│   ├── model/                 (User, ParkingArea, ParkingSlot, Booking, Pricing)
│   ├── dao/                   (DBConnection, UserDAO, ParkingAreaDAO,
│   │                           ParkingSlotDAO, PricingDAO, BookingDAO)
│   ├── servlet/               (Login, Register, Logout, Slot, Booking,
│   │                           CancelBooking, History, AdminArea,
│   │                           AdminSlot, AdminPricing, AdminBookings,
│   │                           AdminDashboard)
│   └── util/SessionFilter.java
│
├── src/main/webapp/
│   ├── WEB-INF/
│   │   ├── web.xml
│   │   └── lib/               <-- drop mysql-connector-j-8.x.x.jar here
│   ├── css/style.css
│   ├── js/slots.js
│   ├── index.jsp
│   ├── login.jsp
│   ├── register.jsp
│   ├── error.jsp
│   ├── user/                  (home.jsp, slots.jsp, booking.jsp, history.jsp)
│   └── admin/                 (login.jsp, dashboard.jsp, areas.jsp,
│                               slots.jsp, pricing.jsp, bookings.jsp)
│
└── sql/schema.sql
```

---

## Setup Steps

### 1. Database

1. Install MySQL 8.
2. Open MySQL Workbench (or CLI) and run the script:
   ```bash
   mysql -u root -p < sql/schema.sql
   ```
3. The script creates the `smart_parking` database, all tables, and sample data.

### 2. JDBC Driver

1. Download **mysql-connector-j-8.x.x.jar** from https://dev.mysql.com/downloads/connector/j/.
2. Copy it into `src/main/webapp/WEB-INF/lib/`.
3. In Eclipse: right-click the project → *Build Path → Configure Build Path → Libraries → Add JARs* → select the connector.

### 3. Update DB credentials

Open `src/main/java/com/parking/dao/DBConnection.java` and edit:

```java
private static final String URL  =
    "jdbc:mysql://localhost:3306/smart_parking?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
private static final String USER = "root";
private static final String PASS = "YOUR_PASSWORD";
```

### 4. Tomcat in Eclipse

1. Install **Apache Tomcat 10.1+** (https://tomcat.apache.org/).
2. In Eclipse: *Window → Preferences → Server → Runtime Environments → Add → Apache Tomcat v10.1* → set its install folder.
3. Right-click the project → *Run As → Run on Server* → choose your Tomcat 10 instance.
4. Eclipse opens the app at `http://localhost:8080/SmartParking/`.

### 5. Login Credentials (sample)

| Role  | Email               | Password  |
|-------|---------------------|-----------|
| User  | john@test.com       | user123   |
| User  | jane@test.com       | user123   |
| Admin | admin@parking.com   | admin123  |

---

## How the App Works

### User flow
1. Register / Login → session created.
2. **Find a Slot** — pick area, date, start time, duration.
3. Slot grid loads, color-coded:
   - **Green** = available
   - **Red**   = already booked for that time window
   - **Yellow** = currently selected
4. Click an available slot → **Confirm Booking**.
5. Booking confirmation page shows unique Booking ID + total price.
6. **My Bookings** → see history, cancel future bookings.

### Admin flow
1. `/admin/login.jsp` → admin sign-in.
2. **Dashboard** — total bookings + revenue + area count.
3. **Areas** — CRUD parking areas.
4. **Slots** — add/delete slots per area, choose 2W/4W.
5. **Pricing** — set per-hour price, peak multiplier and peak window.
6. **Bookings** — view every booking made.

### Concurrency / Double-booking prevention
`BookingDAO.createBooking()` uses a JDBC transaction:

1. `setAutoCommit(false)`.
2. `SELECT ... FOR UPDATE` on overlapping bookings → row-level lock in InnoDB.
3. If a conflict exists → rollback, return `null`.
4. Otherwise → INSERT, commit.

This guarantees that two users hitting "Book" at the same instant cannot both succeed for the same slot.

### Dynamic pricing
`PricingDAO.calculatePrice()` multiplies base rate × hours, then applies the area's peak multiplier whenever the booking start time falls inside the configured peak window (e.g. 18:00–21:00 × 1.5).

### Unique Booking ID
`BookingDAO.generateBookingId()` returns `BK<unix-seconds><4-char-uuid>`.

---

## Validation & Error Handling
- HTML5 form validation (`required`, `type=email`, `type=number`, `pattern`).
- Server-side checks in servlets (empty fields, invalid duration, past times).
- All servlets wrap exceptions and forward to `error.jsp` via `web.xml`.
- `SessionFilter` enforces auth on `/user/*` and `/admin/*`.

---

## Notes
- Passwords are stored in plaintext for simplicity. In production, hash them with BCrypt.
- All times use server time. Adjust `serverTimezone` in the JDBC URL if needed.
