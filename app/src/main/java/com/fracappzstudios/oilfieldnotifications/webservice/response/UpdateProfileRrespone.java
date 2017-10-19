
package com.fracappzstudios.oilfieldnotifications.webservice.response;

import com.fracappzstudios.oilfieldnotifications.model.ProfileuberAlpha;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateProfileRrespone {

    @SerializedName("uber_alpha")
    @Expose
    private ProfileuberAlpha profileuberAlpha;

    public ProfileuberAlpha getProfileuberAlpha() {
        return profileuberAlpha;
    }

    public void setProfileuberAlpha(ProfileuberAlpha profileuberAlpha) {
        this.profileuberAlpha = profileuberAlpha;
    }

}
