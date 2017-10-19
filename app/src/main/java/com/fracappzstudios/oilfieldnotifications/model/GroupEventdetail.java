
package com.fracappzstudios.oilfieldnotifications.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupEventdetail {

    @SerializedName("group")
    @Expose
    private List<Groupdetail> groupdetail = null;
    @SerializedName("event")
    @Expose
    private List<Eventdetail> eventdetail = null;

    public List<Groupdetail> getGroupdetail() {
        return groupdetail;
    }

    public void setGroupdetail(List<Groupdetail> groupdetail) {
        this.groupdetail = groupdetail;
    }

    public List<Eventdetail> getEventdetail() {
        return eventdetail;
    }

    public void setEventdetail(List<Eventdetail> eventdetail) {
        this.eventdetail = eventdetail;
    }

}
