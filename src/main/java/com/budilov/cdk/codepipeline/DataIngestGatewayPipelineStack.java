package com.budilov.cdk.codepipeline;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.SecretValue;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.codepipeline.Artifact;
import software.amazon.awscdk.services.codepipeline.IStage;
import software.amazon.awscdk.services.codepipeline.Pipeline;
import software.amazon.awscdk.services.codepipeline.StageOptions;
import software.amazon.awscdk.services.codepipeline.actions.GitHubSourceAction;
import software.amazon.awscdk.services.codepipeline.actions.GitHubTrigger;
import software.amazon.awscdk.services.elasticsearch.CfnDomain;

import java.io.IOException;
import java.util.List;

public class DataIngestGatewayPipelineStack extends Stack {

    public CfnDomain esDomain;

    public DataIngestGatewayPipelineStack(final Construct scope, final String id) throws IOException {
        this(scope, id, null);
    }

    public DataIngestGatewayPipelineStack(final Construct scope, final String id, final StackProps props) throws IOException {
        super(scope, id, props);

        // DataIngestGateway Pipeline
        Pipeline pipeline = Pipeline.Builder.create(this, "DataIngestGatewayPipeline")
                .pipelineName("DataIngestGatewayPipeline")
                .build();

        Artifact sourceOutput = new Artifact();
        GitHubSourceAction sourceAction = GitHubSourceAction.Builder.create()
                .actionName("GitHub_Source")
                .owner("vbudilov")
                .repo("data-ingest-gateway")
                .oauthToken(SecretValue.ssmSecure("githubToken", "1"))
                .output(sourceOutput)
                .branch("master")
                .trigger(GitHubTrigger.POLL)
                .build();

        IStage githubSource = pipeline.addStage(StageOptions.builder()
                .stageName("Source")
                .actions(List.of(sourceAction))
                .build());

//        pipeline.addStage(StageOptions.builder()
//                        .stageName("Deploy")
//                        .actions(List.of(EcsDeployAction.Builder.create()
//                                .actionName("DeployAction")
//                                .service(EcsStack.albService.getService())
//                                // if your file is called imagedefinitions.json,
//                                // use the `input` property,
//                                // and leave out the `imageFile` property
//                                .input(buildOutput)
//                                // if your file name is _not_ imagedefinitions.json,
//                                // use the `imageFile` property,
//                                // and leave out the `input` property
//                                .imageFile(buildOutput.atPath("imageDef.json"))
//                                .build()))
//                        .build();


    }

}
