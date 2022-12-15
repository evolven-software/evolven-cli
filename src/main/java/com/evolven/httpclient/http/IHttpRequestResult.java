package com.evolven.httpclient.http;

import java.io.OutputStream;

public interface IHttpRequestResult {
    public boolean isSuccess();
    public boolean isError();
    public void print(OutputStream os);
    public int getStatusCode();
    public String getReasonPhrase();
    public String getContent();
}
