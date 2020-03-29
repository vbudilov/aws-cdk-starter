package com.budilov.cdk;

import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.cognito.*;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.FunctionProps;
import software.amazon.awscdk.services.lambda.Runtime;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @author Vladimir Budilov
 *
 * Simple way to create a Cognito User Pool with Lambda triggers
 *
 */
public class CognitoStack extends Stack {
    public CognitoStack(final Construct scope, final String id) throws IOException {
        this(scope, id, null);
    }

    public CognitoStack(final Construct scope, final String id, final StackProps props) throws IOException {
        super(scope, id, props);

        Function postConfirmationFunction = new Function(this, "MyFunction", FunctionProps.builder()
                .runtime(Runtime.NODEJS_12_X)
                .handler("index.handler")
                .code(Code.fromInline(getLambdaFunctionFromFile("cognitoToDynamoDBLambda")))
                .build());

        Function autoConfirmFunction = new Function(this, "MyFunction", FunctionProps.builder()
                .runtime(Runtime.NODEJS_12_X)
                .handler("index.handler")
                .code(Code.fromInline(getLambdaFunctionFromFile("cognitoToDynamoDBLambda")))
                .build());

        UserPool pool = UserPool.Builder.create(this, "MyPool")
                .userPoolName("myPool")
                .selfSignUpEnabled(true)
                .lambdaTriggers(UserPoolTriggers.builder()
                        .postConfirmation(postConfirmationFunction)
                        .build())
//                .userVerification(UserVerificationConfig.builder()
//                        .emailSubject(Properties.COGNITO_EMAIL_VERIFICATION_SUBJECT)
//                        .emailBody(Properties.COGNITO_EMAIL_VERIFICATION_BODY).build())
                .build();

        UserPoolClient upClient = UserPoolClient.Builder.create(this, "MyPoolClient")
                .userPoolClientName("webClient")
                .userPool(pool)
                .generateSecret(false)
                .build();

    }


    private String getLambdaFunctionFromFile(@NotNull String fileName) throws IOException {

        File file = new File(
                getClass().getClassLoader().getResource(fileName + ".js").getFile()
        );

        return new String(Files.readAllBytes(file.toPath()));
    }
}
