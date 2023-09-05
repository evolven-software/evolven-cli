package com.evolven.httpclient.http;

import java.io.OutputStream;

public interface IHttpRequestResult {
    boolean isSuccess();
    boolean isError();
    void print(OutputStream os);
    int getStatusCode();
    String getReasonPhrase();
    String getContent();
}
