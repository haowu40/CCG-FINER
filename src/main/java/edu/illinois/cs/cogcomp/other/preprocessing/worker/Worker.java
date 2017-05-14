package edu.illinois.cs.cogcomp.other.preprocessing.worker;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.rabbitmq.client.*;
import edu.illinois.cs.cogcomp.annotation.AnnotatorException;
import edu.illinois.cs.cogcomp.annotation.AnnotatorServiceConfigurator;
import edu.illinois.cs.cogcomp.annotation.BasicAnnotatorService;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation
        .TextAnnotation;
import edu.illinois.cs.cogcomp.core.utilities.SerializationHelper;
import edu.illinois.cs.cogcomp.core.utilities.configuration.Configurator;
import edu.illinois.cs.cogcomp.core.utilities.configuration.ResourceManager;
import edu.illinois.cs.cogcomp.pipeline.common.PipelineConfigurator;
import edu.illinois.cs.cogcomp.pipeline.main.PipelineFactory;
import edu.illinois.cs.cogcomp.other.preprocessing.GenerateData;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import static edu.illinois.cs.cogcomp.other.preprocessing.GenerateData.GSON;

/**
 * Created by haowu4 on 1/10/17.
 */
public class Worker {
    public static final String TASK_QUEUE_NAME = "test";
    private BasicAnnotatorService processor;

    public static class WorkerParameter {

        @Parameter(names = {"-h", "-host"}, description = "RabbitMQ Hosts")
        String host = "54.204.245.88";

        @Parameter(names = {"-d", "-debug"}, description = "Debug mode")
        boolean debug = false;

    }

    WorkerParameter parameter;

    public Worker(WorkerParameter parameter) throws IOException,
            AnnotatorException {
        Properties props = new Properties();
        this.parameter = parameter;
        props.setProperty(PipelineConfigurator.USE_POS.key,
                Configurator.TRUE);
        props.setProperty(PipelineConfigurator.USE_LEMMA.key,
                Configurator.TRUE);
        props.setProperty(PipelineConfigurator.USE_SHALLOW_PARSE.key,
                Configurator.TRUE);

        props.setProperty(PipelineConfigurator.USE_NER_CONLL.key,
                Configurator.TRUE);
        props.setProperty(PipelineConfigurator.USE_NER_ONTONOTES.key,
                Configurator.TRUE);
        props.setProperty(PipelineConfigurator.USE_STANFORD_PARSE.key,
                Configurator.TRUE);
        props.setProperty(PipelineConfigurator.USE_STANFORD_DEP.key,
                Configurator.TRUE);

        props.setProperty(PipelineConfigurator.USE_SRL_VERB.key,
                Configurator.FALSE);
        props.setProperty(PipelineConfigurator.USE_SRL_NOM.key,
                Configurator.FALSE);
        props.setProperty(
                PipelineConfigurator.THROW_EXCEPTION_ON_FAILED_LENGTH_CHECK.key,
                Configurator.FALSE);
        props.setProperty(
                PipelineConfigurator.USE_JSON.key,
                Configurator.FALSE);
        props.setProperty(
                PipelineConfigurator.USE_LAZY_INITIALIZATION.key,
                Configurator.FALSE);
        props.setProperty(
                PipelineConfigurator.USE_SRL_INTERNAL_PREPROCESSOR.key,
                Configurator.FALSE);


        props.setProperty(AnnotatorServiceConfigurator.DISABLE_CACHE.key,
                Configurator.TRUE);
        props.setProperty(AnnotatorServiceConfigurator.CACHE_DIR.key,
                "/tmp/aswdtgffasdfasd");
        props.setProperty(
                AnnotatorServiceConfigurator.THROW_EXCEPTION_IF_NOT_CACHED.key,
                Configurator.FALSE);
        props.setProperty(
                AnnotatorServiceConfigurator.FORCE_CACHE_UPDATE.key,
                Configurator.TRUE);

        processor = PipelineFactory
                .buildPipeline(new ResourceManager(props));
    }

    public void startWorking() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(parameter.host);
        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        final Channel channel2 = connection.createChannel();

        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
        channel2.queueDeclare("done", true, false, false, null);
        channel2.queueDeclare("failed", true, false, false, null);

//        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        channel.basicQos(1);

        final AtomicInteger counter = new AtomicInteger();

        final Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                String message = new String(body, "UTF-8");

//                System.out.println(" [x] Received '" + message + "'");

                FileUtils.write(new File("/tmp/lastread"), message);

                int v = counter.incrementAndGet();
                System.out.println(String.format("%d processed.. ", v));

                GenerateData.Document d = null;
                TextAnnotation ta = null;
                try {
                    d = GSON.fromJson
                            (message,
                                    GenerateData.Document.class);
                    ta = processor.createAnnotatedTextAnnotation(d.getCorpId
                            (), d
                            .getTextId(), d.getRawText());
                } catch (AnnotatorException e) {
                    e.printStackTrace();
//                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                System.out.println("144");
                if (ta != null) {
//                    System.out.println("146");
                    String js = SerializationHelper.serializeToJson(ta);
                    channel2.basicPublish("", "done",
                            MessageProperties.PERSISTENT_TEXT_PLAIN,
                            js.getBytes());
                } else {
//                    System.out.println("147");
                    d.errorCount++;
//                    System.out.println("148");
                    channel2.basicPublish("", "failed",
                            MessageProperties.PERSISTENT_TEXT_PLAIN,
                            message.getBytes());

//                    System.out.println("Something wrong...");
                }

                channel.basicAck(envelope.getDeliveryTag(), false);

            }

        };

        boolean autoAck = false;
        channel.basicConsume(TASK_QUEUE_NAME, autoAck, consumer);
    }


    public static void main(String[] args) throws IOException,
            AnnotatorException, TimeoutException {

        WorkerParameter parameters = new WorkerParameter();

//        if (args.length == 0) {
//            new JCommander(parameters).usage();
//            System.exit(1);
//        }

        new JCommander(parameters).parse(args);

        Worker gd = new Worker(parameters);
        gd.startWorking();
    }

}
