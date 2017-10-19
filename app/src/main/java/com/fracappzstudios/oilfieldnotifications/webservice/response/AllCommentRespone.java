
package com.fracappzstudios.oilfieldnotifications.webservice.response;

import com.fracappzstudios.oilfieldnotifications.model.AllCommentuberAlpha;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllCommentRespone {

    @SerializedName("uber_alpha")
    @Expose
    private AllCommentuberAlpha allCommentuberAlpha;

    public AllCommentuberAlpha getAllCommentuberAlpha() {
        return allCommentuberAlpha;
    }

    public void setAllCommentuberAlpha(AllCommentuberAlpha allCommentuberAlpha) {
        this.allCommentuberAlpha = allCommentuberAlpha;
    }

}
