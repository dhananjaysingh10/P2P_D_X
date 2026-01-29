package com.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.phonepe.olympus.im.bundle.config.OlympusIMBundleConfig;
import com.phonepe.platform.http.v2.common.HttpConfiguration;
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

    @javax.validation.Valid
    @NotNull
    private OlympusIMBundleConfig olympusIMClientConfig;

    @JsonProperty("docstore")
    private HttpConfiguration docstoreHttpConfig;

}
