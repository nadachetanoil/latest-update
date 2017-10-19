package com.fracappzstudios.oilfieldnotifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

/**
 * Created by harshadpk on 29/04/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFMService";



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Handle data payload of FCM messages.
        Log.d(TAG, "FCM Message Id: " + remoteMessage.getMessageId());
        Log.d(TAG, "FCM Notification Message: " +remoteMessage.getNotification().toString());
        Log.d(TAG, "FCM Data Message: " + remoteMessage.getData().toString());


        sendNotification(remoteMessage);
    }

    private void sendNotification(RemoteMessage remoteMessage)
    {
        try
        {
            String messageTitle = ""+remoteMessage.getNotification().getTitle();
            String messageBody = remoteMessage.getNotification().getBody().toString();
            Intent intent = new Intent(this, HomeActivity.class);
            Log.d(TAG, "FCM Message push_aim: " + remoteMessage.getData().get("push_aim"));

            if(remoteMessage.getData() != null)
            {
                /*JSONObject dataJson = new JSONObject("{\"data\" :{\"push_aim\":\"5\",\n" +
                        "         \"event_id\":\"0I89PZQWW2RSUA7BEJFK240P5DBR7LREST\",\n" +
                        "         \"event_file\":\"\",\n" +
                        "         \"group_id\":\"K7DGRCFEOWFVQ5TEEZJVMT0Y9MII345D\",\n" +
                        "         \"user_id\":\"20\",\n" +
                        "         \"push_id\":88,\n" +
                        "         \"event_description\":\"\",\n" +
                        "         \"event_name\":\"myEvents\",\n" +
                        "         \"comment\":\"NewMyComment\",\n" +
                        "         \"comment_id\":\"N3YRB0O9NLWHYTVGN1V6OKH2LSXNML90YIBA\",\n" +
                        "         \"badge_count\":\"3\"}}");*/
                JSONObject dataJson = new JSONObject(remoteMessage.getData().toString());
                JSONObject dataJsonObject = dataJson.getJSONObject("data");
                Log.e(TAG,"dataJsonObject : "+dataJsonObject.toString());

                if(dataJsonObject.has("push_id"))
                    intent.putExtra("push_id",dataJsonObject.getString("push_id"));

                if (dataJsonObject.has("super_user"))
                    intent.putExtra("super_user",dataJsonObject.getString("super_user"));

                Intent broadcastIntent = new Intent();
                String push_aim = dataJsonObject.getString("push_aim");
                Log.d(TAG, "FCM Message push_aim: " + push_aim);
                broadcastIntent.putExtra("push_aim",push_aim);

               /* if(push_aim.equals("1") || push_aim.equals("2"))
                {
                    *//*broadcastIntent.putExtra("group_id",dataJsonObject.getString("group_id"));
                    broadcastIntent.putExtra("group_name",dataJsonObject.getString("group_name"));
                    broadcastIntent.putExtra("group_description",dataJsonObject.getString("group_description"));*//*
                }
                else if(push_aim.equals("3") || push_aim.equals("4"))
                {
                   *//* broadcastIntent.putExtra("event_id",dataJsonObject.getString("event_id"));
                    broadcastIntent.putExtra("event_name",dataJsonObject.getString("event_name"));
                    broadcastIntent.putExtra("event_file",dataJsonObject.getString("event_file"));
                    broadcastIntent.putExtra("event_description",dataJsonObject.getString("event_description"));*//*
                }
                else if(push_aim.equals("5"))
                {
                   *//* broadcastIntent.putExtra("comment_id",dataJsonObject.getString("comment_id"));
                    broadcastIntent.putExtra("comment",dataJsonObject.getString("comment"));*//*
                }
                else if(push_aim.equals("7"))
                {
                   *//* broadcastIntent.putExtra("group_id",dataJsonObject.getString("group_id"));
                    broadcastIntent.putExtra("group_name",dataJsonObject.getString("group_name"));*//*
                }*/
                broadcastIntent.putExtra("message",remoteMessage.getNotification().getBody());
                broadcastIntent.putExtra("dataJsonObject",dataJsonObject.toString());
                broadcastIntent.setAction(HomeActivity.mBroadcastNotificationAction);
                broadcastIntent.putExtra("Data", "Broadcast Data");
                sendBroadcast(broadcastIntent);
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,100 /* request code */, intent,PendingIntent.FLAG_UPDATE_CURRENT);

            long[] pattern = {500,500,500,500,500};

            Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder notificationBuilder =  new NotificationCompat.Builder(this)
//                .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(messageTitle)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setVibrate(pattern)
                    .setLights(Color.BLUE,1,1)
                    .setSound(defaultSoundUri)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}