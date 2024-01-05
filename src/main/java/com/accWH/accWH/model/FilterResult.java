package com.accWH.accWH.model;

import java.util.List;
import java.util.Map;

public class FilterResult {
    private List<Certificate> certificates;
    private Map<String, Long> certificateCounts;

    public List<Certificate> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<Certificate> certificates) {
        this.certificates = certificates;
    }

    public Map<String, Long> getCertificateCounts() {
        return certificateCounts;
    }

    public void setCertificateCounts(Map<String, Long> certificateCounts) {
        this.certificateCounts = certificateCounts;
    }
}
