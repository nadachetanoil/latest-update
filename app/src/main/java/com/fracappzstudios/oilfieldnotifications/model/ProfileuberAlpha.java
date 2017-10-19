
package com.fracappzstudios.oilfieldnotifications.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileuberAlpha {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("details")
    @Expose
    private Profiledetails profiledetails;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Profiledetails getProfiledetails() {
        return profiledetails;
    }

    public void setProfiledetails(Profiledetails profiledetails) {
        this.profiledetails = profiledetails;
    }

}
