package com.example;

import com.example.entity.Transaction;
import com.example.guice.DaoModule;
import com.example.guice.InjectionFactory;
import com.example.guice.ServiceModule;
import com.example.resources.CampaignResource;
import com.example.resources.InstitutionResource;
import com.example.resources.TransactionResource;
import com.example.resources.UserResource;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import in.vectorpro.dropwizard.swagger.SwaggerBundle;
import in.vectorpro.dropwizard.swagger.SwaggerBundleConfiguration;
import io.appform.dropwizard.sharding.DBShardingBundle;
import io.appform.dropwizard.sharding.config.ShardedHibernateFactory;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

public class MyDropwizardApplication extends Application<MyDropwizardConfiguration> {

    private DBShardingBundle<MyDropwizardConfiguration> shardingBundle;

    public static void main(final String[] args) throws Exception {
        new MyDropwizardApplication().run(args);
    }

    @Override
    public String getName() {
        return "MyDropwizard";
    }

    @Override
    public void initialize(final Bootstrap<MyDropwizardConfiguration> bootstrap) {
        // TODO: application initialization
        this.shardingBundle = getDbShardingBundle();
        bootstrap.addBundle(shardingBundle);
        bootstrap.addBundle(swaggerBundle());
    }

    @Override
    public void run(final MyDropwizardConfiguration configuration,
                    final Environment environment) {
        // Configure Jackson ObjectMapper
        environment.getObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        environment.getObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
        environment.getObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        environment.getObjectMapper().setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        environment.getObjectMapper().enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES);
        environment.getObjectMapper().registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));

        // Configure CORS to allow all incoming traffic
        configureCors(environment);

        // Initialize Guice modules
        InjectionFactory.init(new DaoModule(shardingBundle), new ServiceModule());

        // Register Resources
        environment.jersey().register(InjectionFactory.getInstance(UserResource.class));
        environment.jersey().register(InjectionFactory.getInstance(InstitutionResource.class));
        environment.jersey().register(InjectionFactory.getInstance(TransactionResource.class));
        environment.jersey().register(InjectionFactory.getInstance(CampaignResource.class));
    }

    /**
     * Configure CORS filter to allow all incoming traffic from any origin
     */
    private void configureCors(Environment environment) {
        FilterRegistration.Dynamic cors = environment.servlets().addFilter("CORS", CrossOriginFilter.class);

        // Configure CORS parameters
        cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin,Authorization");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "OPTIONS,GET,PUT,POST,DELETE,HEAD,PATCH");
        cors.setInitParameter(CrossOriginFilter.ALLOW_CREDENTIALS_PARAM, "true");

        // Add URL mapping
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
    }

    private DBShardingBundle<MyDropwizardConfiguration> getDbShardingBundle() {
        return new DBShardingBundle<MyDropwizardConfiguration>("com.example") {
            @Override
            protected ShardedHibernateFactory getConfig(MyDropwizardConfiguration config) {
                return config.getShards();
            }
        };
    }

    SwaggerBundle<MyDropwizardConfiguration> swaggerBundle() {
        return new SwaggerBundle<MyDropwizardConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(MyDropwizardConfiguration configuration) {
                return configuration.getSwagger();
            }
        };
    }


}
