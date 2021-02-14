package com.example.minimoneybox.network.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class ErrorBody implements Serializable {
    private static final long serialVersionUID = -8191543986683233351L;

    @SerializedName("Name") private String name;
    @SerializedName("Message") private String message;
    @SerializedName("ValidationErrors") private List<ValidationError> validationErrorList;
}
