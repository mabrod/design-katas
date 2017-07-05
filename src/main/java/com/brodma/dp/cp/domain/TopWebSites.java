package com.brodma.dp.cp.domain;

import java.util.ArrayList;
import java.util.List;

public class TopWebSites {

    private List<WebSite> top;

    public TopWebSites() {
        this(new ArrayList<WebSite>());
    }

    public TopWebSites(List<WebSite> top) {
        this.top = top;
    }

    public List<WebSite> getTop() {
        return top;
    }
}
