
package com.fracappzstudios.oilfieldnotifications.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllCommentdetail {

    @SerializedName("comment_id")
    @Expose
    private String commentId;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("create_date")
    @Expose
    private String createDate;
    @SerializedName("create_by_user")
    @Expose
    private List<CreateByUser> createByUser = null;
    @SerializedName("comment_member")
    @Expose
    private List<PeopelDirdetail> peopelDirdetail = null;

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public List<CreateByUser> getCreateByUser() {
        return createByUser;
    }

    public void setCreateByUser(List<CreateByUser> createByUser) {
        this.createByUser = createByUser;
    }

    public List<PeopelDirdetail> getPeopelDirdetail() {
        return peopelDirdetail;
    }

    public void setPeopelDirdetail(List<PeopelDirdetail> peopelDirdetail) {
        this.peopelDirdetail = peopelDirdetail;
    }

}
