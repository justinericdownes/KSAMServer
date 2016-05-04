package com.ksam.server.storage;

import com.ksam.server.workflow.OperationPipeline;
import com.ksam.server.workflow.SpatialOperation;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jdownes on 4/10/2016.
 */
public class SpatialManager {
    private static List< OperationPipeline> loadedOps;
    private static List<SpatialRecord> records;
    public static final String RECORD_DIR="./records";
    public static final String OPERATION_DIR="./operations";


    //write kml
    //manage queries



    //need lucene
    public static boolean initialized= false;
    public SpatialManager(){
        init();
    }
    public List<SpatialRecord> getSpatialRecords(){
        return records;
    }
    public static File recordsDir;
    public static File opsDir;
    private void init(){
        //on startup

        if(!initialized){
            recordsDir = new File(RECORD_DIR);
            System.out.println("Checking record directory "+recordsDir.getAbsolutePath());
            if(!recordsDir.exists()) {
                try{
                    recordsDir.mkdir();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else{
                //load records
                File [] recordFiles = recordsDir.listFiles();
                records = new ArrayList<>();
                for(File record: recordFiles){
                    if(record.getName().endsWith(".ser")){
                        try {
                            FileInputStream fis = new FileInputStream(record);
                            ObjectInputStream ois = new ObjectInputStream(fis);
                            SpatialRecord r =  (SpatialRecord) ois.readObject();
                            records.add(r);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
            opsDir = new File(OPERATION_DIR);
            if(!opsDir.exists()){
                try{
                    opsDir.mkdir();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else{
                //load operations
                File[] opsFiles = opsDir.listFiles();
                loadedOps = new ArrayList<>();
                for(File ops : opsFiles){
                    if(ops.getName().endsWith(".ser")){
                        try {
                            FileInputStream fis = new FileInputStream(ops);
                            ObjectInputStream ois = new ObjectInputStream(fis);
                            OperationPipeline o =  (OperationPipeline) ois.readObject();
                            loadedOps.add(o);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        initialized=true;
    }


    public boolean write(SpatialRecord record){
        //write file

        String recordFileName = record.getSourceId()+"_"+record.getRecordId()+".ser";
        File recordFile = new File(recordsDir, recordFileName);
        System.out.println("Spatial record write called : "+recordFile.getAbsolutePath());
        try {
            FileOutputStream fos = new FileOutputStream(recordFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(record);
            oos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        //add or update list
        SpatialRecord found = null;
        for(SpatialRecord r : records){
            if(r.getRecordId().equals(record.getRecordId())&& r.getSourceId().equals(record.getSourceId())){
                found = r;
            }
        }
        if(found!=null){
            records.remove(found);
        }
        records.add(record);
        return true;
    }

    public boolean writeOperation(OperationPipeline op){
        String opFileName = op.getClientId()+"_"+op.getOperationId()+".ser";
        File opFile = new File(opsDir, opFileName);
        try {
            FileOutputStream fos = new FileOutputStream(opFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(op);
            oos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        OperationPipeline found = null;
        for(OperationPipeline o : loadedOps){
            if(o.getOperationId().equals(op.getOperationId())&& o.getClientId().equals(op.getClientId())){
                found = o;
            }
        }
        if(found!=null){
            loadedOps.remove(found);
        }
        loadedOps.add(op);
        return true;
    }

    public Map<String, List<SpatialRecord>> getOperationResults(String userId){
        Map<String, List<SpatialRecord>> results = new HashMap<>();
        for(OperationPipeline op : loadedOps){
            if(op.getClientId().equals(userId)){
                List<SpatialRecord> opResults = op.execute();
                results.put(op.getOperationId(), opResults);
            }
        }
        return results;
    }




}
