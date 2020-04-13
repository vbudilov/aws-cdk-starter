package com.budilov.cdk.ecs;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.ecs.Cluster;
import software.amazon.awscdk.services.ecs.ContainerImage;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedFargateService;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedTaskImageOptions;

import java.io.IOException;

/**
 * Stack that creates IAM roles for EKS Pods
 */
public class EcsFargateStack extends Stack {

    public static ApplicationLoadBalancedFargateService albService;
    public static ApplicationLoadBalancedFargateService albService2;
    public EcsFargateStack(final Construct scope, final String id) throws IOException {
        this(scope, id, null);
    }

    public EcsFargateStack(final Construct scope, final String id, final StackProps props) throws IOException {
        super(scope, id, props);

        Cluster cluster = Cluster.Builder.create(this, "TickerFargateCluster")
                .clusterName("TickerFargateCluster")
                .build();

        // Create a load-balanced Fargate service and make it public
        albService = ApplicationLoadBalancedFargateService.Builder.create(this, "MyFargateService")
                .cluster(cluster)           // Required
                .cpu(512)                   // Default is 256
                .desiredCount(2)            // Default is 1
                .serviceName("TickerService")
                .taskImageOptions(ApplicationLoadBalancedTaskImageOptions.builder()
                        .taskRole(EcsIamStack.dataIngestGatewayRole)
                        .containerPort(80)
                        .image(ContainerImage.fromRegistry("amazon/amazon-ecs-sample"))
                        .build()
                )
                .memoryLimitMiB(1024)       // Default is 512
                .publicLoadBalancer(true)   // Default is false
                .build();

    }

}
