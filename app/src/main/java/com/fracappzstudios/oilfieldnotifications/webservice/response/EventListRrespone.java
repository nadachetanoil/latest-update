
package com.fracappzstudios.oilfieldnotifications.webservice.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.fracappzstudios.oilfieldnotifications.model.EventuberAlpha;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventListRrespone implements Parcelable
{

    @SerializedName("uber_alpha")
    @Expose
    private EventuberAlpha eventuberAlpha;
    public final static Creator<EventListRrespone> CREATOR = new Creator<EventListRrespone>() {


        @SuppressWarnings({
            "unchecked"
        })
        public EventListRrespone createFromParcel(Parcel in) {
            EventListRrespone instance = new EventListRrespone();
            instance.eventuberAlpha = ((EventuberAlpha) in.readValue((EventuberAlpha.class.getClassLoader())));
            return instance;
        }

        public EventListRrespone[] newArray(int size) {
            return (new EventListRrespone[size]);
        }

    }
    ;

    public EventuberAlpha getEventuberAlpha() {
        return eventuberAlpha;
    }

    public void setEventuberAlpha(EventuberAlpha eventuberAlpha) {
        this.eventuberAlpha = eventuberAlpha;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(eventuberAlpha);
    }

    public int describeContents() {
        return  0;
    }

}
