package com.fracappzstudios.oilfieldnotifications.webservice;

import com.fracappzstudios.oilfieldnotifications.webservice.response.AddCommentRespone;
import com.fracappzstudios.oilfieldnotifications.webservice.response.AddEventRrespone;
import com.fracappzstudios.oilfieldnotifications.webservice.response.AllCommentRespone;
import com.fracappzstudios.oilfieldnotifications.webservice.response.DeleteRespone;
import com.fracappzstudios.oilfieldnotifications.webservice.response.EventListRrespone;
import com.fracappzstudios.oilfieldnotifications.webservice.response.ForgotPasswordResponse;
import com.fracappzstudios.oilfieldnotifications.webservice.response.GetEventDetailsRespone;
import com.fracappzstudios.oilfieldnotifications.webservice.response.GetNotificationResponse;
import com.fracappzstudios.oilfieldnotifications.webservice.response.GroupMemberRrespone;
import com.fracappzstudios.oilfieldnotifications.webservice.response.InviteRespone;
import com.fracappzstudios.oilfieldnotifications.webservice.response.LoginResponse;
import com.fracappzstudios.oilfieldnotifications.webservice.response.NewGroupRrespone;
import com.fracappzstudios.oilfieldnotifications.webservice.response.PeopleDIRrespone;
import com.fracappzstudios.oilfieldnotifications.webservice.response.SignupResponse;
import com.fracappzstudios.oilfieldnotifications.webservice.response.UpdateProfileRrespone;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Harshad kathiriya on 6/14/2017.
 */

public interface ApiService {

    @POST("/ws/register.php")
    //first_name, last_name, email , password, phone_no, company_name, device_token, device_type (android or ios)
    @FormUrlEncoded
    Call<SignupResponse> sendSignupRequest(@Field("first_name") String first_name,
                                           @Field("last_name") String last_name,
                                           @Field("email") String email,
                                           @Field("password") String password,
                                           @Field("phone_no") String phone_no,
                                           @Field("company_name") String company_name,
                                           @Field("device_token") String device_token);

    @POST("/ws/login.php") //email,password,device_token,device_type (android or ios)
    @FormUrlEncoded
    Call<LoginResponse> sendLoginRequest(@Field("email") String email,
                                         @Field("password") String password,
                                         @Field("device_token") String device_token,
                                         @Field("device_type") String device_type);

    @POST("/ws/forgot_password.php")
    @FormUrlEncoded
    Call<ForgotPasswordResponse> getForgotPassword(@Field("email") String email);

    @POST("/ws/all_notification.php")
    @FormUrlEncoded
    Call<GetNotificationResponse> getNotification(@Field("user_id") String user_id);

    @POST("/ws/people_directory.php")
    @FormUrlEncoded
    Call<PeopleDIRrespone> getPeopleDirList(@Field("user_id") String user_id);

    @POST("/ws/people_directory.php")
    @FormUrlEncoded
    Call<PeopleDIRrespone> getSearchPeopleDirList(@Field("user_id") String user_id,@Field("search_keyword")String search_keyword);

    @POST("/ws/pull_group.php")
    @FormUrlEncoded
    Call<GroupMemberRrespone> getAllGroupsList(@Field("user_id")String user_id);

    @POST("/ws/add_group.php")
    @Multipart
    Call<NewGroupRrespone> createNewGroup(@Part("group_id") RequestBody group_id,
                                          @Part("user_id")RequestBody user_id,
                                          @Part("group_name")RequestBody group_name,
                                          @Part("group_description")RequestBody group_description,
                                          @Part("user_list")RequestBody user_list,
                                          @Part MultipartBody.Part file);

    @POST("/ws/get_event.php")
    @FormUrlEncoded
    Call<EventListRrespone> getEvent(@Field("user_id")String user_id,@Field("group_id")String group_id);

    @POST("/ws/update_user_profile.php")
    @Multipart
    Call<UpdateProfileRrespone> updateProfile(@Part("user_id") RequestBody user_id,
                                              @Part("email")RequestBody email,
                                              @Part("first_name")RequestBody first_name,
                                              @Part("last_name")RequestBody last_name,
                                              @Part("company_name")RequestBody company_name,
                                              @Part("password")RequestBody password,
                                              @Part("phone_no")RequestBody phone_no,
                                              @Part MultipartBody.Part profile_image);

    @POST("/ws/add_event.php")
    @Multipart
    Call<AddEventRrespone>  addEvent(@Part("event_id") RequestBody event_id,
                                     @Part("user_id")RequestBody user_id,
                                     @Part("group_id")RequestBody group_id,
                                     @Part("event_name")RequestBody event_name,
                                     @Part("event_description")RequestBody event_description,
                                     @Part MultipartBody.Part file1,
                                     @Part MultipartBody.Part file2,
                                     @Part MultipartBody.Part file3,
                                     @Part MultipartBody.Part file4,
                                     @Part MultipartBody.Part file5,
                                     @Part ("user_list")RequestBody user_list);

    @POST("/ws/add_comment.php")
    @FormUrlEncoded
    Call<AddCommentRespone> addComment(@Field("comment_id")String comment_id,
                                       @Field("event_id")String event_id,
                                       @Field("user_id")String user_id,
                                       @Field("group_id")String group_id,
                                       @Field("comment")String comment,
                                       @Field("user_list")String user_list);
    @POST("/ws/get_event_comment.php")
    @FormUrlEncoded
    Call<AllCommentRespone> allCommentResponse(@Field("user_id")String user_id,
                                               @Field("group_id")String group_id,
                                               @Field("event_id")String event_id);

    @POST("/ws/get_all_event.php")
    @FormUrlEncoded
    Call<EventListRrespone> getAllEvent(@Field("user_id")String user_id);

    @POST("/ws/invite_by_mail.php")
    @FormUrlEncoded
    Call<InviteRespone> invite_by_mail(@Field("email")String email ,
                                       @Field("user_email")String user_email,
                                       @Field("user_id")String user_id);


    @POST("/ws/delete_group.php")
    @FormUrlEncoded
    Call<DeleteRespone> delete_group(@Field("user_id")String user_id,
                                     @Field("group_id")String group_id);

    @POST("/ws/delete_event.php")
    @FormUrlEncoded
    Call<DeleteRespone> delete_event(@Field("user_id")String user_id,
                                     @Field("event_id")String event_id);

    @POST("/ws/delete_comment.php")
    @FormUrlEncoded
    Call<DeleteRespone> delete_comment(@Field("user_id")String user_id,
                                       @Field("comment_id")String comment_id);

    @POST("/ws/decrease_badge_count.php")
    @FormUrlEncoded
    Call<DeleteRespone> decrease_badge_count(@Field("user_id")String user_id,
                                             @Field("push_id")String push_id);

    @POST("/ws/delete_notification.php")
    @FormUrlEncoded
    Call<DeleteRespone> deleteAllNotification(@Field("user_id") String user_id);

    @POST("/ws/get_event_detail_andorid.php")
    @FormUrlEncoded
    Call<GetEventDetailsRespone> getEventDetails(@Field("event_id")String event_id);
}
