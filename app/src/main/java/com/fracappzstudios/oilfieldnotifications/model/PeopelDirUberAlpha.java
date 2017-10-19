
package com.fracappzstudios.oilfieldnotifications.model;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PeopelDirUberAlpha implements Parcelable
{

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("details")
    @Expose
    private List<PeopelDirdetail> peopelDirdetails = null;
    public final static Creator<PeopelDirUberAlpha> CREATOR = new Creator<PeopelDirUberAlpha>() {


        @SuppressWarnings({
            "unchecked"
        })
        public PeopelDirUberAlpha createFromParcel(Parcel in) {
            PeopelDirUberAlpha instance = new PeopelDirUberAlpha();
            instance.status = ((String) in.readValue((String.class.getClassLoader())));
            instance.message = ((String) in.readValue((String.class.getClassLoader())));
            in.readList(instance.peopelDirdetails, (PeopelDirdetail.class.getClassLoader()));
            return instance;
        }

        public PeopelDirUberAlpha[] newArray(int size) {
            return (new PeopelDirUberAlpha[size]);
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

    public List<PeopelDirdetail> getPeopelDirdetails() {
        return peopelDirdetails;
    }

    public void setPeopelDirdetails(List<PeopelDirdetail> peopelDirdetails) {
        this.peopelDirdetails = peopelDirdetails;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(message);
        dest.writeList(peopelDirdetails);
    }

    public int describeContents() {
        return  0;
    }

}
