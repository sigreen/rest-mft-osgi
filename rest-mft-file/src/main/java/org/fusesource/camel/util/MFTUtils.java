package org.fusesource.camel.util;

import java.nio.charset.Charset;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.camel.Exchange;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Managed File Transfer Utility class
 * 
 * @author Simon Green, Red Hat North America
 * 
 */
public class MFTUtils {

    Logger LOGGER = LoggerFactory.getLogger(MFTUtils.class);
    private String hmacKey = ""; // shared secret

    /**
     * Setter for the HMAC digital signature
     * @param hmacKey shared secret
     */
    public void setHmacKey(String hmacKey) {
        this.hmacKey = hmacKey;
    }

    
    /**
     * Takes the Exchange body and calculates the expected
     * digital signature for that body, setting the calculatedSignature
     * in the Exchange header
     * 
     * @param exchange
     */
    public void calculateSignature(Exchange exchange) {
        try {
            byte[] body = exchange.getIn().getBody(byte[].class);
            // calculate the LAU on the raw (decoded) payload and set in camel header
            exchange.getIn().setHeader("calculatedSignature", calculateLAU(body));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

    }

    /**
     * Calculate the LAU (digital signature)
     * 
     * @param payload the raw payload (in bytes)
     * @return the LAU signature
     * @throws Exception
     */
    private byte[] calculateLAU(byte[] payload) throws Exception {
        Mac m = Mac.getInstance("HmacSHA256");

        // initialize key with shared secret from SAA
        SecretKeySpec keyspec = new SecretKeySpec(this.hmacKey.getBytes(Charset.forName("US-ASCII")), "HmacSHA256");
        m.init(keyspec);

        // calculate the LAU
        byte[] lau = m.doFinal(payload);
        byte[] lau_to_encode = new byte[16];
        System.arraycopy(lau, 0, lau_to_encode, 0, 16);

        LOGGER.info("LAU value is: [" + Base64.encodeBase64String(lau_to_encode) + "]");

        return Base64.encodeBase64(lau_to_encode);
    }
}
