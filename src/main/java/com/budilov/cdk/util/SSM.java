package com.budilov.cdk.util;

import org.jetbrains.annotations.NotNull;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.PutParameterRequest;
import software.amazon.awssdk.services.ssm.model.PutParameterResponse;

public class SSM {

    SsmClient client;

    SSM() {
        client = SsmClient.builder().build();
    }

    void addParameter(@NotNull String name, @NotNull String value) {
        PutParameterRequest request = PutParameterRequest.builder().name(name).value(value).build();

        PutParameterResponse response = client.putParameter(request);

    }
}
