package com.example.resources;

import com.example.entity.Campaign;
import com.example.service.CampaignService;
import com.example.service.DosctoreService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Path("/campaigns")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
@Tag(name="Campaign Management", description="APIs for managing campaigns")
@Slf4j
public class CampaignResource {

    private final CampaignService campaignService;
    private final DosctoreService dosctoreService;

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

    @POST
    @Path("/{id}/upload-report")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadReportFile(@QueryParam("shardKey") String shardKey,
                                     @PathParam("id") Long id,
                                     @FormDataParam("file") InputStream fileInputStream,
                                     @FormDataParam("file") FormDataContentDisposition fileMetaData) {
        try {
            // Verify campaign exists
            if (!campaignService.campaignExists(shardKey, id)) {
                throw new WebApplicationException("Campaign not found", Response.Status.NOT_FOUND);
            }

            // Create a temporary file
            File tempFile = File.createTempFile("report_", "_" + fileMetaData.getFileName());
            Files.copy(fileInputStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            // Determine content type
            String contentType = Files.probeContentType(tempFile.toPath());
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            // Upload to Docstore
            String fileId = dosctoreService.uploadFile(tempFile, fileMetaData.getFileName(), contentType);

            // Update campaign with file ID using dedicated method
            campaignService.updateReportFileId(shardKey, id, fileId);

            // Clean up temp file
            tempFile.delete();

            return Response.ok()
                    .entity(new FileUploadResponse(fileId, "File uploaded successfully"))
                    .build();
        } catch (Exception e) {
            log.error("Failed to upload report file for campaign: {}", id, e);
            throw new WebApplicationException("Failed to upload report file", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    // Response class for file upload
    @lombok.Data
    @lombok.AllArgsConstructor
    @lombok.NoArgsConstructor
    private static class FileUploadResponse {
        private String fileId;
        private String message;
    }
}
