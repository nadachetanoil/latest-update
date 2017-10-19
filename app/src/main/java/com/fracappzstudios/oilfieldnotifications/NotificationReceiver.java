package com.fracappzstudios.oilfieldnotifications;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.fracappzstudios.oilfieldnotifications.util.Share;
import com.fracappzstudios.oilfieldnotifications.util.SharedPrefs;

import org.json.JSONObject;

/**
 * Created by Kathiriya Harshad on 8/24/2017.
 */

public class NotificationReceiver extends BroadcastReceiver
{
    public static final String TAG = NotificationReceiver.class.getSimpleName();
    public static final String mBroadcastNotificationAction = "com.fracappzstudios.oilfieldnotifications.NotificationBroadcast";


    public interface OnItemClickListener{
        public void OnClickListener(View view);
    }

    public NotificationReceiver() {
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
                try
                {
                    if (intent.getAction().equals(mBroadcastNotificationAction))
                    {
                        Log.e(TAG,intent.getStringExtra("Data") + "\n\n");
                       /* AlertDialog.Builder b = new AlertDialog.Builder(context);
                        b.setMessage("BroadcastReceiver "+intent.getStringExtra("dataJsonObject"));
                        b.create().show();*/
                        final Dialog dialog = new Dialog(context);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialog.setContentView(R.layout.dlg_signup);
                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        dialog.setCancelable(true);
                        dialog.setCanceledOnTouchOutside(false);
                        TextView tv_msg = (TextView)dialog.findViewById(R.id.tv_msg);
                        tv_msg.setText("Notification !");
                        TextView tv_msg_1 = (TextView)dialog.findViewById(R.id.tv_msg_1);
                        tv_msg_1.setText(intent.getExtras().getString("message"));
                        tv_msg.setTypeface(Share.Font.regular);
                        tv_msg_1.setTypeface(Share.Font.regular);
                        Button btn_ok = (Button)dialog.findViewById(R.id.btn_ok);
                        btn_ok.setTypeface(Share.Font.regular);


                        final JSONObject jsonObject = new JSONObject(intent.getStringExtra("dataJsonObject"));
                        Log.e(TAG,"jsonObject ==========> "+jsonObject.toString());


                        btn_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                                try{
                                    if (jsonObject.has("push_aim"))
                                    {
                                        String push_aim = jsonObject.getString("push_aim");

                                        if(push_aim.equals("1") || push_aim.equals("2") || push_aim.equals("7") )
                                        {
                                            Intent myGroupIntent = new Intent(context,MyGroupActivity.class);
                                            context.startActivity(myGroupIntent);
                                        }
                                        else if(push_aim.equals("3") || push_aim.equals("4") || push_aim.equals("5"))
                                        {
                                            Intent eventDetailIntent = new Intent(context,EventDetailsActivity.class);
                                            eventDetailIntent.putExtra("event_id",jsonObject.getString("event_id"));
                                            context.startActivity(eventDetailIntent);
                                        }
                                        else if(jsonObject.has("super_user"))
                                        {
                                            SharedPrefs.save(context,SharedPrefs.SUPER_USER,""+jsonObject.getString("super_user"));
                                        }
                                    }
                                }catch (Exception e){e.printStackTrace();}
                            }
                        });
                        dialog.show();
                    }
                }
                catch (Exception e){e.printStackTrace();}
    }
}
