package com.ksam.server.workflow;

import com.ksam.server.storage.SpatialRecord;
import com.vividsolutions.jts.geom.Geometry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by jdownes on 4/30/2016.
 */
public class Difference extends SpatialOperation {
    @Override
    public List<SpatialRecord> execute(List<SpatialRecord> input) {
        List<SpatialRecord>arg1;
        List<SpatialRecord>arg2 ;
        if(getInputVariables().keySet().size()==1){
            arg1 = input;
            arg2 = getInputVariables().values().iterator().next();
        }else if(getInputVariables().keySet().size()==2){
            Iterator<List<SpatialRecord>> iter = getInputVariables().values().iterator();
            arg1 = iter.next();
            arg2 = iter.next();
        }else{
            return new ArrayList<>();
        }

        return difference(arg1, arg2);
    }
    public List<SpatialRecord> difference(List<SpatialRecord> arg1, List<SpatialRecord> arg2){
        List<SpatialRecord> results = new ArrayList<>();
        for(SpatialRecord a1 : arg1){
            for(SpatialRecord a2 : arg2){
                Geometry i = SpatialRecord.asGEO(a1).difference(SpatialRecord.asGEO(a2));
                if(!i.isEmpty()){
                    SpatialRecord m = a1.merge(a2);
                    results.add(m);
                }
            }
        }
        return results;
    }

    @Override
    public boolean loadArguments(String arguments) {
        String[] vars = arguments.trim().split(",");
        for (String var : vars) {
            this.getInputVariables().put(var.trim(), new ArrayList<SpatialRecord>());
        }
        return true;
    }
}
