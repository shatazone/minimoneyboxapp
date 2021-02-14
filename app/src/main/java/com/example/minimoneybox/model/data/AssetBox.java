
package com.example.minimoneybox.model.data;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class AssetBox {
    @SerializedName("Title") private String title;
}
