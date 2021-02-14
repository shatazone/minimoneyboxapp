
package com.example.minimoneybox.network.data;

import com.example.minimoneybox.model.data.ActionMessage;
import com.example.minimoneybox.model.data.Session;
import com.example.minimoneybox.model.data.User;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class UserLoginResponse {
    @SerializedName("User") private User user;
    @SerializedName("Session") private Session session;
    @SerializedName("ActionMessage") private ActionMessage actionMessage;
    @SerializedName("InformationMessage") private String informationMessage;
}
