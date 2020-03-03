package com.gx.smart.lib.http.lib.model;

import java.util.List;

/**
 * @Author niling on 2019/6/19
 */
public class VerificationKeys {
    private List<VerificationKey> keys;

    public VerificationKeys() {
    }

    public VerificationKeys(List<VerificationKey> keys) {
        this.keys = keys;
    }

    public List<VerificationKey> getKeys() {
        return keys;
    }

    public void setKeys(List<VerificationKey> keys) {
        this.keys = keys;
    }

    @Override
    public String toString() {
        return "VerificationKeys{" +
                "keys=" + keys +
                '}';
    }
}
