package com.prgrms.modi.error;

import org.apache.commons.lang3.builder.ToStringBuilder;
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

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("errorMessage", errorMessage)
            .append("requestUri", requestUri)
            .append("status", status)
            .toString();
    }

}
