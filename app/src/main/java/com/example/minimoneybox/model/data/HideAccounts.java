
package com.example.minimoneybox.model.data;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class HideAccounts {
    @SerializedName("Enabled") private Boolean enabled;
    @SerializedName("IsHidden") private Boolean isHidden;
    @SerializedName("Sequence") private Integer sequence;
}
