package com.budilov.cdk;

import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.cognito.UserPool;
import software.amazon.awscdk.services.cognito.UserPoolClient;
import software.amazon.awscdk.services.cognito.UserPoolTriggers;
import software.amazon.awscdk.services.ssm.ParameterTier;
import software.amazon.awscdk.services.ssm.StringParameter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @author Vladimir Budilov
 * <p>
 * Simple way to create a Cognito User Pool with Lambda triggers
 */
public class CognitoStack extends Stack {
    UserPool pool;
    UserPoolClient upClient;

    public CognitoStack(final Construct scope, final String id) throws IOException {
        this(scope, id, null);
    }

    public CognitoStack(final Construct scope, final String id, final StackProps props) throws IOException {
        super(scope, id, props);

        pool = UserPool.Builder.create(this, "MyPool")
                .userPoolName("myPool")
                .selfSignUpEnabled(true)
                .lambdaTriggers(UserPoolTriggers.builder()
                        .postConfirmation(CognitoLambdaStack.copyUserToDynamoDBLambda) // Copy the user to a DynamoDB Table
                        .preSignUp(CognitoLambdaStack.autoConfirmFunction) // Auto-confirm user
                        .build())
//                .userVerification(UserVerificationConfig.builder()
//                        .emailSubject(Properties.COGNITO_EMAIL_VERIFICATION_SUBJECT)
//                        .emailBody(Properties.COGNITO_EMAIL_VERIFICATION_BODY).build())
                .build();

        upClient = UserPoolClient.Builder.create(this, "MyPoolClient")
                .userPoolClientName("webClient")
                .userPool(pool)
                .generateSecret(false)
                .build();

        StringParameter.Builder.create(this, "cognitoPoolIdSecret")
                .allowedPattern(".*")
                .description("cognitoPoolIdSecret")
                .parameterName("cognitoPoolIdSecret")
                .stringValue(this.pool.getUserPoolId())
                .tier(ParameterTier.STANDARD)
                .build();

        StringParameter.Builder.create(this, "cognitoPoolClientIdSecret")
                .allowedPattern(".*")
                .description("cognitoPoolClientId")
                .parameterName("cognitoPoolClientId")
                .stringValue(this.upClient.getUserPoolClientId())
                .tier(ParameterTier.STANDARD)
                .build();

//        CfnOutput output = CfnOutput.Builder.create(this, "OutputName")
//                .value(this.pool.getUserPoolId())
//                .description("The name of an S3 bucket")
//                .exportName("cognitoPoolId")
//                .build();

//        SSM.addParameter("cognitoPoolId", this.pool.getUserPoolId());
//        SSM.addParameter("cognitoPoolClientId", this.upClient.getUserPoolClientId());

    }


    private String getLambdaFunctionFromFile(@NotNull String fileName) throws IOException {

        File file = new File(
                getClass().getClassLoader().getResource(fileName + ".js").getFile()
        );

        return new String(Files.readAllBytes(file.toPath()));
    }
}
