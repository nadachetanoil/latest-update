
package com.fracappzstudios.oilfieldnotifications.webservice.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.fracappzstudios.oilfieldnotifications.model.LoginUberAlpha;

public class LoginResponse implements Parcelable
{

    @SerializedName("uber_alpha")
    @Expose
    private LoginUberAlpha loginUberAlpha;
    public final static Creator<LoginResponse> CREATOR = new Creator<LoginResponse>() {
        @SuppressWarnings({
            "unchecked"
        })
        public LoginResponse createFromParcel(Parcel in) {
            LoginResponse instance = new LoginResponse();
            instance.loginUberAlpha = ((LoginUberAlpha) in.readValue((LoginUberAlpha.class.getClassLoader())));
            return instance;
        }

        public LoginResponse[] newArray(int size) {
            return (new LoginResponse[size]);
        }

    }
    ;

    public LoginUberAlpha getLoginUberAlpha() {
        return loginUberAlpha;
    }

    public void setLoginUberAlpha(LoginUberAlpha loginUberAlpha) {
        this.loginUberAlpha = loginUberAlpha;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(loginUberAlpha);
    }

    public int describeContents() {
        return  0;
    }

}
