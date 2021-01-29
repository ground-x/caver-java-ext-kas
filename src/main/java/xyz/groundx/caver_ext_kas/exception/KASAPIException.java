/*
 * Copyright 2020 The caver-java-ext-kas Authors
 *
 * Licensed under the Apache License, Version 2.0 (the “License”);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an “AS IS” BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package xyz.groundx.caver_ext_kas.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class KASAPIException extends RuntimeException {
    private int code = 0;
    private Map<String, List<String>> responseHeaders = null;
    private ExceptionDetail responseBody = null;

    public KASAPIException() {}

    public KASAPIException(Throwable throwable) {
        super(throwable);
    }

    public KASAPIException(String message) {
        super(message);
    }

    public KASAPIException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public KASAPIException(ApiException e) {
        this(e.getMessage(), e.getCause(), e.getCode(), e.getResponseHeaders(), e.getResponseBody());
    }

    public KASAPIException(String message, Throwable throwable, int code, Map<String, List<String>> responseHeaders, String responseBody) {
        super(message, throwable);
        this.code = code;
        this.responseHeaders = responseHeaders;
        if(responseBody != null && !responseBody.isEmpty()) {
            setResponseBody(responseBody);
        }
    }

    public KASAPIException(String message, int code, Map<String, List<String>> responseHeaders, String responseBody) {
        this(message, (Throwable) null, code, responseHeaders, responseBody);
    }

    public KASAPIException(String message, Throwable throwable, int code, Map<String, List<String>> responseHeaders) {
        this(message, throwable, code, responseHeaders, null);
    }

    public KASAPIException(int code, Map<String, List<String>> responseHeaders, String responseBody) {
        this((String) null, (Throwable) null, code, responseHeaders, responseBody);
    }

    public KASAPIException(int code, String message) {
        super(message);
        this.code = code;
    }

    public KASAPIException(int code, String message, Map<String, List<String>> responseHeaders, String responseBody) {
        this(code, message);
        this.responseHeaders = responseHeaders;

        if(responseBody != null && !responseBody.isEmpty()) {
            setResponseBody(responseBody);
        }
    }

    public int getCode() {
        return code;
    }

    public Map<String, List<String>> getResponseHeaders() {
        return responseHeaders;
    }

    public ExceptionDetail getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        try {
            ExceptionDetail detail = new ObjectMapper().readValue(responseBody, ExceptionDetail.class);
            this.responseBody = detail;
        } catch (IOException e){
            throw new RuntimeException("Failed to parsed json string : " + responseBody);
        }
    }
}
