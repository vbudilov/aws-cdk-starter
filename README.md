# Collection of useful CDK Stacks

#### Why
I create apps and deployments all of the time. I use different tools for different scenarios. Sometimes I use the Serverless framework
to deploy Lambda functions, sometimes SAM. In some cases I used Terraform to create other resources, and it others I just did it manually. 
This project is going to help me (and hopefully you) to automate some of those resource creations.

The idea is to build out an app (details will come later) that facilitates data ingestion using Kinesis, EKS, DDB, and other 
popular AWS services.

#### What does it do now
- [x] Cognito Stack
- [x] Cognito Stack Lambda Triggers
- [x] DynamoDB Users & Ticker Table
- [x] ElasticSearch service
- [x] IAM role to access Elasticsearch
- [x] Kinesis Stream
- [ ] S3 Bucket (for SPA hosting)
- [ ] SPA CodePipeline
- [ ] EKS (maybe...using eksctl for now)
- [ ] Saving endpoints into the Parameter Store (for discovery)

#### Deployment
```
mvn package
cdk synth
cdk deploy
```

###### Useful commands

 * `mvn package`     compile and run tests
 * `cdk ls`          list all stacks in the app
 * `cdk synth`       emits the synthesized CloudFormation template
 * `cdk deploy`      deploy this stack to your default AWS account/region
 * `cdk diff`        compare deployed stack with current state
 * `cdk docs`        open CDK documentation
