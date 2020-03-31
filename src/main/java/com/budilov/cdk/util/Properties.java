package com.budilov.cdk.util;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

public class Properties {

    public static String REGION = "us-east-1";
    public static String ACCOUNT = "540403165297";
    public static String COGNITO_EMAIL_VERIFICATION_BODY = "Thanks for signing up! Enjoy your experience";
    public static String COGNITO_EMAIL_VERIFICATION_SUBJECT = "Thanks for joining mydomain.com!";

    public static String DDB_USERS_TABLE = "Users";
    public static String DDB_USERS_TABLE_PARTITION_ID = "userId";
    public static String DDB_USERS_TABLE_SORT_KEY = "sortKey";

    public static String DDB_TICKERS_TABLE = "Tickers";
    public static String DDB_TICKERS_TABLE_PARTITION_ID = "tickerId";
    public static String DDB_TICKERS_TABLE_SORT_KEY = "sortKey";

    public static String KINESIS_STREAM_NAME = "MyStream";
    public static int KINESIS_STREAM_SHARD_COUNT = 1;

    public static String ES_NAME = "lselastic";
    // This is the role that will have access to the ES cluster
    public static String ES_ALLOWED_ROLE_NAME = "LiveStreamingRoleToElasticSearch";
    public static String ES_VERSION = "6.8";
    public static String ES_INSTANCE_TYPE = "m5.large.elasticsearch";
    public static int ES_INSTANCE_COUNT = 1;


    public static String readResourceFileContents(@NotNull String fileName, Map<String, String> replacements) throws IOException {

        File file = new File(
                Properties.class.getClassLoader().getResource(fileName).getFile()
        );

        String resourceFile = new String(Files.readAllBytes(file.toPath()));

        if (replacements != null)
            for (Map.Entry<String, String> entry : replacements.entrySet()) {
                resourceFile = resourceFile.replaceAll(entry.getKey(), entry.getValue());
            }
        System.out.println(resourceFile);

        return resourceFile;
    }
}
