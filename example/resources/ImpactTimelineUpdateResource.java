package com.example.resources;

import com.example.entity.ImpactTimelineUpdate;
import com.example.service.ImpactTimelineUpdateService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Tag(name="Impact Timeline Update Resource", description = "APIs for campaign impact timeline updates")
@Path("/campaigns/{campaignId}/impact-updates")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class ImpactTimelineUpdateResource {
    private final ImpactTimelineUpdateService service;

    @GET
    public Response getUpdates(@QueryParam("shardKey") String shardKey,
                              @PathParam("campaignId") Long campaignId) {
        List<ImpactTimelineUpdate> updates = service.getUpdatesForCampaign(shardKey, campaignId);
        return Response.ok(updates).build();
    }

    @POST
    public Response addUpdate(@QueryParam("shardKey") String shardKey,
                              @PathParam("campaignId") Long campaignId,
                              ImpactTimelineUpdate update) {
        update.setCampaignId(campaignId);
        service.addUpdate(shardKey, update);
        return Response.status(Response.Status.CREATED).build();
    }
}
