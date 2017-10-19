
package com.fracappzstudios.oilfieldnotifications.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Member implements Parcelable
{

    @SerializedName("member_id")
    @Expose
    private Object memberId;
    @SerializedName("first_name")
    @Expose
    private Object firstName;
    @SerializedName("last_name")
    @Expose
    private Object lastName;
    @SerializedName("company_name")
    @Expose
    private Object companyName;
    @SerializedName("email")
    @Expose
    private Object email;
    @SerializedName("phone")
    @Expose
    private Object phone;
    @SerializedName("profile_image")
    @Expose
    private Object profileImage;
    public final static Creator<Member> CREATOR = new Creator<Member>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Member createFromParcel(Parcel in) {
            Member instance = new Member();
            instance.memberId = ((Object) in.readValue((Object.class.getClassLoader())));
            instance.firstName = ((Object) in.readValue((Object.class.getClassLoader())));
            instance.lastName = ((Object) in.readValue((Object.class.getClassLoader())));
            instance.companyName = ((Object) in.readValue((Object.class.getClassLoader())));
            instance.email = ((Object) in.readValue((Object.class.getClassLoader())));
            instance.phone = ((Object) in.readValue((Object.class.getClassLoader())));
            instance.profileImage = ((Object) in.readValue((Object.class.getClassLoader())));
            return instance;
        }

        public Member[] newArray(int size) {
            return (new Member[size]);
        }

    }
    ;

    public Object getMemberId() {
        return memberId;
    }

    public void setMemberId(Object memberId) {
        this.memberId = memberId;
    }

    public Object getFirstName() {
        return firstName;
    }

    public void setFirstName(Object firstName) {
        this.firstName = firstName;
    }

    public Object getLastName() {
        return lastName;
    }

    public void setLastName(Object lastName) {
        this.lastName = lastName;
    }

    public Object getCompanyName() {
        return companyName;
    }

    public void setCompanyName(Object companyName) {
        this.companyName = companyName;
    }

    public Object getEmail() {
        return email;
    }

    public void setEmail(Object email) {
        this.email = email;
    }

    public Object getPhone() {
        return phone;
    }

    public void setPhone(Object phone) {
        this.phone = phone;
    }

    public Object getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Object profileImage) {
        this.profileImage = profileImage;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(memberId);
        dest.writeValue(firstName);
        dest.writeValue(lastName);
        dest.writeValue(companyName);
        dest.writeValue(email);
        dest.writeValue(phone);
        dest.writeValue(profileImage);
    }

    public int describeContents() {
        return  0;
    }

}
