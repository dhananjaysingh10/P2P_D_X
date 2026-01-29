package com.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import in.vectorpro.dropwizard.swagger.SwaggerBundle;
import in.vectorpro.dropwizard.swagger.SwaggerBundleConfiguration;
import io.appform.dropwizard.sharding.config.ShardedHibernateFactory;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.hibernate.validator.constraints.*;
import jakarta.validation.constraints.*;

import javax.validation.constraints.NotNull;

@Data
@FieldNameConstants
@JsonIgnoreProperties(ignoreUnknown = true)
public class MyDropwizardConfiguration extends Configuration {

    @javax.validation.Valid
    @NotNull
    private SwaggerBundleConfiguration swagger;

    // TODO: implement service configuration
    @Valid
    @NotNull
    private ShardedHibernateFactory shards;
}
