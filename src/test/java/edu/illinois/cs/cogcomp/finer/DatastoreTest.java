package edu.illinois.cs.cogcomp.finer;

import org.cogcomp.Datastore;
import org.cogcomp.DatastoreException;

/**
 * Created by haowu4 on 5/18/17.
 */
public class DatastoreTest {
    public static void main(String[] args) throws DatastoreException {
        Datastore ds = new Datastore();
        System.out.println(ds.toString());
    }
}
