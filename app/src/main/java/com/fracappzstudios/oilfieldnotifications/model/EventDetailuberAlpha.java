
package com.fracappzstudios.oilfieldnotifications.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventDetailuberAlpha {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("details")
    @Expose
    private List<GroupEventdetail> groupEventdetails = null;

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

    public List<GroupEventdetail> getGroupEventdetails() {
        return groupEventdetails;
    }

    public void setGroupEventdetails(List<GroupEventdetail> groupEventdetails) {
        this.groupEventdetails = groupEventdetails;
    }

}
