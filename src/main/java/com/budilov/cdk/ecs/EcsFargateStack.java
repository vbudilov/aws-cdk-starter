package com.budilov.cdk.ecs;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.ec2.Port;
import software.amazon.awscdk.services.ec2.Vpc;
import software.amazon.awscdk.services.ecs.Protocol;
import software.amazon.awscdk.services.ecs.*;
import software.amazon.awscdk.services.elasticloadbalancingv2.HealthCheck;
import software.amazon.awscdk.services.elasticloadbalancingv2.*;
import software.amazon.awscdk.services.logs.RetentionDays;

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
        Number containerPort = 8080;

        // VPC
        Vpc vpc = Vpc.Builder.create(this, "TickerVPC")
                .build();

        // ALB
        ApplicationLoadBalancer alb = ApplicationLoadBalancer.Builder.create(this, "TickerALB")
                .vpc(vpc)
                .internetFacing(Boolean.TRUE)
                .loadBalancerName("ticker")
                .build();

        // ECS Cluster
        Cluster cluster = Cluster.Builder.create(this, "TickerFargateCluster")
                .vpc(vpc)
                .clusterName("TickerFargateCluster")
                .build();

        // Ingest Gateway Configuration
        FargateTaskDefinition ingestGatewayTaskDefinition = new FargateTaskDefinition(this, "TickersDataIngestGatewayTask", FargateTaskDefinitionProps.builder()
                .cpu(cpu)
                .memoryLimitMiB(memory)
                .taskRole(EcsIamStack.dataIngestGatewayRole)
                .build());
        ContainerDefinition ingestGatewayContainerDefinition = ingestGatewayTaskDefinition.addContainer("DataIngestGatewayContainer", ContainerDefinitionOptions.builder()
                .image(ContainerImage.fromEcrRepository(EcrStack.dataIngestGateway))
                .environment(Map.of("test", "test"))
                .memoryLimitMiB(memory)
                .cpu(cpu)
                .logging(LogDriver.awsLogs(AwsLogDriverProps.builder()
                        .streamPrefix("dataIngest")
                        .logRetention(RetentionDays.ONE_DAY).build()))
                .build());
        ingestGatewayContainerDefinition.addPortMappings(PortMapping.builder()
                .containerPort(containerPort)
                .protocol(Protocol.TCP)
                .build());
        FargateService tickerGatewayService = new FargateService(this, "TickerGatewayFargateService", FargateServiceProps.builder()
                .cluster(cluster)
                .taskDefinition(ingestGatewayTaskDefinition)
                .desiredCount(2)
                .serviceName("tickerGatewayService")
                .build());

        // Allow connections to the fargate service from the ALB TO the listed ports
        tickerGatewayService.getConnections().allowFrom(alb, Port.tcp(80));
        tickerGatewayService.getConnections().allowFrom(alb, Port.tcp(443));

        // Allow connections to the fargate service from the ALB FROM the listed ports
        alb.getConnections().allowTo(tickerGatewayService, Port.tcp(containerPort));

        ApplicationTargetGroup tickerIngestGatewayTG = new ApplicationTargetGroup(this, "TickerIngestGatewayTargetGroup", ApplicationTargetGroupProps.builder()
                .targets(List.of(tickerGatewayService))
                .targetType(TargetType.IP)
                .port(containerPort)
                .protocol(ApplicationProtocol.HTTP)
                .vpc(vpc)
                .healthCheck(HealthCheck.builder()
                        .enabled(true)
                        .path("/ping")
                        .build())
                .build());


        ApplicationListener listener = alb.addListener("TickerIngestGatewayListener", BaseApplicationListenerProps.builder()
                .port(80)
                .defaultTargetGroups(List.of(tickerIngestGatewayTG))
                .build());

//        listener.addTargets("TickerIngestGatewayTarget", AddApplicationTargetsProps.builder()
//                .port(80)
//                .pathPatterns(List.of("/ingest"))
//                .priority(1)
//                .targetGroupName(tickerIngestGatewayTG.getTargetGroupName())
//                .build());


//        IApplicationListener ilistener  = ApplicationListener.fromApplicationListenerAttributes(this, "", ApplicationListenerAttributes.builder()
//                .defaultPort(80)
//                .build());
//        ilistener.addTargetGroups("TickerIngestTargetGroup",AddApplicationTargetGroupsProps.builder()
//                .priority(1)
//                .pathPatterns(List.of("/ingest"))
//                .targetGroups(List.of(tickerIngestGatewayTG))
//                .build());
//        alb.addListener("", BaseApplicationListenerProps.builder()
//                .port(80)
//                .
//                .build());

//        ApplicationListener listener = alb.addListener("Listener", BaseApplicationListenerProps.builder()
//                .port(80)
//                .open(true).build());


//
//        listener.addTargets("TickerIngestTarget", AddApplicationTargetsProps.builder()
//                .port(containerPort)
//                .pathPatterns(List.of("/ingest"))
//                .priority(1)
//                .targetGroupName(tickerIngestGatewayTG.getTargetGroupName())
//                .healthCheck(HealthCheck.builder()
//                        .enabled(true)
//                        .path("/ping")
//                        .build())
//                .build());

//        listener.addTargetGroups("TickerIngestTargetGroup", AddApplicationTargetGroupsProps.builder()
//                .targetGroups(List.of(tickerIngestGatewayTG))
//                .priority(1)
//                .pathPatterns(List.of("/ingest"))
//                .build());
    }

}
