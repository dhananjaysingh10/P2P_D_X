package com.example.utils;

import io.appform.dropwizard.sharding.DBShardingBundle;
import io.appform.dropwizard.sharding.dao.RelationalDao;
import io.appform.ranger.discovery.bundle.id.IdGenerator;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DaoUtils {

    public <T> RelationalDao<T> createRelationalDao(DBShardingBundle<?> bundle, Class<T> tClass) {
        RelationalDao<T> dao = bundle.createRelatedObjectDao(tClass);
        IdGenerator.registerDomainSpecificConstraints(tClass.getName(), id -> dao.getShardCalculator().isOnValidShard(id.getId()));
        return dao;
    }
}
