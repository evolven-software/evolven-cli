package com.evolven.httpclient.http;

import java.io.OutputStream;
import java.io.PrintStream;

public class HttpRequestResult {

    private int statusCode;
    private String reasonPhrase;
    private String content;
    protected HttpRequestResult(int statusCode, String reasonPhrase, String content) {
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
        this.content = content;
    }

    public boolean isSuccess() {
        return statusCode >= 200 && statusCode < 400;
    }

    public boolean isError() {
        return !isSuccess();
    }

    public void print(OutputStream os) {
        PrintStream ps = new PrintStream(os);
        if (!isSuccess()) {
            ps.println("HTTP request error (status code " + statusCode + ").");
            if (reasonPhrase != null) {
                ps.println(reasonPhrase);
            }
        } else {
            ps.println("HTTP request status code: " + statusCode + ".");
        }
        if (content != null) {
            ps.println(content);
        }
    }

    public HttpRequestResult(String msg) {
        this.statusCode = -1;
        this.reasonPhrase = msg;
    }

    public int getStatusCode() {
        return statusCode;
    }

    protected void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }

    protected void setReasonPhrase(String reasonPhrase) {
        this.reasonPhrase = reasonPhrase;
    }

    public String getContent() {
        return content;
    }

    protected void setContent(String content) {
        this.content = content;
    }
}
