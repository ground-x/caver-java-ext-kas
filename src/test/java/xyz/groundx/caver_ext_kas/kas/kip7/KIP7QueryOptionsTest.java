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
