package edu.illinois.cs.cogcomp.finer.components.filters;

import edu.illinois.cs.cogcomp.core.datastructures.textannotation.TextAnnotation;
import edu.illinois.cs.cogcomp.core.utilities.SerializationHelper;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

import java.awt.*;

import static edu.illinois.cs.cogcomp.finer.entry.AnnotationFiles.decompress;


/**
 * Created by hao on 1/17/17.
 */
public class ReadMapDbResult {
    public static void main(String[] args) throws Exception {

        DB db = DBMaker
                .fileDB("/tmp/dump_cache_all_0")
                .closeOnJvmShutdown()
                .make();

        HTreeMap<String, byte[]> store = db.hashMap("annotated")
                .keySerializer(Serializer.STRING)
                .valueSerializer(Serializer.BYTE_ARRAY)
                .createOrOpen();

        System.out.println(store.size());
        int all = 0;
        int wrong = 0;
        for (Object k : store.keySet()){
            all++;
            byte[] json = decompress(store.get(k));
            try{
                TextAnnotation ta = SerializationHelper.deserializeTextAnnotationFromBytes(json);
//                System.out.println(ta);
            }catch (Exception a){
                wrong++;
                System.out.print(String.format("%d /%d\r", wrong, all));
            }
        }
        System.out.println("" + wrong + " failed..");
    }
}
