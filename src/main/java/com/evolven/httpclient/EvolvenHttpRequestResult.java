package com.evolven.httpclient;

import com.evolven.httpclient.http.HttpRequestResult;
import com.evolven.httpclient.http.IHttpRequestResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.OutputStream;
import java.io.PrintStream;

public class EvolvenHttpRequestResult implements IHttpRequestResult {

    private HttpRequestResult httpRequestResult;

    private String evolvenError = null;
    private boolean evolvenErrorParsed = false;

    protected EvolvenHttpRequestResult(HttpRequestResult httpRequestResult) {
        this.httpRequestResult = httpRequestResult;
    }

    protected EvolvenHttpRequestResult(int statusCode, String reasonPhrase, String content) {
        this.httpRequestResult = new HttpRequestResult(statusCode, reasonPhrase, content);
    }

    public EvolvenHttpRequestResult(String msg) {
        this.httpRequestResult = new HttpRequestResult(msg);
    }
    public boolean isSuccess() {
        return this.httpRequestResult.isSuccess() && getError() == null;
    }

    private String setEvolvenError(String msg) {
        evolvenError = msg;
        return evolvenError;
    }

    private String getError() {
        if (evolvenErrorParsed) return evolvenError;
        evolvenErrorParsed = true;

        final String genericMessage = "Invalid response";
        JsonNode responseNode = null;
        try {
            responseNode = new ObjectMapper().readTree(httpRequestResult.getContent());
        } catch (JsonProcessingException e) {
            return setEvolvenError(genericMessage);
        }
        JsonNode nextNode = responseNode.get("Next");
        if (nextNode == null) return setEvolvenError(genericMessage);
        JsonNode errorNode = nextNode.get("Error");
        if (errorNode == null) return null;
        JsonNode msgNode = errorNode.get("Message");
        if (msgNode == null) return setEvolvenError(genericMessage);
        JsonNode textNode = msgNode.get("text");
        if (textNode == null) return setEvolvenError(genericMessage);
        String errorMsg = textNode.asText();
        if (errorMsg == null) return setEvolvenError(genericMessage);
        return setEvolvenError(errorMsg);
    }

    public boolean isError() {
        return !isSuccess();
    }

    public void print(OutputStream os) {
        PrintStream ps = new PrintStream(os);
        httpRequestResult.print(ps);
        if (getError() != null) ps.println(getError());
    }

    @Override
    public int getStatusCode() {
        if (httpRequestResult.isError()) return httpRequestResult.getStatusCode();
        if (getError() != null) return -1;
        return httpRequestResult.getStatusCode();
    }

    @Override
    public String getReasonPhrase() {
        if (httpRequestResult.isError()) return httpRequestResult.getReasonPhrase();
        if (getError() != null) return getError();
        return httpRequestResult.getReasonPhrase();
    }

    @Override
    public String getContent() {
        return httpRequestResult.getContent();
    }
}
