package com.budilov.cdk;

import software.amazon.awscdk.core.App;

public class LiveStreamingApp {


    public static void main(final String[] args) {
        App app = new App();


        DDBUserTableStack ddbUserTableStack = new DDBUserTableStack(app, "UsersTable");

        try {
            new CognitoStack(app, "LiveDataStreaming", null, ddbUserTableStack.getTableName());
        } catch (Exception exc) {
            exc.printStackTrace();
        }

        app.synth();
    }
}