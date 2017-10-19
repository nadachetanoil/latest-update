
package com.fracappzstudios.oilfieldnotifications.webservice.response;

import com.fracappzstudios.oilfieldnotifications.model.PeopelDirUberAlpha;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PeopleDIRrespone {

    @SerializedName("uber_alpha")
    @Expose
    private PeopelDirUberAlpha peopelDirUberAlpha;

    public PeopelDirUberAlpha getPeopelDirUberAlpha() {
        return peopelDirUberAlpha;
    }

    public void setPeopelDirUberAlpha(PeopelDirUberAlpha peopelDirUberAlpha) {
        this.peopelDirUberAlpha = peopelDirUberAlpha;
    }

}
