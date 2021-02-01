
package com.example.minimoneybox.network.data;

import com.example.minimoneybox.model.data.Account;
import com.example.minimoneybox.model.data.ProductResponse;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class InvestorProductsResponse {
    @SerializedName("MoneyboxEndOfTaxYear") private String moneyboxEndOfTaxYear;
    @SerializedName("TotalPlanValue") private Double totalPlanValue;
    @SerializedName("TotalEarnings") private Double totalEarnings;
    @SerializedName("TotalContributionsNet") private Double totalContributionsNet;
    @SerializedName("TotalEarningsAsPercentage") private Double totalEarningsAsPercentage;
    @SerializedName("ProductResponses") private List<ProductResponse> productResponses;
    @SerializedName("Accounts") private List<Account> accounts;
}
