package com.brodma.dp.cp.domain;

import java.util.Optional;

/**
 * Wrapper to add additional metadata without compromising the data structure of a wrapped object.
 */
public class WebSiteRecord<T> {
    private final T record;

    public WebSiteRecord(T record) {
        this.record = record;
    }

    public Optional<T> getRecord() {
        if (record == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(record);
    }
}
