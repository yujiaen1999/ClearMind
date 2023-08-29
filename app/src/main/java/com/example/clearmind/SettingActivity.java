package com.example.clearmind;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class SettingActivity extends AppCompatActivity {

    private DatabaseReference db;
    private String username;
    private Button change_pswd_button;

    private Button notification_button;

    private Button logout_button;

//    private Button delete_account_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Intent intent = getIntent();
        change_pswd_button = (Button) findViewById(R.id.change_pswd);
        logout_button = (Button) findViewById(R.id.logout);
        notification_button = (Button) findViewById(R.id.notification);
//        delete_account_button = (Button) findViewById(R.id.delete_account);

        this.username = intent.getStringExtra("username");
        this.db = FirebaseDatabase.getInstance().getReference(); //get a reference of database
        if (savedInstanceState != null){
            this.username = savedInstanceState.getString("username");
            Log.d("save_username", "onRestoreCreate");
        }

        change_pswd_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                onClickChangePswd();
            }
        });

        change_pswd_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                onClickChangePswd();
            }
        });


        notification_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View viewPopupWindow = layoutInflater.inflate(R.layout.activity_notification_setting, null);
                final PopupWindow popupWindow = new PopupWindow(viewPopupWindow, 900, 900, true);
                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

                Spinner spinner_notification = (Spinner) viewPopupWindow.findViewById(R.id.spinner_notification);
                Button button_confirm = (Button) viewPopupWindow.findViewById(R.id.button_confirm);
                Button button_cancel = (Button) viewPopupWindow.findViewById(R.id.button_cancel);

                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                        R.array.notification_array, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_notification.setAdapter(adapter);

                // alarm manager
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

//                alarmManager.cancel(pendingIntent);

                button_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        String txt_selection = spinner_notification.getSelectedItem().toString();

//                        Intent myIntent = new Intent(getApplicationContext(), TimeBroadcast.class);
//                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, myIntent, PendingIntent.FLAG_IMMUTABLE);
//                        alarmManager.cancel(pendingIntent);

                        Integer frequency = 1;

                        if (txt_selection.equals("Never")){
                            Intent myIntent = new Intent(getApplicationContext(), TimeBroadcast.class);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, myIntent, PendingIntent.FLAG_IMMUTABLE);
                            alarmManager.cancel(pendingIntent);
                            Log.d("NotificationFrequency", "Never");
                        } else{
                            Intent myIntent = new Intent(getApplicationContext(), TimeBroadcast.class);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, myIntent, PendingIntent.FLAG_IMMUTABLE);
                            alarmManager.cancel(pendingIntent);

                            switch (txt_selection){
                                case "Daily":
                                    frequency = 1;
                                    break;
                                case "2-3 times a week":
                                    frequency = 3;
                                    break;
                                case "Once a week":
                                    frequency = 7;
                                    break;
                                default:
                                    break;
                            }

                            Intent myIntent_new = new Intent(getApplicationContext(), TimeBroadcast.class);
                            PendingIntent pendingIntent_new = PendingIntent.getBroadcast(getApplicationContext(), 0, myIntent_new, PendingIntent.FLAG_IMMUTABLE);

                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.HOUR_OF_DAY, 10);
                            calendar.set(Calendar.MINUTE, 00);
                            calendar.set(Calendar.SECOND, 00);

                            Log.d("NotificationFrequency", frequency.toString());

                            // intervalMillis: hours * minutes
//                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), frequency * 1000 ,pendingIntent_new); // test
                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), frequency * 24 * 60 * 60 * 1000, pendingIntent_new);
                        }
                        popupWindow.dismiss();
                    }
                });

                button_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        popupWindow.dismiss();
                    }
                });

            }
        });

        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openMainActivity();
            }
        });
    }



    public void onClickChangePswd(){
        Intent intent = new Intent(getApplicationContext(), ChangePasswordActivity.class);
        intent.putExtra("username", this.username);
        startActivity(intent);
    }

    public void onClickDeleteAccount(View view){
        // TODO: delete all userdata in database

        Log.d("save_username", Boolean.toString(this.username == null));
        this.db.child("users")
                .child(this.username)
                .removeValue();
        this.db.child("register")
                .child(this.username)
                .removeValue();
        this.db.child("security")
                .child(this.username)
                .removeValue();
        this.finish();
        Toast.makeText(SettingActivity.this,  "Account Deleted", Toast.LENGTH_SHORT).show();
        openMainActivity();
    }

    public void openMainActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

}
