package com.ksam.server.workflow;

import com.ksam.server.storage.SpatialRecord;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jdownes on 4/10/2016.
 */
public class OperationPipeline implements Serializable{
    private String clientId;
    private String operationId;
    private List<String> startingWKTs = new ArrayList<>();
    private List<SpatialOperation> ops = new ArrayList<>();
    private Map<String, List<SpatialRecord>> varHolding = new HashMap<>();
    public List<SpatialRecord> execute(){
        List<SpatialRecord> results = new ArrayList<>();

        List<SpatialRecord> start= new ArrayList<>();
        if(startingWKTs!=null && startingWKTs.size()>0){
            WKTReader reader = new WKTReader();
            for(String wkt: startingWKTs){


                SpatialRecord s = new SpatialRecord();
                s.setWkt(wkt);
                start.add(s);

            }
        }
        for(int i =0; i <ops.size();i++){
            SpatialOperation op = ops.get(i);
            System.out.println("Executing op : "+op.getClass().getName());
            if(op.requiresVariableInput()){

                for(String reqVar : op.getInputVariables().keySet()){
                    System.out.println("op requires variable :"+reqVar);
                    if(varHolding.containsKey(reqVar)){
                        System.out.println("Var is found ");
                        op.getInputVariables().put(reqVar, varHolding.get(reqVar));
                    }
                }
            }
            start = op.execute(start);
            if(op.isVariable()){
                varHolding.put(op.getVariable(), start);
                start = new ArrayList<>();
            }
            results = start;
        }
        return results;
    }
    public List<String> getStartingWKTs() {
        return startingWKTs;
    }

    public void setStartingWKTs(List<String> startingWKTs) {
        this.startingWKTs = startingWKTs;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public List<SpatialOperation> getOps() {
        return ops;
    }

    public void setOps(List<SpatialOperation> ops) {
        this.ops = ops;
    }

    public static OperationPipeline parseScript(String script){
        System.out.println("Parsing script "+script);
        OperationPipeline op = new OperationPipeline();
        String[] formulas =script.split(";");

        for(String formula: formulas){
            System.out.println("Formula : "+formula);
            String[] functions = formula.split("->");
            for(String function : functions){
                System.out.println("Found function :"+function);
                String f = function.trim();
                String fl = f.toLowerCase();
                if(fl.startsWith("query")){
                    SpatialOperation s = new Query();
                    System.out.println("Built query");
                    s.loadArguments(parseFunctionArgs(f));
                    op.getOps().add(s);
                }else if(fl.startsWith("buffer")){
                    SpatialOperation s = new Buffer();
                    System.out.println("Built buffer");
                    s.loadArguments(parseFunctionArgs(f));
                    System.out.println("after parse args");
                    op.getOps().add(s);
                }else if(fl.startsWith("difference")){
                    SpatialOperation s = new Difference();
                    s.loadArguments(parseFunctionArgs(f));
                    op.getOps().add(s);
                }else if(fl.startsWith("intersect")){
                    SpatialOperation s = new Intersect();
                    s.loadArguments(parseFunctionArgs(f));
                    op.getOps().add(s);
                }else if(fl.startsWith("$")){
                    //is variable

                    if(op.getOps().size()>0){
                        String varName = fl;
                        op.getOps().get(op.getOps().size()-1).setVariable(varName);
                        System.out.println("Found a variable "+varName);
                    }
                }
            }
        }
        return op;
    }
    public static String parseFunctionArgs(String function){
        System.out.println("Parsing function "+function+" -> "+function.substring(function.indexOf("(")+1, function.lastIndexOf(")")));
        return function.substring(function.indexOf("(")+1, function.lastIndexOf(")"));
    }
}
