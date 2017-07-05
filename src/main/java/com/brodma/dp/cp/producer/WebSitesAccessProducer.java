package com.brodma.dp.cp.producer;

import com.brodma.dp.cp.domain.ProducerResult;
import com.brodma.dp.cp.domain.WebSite;
import com.brodma.dp.cp.domain.WebSiteAccess;
import com.brodma.dp.cp.domain.WebSiteRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class WebSitesAccessProducer implements Callable<ProducerResult> {

  private static final Logger LOG = LogManager.getLogger(WebSitesAccessProducer.class);
  private BlockingQueue<WebSiteRecord<WebSite>> consumer;
  private Path path;
  private String delimiter;
  private LongAdder totalRecords = new LongAdder();
  private Predicate<String> notEmptyOrNull = s -> s != null && !s.isEmpty();
  private Predicate<String> hasFields = s -> s.split(delimiter).length == 2;

  private Function<String,WebSiteRecord> toWebSiteRecord = s -> {
    s = s.trim();
    String [] tokens = s.split(delimiter);
    String website = tokens[0].trim();
    long count = Long.parseLong(tokens[1].trim());
    return new WebSiteRecord(new WebSiteAccess(website, count));
  };

  public WebSitesAccessProducer(BlockingQueue<WebSiteRecord<WebSite>> consumer, Path path, String delimiter) {
    this.consumer = consumer;
    this.path = path;
    this.delimiter = delimiter;
  }
  public WebSitesAccessProducer(BlockingQueue<WebSiteRecord<WebSite>> consumer, Path path) {
   this(consumer, path, "\\s+");
  }

  public void processWebSiteRecords(Stream<String> lines) {
    lines.filter(notEmptyOrNull).filter(hasFields).map(toWebSiteRecord).forEach(s -> {
        add(s);
        totalRecords.increment();
    });
  }

  public void add(WebSiteRecord<WebSite> endRecord) {
    try {
      consumer.put(endRecord);
    } catch (InterruptedException e) {
      LOG.error(e);
    }
  }

  public ProducerResult produceWebSiteAccessData(Path path) throws InterruptedException {
    try (Stream<String> lines = Files.lines(path)) {
      processWebSiteRecords(lines);
      add(new WebSiteRecord<>(null));
    } catch (IOException e) {
      LOG.error(e.getMessage(), e);
    }
    return new ProducerResult(totalRecords.longValue());
  }

  @Override
  public ProducerResult call() throws Exception {
    return produceWebSiteAccessData(path);
  }
}
