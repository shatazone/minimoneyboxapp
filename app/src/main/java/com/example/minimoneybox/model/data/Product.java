
package com.example.minimoneybox.model.data;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Product {
    @SerializedName("Id") private Integer id;
    @SerializedName("Name") private String name;
    @SerializedName("CategoryType") private String categoryType;
    @SerializedName("Type") private String type;
    @SerializedName("FriendlyName") private String friendlyName;
    @SerializedName("CanWithdraw") private Boolean canWithdraw;
    @SerializedName("ProductHexCode") private String productHexCode;
    @SerializedName("AnnualLimit") private Double annualLimit;
    @SerializedName("DepositLimit") private Double depositLimit;
    @SerializedName("BonusMultiplier") private Double bonusMultiplier;
    @SerializedName("MinimumWeeklyDeposit") private Double minimumWeeklyDeposit;
    @SerializedName("MaximumWeeklyDeposit") private Double maximumWeeklyDeposit;
    @SerializedName("Documents") private Documents documents;
    @SerializedName("State") private String state;
    @SerializedName("Lisa") private Lisa lisa;
    @SerializedName("InterestRate") private String interestRate;
    @SerializedName("InterestRateAmount") private Double interestRateAmount;
    @SerializedName("LogoUrl") private String logoUrl;
    @SerializedName("Fund") private Fund fund;

}
