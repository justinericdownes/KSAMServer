package com.ksam.server.workflow;

import com.ksam.server.storage.SpatialManager;
import com.ksam.server.storage.SpatialRecord;
import com.vividsolutions.jts.geom.Geometry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jdownes on 4/13/2016.
 */
public class Query extends SpatialOperation {

    @Override
    public List<SpatialRecord> execute(List<SpatialRecord> input) {
        //if has starting geometry query for coverage inspatial manager first
        //scan for key words
        System.out.println("executing search");
        List<SpatialRecord> prelim = queryWords(keyWords);
        if(input!= null && input.size()>0){
            System.out.println("Input is "+input.size());
            if(keyWords.size()>0) {
                System.out.println("keywords present ");
                prelim = overlaps(input, prelim);
            }else{
                SpatialManager manager = new SpatialManager();
                prelim = overlaps(input, manager.getSpatialRecords());
            }

        }

        return prelim;
    }

    public List<SpatialRecord> overlaps(List<SpatialRecord> arg1, List<SpatialRecord> arg2){
        System.out.println("overlaps called");
        List<SpatialRecord> covers = new ArrayList<>();
        for(SpatialRecord a2 : arg2){
            boolean found = false;
            Geometry g2 = SpatialRecord.asGEO(a2);
            for(SpatialRecord a1: arg1){
                Geometry g1 = SpatialRecord.asGEO(a1);
                if(g1.overlaps(g2)|| g1.contains(g2)|| g1.intersects(g2)||g1.covers(g2)||g1.crosses(g2)||g1.touches(g2)){
                    found = true;
                    break;
                }
            }
            if(found) covers.add(a2);
        }
        return covers;
    }

    private List<String> keyWords = new ArrayList<>();
    @Override
    public boolean loadArguments(String arguments) {
        //right now only key word
        String[] words = arguments.substring(arguments.indexOf("\"")+1, arguments.lastIndexOf("\"")).split("\\s+");
        for(String word : words){
            keyWords.add(word);
        }
        return true;
    }
    public List<SpatialRecord> queryWords(List<String> words){
        List<SpatialRecord> hits = new ArrayList<>();
        SpatialManager manager = new SpatialManager();
        for(SpatialRecord sr: manager.getSpatialRecords()) {
            boolean found = false;
            for (String word : words) {
                System.out.println("Searching for ("+word.toLowerCase()+" within ("+sr.getText().toLowerCase()+")");
                if(sr.getText().toLowerCase().contains(word.toLowerCase())){
                    found = true;
                    break;
                }
            }
            if(found) hits.add(sr);
        }
        return hits;
    }


}
