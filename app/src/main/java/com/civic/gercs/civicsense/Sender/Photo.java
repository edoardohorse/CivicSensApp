package com.civic.gercs.civicsense.Sender;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Photo {

    @SerializedName("url")     @Expose
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Photo(String url, Report report) {
        this.url = url;
    }


    public class ResponsePhoto{
        @SerializedName("error") @Expose
        private Boolean error;
        @SerializedName("photos") @Expose
        private List<Photo> photos = null;

        public Boolean getError() {
            return error;
        }

        public void setError(Boolean error) {
            this.error = error;
        }

        public List<Photo> getPhotos() {
            return photos;
        }

        public void setPhotos(List<Photo> photos) {
            this.photos = photos;
        }


    }

}


