package com.example.guice;

import com.example.resources.CampaignResource;
import com.example.resources.InstitutionResource;
import com.example.resources.TransactionResource;
import com.example.resources.UserResource;
import com.example.service.*;
import com.google.inject.AbstractModule;

import javax.inject.Singleton;

/**
 * Guice module for Service bindings
 */
public class ServiceModule extends AbstractModule {

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
}
