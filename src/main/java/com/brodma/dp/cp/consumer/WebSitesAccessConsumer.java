package com.brodma.dp.cp.consumer;

import com.brodma.dp.cp.domain.TopWebSites;
import com.brodma.dp.cp.domain.WebSite;
import com.brodma.dp.cp.domain.WebSiteAccess;
import com.brodma.dp.cp.domain.WebSiteRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.PriorityBlockingQueue;
import static java.util.stream.Collectors.toList;

public class WebSitesAccessConsumer implements Callable<TopWebSites> {

    private static final Logger LOG = LogManager.getLogger(WebSitesAccessConsumer.class);
    private static final int DEFAULT_TOP = 5;
    private BlockingQueue<WebSiteRecord<WebSite>> consumer;
    private BlockingQueue<WebSite> topWebSites;
    private int maxTop;

    public WebSitesAccessConsumer(BlockingQueue<WebSiteRecord<WebSite>> consumer) {
        this(consumer, DEFAULT_TOP);
    }

    public WebSitesAccessConsumer(BlockingQueue<WebSiteRecord<WebSite>> consumer, int maxTop) {
        this.consumer = consumer;
        this.maxTop = maxTop;
        Comparator byAccessCount = Comparator.comparingLong(WebSiteAccess::getAccess);
        topWebSites = new PriorityBlockingQueue<>(maxTop + 1, byAccessCount);
    }

    public WebSitesAccessConsumer(BlockingQueue<WebSiteRecord<WebSite>> consumer, PriorityBlockingQueue priorityBlockingQueue, int maxTop) {
        this.consumer = consumer;
        topWebSites = priorityBlockingQueue;
        this.maxTop = maxTop;
    }

    public List<WebSite> getTopSites() {
        return topWebSites.stream()
                .limit(maxTop)
                .sorted(Comparator.comparingLong(WebSite::getAccess).reversed())
                .collect(toList());
    }

    @Override
    public TopWebSites call() throws Exception {
        try {
            boolean done = false;
            while(!done) {
                    WebSiteRecord<WebSite> record = consumer.take();
                    if (record.getRecord().isPresent()) {
                        topWebSites.offer(record.getRecord().get());
                        if (topWebSites.size() > maxTop) {
                            topWebSites.remove();
                        }
                    } else {
                        done = true;
                    }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return new TopWebSites(getTopSites());
    }
}
