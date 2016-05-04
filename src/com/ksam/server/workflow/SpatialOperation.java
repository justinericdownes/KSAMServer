package com.ksam.server.workflow;

import com.ksam.server.storage.SpatialRecord;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jdownes on 4/10/2016.
 *
 * classes to implement
 * query, buffer, clip, intersect,
 */
public abstract class SpatialOperation implements Serializable {
    public abstract List<SpatialRecord> execute(List<SpatialRecord> input );
    public abstract boolean loadArguments(String arguments);



    private String variable = null;

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }
    public boolean isVariable(){
        return this.variable !=null;
    }


    private Map<String, List<SpatialRecord>> inputVariables = new HashMap<>();
    public boolean requiresVariableInput(){
        return inputVariables.keySet().size()>0;
    }

    public Map<String, List<SpatialRecord>> getInputVariables() {
        return inputVariables;
    }

    public void setInputVariables(Map<String, List<SpatialRecord>> inputVariables) {
        this.inputVariables = inputVariables;
    }
}
