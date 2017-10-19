
package com.fracappzstudios.oilfieldnotifications.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginUberAlpha implements Parcelable
{

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("details")
    @Expose
    private Logindetails logindetails;
    public final static Creator<LoginUberAlpha> CREATOR = new Creator<LoginUberAlpha>() {


        @SuppressWarnings({
            "unchecked"
        })
        public LoginUberAlpha createFromParcel(Parcel in) {
            LoginUberAlpha instance = new LoginUberAlpha();
            instance.status = ((String) in.readValue((String.class.getClassLoader())));
            instance.message = ((String) in.readValue((String.class.getClassLoader())));
            instance.logindetails = ((Logindetails) in.readValue((Logindetails.class.getClassLoader())));
            return instance;
        }

        public LoginUberAlpha[] newArray(int size) {
            return (new LoginUberAlpha[size]);
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

    public Logindetails getLogindetails() {
        return logindetails;
    }

    public void setLogindetails(Logindetails logindetails) {
        this.logindetails = logindetails;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(message);
        dest.writeValue(logindetails);
    }

    public int describeContents() {
        return  0;
    }

}
