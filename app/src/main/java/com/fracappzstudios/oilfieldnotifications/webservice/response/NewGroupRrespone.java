
package com.fracappzstudios.oilfieldnotifications.webservice.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.fracappzstudios.oilfieldnotifications.model.NewGroupuberAlpha;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewGroupRrespone implements Parcelable
{

    @SerializedName("uber_alpha")
    @Expose
    private NewGroupuberAlpha newGroupuberAlpha;
    public final static Creator<NewGroupRrespone> CREATOR = new Creator<NewGroupRrespone>() {


        @SuppressWarnings({
            "unchecked"
        })
        public NewGroupRrespone createFromParcel(Parcel in) {
            NewGroupRrespone instance = new NewGroupRrespone();
            instance.newGroupuberAlpha = ((NewGroupuberAlpha) in.readValue((NewGroupuberAlpha.class.getClassLoader())));
            return instance;
        }

        public NewGroupRrespone[] newArray(int size) {
            return (new NewGroupRrespone[size]);
        }

    }
    ;

    public NewGroupuberAlpha getNewGroupuberAlpha() {
        return newGroupuberAlpha;
    }

    public void setNewGroupuberAlpha(NewGroupuberAlpha newGroupuberAlpha) {
        this.newGroupuberAlpha = newGroupuberAlpha;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(newGroupuberAlpha);
    }

    public int describeContents() {
        return  0;
    }

}
