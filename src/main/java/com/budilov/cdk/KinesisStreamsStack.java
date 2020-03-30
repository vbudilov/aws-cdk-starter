package com.budilov.cdk;

import com.budilov.cdk.util.Properties;
import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.kinesis.Stream;

public class KinesisStreamsStack extends Stack {
    public KinesisStreamsStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    final public Stream myStream;

    public KinesisStreamsStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        myStream = Stream.Builder.create(this, Properties.KINESIS_STREAM_NAME)
                .streamName(Properties.KINESIS_STREAM_NAME)
                .shardCount(Properties.KINESIS_STREAM_SHARD_COUNT)
                .build();

    }

}
