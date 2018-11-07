package com.civic.gercs.civicsense.Sender;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Report implements Serializable {

    @SerializedName("id")           @Expose private int id;
    @SerializedName("verified")     @Expose private Boolean verified;
    @SerializedName("grade")        @Expose private GradeReport grade = null;
    @SerializedName("city")         @Expose private String city;
    @SerializedName("address")      @Expose private String address;
    @SerializedName("type")         @Expose private String type;
    @SerializedName("description")  @Expose private String description;
    @SerializedName("location")     @Expose private Location location;
    @SerializedName("photos")       @Expose private List<String> photos = null;
    @SerializedName("history")       @Expose private List<History> history = null;
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

    public String getType() {return type;}

    public void setType(String type) {this.type = type;    }

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

    public List<History> getHistory() {return history;}

    public void setHistory(List<History> history) {this.history = history;}

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

        @SerializedName("error")    @Expose private Boolean error;
        @SerializedName("message")    @Expose private String message;
        @SerializedName("data")     @Expose private List<Report> reports = null;

        public String getMessage() {return message;}

        public void setMessage(String message) {this.message = message;}

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

    public class ResponseNewReport{
        @SerializedName("error")    @Expose private Boolean error;
        @SerializedName("message")    @Expose private String message;
        @SerializedName("data")     @Expose private Cdt cdt= null;

        public String getMessage() {return message;}

        public void setMessage(String message) {this.message = message;}

        public Boolean getError() {
            return error;
        }

        public void setError(Boolean error) {
            this.error = error;
        }

        public Cdt getCdt() {
            return cdt;
        }

        public void setCdt(Cdt cdt) {
            this.cdt = cdt;
        }

        public class Cdt{
            public String getCdt() {
                return cdt;
            }

            public void setCdt(String cdt) {
                this.cdt = cdt;
            }

            @SerializedName("cdt") @Expose String cdt;

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

    public class TypeReport{
        @SerializedName("id")   @Expose private int id;
        @SerializedName("name") @Expose private String name;

        public int getId() {return id;}

        public void setId(int id) {this.id = id;}

        public String getName() {return name;}

        public void setName(String name) {this.name = name;}

    }

    public class ResponseTypeReport {
        @SerializedName("error") @Expose private Boolean error;
        @SerializedName("message")    @Expose private String message;
        @SerializedName("data")  @Expose private List<TypeReport> types = null;

        public String getMessage() {return message;}

        public void setMessage(String message) {this.message = message;}

        public Boolean getError() {
            return error;
        }

        public void setError(Boolean error) {
            this.error = error;
        }

        public List<TypeReport> getTypes() {
            return types;
        }

        public void setTypes(List<TypeReport> types) {
            this.types = types;
        }




    }

    public class History{
         @SerializedName("date") @Expose private String date;
         @SerializedName("note") @Expose private String note;

        public String getDate() {return date;}

        public void setDate(String date) {this.date = date;}

        public String getNote() {return note;}

        public void setNote(String note) {this.note = note;}
    }

    public class ResponseHistory{
        @SerializedName("error") @Expose private Boolean error;
        @SerializedName("message")    @Expose private String message;
        @SerializedName("data")  @Expose private List<History> history = null;

        public Boolean getError() {return error;}

        public void setError(Boolean error) {this.error = error;}

        public String getMessage() {return message;}

        public void setMessage(String message) {this.message = message;}

        public List<History> getHistory() {return history;}

        public void setHistory(List<History> history) {this.history = history;}
    }
}