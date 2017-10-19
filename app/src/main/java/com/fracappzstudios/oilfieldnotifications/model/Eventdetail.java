
package com.fracappzstudios.oilfieldnotifications.model;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Eventdetail implements Parcelable
{
    @SerializedName("event_id")
    @Expose
    private String eventId;
    @SerializedName("group_id")
    @Expose
    private String groupId;
    @SerializedName("group_name")
    @Expose
    private String groupName;
    @SerializedName("event_name")
    @Expose
    private String eventName;
    @SerializedName("event_description")
    @Expose
    private String eventDescription;
    @SerializedName("event_image")
    @Expose
    private String eventImage;
    @SerializedName("event_attach_file")
    @Expose
    private String eventAttachFile;
    @SerializedName("create_date")
    @Expose
    private String createDate;
    @SerializedName("create_event_user_id")
    @Expose
    private List<Member> createEventUserId = null;
    @SerializedName("event_member")
    @Expose
    private List<PeopelDirdetail> peopelDirdetail = null;
    public final static Creator<Eventdetail> CREATOR = new Creator<Eventdetail>() {

        @SuppressWarnings({
            "unchecked"
        })
        public Eventdetail createFromParcel(Parcel in) {
            Eventdetail instance = new Eventdetail();
            instance.eventId = ((String) in.readValue((String.class.getClassLoader())));
            instance.groupId = ((String) in.readValue((String.class.getClassLoader())));
            instance.groupName = ((String) in.readValue((String.class.getClassLoader())));
            instance.eventName = ((String) in.readValue((String.class.getClassLoader())));
            instance.eventDescription = ((String) in.readValue((String.class.getClassLoader())));
            instance.eventImage = ((String) in.readValue((String.class.getClassLoader())));
            instance.eventAttachFile = ((String) in.readValue((String.class.getClassLoader())));
            instance.createDate = ((String) in.readValue((String.class.getClassLoader())));
            in.readList(instance.createEventUserId, (Member.class.getClassLoader()));
            in.readList(instance.peopelDirdetail, (PeopelDirdetail.class.getClassLoader()));
            return instance;
        }

        public Eventdetail[] newArray(int size) {
            return (new Eventdetail[size]);
        }

    }
    ;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
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

    public String getEventAttachFile() {
        return eventAttachFile;
    }

    public void setEventAttachFile(String eventAttachFile) {
        this.eventAttachFile = eventAttachFile;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public List<Member> getCreateEventUserId() {
        return createEventUserId;
    }

    public void setCreateEventUserId(List<Member> createEventUserId) {
        this.createEventUserId = createEventUserId;
    }

    public List<PeopelDirdetail> getPeopelDirdetail() {
        return peopelDirdetail;
    }

    public void setPeopelDirdetail(List<PeopelDirdetail> peopelDirdetail) {
        this.peopelDirdetail = peopelDirdetail;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(eventId);
        dest.writeValue(groupId);
        dest.writeValue(groupName);
        dest.writeValue(eventName);
        dest.writeValue(eventDescription);
        dest.writeValue(eventImage);
        dest.writeValue(eventAttachFile);
        dest.writeValue(createDate);
        dest.writeList(createEventUserId);
        dest.writeList(peopelDirdetail);
    }

    public int describeContents() {
        return  0;
    }

    @Override
    public String toString() {
        return "Eventdetail{" +
                "eventId='" + eventId + '\'' +
                ", groupId='" + groupId + '\'' +
                ", groupName='" + groupName + '\'' +
                ", eventName='" + eventName + '\'' +
                ", eventDescription='" + eventDescription + '\'' +
                ", eventImage='" + eventImage + '\'' +
                ", eventAttachFile='" + eventAttachFile + '\'' +
                ", createDate='" + createDate + '\'' +
                ", createEventUserId=" + createEventUserId +
                ", peopelDirdetail=" + peopelDirdetail +
                '}';
    }
}
