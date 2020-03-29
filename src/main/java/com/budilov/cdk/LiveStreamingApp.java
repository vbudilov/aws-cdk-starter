package com.budilov.cdk;

import software.amazon.awscdk.core.App;

public class LiveStreamingApp {


    public static void main(final String[] args) {
        App app = new App();


        try {
            new CognitoStack(app, "LiveDataStreaming");
        } catch (Exception exc) {
            exc.printStackTrace();
        }

        app.synth();
    }
}