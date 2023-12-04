package com.call.zike;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.zegocloud.uikit.prebuilt.call.config.ZegoNotificationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationService;

public class MainActivity extends AppCompatActivity {
    Button loginBtn;
    EditText userNameEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(MainActivity.this, Home.class);
        SharedPreferences preferences = getSharedPreferences("authentication", MODE_PRIVATE);
        String user = preferences.getString("userName", null);
        if (user != null) {
            startService(user);
            startActivity(intent);
        }
        loginBtn =  findViewById(R.id.login_btn);
        userNameEditText = findViewById(R.id.username_input);

        loginBtn.setOnClickListener((view) -> {
            String userName = userNameEditText.getText().toString().trim();

            if (userName.isEmpty()) return;
            startService(userName);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("userName", userName);
            editor.apply();
            startActivity(intent);
        });
    }

    void startService(String userName) {
        Application application = getApplication();
        long appID = 1544203307;
        String appSign = "12f1666e2060349d17e2a7a5074740bdd961ef749f102bd412d3115bee51beb5";
        String userID = userName;

        ZegoUIKitPrebuiltCallInvitationConfig callInvitationConfig = new ZegoUIKitPrebuiltCallInvitationConfig();
        callInvitationConfig.notifyWhenAppRunningInBackgroundOrQuit = true;
        ZegoNotificationConfig notificationConfig = new ZegoNotificationConfig();
        notificationConfig.sound = "zego_uikit_sound_call";
        notificationConfig.channelID = "CallInvitation";
        notificationConfig.channelName = "CallInvitation";
        ZegoUIKitPrebuiltCallInvitationService.init(getApplication(), appID, appSign, userID, userName,callInvitationConfig);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ZegoUIKitPrebuiltCallInvitationService.unInit();
    }
}