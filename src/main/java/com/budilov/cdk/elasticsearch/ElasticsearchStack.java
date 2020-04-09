package com.budilov.cdk.elasticsearch;

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

public class ElasticsearchStack extends Stack {

    public CfnDomain esDomain;

    public ElasticsearchStack(final Construct scope, final String id) throws IOException {
        this(scope, id, null);
    }

    public ElasticsearchStack(final Construct scope, final String id, final StackProps props) throws IOException {
        super(scope, id, props);

        esDomain = CfnDomain.Builder.create(this, Properties.ES_NAME)
                .domainName(Properties.ES_NAME)
                .elasticsearchVersion(Properties.ES_VERSION)
                .elasticsearchClusterConfig(CfnDomain.ElasticsearchClusterConfigProperty.builder()
                        .instanceCount(Properties.ES_INSTANCE_COUNT)
                        .instanceType(Properties.ES_INSTANCE_TYPE)
//                        .dedicatedMasterCount(2)
//                        .dedicatedMasterEnabled(true)
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
                .advancedOptions(Map.of())
                .build();

    }

    private String getMyExternalIP() throws IOException {
        URL url = new URL("http://checkip.amazonaws.com/");
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        return br.readLine();
    }
}
