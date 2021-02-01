
package com.example.minimoneybox.model.data;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class ProductResponse {
    @SerializedName("Id") private Integer id;
    @SerializedName("PlanValue") private Double planValue;
    @SerializedName("Moneybox") private Double moneybox;
    @SerializedName("SubscriptionAmount") private Double subscriptionAmount;
    @SerializedName("TotalFees") private Double totalFees;
    @SerializedName("IsSelected") private Boolean isSelected;
    @SerializedName("IsFavourite") private Boolean isFavourite;
    @SerializedName("CollectionDayMessage") private String collectionDayMessage;
    @SerializedName("WrapperId") private String wrapperId;
    @SerializedName("IsCashBox") private Boolean isCashBox;
    @SerializedName("AssetBox") private AssetBox assetBox;
    @SerializedName("Product") private Product product;
    @SerializedName("InvestorAccount") private InvestorAccount investorAccount;
    @SerializedName("Personalisation") private Personalisation personalisation;
    @SerializedName("Contributions") private Contributions contributions;
    @SerializedName("MoneyboxCircle") private MoneyboxCircle moneyboxCircle;
    @SerializedName("State") private String state;
}
