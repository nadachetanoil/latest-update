
package com.fracappzstudios.oilfieldnotifications.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PeopelDirdetail implements Parcelable
{

    @SerializedName("member_id")
    @Expose
    private String memberId;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("profile_image")
    @Expose
    private String profileImage;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("company_name")
    @Expose
    private String companyName;


    private String id;
    private String is_delete;
    private String user_id;
    private boolean is_select;

    public final static Creator<PeopelDirdetail> CREATOR = new Creator<PeopelDirdetail>() {


        @SuppressWarnings({
            "unchecked"
        })
        public PeopelDirdetail createFromParcel(Parcel in) {
            PeopelDirdetail instance = new PeopelDirdetail();
            instance.memberId = ((String) in.readValue((String.class.getClassLoader())));
            instance.firstName = ((String) in.readValue((String.class.getClassLoader())));
            instance.lastName = ((String) in.readValue((String.class.getClassLoader())));
            instance.email = ((String) in.readValue((String.class.getClassLoader())));
            instance.profileImage = ((String) in.readValue((String.class.getClassLoader())));
            instance.phone = ((String) in.readValue((String.class.getClassLoader())));
            instance.companyName = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public PeopelDirdetail[] newArray(int size) {
            return (new PeopelDirdetail[size]);
        }

    }
    ;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(String is_delete) {
        this.is_delete = is_delete;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public boolean is_select() {
        return is_select;
    }

    public void setIs_select(boolean is_select) {
        this.is_select = is_select;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(memberId);
        dest.writeValue(firstName);
        dest.writeValue(lastName);
        dest.writeValue(email);
        dest.writeValue(profileImage);
        dest.writeValue(phone);
        dest.writeValue(companyName);
    }

    public int describeContents() {
        return  0;
    }

    @Override
    public String toString() {
        return "PeopelDirdetail{" +
                "memberId='" + memberId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", profileImage='" + profileImage + '\'' +
                ", phone='" + phone + '\'' +
                ", companyName='" + companyName + '\'' +
                ", id='" + id + '\'' +
                ", is_delete='" + is_delete + '\'' +
                ", user_id='" + user_id + '\'' +
                ", is_select=" + is_select +
                '}';
    }
}
