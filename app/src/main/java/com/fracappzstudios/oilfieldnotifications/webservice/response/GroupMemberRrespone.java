
package com.fracappzstudios.oilfieldnotifications.webservice.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.fracappzstudios.oilfieldnotifications.model.GroupUberAlpha;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupMemberRrespone implements Parcelable
{

    @SerializedName("uber_alpha")
    @Expose
    private GroupUberAlpha groupUberAlpha;
    public final static Creator<GroupMemberRrespone> CREATOR = new Creator<GroupMemberRrespone>() {
        @SuppressWarnings({
            "unchecked"
        })
        public GroupMemberRrespone createFromParcel(Parcel in) {
            GroupMemberRrespone instance = new GroupMemberRrespone();
            instance.groupUberAlpha = ((GroupUberAlpha) in.readValue((GroupUberAlpha.class.getClassLoader())));
            return instance;
        }

        public GroupMemberRrespone[] newArray(int size) {
            return (new GroupMemberRrespone[size]);
        }

    }
    ;

    public GroupUberAlpha getGroupUberAlpha() {
        return groupUberAlpha;
    }

    public void setGroupUberAlpha(GroupUberAlpha groupUberAlpha) {
        this.groupUberAlpha = groupUberAlpha;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(groupUberAlpha);
    }

    public int describeContents() {
        return  0;
    }

}
