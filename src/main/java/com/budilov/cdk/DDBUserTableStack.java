package com.budilov.cdk;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.dynamodb.Attribute;
import software.amazon.awscdk.services.dynamodb.AttributeType;
import software.amazon.awscdk.services.dynamodb.Table;
import software.amazon.awscdk.services.dynamodb.TableProps;

public class DDBUserTableStack extends Stack {
    public DDBUserTableStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    Table table = null;

    public DDBUserTableStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        table = new Table(this, Properties.DDB_USERS_TABLE, TableProps.builder()
                .partitionKey(Attribute.builder()
                        .name(Properties.DDB_USERS_TABLE_PARTITION_ID)
                        .type(AttributeType.STRING)
                        .build())
                .sortKey(Attribute.builder()
                        .name(Properties.DDB_USERS_TABLE_SORT_KEY)
                        .type(AttributeType.STRING)
                        .build())
                .build());


    }

    public String getTableName() {
        if (table != null)
            return table.getTableName();
        else
            return "UNKNOWN";//todo: better to throw an exception here
    }
}
