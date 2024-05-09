package org.ying.book.utils;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

public class CustomResponseData<T> extends ResponseEntity<T> {

    public CustomResponseData(HttpStatusCode status) {
        super(status);
    }

    public CustomResponseData(T body, HttpStatusCode status) {
        super(body, status);
    }

    public CustomResponseData(MultiValueMap<String, String> headers, HttpStatusCode status) {
        super(headers, status);
    }

    public CustomResponseData(T body, MultiValueMap<String, String> headers, int rawStatus) {
        super(body, headers, rawStatus);
    }

    public CustomResponseData(T body, MultiValueMap<String, String> headers, HttpStatusCode statusCode) {
        super(body, headers, statusCode);
    }
}
