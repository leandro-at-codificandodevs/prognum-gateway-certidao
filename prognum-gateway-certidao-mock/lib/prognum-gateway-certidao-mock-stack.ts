import { Construct } from 'constructs';
import { CfnOutput, Duration, Stack, StackProps } from 'aws-cdk-lib';
import { FunctionUrlAuthType, Runtime } from 'aws-cdk-lib/aws-lambda';
import { NodejsFunction } from 'aws-cdk-lib/aws-lambda-nodejs';

const LAMBDA_TIMEOUT_SECS = 30;
const LAMBDA_MEMORY_SIZE = 128;

export class PrognumGatewayCertidaoMockStack extends Stack {
  constructor(scope: Construct, id: string, props?: StackProps) {
    super(scope, id, props);
    const lambdaName = "prognum-gateway-certidao-mock-api-lambda";
    const lambda = new NodejsFunction(this, lambdaName, {
      functionName: lambdaName,
      entry: "lambda.ts",
      runtime: Runtime.NODEJS_22_X,
      timeout: Duration.seconds(LAMBDA_TIMEOUT_SECS),
      memorySize: LAMBDA_MEMORY_SIZE,
      bundling: { minify: true, sourceMap: true },
      environment: {
        NODE_OPTIONS: "--enable-source-maps",
      }
    });
    const functionUrl = lambda.addFunctionUrl({
      authType: FunctionUrlAuthType.NONE,
    });

    const outputId = "prognum-gateway-certidao-mock-api-url-output";
    new CfnOutput(this, outputId, {
      exportName: outputId,
      value:  functionUrl.url,
    });
  }
}
