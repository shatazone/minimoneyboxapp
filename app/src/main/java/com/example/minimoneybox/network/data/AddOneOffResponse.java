package com.example.minimoneybox.network.data;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddOneOffResponse {
    @SerializedName("Moneybox") private Double moneyBox;
}
