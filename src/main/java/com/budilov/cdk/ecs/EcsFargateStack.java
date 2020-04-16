package com.budilov.cdk.ecs;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.ec2.Port;
import software.amazon.awscdk.services.ec2.Vpc;
import software.amazon.awscdk.services.ecs.*;
import software.amazon.awscdk.services.ecs.Protocol;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedFargateService;
import software.amazon.awscdk.services.elasticloadbalancingv2.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Stack that creates IAM roles for EKS Pods
 */
public class EcsFargateStack extends Stack {


    public EcsFargateStack(final Construct scope, final String id) throws IOException {
        this(scope, id, null);
    }

    public EcsFargateStack(final Construct scope, final String id, final StackProps props) throws IOException {
        super(scope, id, props);
        Number cpu = 512;
        Number memory = 1024;

        Vpc vpc = Vpc.Builder.create(this, "TickerVPC")
                .build();

        ApplicationLoadBalancer alb = ApplicationLoadBalancer.Builder.create(this, "TickerALB")
                .vpc(vpc)
                .internetFacing(Boolean.TRUE)
                .loadBalancerName("ticker")
                .build();

        Cluster cluster = Cluster.Builder.create(this, "TickerFargateCluster")
                .vpc(vpc)
                .clusterName("TickerFargateCluster")
                .build();

        FargateTaskDefinition taskDefinition = new FargateTaskDefinition(this, "TickersTaskDefinition", FargateTaskDefinitionProps.builder()
                .cpu(cpu)
                .memoryLimitMiB(memory)
                .build());

        ContainerDefinition container = taskDefinition.addContainer("DataIngestGatewayContainer", ContainerDefinitionOptions.builder()
                .image(ContainerImage.fromEcrRepository(EcrStack.dataIngestGateway))
                .environment(Map.of("test", "test"))
                .memoryLimitMiB(memory)
                .cpu(cpu)
                .build());
        container.addPortMappings(PortMapping.builder()
                .containerPort(8080)
                .hostPort(8080)
                .protocol(Protocol.TCP)
                .build());
        FargateService tickerGatewayService = new FargateService(this, "TickerGatewayFargateService", FargateServiceProps.builder()
                .cluster(cluster)
                .taskDefinition(taskDefinition)
                .desiredCount(2)
                .serviceName("tickerGatewayService")
                .build());

        tickerGatewayService.getConnections().allowFrom(alb, Port.tcp(80));
        alb.getConnections().allowTo(tickerGatewayService, Port.tcp(8080));

        ApplicationTargetGroup tg = new ApplicationTargetGroup(this, "TickerIngestGatewayTG", ApplicationTargetGroupProps.builder()
                .targets(List.of(tickerGatewayService))
                .port(8080)
                .protocol(ApplicationProtocol.HTTP)
                .vpc(vpc)
                .build());

        alb.addListener("GatewayListener", BaseApplicationListenerProps.builder()
                .port(80)
                .defaultTargetGroups(List.of(tg))
                .build());

    }

}
