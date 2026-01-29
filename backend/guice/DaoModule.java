package com.example.guice;

import com.example.MyDropwizardConfiguration;
import com.example.entity.Campaign;
import com.example.entity.Institution;
import com.example.entity.Transaction;
import com.example.entity.User;
import com.example.utils.DaoUtils;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import io.appform.dropwizard.sharding.DBShardingBundle;
import io.appform.dropwizard.sharding.dao.RelationalDao;

import javax.inject.Singleton;

public class DaoModule extends AbstractModule {
    private DBShardingBundle<MyDropwizardConfiguration> dbShardingBundle;

    public DaoModule(final DBShardingBundle<MyDropwizardConfiguration> dbShardingBundle) {
        this.dbShardingBundle = dbShardingBundle;
    }

    @Provides
    @Singleton
    public RelationalDao<User> provideUserDAO() {
        return DaoUtils.createRelationalDao(dbShardingBundle, User.class);
    }

    @Provides
    @Singleton
    public RelationalDao<Institution> provideInstitutionDAO() {
        return DaoUtils.createRelationalDao(dbShardingBundle, Institution.class);
    }

    @Provides
    @Singleton
    public RelationalDao<Transaction> provideTransactionDAO() {
        return DaoUtils.createRelationalDao(dbShardingBundle, Transaction.class);
    }

    @Provides
    @Singleton
    public RelationalDao<Campaign> provideCampaignDAO() {
        return DaoUtils.createRelationalDao(dbShardingBundle, Campaign.class);
    }

    @Override
    public void configure() {
        //bind(ExpenseStore.class).annotatedWith(Names.named("mockExpenseStore")).to(MockExpenseStore.class).in(Singleton.class);
//        bind(UserStore.class).annotatedWith(Names.named("UserStore")).to(UserStore.class).in(Singleton.class);
//        bind(InstitutionStore.class).annotatedWith(Names.named("InstitutionStore")).to(InstitutionStore.class).in(Singleton.class);
    }
}
