package com.example.MyBookShopApp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ContactConfirmationResponse {
    @JsonProperty("result")
    private Boolean result;

    public Boolean getResult(){
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }
}
