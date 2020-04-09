package com.budilov.cdk.eks;

import com.budilov.cdk.util.Properties;
import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.iam.*;
import software.amazon.awscdk.services.ssm.ParameterTier;
import software.amazon.awscdk.services.ssm.StringParameter;

import java.io.IOException;
import java.util.List;

/**
 * Stack that creates IAM roles for EKS Pods
 */
public class EksIamStack extends Stack {

    public static Role eksDataIngestGatewayRole;

    public EksIamStack(final Construct scope, final String id) throws IOException {
        this(scope, id, null);
    }

    public EksIamStack(final Construct scope, final String id, final StackProps props) throws IOException {
        super(scope, id, props);

        // Data Ingest Gateway Access Role
        eksDataIngestGatewayRole = Role.Builder.create(this, Properties.DATA_INGEST_GATEWAY_ROLE)
                .assumedBy(
                        new CompositePrincipal(
                                ServicePrincipal.Builder.create("lambda").build(),
                                ServicePrincipal.Builder.create("eks").build()
                        )
                )
                .managedPolicies(List.of(ManagedPolicy.fromAwsManagedPolicyName("AmazonKinesisFullAccess")))
                .roleName(Properties.DATA_INGEST_GATEWAY_ROLE)
                .build();

        StringParameter.Builder.create(this, "eksDataIngestGatewayRole")
                .allowedPattern(".*")
                .description("eksDataIngestGatewayRole")
                .parameterName("eksDataIngestGatewayRole")
                .stringValue(eksDataIngestGatewayRole.getRoleName())
                .tier(ParameterTier.STANDARD)
                .build();
    }

}
