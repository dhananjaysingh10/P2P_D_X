package com.example.guice;

import com.codahale.metrics.MetricRegistry;
import com.example.MyDropwizardConfiguration;
import com.example.bindings.InjectableNameBindings;
import com.example.resources.CampaignResource;
import com.example.resources.InstitutionResource;
import com.example.resources.TransactionResource;
import com.example.resources.UserResource;
import com.example.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.phonepe.olympus.im.bundle.OlympusIMBundle;
import com.phonepe.platform.docstore.client.DocstoreClient;
import com.phonepe.platform.docstore.client.impl.HttpDocstoreClient;
import com.phonepe.platform.http.v2.common.HttpConfiguration;
import com.phonepe.platform.http.v2.discovery.ServiceEndpointProviderFactory;

import javax.inject.Named;
import javax.inject.Singleton;
import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Guice module for Service bindings
 */
public class ServiceModule extends AbstractModule {

    private final OlympusIMBundle<MyDropwizardConfiguration> olympusIMBundle;

    public ServiceModule(OlympusIMBundle<MyDropwizardConfiguration> olympusIMBundle) {
        this.olympusIMBundle = olympusIMBundle;
    }

    @Override
    protected void configure() {
        // Bind Services
        bind(UserService.class).to(UserServiceImpl.class).in(Singleton.class);
        bind(InstitutionService.class).to(InstitutionServiceImpl.class).in(Singleton.class);
        bind(TransactionService.class).to(TransactionServiceImpl.class).in(Singleton.class);
        bind(CampaignService.class).to(CampaignServiceImpl.class).in(Singleton.class);

        // Bind Resources
        bind(UserResource.class).in(Singleton.class);
        bind(InstitutionResource.class).in(Singleton.class);
        bind(TransactionResource.class).in(Singleton.class);
        bind(CampaignResource.class).in(Singleton.class);
    }

//    @Provides
//    @Singleton
//    @Named(InjectableNameBindings.DOCSTORE_HTTP_CONFIG)
//    public HttpConfiguration provideDocstoreHttpConfiguration(MyDropwizardConfiguration configuration) {
//        // Assuming you have a docstore config in your MyDropwizardConfiguration
//        // Adjust the configuration access based on your actual config structure
//        return HttpConfiguration.builder()
//                .build();
//    }
//
//
//    @Provides
//    @com.google.inject.Singleton
//    public DocstoreClient provideHttpDocstoreClient(
//            @Named(InjectableNameBindings.DOCSTORE_HTTP_CONFIG) final HttpConfiguration httpConfiguration,
//            final MetricRegistry metricRegistry,
//            final ObjectMapper objectMapper,
//            final ServiceEndpointProviderFactory serviceEndpointProviderFactory)
//            throws GeneralSecurityException, IOException {
//        return new HttpDocstoreClient(objectMapper,
//                () -> olympusIMBundle.getOlympusIMClient().getSystemAuthHeader(), httpConfiguration,
//                metricRegistry, serviceEndpointProviderFactory, false);
//    }
}
