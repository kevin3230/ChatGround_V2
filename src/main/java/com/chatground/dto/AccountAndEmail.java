package com.chatground.dto;

import lombok.Data;

@Data
public class AccountAndEmail {
    private boolean isUnique;
    private boolean isError = false;
    private String message;
}
