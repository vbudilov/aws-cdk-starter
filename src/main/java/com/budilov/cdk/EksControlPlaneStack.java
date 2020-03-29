package com.budilov.cdk;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;

public class EksControlPlaneStack extends Stack {
    public EksControlPlaneStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public EksControlPlaneStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        // The code that defines your stack goes here
    }
}
