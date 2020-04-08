package com.budilov.cdk;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.ec2.SubnetConfiguration;
import software.amazon.awscdk.services.ec2.SubnetType;
import software.amazon.awscdk.services.ec2.Vpc;

import java.util.List;

public class VpcStack extends Stack {
    public String VPC_NAME = "EKSVpc";

    public VpcStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public VpcStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        Vpc vpc = Vpc.Builder.create(this, VPC_NAME)
                .cidr("10.0.0.0/16")
                .subnetConfiguration(List.of(
                        SubnetConfiguration.builder().name("eksAZ-us-east-1a").subnetType(SubnetType.PRIVATE).build())
                )
                .maxAzs(3)
                .build();
    }
}
