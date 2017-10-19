
package com.fracappzstudios.oilfieldnotifications.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommentuberAlpha {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("details")
    @Expose
    private Commentdetails commentdetails;

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

    public Commentdetails getCommentdetails() {
        return commentdetails;
    }

    public void setCommentdetails(Commentdetails commentdetails) {
        this.commentdetails = commentdetails;
    }

}
