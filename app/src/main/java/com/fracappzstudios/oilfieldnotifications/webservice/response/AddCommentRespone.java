
package com.fracappzstudios.oilfieldnotifications.webservice.response;

import com.fracappzstudios.oilfieldnotifications.model.CommentuberAlpha;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddCommentRespone {

    @SerializedName("uber_alpha")
    @Expose
    private CommentuberAlpha commentuberAlpha;

    public CommentuberAlpha getCommentuberAlpha() {
        return commentuberAlpha;
    }

    public void setCommentuberAlpha(CommentuberAlpha commentuberAlpha) {
        this.commentuberAlpha = commentuberAlpha;
    }

}
