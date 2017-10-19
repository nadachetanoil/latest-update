
package com.fracappzstudios.oilfieldnotifications.model;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupUberAlpha implements Parcelable
{

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("details")
    @Expose
    private List<Groupdetail> groupdetails = null;
    public final static Creator<GroupUberAlpha> CREATOR = new Creator<GroupUberAlpha>() {


        @SuppressWarnings({
            "unchecked"
        })
        public GroupUberAlpha createFromParcel(Parcel in) {
            GroupUberAlpha instance = new GroupUberAlpha();
            instance.status = ((String) in.readValue((String.class.getClassLoader())));
            instance.message = ((String) in.readValue((String.class.getClassLoader())));
            in.readList(instance.groupdetails, (Groupdetail.class.getClassLoader()));
            return instance;
        }

        public GroupUberAlpha[] newArray(int size) {
            return (new GroupUberAlpha[size]);
        }

    }
    ;

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

    public List<Groupdetail> getGroupdetails() {
        return groupdetails;
    }

    public void setGroupdetails(List<Groupdetail> groupdetails) {
        this.groupdetails = groupdetails;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(message);
        dest.writeList(groupdetails);
    }

    public int describeContents() {
        return  0;
    }

}
