package com.example.resources;

import com.example.entity.Transaction;
import com.example.service.TransactionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/transactions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
@Tag(name="Transaction Resource", description="APIs for managing transactions")
public class TransactionResource {

    private final TransactionService transactionService;

    @GET
    public Response getAllTransactions(@QueryParam("shardKey") String shardKey) {
        List<Transaction> transactions = transactionService.getAllTransactions(shardKey);
        return Response.ok(transactions).build();
    }

    @GET
    @Path("/{transactionId}")
    public Response getTransactionById(@QueryParam("shardKey") String shardKey,
                                       @PathParam("transactionId") String transactionId) {
        return transactionService.getTransactionById(shardKey, transactionId)
                .map(transaction -> Response.ok(transaction).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    public Response createTransaction(@QueryParam("shardKey") String shardKey,
                                      Transaction transaction) {
        transactionService.createTransaction(shardKey, transaction);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/{transactionId}")
    public Response updateTransaction(@QueryParam("shardKey") String shardKey,
                                      @PathParam("transactionId") String transactionId,
                                      Transaction transaction) {
        transactionService.updateTransaction(shardKey, transactionId, transaction);
        return Response.ok().build();
    }

    @GET
    @Path("/{transactionId}/exists")
    public Response transactionExists(@QueryParam("shardKey") String shardKey,
                                      @PathParam("transactionId") String transactionId) {
        boolean exists = transactionService.transactionExists(shardKey, transactionId);
        return Response.ok(exists).build();
    }

    @GET
    @Path("/donor/{donorId}")
    public Response getTransactionsByDonorId(@QueryParam("shardKey") String shardKey,
                                             @PathParam("donorId") Long donorId) {
        List<Transaction> transactions = transactionService.getTransactionsByDonorId(shardKey, donorId);
        return Response.ok(transactions).build();
    }

    @GET
    @Path("/campaign/{campaignId}")
    public Response getTransactionsByCampaignId(@QueryParam("shardKey") String shardKey,
                                                @PathParam("campaignId") Long campaignId) {
        List<Transaction> transactions = transactionService.getTransactionsByCampaignId(shardKey, campaignId);
        return Response.ok(transactions).build();
    }

    @GET
    @Path("/status/{status}")
    public Response getTransactionsByStatus(@QueryParam("shardKey") String shardKey,
                                            @PathParam("status") String status) {
        List<Transaction> transactions = transactionService.getTransactionsByStatus(shardKey, status);
        return Response.ok(transactions).build();
    }
}
