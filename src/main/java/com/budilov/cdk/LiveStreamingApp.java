package com.budilov.cdk;

import software.amazon.awscdk.core.App;

public class LiveStreamingApp {


    public static void main(final String[] args) throws Exception {
        App app = new App();

        // DynamoDB Stack
        DDBUserTableStack ddbUserTableStack = new DDBUserTableStack(app, "UsersTable");

        // Kinesis
        KinesisStreamsStack kinesisStreamsStack = new KinesisStreamsStack(app, "KinesisStream");

        // Cognito
        CognitoStack cognitoStack = new CognitoStack(app, "LiveDataStreaming", null, ddbUserTableStack.usersTable.getTableName());

        app.synth();
    }
}