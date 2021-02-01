package com.example.minimoneybox.network.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Data;

@Data
public class ValidationError implements Serializable {
    private static final long serialVersionUID = 6621014676030972427L;

    @SerializedName("Name") private String name;
    @SerializedName("Message") private String message;
}
