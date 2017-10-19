
package com.fracappzstudios.oilfieldnotifications.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.Streams;

public class Logindetails implements Parcelable
{

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone_no")
    @Expose
    private String phoneNo;
    @SerializedName("device_token")
    @Expose
    private String deviceToken;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("super_user")
    @Expose
    private String superUser;
    @SerializedName("profile_image")
    @Expose
    private String profile_image;

    public final static Creator<Logindetails> CREATOR = new Creator<Logindetails>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Logindetails createFromParcel(Parcel in) {
            Logindetails instance = new Logindetails();
            instance.userId = ((String) in.readValue((String.class.getClassLoader())));
            instance.firstName = ((String) in.readValue((String.class.getClassLoader())));
            instance.lastName = ((String) in.readValue((String.class.getClassLoader())));
            instance.email = ((String) in.readValue((String.class.getClassLoader())));
            instance.phoneNo = ((String) in.readValue((String.class.getClassLoader())));
            instance.deviceToken = ((String) in.readValue((String.class.getClassLoader())));
            instance.companyName = ((String) in.readValue((String.class.getClassLoader())));
            instance.superUser = ((String) in.readValue((String.class.getClassLoader())));
            instance.profile_image = ((String)in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public Logindetails[] newArray(int size) {
            return (new Logindetails[size]);
        }

    }
    ;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getSuperUser() {
        return superUser;
    }

    public void setSuperUser(String superUser) {
        this.superUser = superUser;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(userId);
        dest.writeValue(firstName);
        dest.writeValue(lastName);
        dest.writeValue(email);
        dest.writeValue(phoneNo);
        dest.writeValue(deviceToken);
        dest.writeValue(companyName);
        dest.writeValue(superUser);
        dest.writeValue(profile_image);
    }

    public int describeContents() {
        return  0;
    }

}
