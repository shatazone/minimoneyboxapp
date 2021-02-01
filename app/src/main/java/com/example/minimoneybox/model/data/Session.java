
package com.example.minimoneybox.model.data;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Session {
    @SerializedName("BearerToken") private String bearerToken;
    @SerializedName("ExternalSessionId") private String externalSessionId;
    @SerializedName("SessionExternalId") private String sessionExternalId;
    @SerializedName("ExpiryInSeconds") private Integer expiryInSeconds;
}
