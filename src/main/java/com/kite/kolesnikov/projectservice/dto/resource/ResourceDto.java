package com.kite.kolesnikov.projectservice.dto.resource;

import com.kite.kolesnikov.projectservice.entity.resource.ResourceStatus;
import com.kite.kolesnikov.projectservice.entity.resource.ResourceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigInteger;
import java.time.ZonedDateTime;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ResourceDto {
    private String name;
    private BigInteger size;
    private ResourceType type;
    private ResourceStatus status;
    private Long projectId;
    private Long createdById;
    private ZonedDateTime createdAt;
}
