package com.budilov.cdk;

import com.budilov.cdk.eks.EcrStack;
import com.budilov.cdk.elasticsearch.ElasticsearchIamStack;
import com.budilov.cdk.elasticsearch.ElasticsearchStack;
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

        // IAM -- this step needs to go before creating the ES cluster otherwise the cluster will fail...that's why
        // it was extracted from the ES stack and made into its own stack
        ElasticsearchIamStack iamStack = new ElasticsearchIamStack(app, "IamES");

        // Elasticsearch service
        ElasticsearchStack elasticsearchStack = new ElasticsearchStack(app, "LiveStreamingES");

        // ECR
        EcrStack ecrStack = new EcrStack(app, "EcrStack");

        app.synth();
    }
}