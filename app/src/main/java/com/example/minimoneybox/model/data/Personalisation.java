
package com.example.minimoneybox.model.data;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Personalisation {
    @SerializedName("QuickAddDeposit") private QuickAddDeposit quickAddDeposit;
    @SerializedName("HideAccounts") private HideAccounts hideAccounts;
}
