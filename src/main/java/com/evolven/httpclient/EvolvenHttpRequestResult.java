package com.evolven.httpclient;

import com.evolven.common.StringUtils;
import com.evolven.httpclient.http.HttpRequestResult;
import com.evolven.httpclient.http.IHttpRequestResult;
import com.evolven.logging.LoggerManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.logging.Logger;

public class EvolvenHttpRequestResult implements IHttpRequestResult {

    private HttpRequestResult httpRequestResult;

    private String evolvenError = null;
    private boolean evolvenErrorParsed = false;

    private static Logger logger = LoggerManager.getLogger(EvolvenHttpRequestResult.class);

    protected EvolvenHttpRequestResult(HttpRequestResult httpRequestResult) {
        this.httpRequestResult = httpRequestResult;
        logger.fine("content: " + httpRequestResult.getContent());
        logger.fine("status code: " + httpRequestResult.getStatusCode());
        logger.fine("reason phrase: " + httpRequestResult.getReasonPhrase());
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
        String content = httpRequestResult.getContent();
        if (StringUtils.isNullOrBlank(content)) {
            return null;
        }
        JsonNode responseNode = null;
        try {
            responseNode = new ObjectMapper().readTree(content);
        } catch (JsonProcessingException e) {
            return null;
        }
        final String genericMessage = "Invalid response.";
        JsonNode nextNode = responseNode.get("Next");
        JsonNode errorNode;
        if (nextNode == null) {
            errorNode = responseNode.get("Error");
            if (errorNode == null) return setEvolvenError(genericMessage);
//        if (nextNode == null) return null;
        } else {
            errorNode = nextNode.get("Error");
            if (errorNode == null) return null;
        }
        JsonNode msgNode = errorNode.get("Message");
        if (msgNode == null) return setEvolvenError(genericMessage);
        JsonNode textNode = msgNode.get("text");
        if (textNode == null) return setEvolvenError(genericMessage);
        String errorMsg = textNode.asText();
        if (errorMsg == null) return setEvolvenError(genericMessage);
        return setEvolvenError(errorMsg + ".");
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
