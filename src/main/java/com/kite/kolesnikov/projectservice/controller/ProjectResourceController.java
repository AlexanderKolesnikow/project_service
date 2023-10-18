package com.kite.kolesnikov.projectservice.controller;

import com.kite.kolesnikov.projectservice.config.context.UserContext;
import com.kite.kolesnikov.projectservice.dto.resource.GetResourceDto;
import com.kite.kolesnikov.projectservice.dto.resource.ResourceDto;
import com.kite.kolesnikov.projectservice.dto.resource.UpdateResourceDto;
import com.kite.kolesnikov.projectservice.service.ProjectResourceService;
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
public class ProjectResourceController {
    private final ProjectResourceService projectFileService;
    private final UserContext userContext;

    @PostMapping()
    public ResourceDto uploadFile(@RequestParam("file") MultipartFile multipartFile,
                                  @RequestParam long projectId) {
        long userId = userContext.getUserId();

        return projectFileService.uploadFile(multipartFile, projectId, userId);
    }

    @PutMapping("/{resourceId}")
    public UpdateResourceDto updateFile(@RequestParam("file") MultipartFile multipartFile,
                                        @PathVariable long resourceId) {
        long userId = userContext.getUserId();

        return projectFileService.updateFile(multipartFile, resourceId, userId);
    }

    @GetMapping("/{resourceId}/download/")
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
    public void deleteFile(@PathVariable long resourceId) {
        long userId = userContext.getUserId();

        projectFileService.deleteFile(resourceId, userId);
    }
}
