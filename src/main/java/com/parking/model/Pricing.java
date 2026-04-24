package com.parking.model;

import java.math.BigDecimal;
import java.sql.Time;

/** Pricing rule per area and vehicle type. */
public class Pricing {
    private int priceId;
    private int areaId;
    private String vehicleType;
    private BigDecimal pricePerHour;
    private BigDecimal peakMultiplier;
    private Time peakStart;
    private Time peakEnd;

    public Pricing() {}

    public int getPriceId() { return priceId; }
    public void setPriceId(int priceId) { this.priceId = priceId; }
    public int getAreaId() { return areaId; }
    public void setAreaId(int areaId) { this.areaId = areaId; }
    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }
    public BigDecimal getPricePerHour() { return pricePerHour; }
    public void setPricePerHour(BigDecimal pricePerHour) { this.pricePerHour = pricePerHour; }
    public BigDecimal getPeakMultiplier() { return peakMultiplier; }
    public void setPeakMultiplier(BigDecimal peakMultiplier) { this.peakMultiplier = peakMultiplier; }
    public Time getPeakStart() { return peakStart; }
    public void setPeakStart(Time peakStart) { this.peakStart = peakStart; }
    public Time getPeakEnd() { return peakEnd; }
    public void setPeakEnd(Time peakEnd) { this.peakEnd = peakEnd; }
}
