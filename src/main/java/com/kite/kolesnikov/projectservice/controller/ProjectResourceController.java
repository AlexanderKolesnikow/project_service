package com.kite.kolesnikov.projectservice.controller;

import com.kite.kolesnikov.projectservice.dto.resource.GetResourceDto;
import com.kite.kolesnikov.projectservice.dto.resource.ResourceDto;
import com.kite.kolesnikov.projectservice.dto.resource.UpdateResourceDto;
import com.kite.kolesnikov.projectservice.service.ProjectResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
@Tag(name = "Project Resource controller")
public class ProjectResourceController {
    private final ProjectResourceService projectFileService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Upload a file",
            description = "Uploads a file to the cloud object storage and saves metadata to the database.")
    public ResourceDto uploadFile(@RequestParam("file") MultipartFile multipartFile,
                                  @RequestParam long projectId,
                                  @Parameter(description = "User ID", required = true, in = ParameterIn.HEADER)
                                  @RequestHeader("x-user-id") Long userId) {

        return projectFileService.uploadFile(multipartFile, projectId, userId);
    }

    @PutMapping("/{resourceId}")
    @Operation(
            summary = "Update a file",
            description = "Updates a file in the cloud object storage and refreshes the metadata in the database.")
    public UpdateResourceDto updateFile(@RequestParam("file") MultipartFile multipartFile,
                                        @PathVariable long resourceId,
                                        @Parameter(description = "User ID", required = true, in = ParameterIn.HEADER)
                                        @RequestHeader("x-user-id") Long userId) {

        return projectFileService.updateFile(multipartFile, resourceId, userId);
    }

    @GetMapping("/{resourceId}/download")
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
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable long resourceId,
                                                            @Parameter(description = "User ID", required = true, in = ParameterIn.HEADER)
                                                            @RequestHeader("x-user-id") Long userId) {

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
    public void deleteFile(@PathVariable long resourceId,
                           @Parameter(description = "User ID", required = true, in = ParameterIn.HEADER)
                           @RequestHeader("x-user-id") Long userId) {

        projectFileService.deleteFile(resourceId, userId);
    }
}
