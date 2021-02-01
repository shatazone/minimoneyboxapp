
package com.example.minimoneybox.model.data;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Action {
    @SerializedName("Label") private String label;
    @SerializedName("Amount") private Double amount;
    @SerializedName("Type") private String type;
    @SerializedName("Animation") private String animation;
}
