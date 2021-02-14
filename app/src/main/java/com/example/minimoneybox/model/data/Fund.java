
package com.example.minimoneybox.model.data;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Fund {
    @SerializedName("FundId") private Integer fundId;
    @SerializedName("Name") private String name;
    @SerializedName("LogoUrl") private String logoUrl;
    @SerializedName("IsFundDMB") private Boolean isFundDMB;
}
