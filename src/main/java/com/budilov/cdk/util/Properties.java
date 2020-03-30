package com.budilov.cdk.util;

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

}
