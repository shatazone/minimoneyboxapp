package com.example.minimoneybox.network.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NetworkResponse<T> {
    private int code;
    private T body;
    private ErrorBody errorBody;

    public boolean isSuccessful() {
        return code >= 200 && code < 300;
    }
}
