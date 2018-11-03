package com.civic.gercs.civicsense.Sender;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Report implements Serializable {

    @SerializedName("id")           @Expose private int id;
    @SerializedName("verified")     @Expose private Boolean verified;
    @SerializedName("grade")        @Expose private GradeReport grade = null;
    @SerializedName("city")         @Expose private String city;
    @SerializedName("address")      @Expose private String address;
    @SerializedName("description")  @Expose private String description;
    @SerializedName("location")     @Expose private Location location;
    @SerializedName("photos")       @Expose private List<String> photos = null;
    @SerializedName("cdt")          @Expose private String cdt = null;

    private ReportModel reportModel = null;

    public Boolean isVerified() {
        return verified;
    }

    public GradeReport getGrade() {
        return grade;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public void setGrade(GradeReport grade) {
        this.grade = grade;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getCity() {return city;}

    public void setCity(String city) {this.city = city;}

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public String getCdt() {return cdt;}

    public void setCdt(String cdt) {this.cdt = cdt;}


    public Report(){};

    public ReportModel getModel(){ return new ReportModel(this.address, this.description, this.grade);}

    public Report(String city, String address, String description, Location location) {
        this.city = city;
        this.address = address;
        this.description = description;
        this.location = location;
    }

    public class ResponseReport {

        @SerializedName("error")
        @Expose
        private Boolean error;
        @SerializedName("data")
        @Expose
        private List<Report> reports = null;

        public Boolean getError() {
            return error;
        }

        public void setError(Boolean error) {
            this.error = error;
        }

        public List<Report> getReports() {
            return reports;
        }

        public void setReports(List<Report> reports) {
            this.reports = reports;
        }

    }

    public class ReportModel{
        public String address;
        public String descrition;
        public GradeReport grade;

        public ReportModel(String address, String descrition, GradeReport grade) {
            this.address = address;
            this.descrition = descrition;
            this.grade = grade;
        }
    }
}