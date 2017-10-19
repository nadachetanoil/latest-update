
package com.fracappzstudios.oilfieldnotifications.webservice.response;

import com.fracappzstudios.oilfieldnotifications.model.InviteuberAlpha;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InviteRespone {

    @SerializedName("uber_alpha")
    @Expose
    private InviteuberAlpha inviteuberAlpha;

    public InviteuberAlpha getInviteuberAlpha() {
        return inviteuberAlpha;
    }

    public void setInviteuberAlpha(InviteuberAlpha inviteuberAlpha) {
        this.inviteuberAlpha = inviteuberAlpha;
    }

}
