
package com.fracappzstudios.oilfieldnotifications.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewEventdetails {

    @SerializedName("event_id")
    @Expose
    private String eventId;
    @SerializedName("event_name")
    @Expose
    private String eventName;
    @SerializedName("event_description")
    @Expose
    private String eventDescription;
    @SerializedName("event_image")
    @Expose
    private String eventImage;
    @SerializedName("event_file")
    @Expose
    private String eventFile;
    @SerializedName("create_date")
    @Expose
    private String createDate;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("create_by_user")
    @Expose
    private List<CreateByUser> createByUser = null;
    @SerializedName("group_id")
    @Expose
    private String groupId;
    @SerializedName("group_name")
    @Expose
    private String groupName;
    @SerializedName("event_member")
    @Expose
    private List<Member> eventMember = null;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventImage() {
        return eventImage;
    }

    public void setEventImage(String eventImage) {
        this.eventImage = eventImage;
    }

    public String getEventFile() {
        return eventFile;
    }

    public void setEventFile(String eventFile) {
        this.eventFile = eventFile;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<CreateByUser> getCreateByUser() {
        return createByUser;
    }

    public void setCreateByUser(List<CreateByUser> createByUser) {
        this.createByUser = createByUser;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<Member> getEventMember() {
        return eventMember;
    }

    public void setEventMember(List<Member> eventMember) {
        this.eventMember = eventMember;
    }

}
