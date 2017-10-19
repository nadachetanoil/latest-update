
package com.fracappzstudios.oilfieldnotifications.webservice.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.fracappzstudios.oilfieldnotifications.model.ForgotPwduberAlpha;

public class ForgotPasswordResponse implements Parcelable
{

    @SerializedName("uber_alpha")
    @Expose
    private ForgotPwduberAlpha forgotPwduberAlpha;
    public final static Creator<ForgotPasswordResponse> CREATOR = new Creator<ForgotPasswordResponse>() {

        @SuppressWarnings({
            "unchecked"
        })
        public ForgotPasswordResponse createFromParcel(Parcel in) {
            ForgotPasswordResponse instance = new ForgotPasswordResponse();
            instance.forgotPwduberAlpha = ((ForgotPwduberAlpha) in.readValue((ForgotPwduberAlpha.class.getClassLoader())));
            return instance;
        }

        public ForgotPasswordResponse[] newArray(int size) {
            return (new ForgotPasswordResponse[size]);
        }

    }
    ;

    public ForgotPwduberAlpha getForgotPwduberAlpha() {
        return forgotPwduberAlpha;
    }

    public void setForgotPwduberAlpha(ForgotPwduberAlpha forgotPwduberAlpha) {
        this.forgotPwduberAlpha = forgotPwduberAlpha;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(forgotPwduberAlpha);
    }

    public int describeContents() {
        return  0;
    }

}
