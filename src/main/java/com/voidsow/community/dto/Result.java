package com.voidsow.community.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Result {
    int code;
    String message;
    Object data;
}
