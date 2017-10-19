
package com.fracappzstudios.oilfieldnotifications.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewGroupuberAlpha implements Parcelable
{

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("details")
    @Expose
    private Groupdetail newGroupdetails;
    public final static Creator<NewGroupuberAlpha> CREATOR = new Creator<NewGroupuberAlpha>() {


        @SuppressWarnings({
            "unchecked"
        })
        public NewGroupuberAlpha createFromParcel(Parcel in) {
            NewGroupuberAlpha instance = new NewGroupuberAlpha();
            instance.status = ((String) in.readValue((String.class.getClassLoader())));
            instance.message = ((String) in.readValue((String.class.getClassLoader())));
            instance.newGroupdetails = ((Groupdetail) in.readValue((Groupdetail.class.getClassLoader())));
            return instance;
        }

        public NewGroupuberAlpha[] newArray(int size) {
            return (new NewGroupuberAlpha[size]);
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

    public Groupdetail getNewGroupdetails() {
        return newGroupdetails;
    }

    public void setNewGroupdetails(Groupdetail newGroupdetails) {
        this.newGroupdetails = newGroupdetails;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(message);
        dest.writeValue(newGroupdetails);
    }

    public int describeContents() {
        return  0;
    }

}
