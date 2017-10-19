
package com.fracappzstudios.oilfieldnotifications.model;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Groupdetail implements Parcelable
{
    @SerializedName("group_id")
    @Expose
    private String groupId;
    @SerializedName("group_name")
    @Expose
    private String groupName;
    @SerializedName("group_description")
    @Expose
    private String groupDescription;
    @SerializedName("group_image")
    @Expose
    private String groupImage;
    @SerializedName("create_date")
    @Expose
    private String createDate;
    @SerializedName("updated_date")
    @Expose
    private String updatedDate;
    @SerializedName("group_member")
    @Expose
    private List<PeopelDirdetail> peopelDirdetail = null;
    public final static Creator<Groupdetail> CREATOR = new Creator<Groupdetail>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Groupdetail createFromParcel(Parcel in) {
            Groupdetail instance = new Groupdetail();
            instance.groupId = ((String) in.readValue((String.class.getClassLoader())));
            instance.groupName = ((String) in.readValue((String.class.getClassLoader())));
            instance.groupDescription = ((String) in.readValue((String.class.getClassLoader())));
            instance.groupImage = ((String) in.readValue((String.class.getClassLoader())));
            instance.createDate = ((String) in.readValue((String.class.getClassLoader())));
            instance.updatedDate = ((String) in.readValue((String.class.getClassLoader())));
            in.readList(instance.peopelDirdetail, (PeopelDirdetail.class.getClassLoader()));
            return instance;
        }

        public Groupdetail[] newArray(int size) {
            return (new Groupdetail[size]);
        }

    }
    ;

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

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public String getGroupImage() {
        return groupImage;
    }

    public void setGroupImage(String groupImage) {
        this.groupImage = groupImage;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public List<PeopelDirdetail> getPeopelDirdetail() {
        return peopelDirdetail;
    }

    public void setPeopelDirdetail(List<PeopelDirdetail> peopelDirdetail) {
        this.peopelDirdetail = peopelDirdetail;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(groupId);
        dest.writeValue(groupName);
        dest.writeValue(groupDescription);
        dest.writeValue(groupImage);
        dest.writeValue(createDate);
        dest.writeValue(updatedDate);
        dest.writeList(peopelDirdetail);
    }

    public int describeContents() {
        return  0;
    }

}
