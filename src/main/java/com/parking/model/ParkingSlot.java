package com.parking.model;

/** Represents an individual parking slot inside a parking area. */
public class ParkingSlot {
    private int slotId;
    private int areaId;
    private String slotNumber;
    private String vehicleType;   // 2W or 4W
    private boolean active;

    // Transient: filled when querying availability for a given time range.
    private boolean booked;

    public ParkingSlot() {}

    public int getSlotId() { return slotId; }
    public void setSlotId(int slotId) { this.slotId = slotId; }
    public int getAreaId() { return areaId; }
    public void setAreaId(int areaId) { this.areaId = areaId; }
    public String getSlotNumber() { return slotNumber; }
    public void setSlotNumber(String slotNumber) { this.slotNumber = slotNumber; }
    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public boolean isBooked() { return booked; }
    public void setBooked(boolean booked) { this.booked = booked; }
}
