package com.driver.model;

import javax.persistence.*;

@Entity
public class Cab {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int cabId;
    int perKm;
    boolean available ;

    @OneToOne(mappedBy = "cab", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Driver driver ;

    public Cab() {
    }

    public Cab(int cabId, int perKm, boolean available, Driver driver) {
        this.cabId = cabId;
        this.perKm = perKm;
        this.available = available;
        this.driver = driver;
    }

    public int getCabId() {
        return cabId;
    }

    public void setCabId(int cabId) {
        this.cabId = cabId;
    }

    public int getPerKm() {
        return perKm;
    }

    public void setPerKm(int perKm) {
        this.perKm = perKm;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }
}