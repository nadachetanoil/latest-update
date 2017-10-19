
package com.fracappzstudios.oilfieldnotifications.webservice.response;

import com.fracappzstudios.oilfieldnotifications.model.EventDetailuberAlpha;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetEventDetailsRespone {

    @SerializedName("uber_alpha")
    @Expose
    private EventDetailuberAlpha eventDetailuberAlpha;

    public EventDetailuberAlpha getEventDetailuberAlpha() {
        return eventDetailuberAlpha;
    }

    public void setEventDetailuberAlpha(EventDetailuberAlpha eventDetailuberAlpha) {
        this.eventDetailuberAlpha = eventDetailuberAlpha;
    }

}
