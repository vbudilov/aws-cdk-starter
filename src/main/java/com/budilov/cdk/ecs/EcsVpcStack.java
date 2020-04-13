package com.budilov.cdk.ecs;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.ec2.Vpc;
import software.amazon.awscdk.services.elasticloadbalancingv2.ApplicationLoadBalancer;

import java.io.IOException;

/**
 * Stack that creates IAM roles for EKS Pods
 */
public class EcsVpcStack extends Stack {

    public static Vpc vpc;
    public static ApplicationLoadBalancer lb;
    public EcsVpcStack(final Construct scope, final String id) throws IOException {
        this(scope, id, null);
    }

    public EcsVpcStack(final Construct scope, final String id, final StackProps props) throws IOException {
        super(scope, id, props);
        vpc = Vpc.Builder.create(this, "TickerVPC")
                .build();

        lb = ApplicationLoadBalancer.Builder.create(this, "TickerALB")
                        .vpc(vpc)
                        .internetFacing(Boolean.TRUE)
                        .loadBalancerName("ticker")
                        .build();

//        ApplicationLoadBalancedTaskImageOptions.builder()
//                .image(ContainerImage.fromEcrRepository(EcrStack.dataIngestGateway))
//                .executionRole(EcsIamStack.dataIngestGatewayRole)
//                .build();

    }

}
