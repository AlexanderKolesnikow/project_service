package com.kite.kolesnikov.projectservice.util;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.kite.kolesnikov.projectservice.dto.resource.ResourceDto;
import com.kite.kolesnikov.projectservice.dto.resource.UpdateResourceDto;
import com.kite.kolesnikov.projectservice.dto.resource.GetResourceDto;
import com.kite.kolesnikov.projectservice.exception.FileUploadException;
import com.kite.kolesnikov.projectservice.exception.InvalidAccessException;
import com.kite.kolesnikov.projectservice.exception.StorageSpaceExceededException;
import com.kite.kolesnikov.projectservice.jpa.ResourceRepository;
import com.kite.kolesnikov.projectservice.mapper.ResourceMapperImpl;
import com.kite.kolesnikov.projectservice.entity.project.Project;
import com.kite.kolesnikov.projectservice.entity.project.ProjectStatus;
import com.kite.kolesnikov.projectservice.entity.project.ProjectVisibility;
import com.kite.kolesnikov.projectservice.entity.resource.Resource;
import com.kite.kolesnikov.projectservice.entity.resource.ResourceStatus;
import com.kite.kolesnikov.projectservice.entity.resource.ResourceType;
import com.kite.kolesnikov.projectservice.entity.Team;
import com.kite.kolesnikov.projectservice.entity.TeamMember;
import com.kite.kolesnikov.projectservice.entity.TeamRole;
import com.kite.kolesnikov.projectservice.repository.ProjectRepository;
import com.kite.kolesnikov.projectservice.service.ProjectResourceService;
import com.kite.kolesnikov.projectservice.service.TeamMemberService;
import com.kite.kolesnikov.projectservice.validator.ProjectResourceValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class ProjectResourceServiceTest {
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private ResourceRepository resourceRepository;
    @Mock
    private FileService fileService;
    @Mock
    private TeamMemberService teamMemberService;
    @Spy
    private ProjectResourceValidator projectResourceValidator;
    @Spy
    private ResourceMapperImpl resourceMapper;
    @InjectMocks
    private ProjectResourceService projectFileService;

    private Project project;
    private MockMultipartFile multipartFile;
    private Resource resource;
    private TeamMember teamMember;

    @BeforeEach
    void setUp() {
        LocalDateTime createdAt = LocalDateTime.of(2023, 8, 31, 15, 30);

        teamMember = TeamMember.builder()
                .id(1L)
                .userId(1L)
                .roles(new ArrayList<>(List.of(TeamRole.DEVELOPER)))
                .build();

        TeamMember projectManager = TeamMember.builder()
                .id(2L)
                .userId(2L)
                .roles(new ArrayList<>(List.of(TeamRole.MANAGER)))
                .build();

        Team team = Team.builder()
                .id(1L)
                .teamMembers(new ArrayList<>(List.of(teamMember, projectManager)))
                .build();

        project = Project.builder()
                .id(1L)
                .maxStorageSize(BigInteger.valueOf(12L))
                .storageSize(BigInteger.valueOf(12L))
                .status(ProjectStatus.CREATED)
                .visibility(ProjectVisibility.PUBLIC)
                .createdAt(createdAt)
                .teams(new ArrayList<>(List.of(team)))
                .build();

        multipartFile = new MockMultipartFile(
                "testFile", "test.txt", "text/plain", "Test content".getBytes());

        String fileName = multipartFile.getOriginalFilename();
        long size = multipartFile.getSize();
        String key = String.format("p%d_%s_%s", 1L, size, fileName);

        resource = Resource.builder()
                .status(ResourceStatus.ACTIVE)
                .key(key)
                .type(ResourceType.TEXT)
                .size(BigInteger.valueOf(size))
                .project(project)
                .createdBy(teamMember)
                .name(fileName)
                .build();
    }

    @Test
    public void testUploadFile_Successful() {
        ResourceDto expectedDto = ResourceDto.builder()
                .name("test.txt")
                .type(ResourceType.TEXT)
                .size(BigInteger.valueOf(12L))
                .projectId(1L)
                .status(ResourceStatus.ACTIVE)
                .createdById(1L)
                .build();

        Mockito.when(projectRepository.getProjectById(1L)).thenReturn(project);
        Mockito.when(teamMemberService.findByUserIdAndProjectId(Mockito.anyLong(), Mockito.anyLong())).thenReturn(teamMember);

        ResourceDto resourceDto = projectFileService.uploadFile(multipartFile, 1L, 1L);

        assertEquals(expectedDto, resourceDto);
        assertEquals(BigInteger.valueOf(0L), project.getStorageSize());
        Mockito.verify(resourceRepository, Mockito.times(1)).save(any(Resource.class));
    }

    @Test
    public void testUploadFile_StorageExceeded() {
        MockMultipartFile bigFile = new MockMultipartFile(
                "testFile", "test.txt", "text/plain", "Test content!".getBytes());

        Mockito.when(projectRepository.getProjectById(1L)).thenReturn(project);

        assertThrows(StorageSpaceExceededException.class,
                () -> projectFileService.uploadFile(bigFile, 1L, 1L));
    }


    @ParameterizedTest
    @ValueSource(ints = {1, 2})
    public void testUpdateFile_Successful(int userId) {
        project.setStorageSize(BigInteger.valueOf(0L));

        MockMultipartFile multipartFileUpdated = new MockMultipartFile(
                "testFile", "test.txt", "text/plain", "Test".getBytes());

        String fileNameUpdated = multipartFileUpdated.getOriginalFilename();
        long sizeUpdated = multipartFileUpdated.getSize();

        UpdateResourceDto expectedOutput = UpdateResourceDto.builder()
                .status(ResourceStatus.ACTIVE)
                .name(fileNameUpdated)
                .createdById(1L)
                .type(ResourceType.TEXT)
                .size(BigInteger.valueOf(sizeUpdated))
                .projectId(1L)
                .build();

        Mockito.when(resourceRepository.getReferenceById(1L)).thenReturn(resource);

        UpdateResourceDto outputDto = projectFileService.updateFile(multipartFileUpdated, 1L, userId);

        assertEquals(expectedOutput, outputDto);
        assertEquals(BigInteger.valueOf(8L), project.getStorageSize());
        Mockito.verify(fileService, Mockito.times(1)).delete("p1_4_test.txt");
        Mockito.verify(resourceRepository, Mockito.times(1)).save(resource);
    }

    @Test
    public void testUpdateFile_FileNameDoesNotMatch() {
        MockMultipartFile multipartFileUpdated = new MockMultipartFile(
                "testFile", "file.txt", "text/plain", "Test".getBytes());

        Mockito.when(resourceRepository.getReferenceById(1L)).thenReturn(resource);

        assertThrows(FileUploadException.class,
                () -> projectFileService.updateFile(multipartFileUpdated, 1L, 1L));

    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3})
    public void testUpdateFile_UserCantChangeFile(int userId) {
        project.setStorageSize(BigInteger.valueOf(0L));
        MockMultipartFile multipartFileUpdated = new MockMultipartFile(
                "testFile", "test.txt", "text/plain", "Test".getBytes());

        TeamMember createdBy = TeamMember.builder()
                .id((long) userId)
                .userId((long) userId)
                .build();

        resource.setCreatedBy(createdBy);
        Mockito.when(resourceRepository.getReferenceById(1L)).thenReturn(resource);

        assertThrows(InvalidAccessException.class,
                () -> projectFileService.updateFile(multipartFileUpdated, 1L, 1L));
    }

    @Test
    public void testUpdateFile_StorageExceeded() {
        project.setStorageSize(BigInteger.valueOf(0L));
        MockMultipartFile multipartFileUpdated = new MockMultipartFile(
                "testFile", "test.txt", "text/plain", "Test content!".getBytes());

        Mockito.when(resourceRepository.getReferenceById(1L)).thenReturn(resource);

        assertThrows(StorageSpaceExceededException.class,
                () -> projectFileService.updateFile(multipartFileUpdated, 1L, 1L));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2})
    public void testDeleteFile_Successful(int userId) {
        project.setStorageSize(BigInteger.valueOf(0L));
        Mockito.when(resourceRepository.getReferenceById(1L)).thenReturn(resource);

        projectFileService.deleteFile(1L, userId);

        assertEquals(BigInteger.valueOf(12L), project.getStorageSize());
        assertEquals(ResourceStatus.DELETED, resource.getStatus());
        Mockito.verify(resourceRepository, Mockito.times(1)).save(any(Resource.class));
        Mockito.verify(projectRepository, Mockito.times(1)).save(any(Project.class));
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3})
    public void testDeleteFile_UserCantDeleteFile(int userId) {
        TeamMember createdBy = TeamMember.builder()
                .id((long) userId)
                .userId((long) userId)
                .build();

        resource.setCreatedBy(createdBy);
        Mockito.when(resourceRepository.getReferenceById(1L)).thenReturn(resource);

        assertThrows(InvalidAccessException.class,
                () -> projectFileService.deleteFile(1L, 1L));
    }

    @Test
    public void testGetFile_Successful() throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(multipartFile.getContentType());

        S3Object s3Object = new S3Object();
        s3Object.setObjectMetadata(metadata);
        s3Object.setObjectContent(new ByteArrayInputStream(multipartFile.getBytes()));

        Mockito.when(resourceRepository.getReferenceById(1L)).thenReturn(resource);

        Mockito.when(fileService.getFile(resource.getKey())).thenReturn(s3Object);

        GetResourceDto result = projectFileService.getFile(1L, 1L);

        assertEquals("test.txt", result.getName());
        assertEquals("text/plain", result.getType());
        assertEquals(12L, result.getSize());

        Mockito.verify(resourceRepository, Mockito.times(1)).getReferenceById(1L);
        Mockito.verify(fileService, Mockito.times(1)).getFile(resource.getKey());
    }
}
