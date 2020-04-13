package com.budilov.cdk.elasticsearch;

import com.budilov.cdk.util.Properties;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.elasticsearch.CfnDomain;
import software.amazon.awscdk.services.iam.PolicyDocument;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.ssm.ParameterTier;
import software.amazon.awscdk.services.ssm.StringParameter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;

public class ElasticsearchStack extends Stack {

    public CfnDomain esDomain;

    public ElasticsearchStack(final Construct scope, final String id, Role iamAccessRole) throws IOException {
        this(scope, id, null, iamAccessRole);
    }

    public ElasticsearchStack(final Construct scope, final String id, final StackProps props, Role iamAccessRole) throws IOException {
        super(scope, id, props);

        esDomain = CfnDomain.Builder.create(this, Properties.ES_NAME)
                .domainName(Properties.ES_NAME)
                .elasticsearchVersion(Properties.ES_VERSION)
                .elasticsearchClusterConfig(CfnDomain.ElasticsearchClusterConfigProperty.builder()
                        .instanceCount(Properties.ES_INSTANCE_COUNT)
                        .instanceType(Properties.ES_INSTANCE_TYPE)
                        .build())
                .accessPolicies(PolicyDocument.fromJson(new ObjectMapper().readValue(Properties.readResourceFileContents("elasticsearch-access-policy.json",
                        Map.of("ACCOUNT_NAME_REPLACE_ME", Properties.ACCOUNT,
                                "ROLE_NAME_REPLACE_ME", Properties.ES_ALLOWED_ROLE_NAME, // can't use the actual instance object's role here for some reason...no idea why for now
                                "REGION_REPLACE_ME", Properties.REGION,
                                "ES_DOMAIN_NAME_REPLACE_ME", Properties.ES_NAME,
                                "EXTERNAL_IP_ADDRESS_REPLACE_ME", getMyExternalIP() // For testing purposes
                        )), Map.class)))
                .ebsOptions(CfnDomain.EBSOptionsProperty.builder()
                        .ebsEnabled(true)
                        .volumeType("standard")
                        .volumeSize(10)
                        .build())
//                .advancedOptions(Map.of())
                .build();

        StringParameter.Builder.create(this, "esDomain")
                .allowedPattern(".*")
                .description("esDomain")
                .parameterName("esDomain")
                .stringValue(esDomain.getDomainName())
                .tier(ParameterTier.STANDARD)
                .build();

        StringParameter.Builder.create(this, "esDomainEndpoint")
                .allowedPattern(".*")
                .description("esDomainEndpoint")
                .parameterName("esDomainEndpoint")
                .stringValue(esDomain.getAttrDomainEndpoint())
                .tier(ParameterTier.STANDARD)
                .build();

    }

    private String getMyExternalIP() throws IOException {
        URL url = new URL("http://checkip.amazonaws.com/");
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        return br.readLine();
    }
}
