
package com.fracappzstudios.oilfieldnotifications.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Details implements Parcelable
{

    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("device_token")
    @Expose
    private String deviceToken;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    public final static Creator<Details> CREATOR = new Creator<Details>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Details createFromParcel(Parcel in) {
            Details instance = new Details();
            instance.userId = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.firstName = ((String) in.readValue((String.class.getClassLoader())));
            instance.lastName = ((String) in.readValue((String.class.getClassLoader())));
            instance.email = ((String) in.readValue((String.class.getClassLoader())));
            instance.phone = ((String) in.readValue((String.class.getClassLoader())));
            instance.deviceToken = ((String) in.readValue((String.class.getClassLoader())));
            instance.companyName = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public Details[] newArray(int size) {
            return (new Details[size]);
        }

    }
    ;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(userId);
        dest.writeValue(firstName);
        dest.writeValue(lastName);
        dest.writeValue(email);
        dest.writeValue(phone);
        dest.writeValue(deviceToken);
        dest.writeValue(companyName);
    }

    public int describeContents() {
        return  0;
    }

}
