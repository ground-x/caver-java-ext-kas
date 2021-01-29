package xyz.groundx.caver_ext_kas.kas.wallet;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import xyz.groundx.caver_ext_kas.kas.tokenhistory.TokenHistoryQueryOptions;

import java.security.InvalidParameterException;

import static org.junit.Assert.assertEquals;

public class WalletQueryOptionsTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void setType() {
        String[] status = new String[] {"all", "enabled", "disable", "corrupted"};

        WalletQueryOptions options = new WalletQueryOptions();
        for(int i=0; i<status.length; i++) {
            options.setStatus(status[i]);
            assertEquals(status[i], options.getStatus());
        }
    }

    @Test
    public void setStatusWithEnum() {
        WalletQueryOptions options = new WalletQueryOptions();
        options.setStatus(WalletQueryOptions.ACCOUNT_STATUS.ALL);
        assertEquals("all", options.getStatus());

        options.setStatus(WalletQueryOptions.ACCOUNT_STATUS.ENABLED);
        assertEquals("enabled", options.getStatus());

        options.setStatus(WalletQueryOptions.ACCOUNT_STATUS.DISABLE);
        assertEquals("disable", options.getStatus());

        options.setStatus(WalletQueryOptions.ACCOUNT_STATUS.CORRUPTED);
        assertEquals("corrupted", options.getStatus());
    }

    @Test
    public void invalidTypeTest() {
        expectedException.expect(InvalidParameterException.class);
        expectedException.expectMessage("The status parameter have one of the following: ['all', 'enabled', 'disable', 'corrupted']");

        String type = "invalid";
        WalletQueryOptions options = new WalletQueryOptions();
        options.setStatus(type);
    }
}
