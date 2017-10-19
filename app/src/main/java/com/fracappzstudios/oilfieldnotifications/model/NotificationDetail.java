
package com.fracappzstudios.oilfieldnotifications.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationDetail {

    @SerializedName("push_id")
    @Expose
    private String pushId;
    @SerializedName("open")
    @Expose
    private String open;
    @SerializedName("create_date")
    @Expose
    private String createDate;
    @SerializedName("file")
    @Expose
    private String file;
    @SerializedName("alert_msg")
    @Expose
    private String alert_msg;
    @SerializedName("group_id")
    @Expose
    private String group_id;
    @SerializedName("group_name")
    @Expose
    private String groupName;
    @SerializedName("event_id")
    @Expose
    private String event_id;
    @SerializedName("event_name")
    @Expose
    private String eventName;
    @SerializedName("sender_first_name")
    @Expose
    private String senderFirstName;
    @SerializedName("sender_last_name")
    @Expose
    private String senderLastName;
    @SerializedName("sender_email")
    @Expose
    private String senderEmail;

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getAlert_msg() {
        return alert_msg;
    }

    public void setAlert_msg(String alert_msg) {
        this.alert_msg = alert_msg;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getSenderFirstName() {
        return senderFirstName;
    }

    public void setSenderFirstName(String senderFirstName) {
        this.senderFirstName = senderFirstName;
    }

    public String getSenderLastName() {
        return senderLastName;
    }

    public void setSenderLastName(String senderLastName) {
        this.senderLastName = senderLastName;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    @Override
    public String toString() {
        return "NotificationDetail{" +
                "pushId='" + pushId + '\'' +
                ", open='" + open + '\'' +
                ", createDate='" + createDate + '\'' +
                ", file='" + file + '\'' +
                ", alert_msg='" + alert_msg + '\'' +
                ", group_id='" + group_id + '\'' +
                ", groupName='" + groupName + '\'' +
                ", event_id='" + event_id + '\'' +
                ", eventName='" + eventName + '\'' +
                ", senderFirstName='" + senderFirstName + '\'' +
                ", senderLastName='" + senderLastName + '\'' +
                ", senderEmail='" + senderEmail + '\'' +
                '}';
    }
}
