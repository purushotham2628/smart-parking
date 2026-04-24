package com.parking.dao;

import com.parking.model.ParkingArea;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** DAO for parking_areas table. */
public class ParkingAreaDAO {

    public List<ParkingArea> getAll() throws SQLException {
        List<ParkingArea> list = new ArrayList<>();
        String sql = "SELECT * FROM parking_areas ORDER BY area_id";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public ParkingArea getById(int id) throws SQLException {
        String sql = "SELECT * FROM parking_areas WHERE area_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    public boolean add(ParkingArea a) throws SQLException {
        String sql = "INSERT INTO parking_areas(name,location,description) VALUES(?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, a.getName());
            ps.setString(2, a.getLocation());
            ps.setString(3, a.getDescription());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean update(ParkingArea a) throws SQLException {
        String sql = "UPDATE parking_areas SET name=?,location=?,description=? WHERE area_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, a.getName());
            ps.setString(2, a.getLocation());
            ps.setString(3, a.getDescription());
            ps.setInt(4, a.getAreaId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM parking_areas WHERE area_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private ParkingArea mapRow(ResultSet rs) throws SQLException {
        ParkingArea a = new ParkingArea();
        a.setAreaId(rs.getInt("area_id"));
        a.setName(rs.getString("name"));
        a.setLocation(rs.getString("location"));
        a.setDescription(rs.getString("description"));
        return a;
    }
}
