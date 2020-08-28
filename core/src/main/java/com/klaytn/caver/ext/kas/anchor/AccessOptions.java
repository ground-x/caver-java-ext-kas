package com.klaytn.caver.ext.kas.anchor;

import com.squareup.okhttp.Credentials;

import java.util.ArrayList;
import java.util.List;

public class AccessOptions {

    String accessKeyId;
    String secretAccessKey;
    List<String> krn;

    public AccessOptions() {
    }

    public AccessOptions(String accessKeyId, String secretAccessKey, List<String> krn) {
        this.accessKeyId = accessKeyId;
        this.secretAccessKey = secretAccessKey;
        this.krn = krn;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public String getSecretAccessKey() {
        return secretAccessKey;
    }

    public List<String> getKrn() {
        return krn;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public void setSecretAccessKey(String secretAccessKey) {
        this.secretAccessKey = secretAccessKey;
    }

    public void setKrn(List<String> krn) {
        this.krn = krn;
    }

    public String getAuthorizationString() {
        return Credentials.basic(accessKeyId, secretAccessKey);
    }
}
