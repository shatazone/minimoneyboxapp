
package com.example.minimoneybox.model.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class ActionMessage {
    @SerializedName("Type") private String type;
    @SerializedName("Message") private String message;
    @SerializedName("Actions") private List<Action> actions = null;
}
