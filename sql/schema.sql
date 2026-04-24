-- Smart Parking Slot Booking System - MySQL Schema
-- Run this in MySQL Workbench or CLI before starting the app.

DROP DATABASE IF EXISTS smart_parking;
CREATE DATABASE smart_parking;
USE smart_parking;

-- ============================================================
-- USERS
-- ============================================================
CREATE TABLE users (
    user_id      INT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(100) NOT NULL,
    email        VARCHAR(100) NOT NULL UNIQUE,
    password     VARCHAR(255) NOT NULL,
    phone        VARCHAR(20),
    role         ENUM('USER','ADMIN') DEFAULT 'USER',
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ============================================================
-- PARKING AREAS
-- ============================================================
CREATE TABLE parking_areas (
    area_id      INT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(100) NOT NULL,
    location     VARCHAR(200) NOT NULL,
    description  TEXT,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ============================================================
-- PARKING SLOTS
-- ============================================================
CREATE TABLE parking_slots (
    slot_id      INT AUTO_INCREMENT PRIMARY KEY,
    area_id      INT NOT NULL,
    slot_number  VARCHAR(20) NOT NULL,
    vehicle_type ENUM('2W','4W') NOT NULL,
    is_active    BOOLEAN DEFAULT TRUE,
    UNIQUE KEY uk_area_slot (area_id, slot_number),
    FOREIGN KEY (area_id) REFERENCES parking_areas(area_id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ============================================================
-- PRICING (per area + vehicle type)
-- ============================================================
CREATE TABLE pricing (
    price_id        INT AUTO_INCREMENT PRIMARY KEY,
    area_id         INT NOT NULL,
    vehicle_type    ENUM('2W','4W') NOT NULL,
    price_per_hour  DECIMAL(10,2) NOT NULL,
    peak_multiplier DECIMAL(4,2) DEFAULT 1.00,
    peak_start      TIME DEFAULT '18:00:00',
    peak_end        TIME DEFAULT '21:00:00',
    UNIQUE KEY uk_area_vt (area_id, vehicle_type),
    FOREIGN KEY (area_id) REFERENCES parking_areas(area_id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ============================================================
-- BOOKINGS
-- ============================================================
CREATE TABLE bookings (
    booking_id      VARCHAR(20) PRIMARY KEY,        -- e.g. BK20260422XXXX
    user_id         INT NOT NULL,
    slot_id         INT NOT NULL,
    start_time      DATETIME NOT NULL,
    end_time        DATETIME NOT NULL,
    duration_hours  INT NOT NULL,
    total_price     DECIMAL(10,2) NOT NULL,
    status          ENUM('ACTIVE','CANCELLED','COMPLETED') DEFAULT 'ACTIVE',
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (slot_id) REFERENCES parking_slots(slot_id),
    INDEX idx_slot_time (slot_id, start_time, end_time),
    INDEX idx_user (user_id)
) ENGINE=InnoDB;

-- ============================================================
-- SAMPLE DATA
-- ============================================================
-- Default admin: admin@parking.com / admin123
-- Default user : john@test.com / user123
INSERT INTO users (name, email, password, phone, role) VALUES
('Admin User', 'admin@parking.com', 'admin123', '9999999999', 'ADMIN'),
('John Doe',   'john@test.com',     'user123',  '9876543210', 'USER'),
('Jane Smith', 'jane@test.com',     'user123',  '9876543211', 'USER');

INSERT INTO parking_areas (name, location, description) VALUES
('City Mall Parking',   'MG Road, Bangalore',   'Underground parking, 24x7 security'),
('Airport Parking',     'Terminal 1, BLR',      'Long-term parking near airport'),
('Tech Park Parking',   'Whitefield, Bangalore','Office complex parking');

-- City Mall slots (area_id=1)
INSERT INTO parking_slots (area_id, slot_number, vehicle_type) VALUES
(1,'A1','4W'),(1,'A2','4W'),(1,'A3','4W'),(1,'A4','4W'),(1,'A5','4W'),
(1,'A6','4W'),(1,'A7','4W'),(1,'A8','4W'),
(1,'B1','2W'),(1,'B2','2W'),(1,'B3','2W'),(1,'B4','2W'),(1,'B5','2W'),(1,'B6','2W');

-- Airport slots (area_id=2)
INSERT INTO parking_slots (area_id, slot_number, vehicle_type) VALUES
(2,'C1','4W'),(2,'C2','4W'),(2,'C3','4W'),(2,'C4','4W'),
(2,'D1','2W'),(2,'D2','2W'),(2,'D3','2W');

-- Tech Park slots (area_id=3)
INSERT INTO parking_slots (area_id, slot_number, vehicle_type) VALUES
(3,'E1','4W'),(3,'E2','4W'),(3,'E3','4W'),(3,'E4','4W'),(3,'E5','4W'),
(3,'F1','2W'),(3,'F2','2W'),(3,'F3','2W'),(3,'F4','2W');

-- Pricing
INSERT INTO pricing (area_id, vehicle_type, price_per_hour, peak_multiplier) VALUES
(1,'4W',60.00,1.50),(1,'2W',30.00,1.50),
(2,'4W',80.00,1.25),(2,'2W',40.00,1.25),
(3,'4W',50.00,1.30),(3,'2W',25.00,1.30);
