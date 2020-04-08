package com.budilov.cdk;

import com.budilov.cdk.util.Properties;
import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.elasticsearch.CfnDomain;
import software.amazon.awscdk.services.iam.CompositePrincipal;
import software.amazon.awscdk.services.iam.ManagedPolicy;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.iam.ServicePrincipal;

import java.io.IOException;
import java.util.List;

public class ElasticsearchIamStack extends Stack {

    public static Role esAccessRole;

    public ElasticsearchIamStack(final Construct scope, final String id) throws IOException {
        this(scope, id, null);
    }

    public ElasticsearchIamStack(final Construct scope, final String id, final StackProps props) throws IOException {
        super(scope, id, props);

        // ES Access Role
        esAccessRole = Role.Builder.create(this, Properties.ES_ALLOWED_ROLE_NAME)
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
