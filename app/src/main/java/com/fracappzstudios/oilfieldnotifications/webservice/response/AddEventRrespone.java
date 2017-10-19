
package com.fracappzstudios.oilfieldnotifications.webservice.response;

import com.fracappzstudios.oilfieldnotifications.model.NewEventuberAlpha;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddEventRrespone {

    @SerializedName("uber_alpha")
    @Expose
    private NewEventuberAlpha newEventuberAlpha;

    public NewEventuberAlpha getNewEventuberAlpha() {
        return newEventuberAlpha;
    }

    public void setNewEventuberAlpha(NewEventuberAlpha newEventuberAlpha) {
        this.newEventuberAlpha = newEventuberAlpha;
    }


}
