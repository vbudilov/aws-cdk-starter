package com.budilov.cdk;

import com.budilov.cdk.util.Properties;
import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.cognito.UserPool;
import software.amazon.awscdk.services.cognito.UserPoolClient;
import software.amazon.awscdk.services.cognito.UserPoolTriggers;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.FunctionProps;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.ssm.ParameterTier;
import software.amazon.awscdk.services.ssm.StringParameter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

/**
 * @author Vladimir Budilov
 * <p>
 * Simple way to create a Cognito User Pool with Lambda triggers
 */
public class CognitoLambdaStack extends Stack {
    static Function copyUserToDynamoDBLambda;
    static Function autoConfirmFunction;

    public CognitoLambdaStack(final Construct scope, final String id) throws IOException {
        this(scope, id, null, Properties.DDB_USERS_TABLE);
    }

    public CognitoLambdaStack(final Construct scope, final String id, final StackProps props, final String usersTableName) throws IOException {
        super(scope, id, props);

         copyUserToDynamoDBLambda = new Function(this, "copyUserToDynamoDBLambda", FunctionProps.builder()
                .runtime(Runtime.NODEJS_12_X)
                .handler("index.handler")
                .code(Code.fromInline(getLambdaFunctionFromFile("cognitoToDynamoDBLambda")))
                .environment(Map.of("TABLE_NAME", usersTableName,
                        "REGION", Properties.REGION,
                        "PARTITION_ID", Properties.DDB_USERS_TABLE_PARTITION_ID,
                        "SORT_KEY", Properties.DDB_USERS_TABLE_SORT_KEY))
                .build());

        autoConfirmFunction = new Function(this, "autoConfirmFunction", FunctionProps.builder()
                .runtime(Runtime.NODEJS_12_X)
                .handler("index.handler")
                .code(Code.fromInline(getLambdaFunctionFromFile("cognitoAutoConfirmUser")))
                .build());

    }


    private String getLambdaFunctionFromFile(@NotNull String fileName) throws IOException {

        File file = new File(
                getClass().getClassLoader().getResource(fileName + ".js").getFile()
        );

        return new String(Files.readAllBytes(file.toPath()));
    }
}
