package com.evolven.httpclient.http;

public class HttpRequestResult {

    private int statusCode;
    private String reasonPhrase;
    private String content;
    protected HttpRequestResult(int statusCode, String reasonPhrase, String content) {
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
        this.content = content;
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
