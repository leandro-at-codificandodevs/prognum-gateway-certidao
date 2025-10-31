#!/usr/bin/env node
import * as cdk from 'aws-cdk-lib/core';
import { PrognumGatewayCertidaoMockStack } from '../lib/prognum-gateway-certidao-mock-stack';

const app = new cdk.App();
new PrognumGatewayCertidaoMockStack(app, 'prognum-gateway-certidao-mock-DEV-stack', {});
