package com.budilov.cdk;

import com.budilov.cdk.util.Properties;
import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.dynamodb.Attribute;
import software.amazon.awscdk.services.dynamodb.AttributeType;
import software.amazon.awscdk.services.dynamodb.Table;
import software.amazon.awscdk.services.dynamodb.TableProps;
import software.amazon.awscdk.services.ssm.ParameterTier;
import software.amazon.awscdk.services.ssm.StringParameter;

public class S3PublicBucketStack extends Stack {

    public S3PublicBucketStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public S3PublicBucketStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);


//        StringParameter.Builder.create(this, "tickerTable")
//                .allowedPattern(".*")
//                .description("tickerTable")
//                .parameterName("tickerTable")
//                .stringValue(tickerTable.getTableName())
//                .tier(ParameterTier.STANDARD)
//                .build();
    }

}
