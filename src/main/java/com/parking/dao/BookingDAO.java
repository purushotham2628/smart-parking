package com.parking.dao;

import com.parking.model.Booking;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** DAO for bookings table. Uses transactions + row locking for concurrency safety. */
public class BookingDAO {

    /** Generate a unique booking id like BK20260422XXXXXX */
    public static String generateBookingId() {
        String ts  = String.valueOf(System.currentTimeMillis() / 1000); // seconds
        String rnd = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        return "BK" + ts + rnd;
    }

    /**
     * Create a booking with full concurrency control.
     *  1. Begin transaction
     *  2. SELECT ... FOR UPDATE on overlapping bookings (row-level lock)
     *  3. If count == 0  → INSERT new booking + commit
     *  4. Else            → rollback and return null
     */
    public Booking createBooking(int userId, int slotId, Timestamp start, Timestamp end,
                                 int hours, BigDecimal price) throws SQLException {
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);

            // Lock conflicting rows so no concurrent transaction can insert overlap.
            String checkSql =
                "SELECT booking_id FROM bookings " +
                "WHERE slot_id=? AND status='ACTIVE' " +
                "AND start_time < ? AND end_time > ? FOR UPDATE";
            try (PreparedStatement ps = con.prepareStatement(checkSql)) {
                ps.setInt(1, slotId);
                ps.setTimestamp(2, end);
                ps.setTimestamp(3, start);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        con.rollback();
                        return null;     // already booked
                    }
                }
            }

            String bookingId = generateBookingId();
            String insertSql =
                "INSERT INTO bookings(booking_id,user_id,slot_id,start_time,end_time," +
                "duration_hours,total_price,status) VALUES(?,?,?,?,?,?,?, 'ACTIVE')";
            try (PreparedStatement ps = con.prepareStatement(insertSql)) {
                ps.setString(1, bookingId);
                ps.setInt(2, userId);
                ps.setInt(3, slotId);
                ps.setTimestamp(4, start);
                ps.setTimestamp(5, end);
                ps.setInt(6, hours);
                ps.setBigDecimal(7, price);
                ps.executeUpdate();
            }
            con.commit();

            Booking b = new Booking();
            b.setBookingId(bookingId);
            b.setUserId(userId);
            b.setSlotId(slotId);
            b.setStartTime(start);
            b.setEndTime(end);
            b.setDurationHours(hours);
            b.setTotalPrice(price);
            b.setStatus("ACTIVE");
            return b;
        } catch (SQLException e) {
            if (con != null) con.rollback();
            throw e;
        } finally {
            if (con != null) { con.setAutoCommit(true); con.close(); }
        }
    }

    /** Bookings for a particular user with joined slot/area data. */
    public List<Booking> getByUser(int userId) throws SQLException {
        List<Booking> list = new ArrayList<>();
        String sql =
            "SELECT b.*, s.slot_number, s.vehicle_type, a.name AS area_name " +
            "FROM bookings b " +
            "JOIN parking_slots s ON b.slot_id=s.slot_id " +
            "JOIN parking_areas a ON s.area_id=a.area_id " +
            "WHERE b.user_id=? ORDER BY b.created_at DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs, true));
            }
        }
        return list;
    }

    public List<Booking> getAll() throws SQLException {
        List<Booking> list = new ArrayList<>();
        String sql =
            "SELECT b.*, s.slot_number, s.vehicle_type, a.name AS area_name, u.name AS user_name " +
            "FROM bookings b " +
            "JOIN parking_slots s ON b.slot_id=s.slot_id " +
            "JOIN parking_areas a ON s.area_id=a.area_id " +
            "JOIN users u ON b.user_id=u.user_id " +
            "ORDER BY b.created_at DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Booking b = mapRow(rs, true);
                b.setUserName(rs.getString("user_name"));
                list.add(b);
            }
        }
        return list;
    }

    /** Cancel a future booking owned by the given user. */
    public boolean cancel(String bookingId, int userId) throws SQLException {
        String sql = "UPDATE bookings SET status='CANCELLED' " +
                     "WHERE booking_id=? AND user_id=? AND status='ACTIVE' AND start_time > NOW()";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, bookingId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        }
    }

    /** Dashboard stats: total bookings + total revenue (active + completed). */
    public long countAll() throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM bookings");
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getLong(1) : 0;
        }
    }

    public BigDecimal totalRevenue() throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                 "SELECT COALESCE(SUM(total_price),0) FROM bookings WHERE status<>'CANCELLED'");
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getBigDecimal(1) : BigDecimal.ZERO;
        }
    }

    private Booking mapRow(ResultSet rs, boolean withJoins) throws SQLException {
        Booking b = new Booking();
        b.setBookingId(rs.getString("booking_id"));
        b.setUserId(rs.getInt("user_id"));
        b.setSlotId(rs.getInt("slot_id"));
        b.setStartTime(rs.getTimestamp("start_time"));
        b.setEndTime(rs.getTimestamp("end_time"));
        b.setDurationHours(rs.getInt("duration_hours"));
        b.setTotalPrice(rs.getBigDecimal("total_price"));
        b.setStatus(rs.getString("status"));
        b.setCreatedAt(rs.getTimestamp("created_at"));
        if (withJoins) {
            b.setSlotNumber(rs.getString("slot_number"));
            b.setVehicleType(rs.getString("vehicle_type"));
            b.setAreaName(rs.getString("area_name"));
        }
        return b;
    }
}
