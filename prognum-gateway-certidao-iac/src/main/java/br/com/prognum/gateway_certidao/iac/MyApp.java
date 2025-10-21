package br.com.prognum.gateway_certidao.iac;

import software.amazon.awscdk.App;
import software.amazon.awscdk.StackProps;

public class MyApp extends App{
    public static void main(final String[] args) {
    	MyApp app = new MyApp();

        String environment = (String) app.getNode().tryGetContext("environment");
        if (environment == null) {
            throw new IllegalArgumentException("Ambiente não foi passado no deploy");
        }

        Config config = Config.create(app);

        new MyStack(app, config, StackProps.builder().build());

        app.synth();
    }
}

