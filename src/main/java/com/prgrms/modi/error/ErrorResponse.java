package com.prgrms.modi.error;

import org.springframework.http.HttpStatus;

public class ErrorResponse {

    private final String errorMessage;
    private final String requestUri;
    private final HttpStatus status;

    public ErrorResponse(String errorMessage, String requestUri, HttpStatus status) {
        this.errorMessage = errorMessage;
        this.requestUri = requestUri;
        this.status = status;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ErrorResponse{");
        sb.append("errorMessage='").append(errorMessage).append('\'');
        sb.append(", requestUri='").append(requestUri).append('\'');
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }
}
