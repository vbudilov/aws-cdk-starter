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

public class ElasticsearchStack extends Stack {

    public CfnDomain esDomain;

    public ElasticsearchStack(final Construct scope, final String id) throws IOException {
        this(scope, id, null);
    }

    public ElasticsearchStack(final Construct scope, final String id, final StackProps props) throws IOException {
        super(scope, id, props);

        System.out.println(new ObjectMapper().readValue(Properties.readResourceFileContents("elasticsearch-access-policy.json",
                Map.of("ACCOUNT_NAME_REPLACE_ME", Properties.ACCOUNT,
                        "ROLE_NAME_REPLACE_ME", Properties.ES_ALLOWED_ROLE_NAME,
                        "REGION_REPLACE_ME", Properties.REGION,
                        "ES_DOMAIN_NAME_REPLACE_ME", Properties.ES_NAME,
                        "EXTERNAL_IP_ADDRESS_REPLACE_ME", getMyExternalIP() // For testing purposes
                )), Map.class));


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
                                "ROLE_NAME_REPLACE_ME", Properties.ES_ALLOWED_ROLE_NAME,
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
/*
 1/2 | 11:09:43 AM | UPDATE_FAILED        | AWS::Elasticsearch::Domain | lselastic Error setting policy: [{"Version":"2012-10-17","Statement":[{"Action":"es:*","Resource":"arn:aws:es:us-east-1:540403165297:domain/lselastic/*","Effect":"Allow","Principal":{"AWS":"arn:aws:iam::540403165297:role/LiveStreamingRoleToElasticSearch"}},{"Condition":{"IpAddress":{"aws:SourceIp":"108.52.199.76"}},"Action":"es:*","Resource":"arn:aws:es:us-east-1:540403165297:domain/lselastic/*","Effect":"Allow","Principal":"*","Sid":""}]}] (Service: AWSElasticsearch; Status Code: 409; Error Code: InvalidTypeException; Request ID: 71d57807-b97d-4b1e-b3b7-fb5ce6c2d460)
	/tmp/jsii-java-runtime12634847751947457510/jsii-runtime.js:7836:49
	\_ Kernel._wrapSandboxCode (/tmp/jsii-java-runtime12634847751947457510/jsii-runtime.js:8296:20)
	\_ Kernel._create (/tmp/jsii-java-runtime12634847751947457510/jsii-runtime.js:7836:26)
	\_ Kernel.create (/tmp/jsii-java-runtime12634847751947457510/jsii-runtime.js:7583:21)
	\_ KernelHost.processRequest (/tmp/jsii-java-runtime12634847751947457510/jsii-runtime.js:7371:28)
	\_ KernelHost.run (/tmp/jsii-java-runtime12634847751947457510/jsii-runtime.js:7311:14)
	\_ Immediate._onImmediate (/tmp/jsii-java-runtime12634847751947457510/jsii-runtime.js:7314:37)
	\_ processImmediate (internal/timers.js:456:21)

 */