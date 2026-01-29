package com.example.service;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.platform.docstore.client.impl.HttpDocstoreClient;
import com.phonepe.platform.docstore.client.models.SearchV2Meta;
import com.phonepe.platform.docstore.model.request.documents.upload.FileUploadRequest;
import com.phonepe.platform.docstore.model.request.documents.upload.context.fileupload.InternalFileUploadContext;
import com.phonepe.platform.docstore.model.response.DocSearchResult;
import com.phonepe.platform.docstore.model.response.DocStoreUploadResponse;
import com.phonepe.platform.http.v2.common.HttpConfiguration;
import com.phonepe.platform.http.v2.common.StaticServiceEndpointProvider;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Singleton
public class DosctoreService {
    private static final int CHUNK_SIZE = 1024 * 1024;
    private static final String TOKEN = "O-Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJpZGVudGl0eU1hbmFnZXIiLCJ2ZXJzaW9uIjoiNC4wIiwidGlkIjoiZjI1NTBkZmEtYzEwMS00OWQyLTgyZGYtY2NhN2I4N2MwMDk4Iiwic2lkIjoiZjU1Y2Y5NTItOWMzMi00Mjc3LWI0NTctNThkNjkxYzUwOGM1IiwiaWF0IjoxNzY5NjY3MTM1LCJleHAiOjE3NzAyNzE5MzV9.SN7qDUcBX381DFO6XXu8memz61z1tRHh5cMMoc12twAlFFRnvPynXv6NHNGlc-kT5fZ4mc4J6PJ0YRjFxi7jZA";
    private static final String NAMESPACE = "test";

    private final HttpDocstoreClient client;
    private final ObjectMapper objectMapper;
    private final MetricRegistry metricRegistry;

    @Inject
    public DosctoreService(ObjectMapper objectMapper, MetricRegistry metricRegistry) throws GeneralSecurityException, IOException {
        this.objectMapper = objectMapper;
        this.metricRegistry = metricRegistry;
        HttpConfiguration httpConfig = getNixyConfig();
        this.client = new HttpDocstoreClient(objectMapper, () -> TOKEN, httpConfig,
                metricRegistry, () -> new StaticServiceEndpointProvider(httpConfig), false);
    }

    private static HttpConfiguration getNixyConfig() {
        String host = "docstore.nixy.stg-drove.phonepe.nb6";
        return HttpConfiguration.builder()
                .host(host)
                .port(80)
                .secure(false)
                .connectTimeoutMs(600_000)
                .idleTimeOutSeconds(6000)
                .opTimeoutMs(600_000)
                .refreshTimeInMs(600_000)
                .build();
    }

    /**
     * Upload a file to Docstore and return the file ID
     *
     * @param file        The file to upload
     * @param fileName    The name of the file
     * @param contentType The content type of the file (e.g., "text/plain", "application/pdf")
     * @return The file ID from Docstore
     */
    /**
     * Upload a file to Docstore and return the file ID
     *
     * @param file        The file to upload
     * @param fileName    The name of the file
     * @param contentType The content type of the file (e.g., "text/plain", "application/pdf")
     * @return The file ID from Docstore
     */
    public String uploadFile(File file, String fileName, String contentType) {
        try {
            log.info("Uploading file: {} with content type: {}", fileName, contentType);

            DocStoreUploadResponse response = client.uploadFileV2(
                    fileName,
                    java.nio.file.Files.readAllBytes(file.toPath()),
                    contentType,
                    FileUploadRequest.builder()
                            .namespace(NAMESPACE)
                            .fileUploadContext(new InternalFileUploadContext())
                            .build(),
                    NAMESPACE);

            log.info("File uploaded successfully. File ID: {}", response.getContext().getId());
            return response.getContext().getId();
        } catch (Exception e) {
            log.error("Failed to upload file: {}", fileName, e);
            throw new RuntimeException("Failed to upload file to Docstore", e);
        }
    }



    /**
     * Download a file from Docstore by file ID and save it to the specified path
     *
     * @param fileId     The Docstore file ID
     * @param outputPath The path where the file should be saved
     * @return true if download was successful
     */
    public boolean downloadFile(String fileId, Path outputPath) {
        try {
            log.info("Downloading file with ID: {} to path: {}", fileId, outputPath);
            boolean isDone = client.getStreamingFile(fileId, outputPath);
            log.info("File downloaded successfully: {}", isDone);
            return isDone;
        } catch (Exception e) {
            log.error("Failed to download file with ID: {}", fileId, e);
            throw new RuntimeException("Failed to download file from Docstore", e);
        }
    }

    /**
     * Get file metadata from Docstore
     *
     * @param fileId The Docstore file ID
     * @return The file name
     */
    public String getFileName(String fileId) {
        try {
            return client.getFileMeta(fileId).getFileName();
        } catch (Exception e) {
            log.error("Failed to get file metadata for ID: {}", fileId, e);
            throw new RuntimeException("Failed to get file metadata from Docstore", e);
        }
    }

    /**
     * Search for files in Docstore
     *
     * @param searchQuery The query to search for
     * @return List of matching files
     */
    public List<DocSearchResult> searchFiles(String searchQuery) {
        try {
            log.info("Searching for files with query: {}", searchQuery);
            List<DocSearchResult> files = client.searchV2(NAMESPACE, SearchV2Meta.builder().size(100).build());
            return files.stream()
                    .filter(metadata -> metadata.getFileName().contains(searchQuery))
                    .toList();
        } catch (Exception e) {
            log.error("Failed to search files with query: {}", searchQuery, e);
            throw new RuntimeException("Failed to search files in Docstore", e);
        }
    }
}

