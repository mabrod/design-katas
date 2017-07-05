package com.brodma.dp.cp.domain;

public final class ProducerResult {

    private long totalRecords;

    public ProducerResult(long totalRecords) {
        this.totalRecords = totalRecords;
    }

    public long getTotalRecords() {
        return totalRecords;
    }
}
