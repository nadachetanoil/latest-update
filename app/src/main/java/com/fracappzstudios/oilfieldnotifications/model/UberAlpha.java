
package com.fracappzstudios.oilfieldnotifications.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UberAlpha implements Parcelable
{

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("details")
    @Expose
    private Details details;
    public final static Creator<UberAlpha> CREATOR = new Creator<UberAlpha>() {


        @SuppressWarnings({
            "unchecked"
        })
        public UberAlpha createFromParcel(Parcel in) {
            UberAlpha instance = new UberAlpha();
            instance.status = ((String) in.readValue((String.class.getClassLoader())));
            instance.message = ((String) in.readValue((String.class.getClassLoader())));
            instance.details = ((Details) in.readValue((Details.class.getClassLoader())));
            return instance;
        }

        public UberAlpha[] newArray(int size) {
            return (new UberAlpha[size]);
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

    public Details getDetails() {
        return details;
    }

    public void setDetails(Details details) {
        this.details = details;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(message);
        dest.writeValue(details);
    }

    public int describeContents() {
        return  0;
    }

}
