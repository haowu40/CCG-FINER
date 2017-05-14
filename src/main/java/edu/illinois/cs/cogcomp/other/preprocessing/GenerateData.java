package edu.illinois.cs.cogcomp.other.preprocessing;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import edu.illinois.cs.cogcomp.annotation.AnnotatorException;
import edu.illinois.cs.cogcomp.thrift.curator.Record;
import org.apache.commons.io.FileUtils;
import org.apache.thrift.TDeserializer;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static edu.illinois.cs.cogcomp.finer.utils.StringUtils.pad;


/**
 * Created by haowu4 on 1/10/17.
 */
public class GenerateData {

    public static final Gson GSON = new GsonBuilder().create();

    public static class Document {
        String rawText;
        String corpId;
        String textId;

        public int errorCount = 0;


        public Document(String corpId, String textId, String rawText) {
            this.rawText = rawText;
            this.corpId = corpId;
            this.textId = textId;
        }

        public String getRawText() {
            return rawText;
        }

        public String getCorpId() {
            return corpId;
        }

        public String getTextId() {
            return textId;
        }
    }

    public static class GenerateDataParameter {

        @Parameter(names = {"-h", "-host"}, description = "RabbitMQ Hosts")
        String host;

        @Parameter(names = {"-f", "-folder"}, description = "Folder " +
                "location that contains the list of Records")
        String recordFolders;
        @Parameter(names = {"-l", "-filelist"}, description = "List of " +
                "files.")
        String fileLists;
    }

    private GenerateDataParameter parameter;

    public GenerateData(GenerateDataParameter parameter) throws IOException,
            AnnotatorException {
        this.parameter = parameter;

    }

    public void processFiles() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(parameter.host);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare("test", true, false, false, null);


        List<String> files = null;
        try {
            files = FileUtils.readLines(new File(parameter.fileLists),
                    Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }

        int fileCounter = 0;
        int length = files.size();
        int fileCounterNonReset = 0;

        System.out.println("Start processing " + length + " files");

        for (String file : files) {
            fileCounterNonReset++;

            System.out.printf("Processed document %s/%s\r",
                    pad(Integer.toString(fileCounterNonReset),
                            9, ' '),
                    pad(Integer.toString(length), 9, ' '));

            Record r = null;
            try {
                File input = new File(parameter.recordFolders, file);
                r = deserializeRecord(FileUtils.readFileToByteArray(input));
            } catch (TException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (r == null) {
                continue;
            }

            Document d = new Document("kbp", file, r.getRawText());
            channel.basicPublish("", "test",
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    GSON.toJson(d).getBytes());


        }
    }

    public static Record deserializeRecord(byte[] bytes) throws IOException,
            TException {
        Record obj = new Record();
        TDeserializer td = new TDeserializer(new TBinaryProtocol.Factory());
        td.deserialize(obj, bytes);
        return obj;
    }


    public static void main(String[] args) throws IOException,
            AnnotatorException, TException, TimeoutException {

        GenerateDataParameter parameters = new GenerateDataParameter();

        if (args.length == 0) {
            new JCommander(parameters).usage();
            System.exit(1);
        }

        new JCommander(parameters).parse(args);

        GenerateData gd = new GenerateData(parameters);
        gd.processFiles();
    }
}
