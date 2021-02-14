
package com.example.minimoneybox.model.data;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Wrapper {
    @SerializedName("Id") private String id;
    @SerializedName("TotalValue") private Double totalValue;
    @SerializedName("TotalContributions") private Double totalContributions;
    @SerializedName("EarningsNet") private Double earningsNet;
    @SerializedName("EarningsAsPercentage") private Double earningsAsPercentage;
}
