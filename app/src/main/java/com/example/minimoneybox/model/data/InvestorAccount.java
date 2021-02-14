
package com.example.minimoneybox.model.data;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class InvestorAccount {
    @SerializedName("ContributionsNet") private Double contributionsNet;
    @SerializedName("EarningsNet") private Double earningsNet;
    @SerializedName("EarningsAsPercentage") private Double earningsAsPercentage;
    @SerializedName("TodaysInterest") private Double todaysInterest;
}
