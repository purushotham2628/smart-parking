package com.parking.dao;

import com.parking.model.Pricing;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/** DAO for pricing table + dynamic price calculation. */
public class PricingDAO {

    public Pricing get(int areaId, String vehicleType) throws SQLException {
        String sql = "SELECT * FROM pricing WHERE area_id=? AND vehicle_type=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, areaId);
            ps.setString(2, vehicleType);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    public List<Pricing> getAll() throws SQLException {
        List<Pricing> list = new ArrayList<>();
        String sql = "SELECT * FROM pricing ORDER BY area_id, vehicle_type";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public boolean upsert(Pricing p) throws SQLException {
        String sql = "INSERT INTO pricing(area_id,vehicle_type,price_per_hour,peak_multiplier,peak_start,peak_end) "
                   + "VALUES(?,?,?,?,?,?) "
                   + "ON DUPLICATE KEY UPDATE price_per_hour=VALUES(price_per_hour), "
                   + "peak_multiplier=VALUES(peak_multiplier), peak_start=VALUES(peak_start), peak_end=VALUES(peak_end)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, p.getAreaId());
            ps.setString(2, p.getVehicleType());
            ps.setBigDecimal(3, p.getPricePerHour());
            ps.setBigDecimal(4, p.getPeakMultiplier());
            ps.setTime(5, p.getPeakStart());
            ps.setTime(6, p.getPeakEnd());
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Dynamic pricing: total = pricePerHour * hours, multiplied by peakMultiplier
     * for any portion of the booking that overlaps the peak window.
     * Simplified: if booking start falls inside peak window, full multiplier applied.
     */
    public BigDecimal calculatePrice(Pricing p, Timestamp start, int hours) {
        BigDecimal base = p.getPricePerHour().multiply(BigDecimal.valueOf(hours));
        if (p.getPeakStart() == null || p.getPeakEnd() == null) return base;

        LocalTime startLt = start.toLocalDateTime().toLocalTime();
        LocalTime peakS = p.getPeakStart().toLocalTime();
        LocalTime peakE = p.getPeakEnd().toLocalTime();

        boolean inPeak = !startLt.isBefore(peakS) && startLt.isBefore(peakE);
        if (inPeak) {
            return base.multiply(p.getPeakMultiplier()).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        return base.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private Pricing mapRow(ResultSet rs) throws SQLException {
        Pricing p = new Pricing();
        p.setPriceId(rs.getInt("price_id"));
        p.setAreaId(rs.getInt("area_id"));
        p.setVehicleType(rs.getString("vehicle_type"));
        p.setPricePerHour(rs.getBigDecimal("price_per_hour"));
        p.setPeakMultiplier(rs.getBigDecimal("peak_multiplier"));
        p.setPeakStart(rs.getTime("peak_start"));
        p.setPeakEnd(rs.getTime("peak_end"));
        return p;
    }
}
