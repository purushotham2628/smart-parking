package com.parking.dao;

import com.parking.model.ParkingSlot;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** DAO for parking_slots table. */
public class ParkingSlotDAO {

    public List<ParkingSlot> getByArea(int areaId) throws SQLException {
        List<ParkingSlot> list = new ArrayList<>();
        String sql = "SELECT * FROM parking_slots WHERE area_id=? ORDER BY vehicle_type, slot_number";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, areaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    /**
     * Returns all slots in an area annotated with whether they are booked
     * during the requested [start, end) time window.
     * Uses a LEFT JOIN with overlap check (start1 < end2 AND end1 > start2).
     */
    public List<ParkingSlot> getAvailability(int areaId, Timestamp start, Timestamp end)
            throws SQLException {
        List<ParkingSlot> list = new ArrayList<>();
        String sql =
            "SELECT s.*, " +
            " (SELECT COUNT(*) FROM bookings b WHERE b.slot_id=s.slot_id " +
            "    AND b.status='ACTIVE' AND b.start_time < ? AND b.end_time > ?) AS conflicts " +
            "FROM parking_slots s WHERE s.area_id=? AND s.is_active=TRUE " +
            "ORDER BY s.vehicle_type, s.slot_number";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setTimestamp(1, end);
            ps.setTimestamp(2, start);
            ps.setInt(3, areaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ParkingSlot s = mapRow(rs);
                    s.setBooked(rs.getInt("conflicts") > 0);
                    list.add(s);
                }
            }
        }
        return list;
    }

    public ParkingSlot getById(int slotId) throws SQLException {
        String sql = "SELECT * FROM parking_slots WHERE slot_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, slotId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    public boolean add(ParkingSlot s) throws SQLException {
        String sql = "INSERT INTO parking_slots(area_id,slot_number,vehicle_type,is_active) VALUES(?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, s.getAreaId());
            ps.setString(2, s.getSlotNumber());
            ps.setString(3, s.getVehicleType());
            ps.setBoolean(4, s.isActive());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int slotId) throws SQLException {
        String sql = "DELETE FROM parking_slots WHERE slot_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, slotId);
            return ps.executeUpdate() > 0;
        }
    }

    private ParkingSlot mapRow(ResultSet rs) throws SQLException {
        ParkingSlot s = new ParkingSlot();
        s.setSlotId(rs.getInt("slot_id"));
        s.setAreaId(rs.getInt("area_id"));
        s.setSlotNumber(rs.getString("slot_number"));
        s.setVehicleType(rs.getString("vehicle_type"));
        s.setActive(rs.getBoolean("is_active"));
        return s;
    }
}
