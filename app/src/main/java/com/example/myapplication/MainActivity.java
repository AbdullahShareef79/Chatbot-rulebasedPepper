package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.builder.ChatBuilder;
import com.aldebaran.qi.sdk.builder.QiChatbotBuilder;
import com.aldebaran.qi.sdk.builder.TopicBuilder;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.object.conversation.Chat;
import com.aldebaran.qi.sdk.object.conversation.QiChatVariable;
import com.aldebaran.qi.sdk.object.conversation.QiChatbot;
import com.aldebaran.qi.sdk.object.conversation.Topic;

public class MainActivity extends RobotActivity implements RobotLifecycleCallbacks {

    private static final String TAG = "MainActivity"; // For logging


    // The QiContext provided by the QiSDK
    private QiContext qiContext = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Register the RobotLifecycleCallbacks to this Activity
        QiSDK.register(this, this);
    }

    @Override
    protected void onDestroy() {
        // Unregister the RobotLifecycleCallbacks for this Activity
        QiSDK.unregister(this, this);
        super.onDestroy();
    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        // Store the provided QiContext
        this.qiContext = qiContext;

        // Initialize chat-bot actions
        initChatbot();
    }

    @Override
    public void onRobotFocusLost() {
        // Remove the QiContext
        this.qiContext = null;
    }

    @Override
    public void onRobotFocusRefused(String reason) {
        Log.e(TAG, "Robot focus refused: " + reason);
    }

    // Initialize the chat-bot and set up the chat topic and variables
    private void initChatbot() {
        // Create a chat topic
        Topic topic = TopicBuilder.with(qiContext)
                .withResource(R.raw.dialog) // Set the topic resource (update to your file)
                .build();

        // Create a new QiChat bot
        QiChatbot qiChatbot = QiChatbotBuilder.with(qiContext)
                .withTopic(topic)
                .build();

        // Create a new Chat action
        // Store the Chat action
        Chat chatAction = ChatBuilder.with(qiContext)
                .withChatbot(qiChatbot)
                .build();

        // Set up a listener for a chat variable
        QiChatVariable nameVariable = qiChatbot.variable("Name");
        nameVariable.addOnValueChangedListener(currentValue -> Log.i(TAG, "Chat var Name: " + currentValue));

        // Set up a listener for drink variable or any other variable
        QiChatVariable drinkVariable = qiChatbot.variable("Drink");
        drinkVariable.addOnValueChangedListener(currentValue -> {
            Log.i(TAG, "Chat var Drink: " + currentValue);
            imageViewSetValue(); // Update this method if needed
        });

        // Start the chat action asynchronously
        chatAction.async().run();
    }

    // Update the ImageView based on the drink variable (placeholder method)
    private void imageViewSetValue() {
        // Implement logic to update the ImageView based on the current value
    }
}
