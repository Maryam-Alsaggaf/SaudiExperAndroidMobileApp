package com.example.saudiexpertsecondversion.Object;

import java.io.Serializable;

public class Appointment implements Serializable {
    private String status;
    private String numberOfHours;
    private String tourID;
    private String startTime;
    private String appointmentID;
    private String meetingLocation;
    private String date;
    private String tourGuideKey;
    private String touristKey;
    public Appointment(){

    }

    public String getTouristKey() {
        return touristKey;
    }

    public void setTouristKey(String touristKey) {
        this.touristKey = touristKey;
    }

    public String getTourGuideKey() {
        return tourGuideKey;
    }

    public void setTourGuideKey(String tourGuideKey) {
        this.tourGuideKey = tourGuideKey;
    }

    public String getMeetingLocation() {
        return meetingLocation;
    }

    public void setMeetingLocation(String meetingLocation) {
        this.meetingLocation = meetingLocation;
    }

    public String getNumberOfHours() {
        return numberOfHours;
    }

    public void setNumberOfHours(String numberOfHours) {
        this.numberOfHours = numberOfHours;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(String appointmentID) {
        this.appointmentID = appointmentID;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getTourID() {
        return tourID;
    }

    public void setTourID(String tourID) {
        this.tourID = tourID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
