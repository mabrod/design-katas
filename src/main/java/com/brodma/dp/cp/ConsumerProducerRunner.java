package com.brodma.dp.cp;

import com.brodma.dp.cp.consumer.WebSitesAccessConsumer;
import com.brodma.dp.cp.domain.*;
import com.brodma.dp.cp.producer.WebSitesAccessProducer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.Log4jThreadFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

public class ConsumerProducerRunner {

  private static final Logger LOG = LogManager.getLogger(ConsumerProducerRunner.class);

  public static void main(String[] args) throws Exception {

    if (args.length != 1) {
      LOG.info("Provide path to a file containing data.");
      LOG.info("e.g: java -jar <your_jar> <file>");
      return;
    }

    final Path filePath = Paths.get(args[0]);

    validate(filePath);

    int maxTopSitesToShow = 5;
    final BlockingQueue<WebSiteRecord<WebSite>> queue = new ArrayBlockingQueue<>(10);

    ExecutorService producerService = Executors.newSingleThreadExecutor(Log4jThreadFactory.createThreadFactory("Producer-%d"));
    ExecutorService consumerService = Executors.newSingleThreadExecutor(Log4jThreadFactory.createThreadFactory("Consumer-%d"));

    WebSitesAccessProducer producer = new WebSitesAccessProducer(queue, filePath);
    ExecutorCompletionService producerCompletionService = new ExecutorCompletionService(producerService);

    final Comparator compareByAccess = Comparator.comparingLong(WebSiteAccess::getAccess);
    final PriorityBlockingQueue topWebSitesQueue = new PriorityBlockingQueue<>(maxTopSitesToShow, compareByAccess);
    WebSitesAccessConsumer consumer = new WebSitesAccessConsumer(queue, topWebSitesQueue, maxTopSitesToShow);
    ExecutorCompletionService consumerCompletionService = new ExecutorCompletionService(consumerService);

    producerCompletionService.submit(producer);
    consumerCompletionService.submit(consumer);

    Future<ProducerResult> producerResult = producerCompletionService.take();
    Future<TopWebSites> consumerResult = consumerCompletionService.take();

    // clean up
    producerService.shutdown();
    consumerService.shutdown();
    producerService.awaitTermination(10, TimeUnit.SECONDS);
    consumerService.awaitTermination(10, TimeUnit.SECONDS);

    LOG.info("Records produced {} ", () -> logRecordsProduced(producerResult));
    LOG.info(maxTopSitesToShow + " highest top websites based on access {} ", () -> logTopWebSites(consumerResult));
  }

  private static long logRecordsProduced(Future<ProducerResult> producerResult) {
    long total = 0L;
    try {
      total = producerResult.get().getTotalRecords();
    } catch (InterruptedException | ExecutionException e) {
      LOG.error(e);
    }
    return total;
  }

  private static List<WebSite> logTopWebSites( Future<TopWebSites> consumerResult) {
    List<WebSite> ret = null;
    try {
      ret = consumerResult.get().getTop();
    } catch (InterruptedException | ExecutionException e) {
      LOG.error(e);
    }
    return ret;
  }

  private static void validate(Path filePath) {

    if (Objects.isNull(filePath)) {
      throw new IllegalArgumentException(String.format("%s path to file can not be null.", filePath));
    }

    if (!Files.exists(filePath)) {
      throw new IllegalArgumentException(String.format("%s file does not exist.", filePath));
    }

    if (Files.isDirectory(filePath)) {
      throw new IllegalArgumentException(String.format("%s file can not be a directory.", filePath));
    }

    if (!Files.isReadable(filePath)) {
      throw new IllegalArgumentException(String.format("%s file must be readable.", filePath));
    }
  }
}
