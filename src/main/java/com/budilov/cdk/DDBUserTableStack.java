package com.budilov.cdk;

import com.budilov.cdk.util.Properties;
import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.dynamodb.Attribute;
import software.amazon.awscdk.services.dynamodb.AttributeType;
import software.amazon.awscdk.services.dynamodb.Table;
import software.amazon.awscdk.services.dynamodb.TableProps;

public class DDBUserTableStack extends Stack {
    final public Table usersTable;
    final public Table tickerTable;

    public DDBUserTableStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public DDBUserTableStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        usersTable = new Table(this, Properties.DDB_USERS_TABLE, TableProps.builder()
                .partitionKey(Attribute.builder()
                        .name(Properties.DDB_USERS_TABLE_PARTITION_ID)
                        .type(AttributeType.STRING)
                        .build())
                .sortKey(Attribute.builder()
                        .name(Properties.DDB_USERS_TABLE_SORT_KEY)
                        .type(AttributeType.STRING)
                        .build())
                .build());

        tickerTable = new Table(this, Properties.DDB_TICKERS_TABLE, TableProps.builder()
                .partitionKey(Attribute.builder()
                        .name(Properties.DDB_TICKERS_TABLE_PARTITION_ID)
                        .type(AttributeType.STRING)
                        .build())
                .sortKey(Attribute.builder()
                        .name(Properties.DDB_TICKERS_TABLE_SORT_KEY)
                        .type(AttributeType.STRING)
                        .build())
                .build());
    }

}
