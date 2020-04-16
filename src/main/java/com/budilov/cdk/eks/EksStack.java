package com.budilov.cdk.eks;

import com.budilov.cdk.util.Properties;
import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.ec2.InstanceType;
import software.amazon.awscdk.services.eks.Cluster;

import java.io.IOException;

/**
 * TODO: Stack that creates IAM roles for EKS Pods
 */
public class EksStack extends Stack {

    public EksStack(final Construct scope, final String id) throws IOException {
        this(scope, id, null);
    }

    public EksStack(final Construct scope, final String id, final StackProps props) throws IOException {
        super(scope, id, props);

        Cluster cluster = Cluster.Builder.create(this, Properties.EKS_CLUSTER_NAME)
                .version("1.15")
//                .vpc(VpcStack.vpc)
                .defaultCapacity(2)
                .defaultCapacityInstance(new InstanceType("t3.large"))

                .build();

//        cluster.addNodegroup("tickerNG", NodegroupOptions.builder()
//                .minSize(2)
//                .maxSize(6)
//                .desiredSize(2)
//                .diskSize(10)
//                .nodeRole(EcsIamStack.tickerNodeRole)
//                .build());


//        StringParameter.Builder.create(this, "dataIngestGatewayEcr")
//                .allowedPattern(".*")
//                .description("dataIngestGatewayEcr")
//                .parameterName("dataIngestGatewayEcr")
//                .stringValue(dataIngestGateway.getRepositoryName())
//                .tier(ParameterTier.STANDARD)
//                .build();
    }

}
