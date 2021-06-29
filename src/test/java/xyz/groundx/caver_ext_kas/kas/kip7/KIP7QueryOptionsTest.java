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

package xyz.groundx.caver_ext_kas.kas.kip7;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.security.InvalidParameterException;

import static org.junit.Assert.assertEquals;

public class KIP7QueryOptionsTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void statusTest() {
        KIP7QueryOptions options = new KIP7QueryOptions();
        options.setStatus("all");
        assertEquals("all", options.getStatus());

        options.setStatus("init");
        assertEquals("init", options.getStatus());

        options.setStatus("submitted");
        assertEquals("submitted", options.getStatus());

        options.setStatus("deployed");
        assertEquals("deployed", options.getStatus());
    }

    @Test
    public void invalidStatusTest() {
        expectedException.expect(InvalidParameterException.class);
        expectedException.expectMessage("The status parameter have one of the following:");

        KIP7QueryOptions options = new KIP7QueryOptions();
        options.setStatus("invalid");
    }

    @Test
    public void statusTest_WithEnum() {
        KIP7QueryOptions options = new KIP7QueryOptions();
        options.setStatus(KIP7QueryOptions.STATUS_TYPE.ALL);
        assertEquals("all", options.getStatus());

        options.setStatus(KIP7QueryOptions.STATUS_TYPE.INIT);
        assertEquals("init", options.getStatus());

        options.setStatus(KIP7QueryOptions.STATUS_TYPE.SUBMITTED);
        assertEquals("submitted", options.getStatus());

        options.setStatus(KIP7QueryOptions.STATUS_TYPE.DEPLOYED);
        assertEquals("deployed", options.getStatus());
    }
}
