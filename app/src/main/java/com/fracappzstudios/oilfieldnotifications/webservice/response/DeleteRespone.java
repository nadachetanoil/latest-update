
package com.fracappzstudios.oilfieldnotifications.webservice.response;

import com.fracappzstudios.oilfieldnotifications.model.DeleteuberAlpha;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeleteRespone {

    @SerializedName("uber_alpha")
    @Expose
    private DeleteuberAlpha deleteuberAlpha;

    public DeleteuberAlpha getDeleteuberAlpha() {
        return deleteuberAlpha;
    }

    public void setDeleteuberAlpha(DeleteuberAlpha deleteuberAlpha) {
        this.deleteuberAlpha = deleteuberAlpha;
    }

}
