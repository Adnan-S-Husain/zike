package com.call.zike;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zegocloud.uikit.prebuilt.call.invite.widget.ZegoSendCallInvitationButton;
import com.zegocloud.uikit.service.defines.ZegoUIKitUser;

import java.util.Collections;

import io.alterac.blurkit.BlurLayout;

public class Home extends AppCompatActivity {
    BlurLayout blurLayout1, blurLayout2;
    Button logoutButton;
    ZegoSendCallInvitationButton videoCallButton, voiceCallButton;
    TextView userNameTextView;
    EditText targetUserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        SharedPreferences preferences = getSharedPreferences("authentication", MODE_PRIVATE);
        String userName = preferences.getString("userName", null);

        userNameTextView = findViewById(R.id.username_text_view);
        logoutButton = findViewById(R.id.logout_btn);
        voiceCallButton = findViewById(R.id.voice_call_btn);
        videoCallButton = findViewById(R.id.video_call_btn);
        targetUserName = findViewById(R.id.target_username);
        blurLayout1 = findViewById(R.id.blurLayout1);
        blurLayout2 = findViewById(R.id.blurLayout2);

        userNameTextView.setText("" + userName);
        logoutButton.setOnClickListener((v -> {
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("userName");
            editor.apply();
            Intent intent = new Intent(Home.this, MainActivity.class);
            startActivity(intent);
        }));

        targetUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String targetName = targetUserName.getText().toString().trim();
                startVoiceCall(targetName);
                startVideoCall(targetName);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        blurLayout1.startBlur();
        blurLayout2.startBlur();
        startVoiceCall("");
        startVideoCall("");
    }

    @Override
    protected void onStop() {
        blurLayout1.pauseBlur();
        blurLayout2.pauseBlur();

        super.onStop();
    }

    void startVoiceCall(String targetUserName) {
        String targetUserID = targetUserName;
        ZegoSendCallInvitationButton button = voiceCallButton;
        button.setIsVideoCall(false);
        button.setResourceID("zego_uikit_call");
        button.setInvitees(Collections.singletonList(new ZegoUIKitUser(targetUserID,targetUserName)));
    }

    void startVideoCall(String targetUserName) {
        String targetUserID = targetUserName;
        ZegoSendCallInvitationButton button = videoCallButton;
        button.setIsVideoCall(true);
        button.setResourceID("zego_uikit_call");
        button.setInvitees(Collections.singletonList(new ZegoUIKitUser(targetUserID,targetUserName)));
    }
}