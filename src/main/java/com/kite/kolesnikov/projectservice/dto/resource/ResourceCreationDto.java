package com.kite.kolesnikov.projectservice.dto.resource;

import com.kite.kolesnikov.projectservice.entity.project.Project;
import com.kite.kolesnikov.projectservice.entity.TeamMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResourceCreationDto {
    private MultipartFile multipartFile;
    private Project project;
    private TeamMember teamMember;
    private String fileKey;
}
