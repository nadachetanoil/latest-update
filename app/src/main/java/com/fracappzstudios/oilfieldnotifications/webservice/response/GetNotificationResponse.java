
package com.fracappzstudios.oilfieldnotifications.webservice.response;

import com.fracappzstudios.oilfieldnotifications.model.NotificationUberAlpha;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetNotificationResponse {

    @SerializedName("uber_alpha")
    @Expose
    private NotificationUberAlpha notificationUberAlpha;

    public NotificationUberAlpha getNotificationUberAlpha() {
        return notificationUberAlpha;
    }

    public void setNotificationUberAlpha(NotificationUberAlpha notificationUberAlpha) {
        this.notificationUberAlpha = notificationUberAlpha;
    }

}
