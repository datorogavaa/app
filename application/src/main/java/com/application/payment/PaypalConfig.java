// src/main/java/com/yourapp/config/PayPalConfig.java
package com.application.payment;

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;

import java.util.HashMap;
import java.util.Map;

public class PaypalConfig {
    private static final String CLIENT_ID = "ARRH9Uj4789l4ArioWVigwxg8TDKkCRwVjRHZUglHUv8svCjptuG101AYNgvUQ16V_qhKEevs_r2nty0";
    private static final String CLIENT_SECRET = "EDJDyALhPJW_Kn0BhgzHuiNrxs11ddh3zgBd4MNlxx23cUCDa3BFiTMPmL9WWQn4g9K9aLk9I2GnravC";
    private static final String MODE = "sandbox";

    public static APIContext getAPIContext() throws PayPalRESTException {
        Map<String, String> config = new HashMap<>();
        config.put("mode", MODE);

        OAuthTokenCredential tokenCredential = new OAuthTokenCredential(CLIENT_ID, CLIENT_SECRET, config);
        String accessToken = tokenCredential.getAccessToken();

        APIContext context = new APIContext(accessToken);
        context.setConfigurationMap(config);
        return context;
    }
}
