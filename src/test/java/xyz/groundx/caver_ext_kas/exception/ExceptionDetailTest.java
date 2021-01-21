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
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class ExceptionDetailTest {
    @Test
    public void parse() throws IOException {
        String test = "{\"code\":1061010,\"message\":\"data don't exist\",\"requestId\":\"6a1d4aba-fdc3-9a8a-bec7-c5de75abf9dd\"}";

        ObjectMapper mapper = new ObjectMapper();
        ExceptionDetail detail = mapper.readValue(test, ExceptionDetail.class);

        assertEquals(1061010, detail.getCode());
        assertEquals("data don't exist", detail.getMessage());
        assertEquals("6a1d4aba-fdc3-9a8a-bec7-c5de75abf9dd", detail.getRequestId());
    }

    @Test
    public void ignoreNotExistedField() throws IOException {
        String test = "{\"code\":1061010,\"message\":\"data don't exist\"}";

        ObjectMapper mapper = new ObjectMapper();
        ExceptionDetail detail = mapper.readValue(test, ExceptionDetail.class);

        assertEquals(1061010, detail.getCode());
        assertEquals("data don't exist", detail.getMessage());
    }

    @Test
    public void ignoreUnknownField() throws IOException {
        String test = "{\"code\":1061010,\"message\":\"data don't exist\",\"requestId\":\"6a1d4aba-fdc3-9a8a-bec7-c5de75abf9dd\", \"unknown\": 1234}";

        ObjectMapper mapper = new ObjectMapper();
        ExceptionDetail detail = mapper.readValue(test, ExceptionDetail.class);

        assertEquals(1061010, detail.getCode());
        assertEquals("data don't exist", detail.getMessage());
        assertEquals("6a1d4aba-fdc3-9a8a-bec7-c5de75abf9dd", detail.getRequestId());
    }
}