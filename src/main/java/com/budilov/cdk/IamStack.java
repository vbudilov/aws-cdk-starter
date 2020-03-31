package com.budilov.cdk;

import com.budilov.cdk.util.Properties;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.elasticsearch.CfnDomain;
import software.amazon.awscdk.services.iam.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class IamStack extends Stack {

    public CfnDomain esDomain;

    public IamStack(final Construct scope, final String id) throws IOException {
        this(scope, id, null);
    }

    public IamStack(final Construct scope, final String id, final StackProps props) throws IOException {
        super(scope, id, props);

        // ES Access Role
        Role esAccessRole = Role.Builder.create(this, Properties.ES_ALLOWED_ROLE_NAME)
                .assumedBy(
                        new CompositePrincipal(
                                ServicePrincipal.Builder.create("lambda").build(),
                                ServicePrincipal.Builder.create("eks").build()
                        )
                )
                .managedPolicies(List.of(ManagedPolicy.fromAwsManagedPolicyName("AmazonESFullAccess")))
                .roleName(Properties.ES_ALLOWED_ROLE_NAME)
                .build();


    }

}
