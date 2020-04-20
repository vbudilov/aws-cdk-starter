package com.budilov.cdk;

import com.budilov.cdk.ecs.EcrStack;
import com.budilov.cdk.ecs.EcsFargateStack;
import com.budilov.cdk.ecs.EcsIamStack;
import com.budilov.cdk.elasticsearch.ElasticsearchIamStack;
import com.budilov.cdk.elasticsearch.ElasticsearchStack;
import software.amazon.awscdk.core.App;

public class LiveStreamingApp {


    public static void main(final String[] args) throws Exception {
        App app = new App();

        // DynamoDB Stack
        DDBUserTableStack ddbUserTableStack = new DDBUserTableStack(app, "TickerDDBTables");

        // Kinesis
        KinesisStreamsStack kinesisStreamsStack = new KinesisStreamsStack(app, "TickerKinesisStreams");

        // Cognito
        CognitoLambdaStack cognitoLambdaStack = new CognitoLambdaStack(app, "TickerCognitoLambdaStack");
        CognitoStack cognitoStack = new CognitoStack(app, "TickerCognitoUP");
        cognitoStack.addDependency(cognitoLambdaStack);

        // ElasticSearch
        ElasticsearchIamStack iamStack = new ElasticsearchIamStack(app, "TickerESIAM");
        ElasticsearchStack elasticsearchStack = new ElasticsearchStack(app, "TickerES", iamStack.esAccessRole);
        // before the ES cluster can be created the allowed IAM role needs to be there otherwise the access policy can't be applied
        elasticsearchStack.addDependency(iamStack);

        // Containers
        EcsIamStack ecsIamStack = new EcsIamStack(app, "TickerEcsIamStack");
        EcrStack ecrStack = new EcrStack(app, "TickerEcrStack");
        EcsFargateStack ecsFargateStack = new EcsFargateStack(app, "TickerEcsStack");

        app.synth();
    }
}