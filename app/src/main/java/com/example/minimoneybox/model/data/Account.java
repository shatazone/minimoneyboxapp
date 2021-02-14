
package com.example.minimoneybox.model.data;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Account {
    @SerializedName("Type") private String type;
    @SerializedName("Name") private String name;
    @SerializedName("Wrapper") private Wrapper wrapper;
}
