package com.civic.gercs.civicsense.Sender;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class City implements Serializable{


    @SerializedName("id")           @Expose private int id;
    @SerializedName("name")         @Expose private String name;
    @SerializedName("address")      @Expose private String address;
    @SerializedName("location")     @Expose private Location location;
    @SerializedName("bound_south")  @Expose private Location boundSouth;
    @SerializedName("bound_north")  @Expose private Location boundNorth;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Location getBoundSouth() {
        return boundSouth;
    }

    public void setBoundSouth(Location boundSouth) {
        this.boundSouth = boundSouth;
    }

    public Location getBoundNorth() {
        return boundNorth;
    }

    public void setBoundNorth(Location boundNorth) {
        this.boundNorth = boundNorth;
    }

    public City(){};

    public City(int id, String name, Location location, String address, Location boundSouth, Location boundNorth) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.address  = address;
        this.boundSouth = boundSouth;
        this.boundNorth = boundNorth;
    }

    public class ResponseCity {

        @SerializedName("error")
        @Expose
        private Boolean error;
        @SerializedName("city")
        @Expose
        private City city;



        public Boolean getError() {
            return error;
        }

        public void setError(Boolean error) {
            this.error = error;
        }

        public City getCity() {
            return city;
        }

        public void setCity(City city) {

            this.city = city;
        }

    }
}