package com.example.resources;

import com.example.entity.Campaign;
import com.example.service.CampaignService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/campaigns")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
@Tag(name="Campaign Management", description="APIs for managing campaigns")
public class CampaignResource {

    private final CampaignService campaignService;

    @GET
    public Response getAllCampaigns(@QueryParam("shardKey") String shardKey) {
        List<Campaign> campaigns = campaignService.getAllCampaigns(shardKey);
        return Response.ok(campaigns).build();
    }

    @GET
    @Path("/{id}")
    public Response getCampaignById(@QueryParam("shardKey") String shardKey,
                                    @PathParam("id") Long id) {
        return campaignService.getCampaignById(shardKey, id)
                .map(campaign -> Response.ok(campaign).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    public Response createCampaign(@QueryParam("shardKey") String shardKey,
                                   Campaign campaign) {
        campaignService.createCampaign(shardKey, campaign);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateCampaign(@QueryParam("shardKey") String shardKey,
                                   @PathParam("id") Long id,
                                   Campaign campaign) {
        campaignService.updateCampaign(shardKey, id, campaign);
        return Response.ok().build();
    }

    @GET
    @Path("/{id}/exists")
    public Response campaignExists(@QueryParam("shardKey") String shardKey,
                                   @PathParam("id") Long id) {
        boolean exists = campaignService.campaignExists(shardKey, id);
        return Response.ok(exists).build();
    }

    @GET
    @Path("/beneficiary/{beneficiaryId}")
    public Response getCampaignsByBeneficiaryId(@QueryParam("shardKey") String shardKey,
                                                @PathParam("beneficiaryId") Long beneficiaryId) {
        List<Campaign> campaigns = campaignService.getCampaignsByBeneficiaryId(shardKey, beneficiaryId);
        return Response.ok(campaigns).build();
    }

    @GET
    @Path("/institution/{institutionId}")
    public Response getCampaignsByInstitutionId(@QueryParam("shardKey") String shardKey,
                                                @PathParam("institutionId") Long institutionId) {
        List<Campaign> campaigns = campaignService.getCampaignsByInstitutionId(shardKey, institutionId);
        return Response.ok(campaigns).build();
    }

    @GET
    @Path("/live")
    public Response getLiveCampaigns(@QueryParam("shardKey") String shardKey) {
        List<Campaign> campaigns = campaignService.getLiveCampaigns(shardKey);
        return Response.ok(campaigns).build();
    }

    @GET
    @Path("/approved")
    public Response getApprovedCampaigns(@QueryParam("shardKey") String shardKey) {
        List<Campaign> campaigns = campaignService.getApprovedCampaigns(shardKey);
        return Response.ok(campaigns).build();
    }

    @GET
    @Path("/fulfilled")
    public Response getFulfilledCampaigns(@QueryParam("shardKey") String shardKey) {
        List<Campaign> campaigns = campaignService.getFulfilledCampaigns(shardKey);
        return Response.ok(campaigns).build();
    }
}
