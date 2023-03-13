package com.example.saudiexpertsecondversion.Object;

import java.io.Serializable;

public class Tour implements Serializable {
    private String tourCity;
    private String tourImageUrl;
    private String tourPrice;
    private String tourTitle;
    private String tourID;
    private String tourDescription;
    private String tourGuideKey;
    private String isBooked;

    public String getIsBooked() {
        return isBooked;
    }

    public void setIsBooked(String isBooked) {
        this.isBooked = isBooked;
    }

    public Tour() {

    }

    public String getTourGuideKey() {
        return tourGuideKey;
    }

    public void setTourGuideKey(String tourGuideKey) {
        this.tourGuideKey = tourGuideKey;
    }

    public String getTourDescription() {
        return tourDescription;
    }

    public void setTourDescription(String tourDescription) {
        this.tourDescription = tourDescription;
    }

    public String getTourID() {
        return tourID;
    }

    public void setTourID(String tourID) {
        this.tourID = tourID;
    }

    public String getTourTitle() {
        return tourTitle;
    }

    public void setTourTitle(String tourTitle) {
        this.tourTitle = tourTitle;
    }

    public String getTourPrice() {
        return tourPrice;
    }

    public void setTourPrice(String tourPrice) {
        this.tourPrice = tourPrice;
    }

    public String getTourImageUrl() {
        return tourImageUrl;
    }

    public void setTourImageUrl(String tourImageUrl) {
        this.tourImageUrl = tourImageUrl;
    }

    public String getTourCity() {
        return tourCity;
    }

    public void setTourCity(String tourCity) {
        this.tourCity = tourCity;
    }

}
