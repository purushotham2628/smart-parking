package com.parking.model;

/** Represents a parking area / facility. */
public class ParkingArea {
    private int areaId;
    private String name;
    private String location;
    private String description;

    public ParkingArea() {}
    public ParkingArea(String name, String location, String description) {
        this.name = name;
        this.location = location;
        this.description = description;
    }

    public int getAreaId() { return areaId; }
    public void setAreaId(int areaId) { this.areaId = areaId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
