package org.letuslearn.springkubedemo.controller.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StatusDto {

    private String status;
    private String message;
}
