package com.budilov.cdk;

import com.budilov.cdk.util.Properties;
import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.kinesis.Stream;
import software.amazon.awscdk.services.ssm.ParameterTier;
import software.amazon.awscdk.services.ssm.StringParameter;

public class KinesisStreamsStack extends Stack {
    final public Stream tickerStream;

    public KinesisStreamsStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public KinesisStreamsStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        tickerStream = Stream.Builder.create(this, "TickerStream")
                .streamName(Properties.KINESIS_STREAM_NAME)
                .shardCount(Properties.KINESIS_STREAM_SHARD_COUNT)
                .build();

        StringParameter.Builder.create(this, "tickerStream")
                .allowedPattern(".*")
                .description("tickerStream")
                .parameterName("tickerStream")
                .stringValue(tickerStream.getStreamName())
                .tier(ParameterTier.STANDARD)
                .build();

    }

}
