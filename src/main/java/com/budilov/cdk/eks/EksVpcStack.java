package com.budilov.cdk.eks;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.ec2.Vpc;

/**
 * TODO
 */
public class EksVpcStack extends Stack {
    public static Vpc vpc;
    public String VPC_NAME = "EKSVpc";

    public EksVpcStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public EksVpcStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        vpc = Vpc.Builder.create(this, VPC_NAME)
                .cidr("10.0.0.0/16")
                .maxAzs(4)
                .build();
    }
}
