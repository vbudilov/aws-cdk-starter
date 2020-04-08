package com.budilov.cdk.codepipeline;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.elasticsearch.CfnDomain;

import java.io.IOException;

public class CodePipelineStack extends Stack {

    public CfnDomain esDomain;

    public CodePipelineStack(final Construct scope, final String id) throws IOException {
        this(scope, id, null);
    }

    public CodePipelineStack(final Construct scope, final String id, final StackProps props) throws IOException {
        super(scope, id, props);


    }

}
