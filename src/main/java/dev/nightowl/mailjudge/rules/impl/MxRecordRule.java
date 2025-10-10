package dev.nightowl.mailjudge.rules.impl;

import dev.nightowl.mailjudge.rules.Rule;
import lombok.extern.java.Log;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

/**
 * Validates that the domain has valid MX records.
 * This performs an actual DNS lookup and may be slower.
 */
@Log
public class MxRecordRule implements Rule {

    @Override
    public boolean validate(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }

        int atIndex = email.indexOf('@');
        if (atIndex == -1) {
            return false;
        }

        String domain = email.substring(atIndex + 1);

        try {
            Lookup lookup = new Lookup(domain, Type.MX);
            Record[] records = lookup.run();
            return records != null && records.length > 0;
        } catch (Exception e) {
            log.fine("MX lookup failed for domain: " + domain + " - " + e.getMessage());
            return false;
        }
    }

    @Override
    public String getErrorMessage() {
        return "Domain has no valid MX records";
    }
}
