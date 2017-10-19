
package com.fracappzstudios.oilfieldnotifications.model;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventuberAlpha implements Parcelable
{

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("details")
    @Expose
    private List<Eventdetail> eventdetails = null;
    public final static Creator<EventuberAlpha> CREATOR = new Creator<EventuberAlpha>() {


        @SuppressWarnings({
            "unchecked"
        })
        public EventuberAlpha createFromParcel(Parcel in) {
            EventuberAlpha instance = new EventuberAlpha();
            instance.status = ((String) in.readValue((String.class.getClassLoader())));
            instance.message = ((String) in.readValue((String.class.getClassLoader())));
            in.readList(instance.eventdetails, (Eventdetail.class.getClassLoader()));
            return instance;
        }

        public EventuberAlpha[] newArray(int size) {
            return (new EventuberAlpha[size]);
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

    public List<Eventdetail> getEventdetails() {
        return eventdetails;
    }

    public void setEventdetails(List<Eventdetail> eventdetails) {
        this.eventdetails = eventdetails;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(message);
        dest.writeList(eventdetails);
    }

    public int describeContents() {
        return  0;
    }

    @Override
    public String toString() {
        return "EventuberAlpha{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", eventdetails=" + eventdetails +
                '}';
    }
}
