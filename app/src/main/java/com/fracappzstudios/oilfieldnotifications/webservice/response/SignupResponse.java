
package com.fracappzstudios.oilfieldnotifications.webservice.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.fracappzstudios.oilfieldnotifications.model.UberAlpha;

public class SignupResponse implements Parcelable
{

    @SerializedName("uber_alpha")
    @Expose
    private UberAlpha uberAlpha;
    public final static Creator<SignupResponse> CREATOR = new Creator<SignupResponse>() {


        @SuppressWarnings({
            "unchecked"
        })
        public SignupResponse createFromParcel(Parcel in) {
            SignupResponse instance = new SignupResponse();
            instance.uberAlpha = ((UberAlpha) in.readValue((UberAlpha.class.getClassLoader())));
            return instance;
        }

        public SignupResponse[] newArray(int size) {
            return (new SignupResponse[size]);
        }

    }
    ;

    public UberAlpha getUberAlpha() {
        return uberAlpha;
    }

    public void setUberAlpha(UberAlpha uberAlpha) {
        this.uberAlpha = uberAlpha;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(uberAlpha);
    }

    public int describeContents() {
        return  0;
    }

}
