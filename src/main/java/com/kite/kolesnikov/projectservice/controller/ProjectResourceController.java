package com.kite.kolesnikov.projectservice.controller;

import com.kite.kolesnikov.projectservice.config.context.UserContext;
import com.kite.kolesnikov.projectservice.dto.resource.GetResourceDto;
import com.kite.kolesnikov.projectservice.dto.resource.ResourceDto;
import com.kite.kolesnikov.projectservice.dto.resource.UpdateResourceDto;
import com.kite.kolesnikov.projectservice.service.ProjectResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
@Tag(name = "Project Resource controller")
public class ProjectResourceController {
    private final ProjectResourceService projectFileService;
    private final UserContext userContext;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Upload a file",
            description = "Uploads a file to the cloud object storage and saves metadata to the database.")
    public ResourceDto uploadFile(@RequestParam("file") MultipartFile multipartFile,
                                  @RequestParam long projectId) {
        long userId = userContext.getUserId();

        return projectFileService.uploadFile(multipartFile, projectId, userId);
    }

    @PutMapping("/{resourceId}")
    @Operation(
            summary = "Update a file",
            description = "Updates a file in the cloud object storage and refreshes the metadata in the database.")
    public UpdateResourceDto updateFile(@RequestParam("file") MultipartFile multipartFile,
                                        @PathVariable long resourceId) {
        long userId = userContext.getUserId();

        return projectFileService.updateFile(multipartFile, resourceId, userId);
    }

    @GetMapping("/{resourceId}/download/")
    @Operation(
            summary = "Download a file",
            description = "Downloads a file from the cloud object storage. File metadata is retrieved from the database.",
            responses = {
                    @ApiResponse(
                            description = "The file is successfully retrieved and downloaded.",
                            content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE)
                    ),
                    @ApiResponse(responseCode = "404", description = "The file does not exist.")
            }
    )
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable long resourceId) {
        long userId = userContext.getUserId();
        GetResourceDto resourceDto = projectFileService.getFile(resourceId, userId);
        InputStreamResource inputStreamResource = new InputStreamResource(resourceDto.getInputStream());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resourceDto.getName() + "\"");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resourceDto.getSize())
                .contentType(MediaType.parseMediaType(resourceDto.getType()))
                .body(inputStreamResource);
    }

    @DeleteMapping("/{resourceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a file",
            description = "Deletes a file from the storage and its metadata from the database.")
    public void deleteFile(@PathVariable long resourceId) {
        long userId = userContext.getUserId();

        projectFileService.deleteFile(resourceId, userId);
    }
}
