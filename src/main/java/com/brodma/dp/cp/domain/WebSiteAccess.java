package com.brodma.dp.cp.domain;

import java.io.Serializable;
import java.util.Objects;

public final class WebSiteAccess implements WebSite, Serializable {

    private static final String UNKNOWN = "UKNOWN";
    private final String website;
    private final long count;

    public WebSiteAccess() {
        this(UNKNOWN, 0);
    }
    public WebSiteAccess(final String website, final long count) {
        this.website = website;
        this.count = count;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        WebSiteAccess that = (WebSiteAccess) other;
        return Objects.equals(website, that.website) && Objects.equals(count, that.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(website, count);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WebSiteRecord{");
        sb.append("website='").append(website);
        sb.append(", count=").append(count);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public String getWebSite() {
        return website;
    }

    @Override
    public long getAccess() {
        return count;
    }
}
