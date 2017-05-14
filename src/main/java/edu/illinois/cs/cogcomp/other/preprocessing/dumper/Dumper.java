package edu.illinois.cs.cogcomp.other.preprocessing.dumper;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.rabbitmq.client.*;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation
        .TextAnnotation;
import edu.illinois.cs.cogcomp.core.utilities.SerializationHelper;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeoutException;

/**
 * Created by haowu4 on 1/10/17.
 */
public class Dumper {
    public static class DumperParameter {

        @Parameter(names = {"-h", "-host"}, description = "RabbitMQ Hosts")
        String host = "54.204.245.88";

        @Parameter(names = {"-o", "-output"}, description = "Output location")
        String output;

        @Parameter(names = {"-d", "-done"}, description = "Output one time " +
                "and done")
        boolean oneAndDone;

        @Parameter(names = {"-s", "-start"}, description = "Number of db file" +
                " id to start with.")
        int start = 0;

        @Parameter(names = {"-l", "-limit"}, description = "Number of " +
                "document per db file.")
        int limit = 10000;

        @Override
        public String toString() {
            return "DumperParameter{" +
                    "host='" + host + '\'' +
                    ", output='" + output + '\'' +
                    ", oneAndDone=" + oneAndDone +
                    ", start=" + start +
                    ", limit=" + limit +
                    '}';
        }
    }


    DumperParameter parameter;

    DB db;
    HTreeMap<String, String> store;
    int counter = 0;
    int fileCounter = 0;

    public Dumper(DumperParameter parameter) {
        counter = parameter.start;
        this.parameter = parameter;
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(parameter.host);
        makeNewDB();
    }

    public void makeNewDB() {
        if (counter > 0 && db != null) {
            db.commit();
            store.close();
            db.close();
        }

        db = DBMaker
                .fileDB(String.format("%s_%d", parameter.output, counter))
                .closeOnJvmShutdown()
                .make();

        store = db.hashMap("annotated")
                .keySerializer(Serializer.STRING)
                .valueSerializer(Serializer.STRING)
                .createOrOpen();
        counter++;
    }

    public synchronized boolean pushToMap(String k, String v) {
//        boolean ret = false;
        fileCounter++;
        store.put(k, v);
        db.commit();
        if (fileCounter == parameter.limit) {
            fileCounter = 0;
            return true;
        }
        return false;
    }

    public void startWorking() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(parameter.host);
        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();
        final Channel channel1 = connection.createChannel();

        channel.queueDeclare("done", true, false, false, null);
        channel1.queueDeclare("backup", true, false, false, null);

        channel.basicQos(1);

        final Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
//                channel1.basicPublish("", "backup",
//                        MessageProperties.PERSISTENT_TEXT_PLAIN,
//                        body);

                System.out.print(String.format("%d processed ", fileCounter));

                String message = new
                        String(body, Charset.defaultCharset());
                boolean newDbMade = false;
                TextAnnotation ta = null;
                try {
                    ta = SerializationHelper.deserializeFromJson
                            (message);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (ta != null) {
                    newDbMade = pushToMap(ta.getId(), message);
                }

                System.out.print(" pushed to map");

                if (newDbMade) {
                    System.out.print("New DB made");

                    try {
                        channel1.queuePurge("backup");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (parameter.oneAndDone) {
                        System.exit(0);
                    } else {
                        makeNewDB();
                    }
                }

                System.out.print("...");

                channel.basicAck(envelope.getDeliveryTag(), false);
                System.out.print(" Done \r");

            }
        };

        boolean autoAck = false;
        channel.basicConsume("done", autoAck, consumer);
    }

    public static void main(String[] args) throws IOException,
            TimeoutException {

        DumperParameter parameters = new DumperParameter();

        new JCommander(parameters).parse(args);
        System.out.println(parameters);
        Dumper gd = new Dumper(parameters);
        gd.startWorking();
    }

}
