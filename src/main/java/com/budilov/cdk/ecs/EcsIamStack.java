package com.budilov.cdk.ecs;

import com.budilov.cdk.util.Properties;
import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.iam.CompositePrincipal;
import software.amazon.awscdk.services.iam.ManagedPolicy;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.iam.ServicePrincipal;

import java.io.IOException;
import java.util.List;

/**
 * Stack that creates IAM roles for EKS Pods
 */
public class EcsIamStack extends Stack {

    static Role dataIngestGatewayRole;
    static Role tickerNodeRole;

    public EcsIamStack(final Construct scope, final String id) throws IOException {
        this(scope, id, null);
    }

    public EcsIamStack(final Construct scope, final String id, final StackProps props) throws IOException {
        super(scope, id, props);
        tickerNodeRole = Role.Builder.create(this, Properties.TICKER_NODES_ROLE)
                .assumedBy(
                        new CompositePrincipal(
                                ServicePrincipal.Builder.create("eks").build(),
                                ServicePrincipal.Builder.create("ec2").build(),
                                ServicePrincipal.Builder.create("ecs").build(),
                                ServicePrincipal.Builder.create("ecs-tasks").build()
                        )
                )
                .managedPolicies(List.of(ManagedPolicy.fromAwsManagedPolicyName("AmazonKinesisFullAccess")))
                .roleName(Properties.TICKER_NODES_ROLE)
                .build();

        // Data Ingest Gateway Access Role
        dataIngestGatewayRole = Role.Builder.create(this, Properties.DATA_INGEST_GATEWAY_ROLE)
                .assumedBy(
                        new CompositePrincipal(
                                ServicePrincipal.Builder.create("lambda").build(),
                                ServicePrincipal.Builder.create("eks").build(),
                                ServicePrincipal.Builder.create("ecs").build(),
                                ServicePrincipal.Builder.create("ecs-tasks").build()
                        )
                )
                .managedPolicies(List.of(ManagedPolicy.fromAwsManagedPolicyName("AmazonKinesisFullAccess"),
                        ManagedPolicy.fromAwsManagedPolicyName("AmazonSSMFullAccess")))
                .roleName(Properties.DATA_INGEST_GATEWAY_ROLE)
                .build();

    }

}
