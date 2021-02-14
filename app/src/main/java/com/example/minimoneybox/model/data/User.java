
package com.example.minimoneybox.model.data;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class User {
    @SerializedName("UserId") private String userId;
    @SerializedName("HasVerifiedEmail") private Boolean hasVerifiedEmail;
    @SerializedName("IsPinSet") private Boolean isPinSet;
    @SerializedName("AmlStatus") private String amlStatus;
    @SerializedName("AmlAttempts") private Integer amlAttempts;
    @SerializedName("RoundUpMode") private String roundUpMode;
    @SerializedName("JisaRoundUpMode") private String jisaRoundUpMode;
    @SerializedName("InvestorProduct") private String investorProduct;
    @SerializedName("RegistrationStatus") private String registrationStatus;
    @SerializedName("JisaRegistrationStatus") private String jisaRegistrationStatus;
    @SerializedName("DirectDebitMandateStatus") private String directDebitMandateStatus;
    @SerializedName("DateCreated") private String dateCreated;
    @SerializedName("Animal") private String animal;
    @SerializedName("ReferralCode") private String referralCode;
    @SerializedName("IntercomHmac") private String intercomHmac;
    @SerializedName("IntercomHmaciOS") private String intercomHmaciOS;
    @SerializedName("IntercomHmacAndroid") private String intercomHmacAndroid;
    @SerializedName("HasCompletedTutorial") private Boolean hasCompletedTutorial;
    @SerializedName("LastPayment") private Double lastPayment;
    @SerializedName("PreviousMoneyboxAmount") private Double previousMoneyboxAmount;
    @SerializedName("MoneyboxRegistrationStatus") private String moneyboxRegistrationStatus;
    @SerializedName("Email") private String email;
    @SerializedName("FirstName") private String firstName;
    @SerializedName("LastName") private String lastName;
    @SerializedName("MobileNumber") private String mobileNumber;
    @SerializedName("RoundUpWholePounds") private Boolean roundUpWholePounds;
    @SerializedName("DoubleRoundUps") private Boolean doubleRoundUps;
    @SerializedName("MoneyboxAmount") private Double moneyboxAmount;
    @SerializedName("InvestmentTotal") private Double investmentTotal;
    @SerializedName("CanReinstateMandate") private Boolean canReinstateMandate;
    @SerializedName("DirectDebitHasBeenSubmitted") private Boolean directDebitHasBeenSubmitted;
    @SerializedName("MonthlyBoostEnabled") private Boolean monthlyBoostEnabled;
    @SerializedName("MonthlyBoostAmount") private Double monthlyBoostAmount;
    @SerializedName("MonthlyBoostDay") private Integer monthlyBoostDay;
    @SerializedName("RestrictedDevice") private Boolean restrictedDevice;
    @SerializedName("EmailTwoFactorEnabled") private Boolean emailTwoFactorEnabled;
    @SerializedName("Cohort") private Integer cohort;
}
