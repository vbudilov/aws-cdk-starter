package com.budilov.cdk.ecs;

import com.budilov.cdk.util.Properties;
import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.ecr.LifecycleRule;
import software.amazon.awscdk.services.ecr.Repository;
import software.amazon.awscdk.services.ssm.ParameterTier;
import software.amazon.awscdk.services.ssm.StringParameter;

import java.io.IOException;
import java.util.List;

/**
 * Stack that creates IAM roles for EKS Pods
 */
public class EcrStack extends Stack {

    public static Repository dataIngestGateway;
    public static Repository kinesisConsumerService;

    public EcrStack(final Construct scope, final String id) throws IOException {
        this(scope, id, null);
    }

    public EcrStack(final Construct scope, final String id, final StackProps props) throws IOException {
        super(scope, id, props);

        dataIngestGateway = Repository.Builder.create(this, Properties.ECR_DATA_INGEST_GATEWAY_NAME)
                .repositoryName(Properties.ECR_DATA_INGEST_GATEWAY_NAME)
                .lifecycleRules(List.of(LifecycleRule
                        .builder()
                        .maxImageCount(9999)
                        .build())
                )
                .build();

        kinesisConsumerService = Repository.Builder.create(this, Properties.ECR_KINESIS_CONSUMER_NAME)
                .repositoryName(Properties.ECR_KINESIS_CONSUMER_NAME)
                .lifecycleRules(List.of(LifecycleRule
                        .builder()
                        .maxImageCount(9999)
                        .build())
                )
                .build();

        StringParameter.Builder.create(this, "dataIngestGatewayEcr")
                .allowedPattern(".*")
                .description("dataIngestGatewayEcr")
                .parameterName("dataIngestGatewayEcr")
                .stringValue(dataIngestGateway.getRepositoryName())
                .tier(ParameterTier.STANDARD)
                .build();

    }

}
