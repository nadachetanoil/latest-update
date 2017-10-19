
package com.fracappzstudios.oilfieldnotifications.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewEventuberAlpha {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("details")
    @Expose
    private NewEventdetails newEventdetails;

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

    public NewEventdetails getNewEventdetails() {
        return newEventdetails;
    }

    public void setNewEventdetails(NewEventdetails newEventdetails) {
        this.newEventdetails = newEventdetails;
    }

}
