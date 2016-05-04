package com.ksam.server.workflow;

import com.ksam.server.storage.SpatialRecord;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTWriter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jdownes on 4/30/2016.
 */
public class Buffer extends SpatialOperation {
    @Override
    public List<SpatialRecord> execute(List<SpatialRecord> input) {

        List<SpatialRecord> out = new ArrayList<>();
        WKTWriter writer = new WKTWriter();
        for(SpatialRecord in : input){
            SpatialRecord copy = in.copy();
            Geometry g = SpatialRecord.asGEO(in);
            double lat = metersToLongitude(g.getCentroid().getY(), Math.round(distance));
            copy.setWkt(writer.write(SpatialRecord.asGEO(in).buffer(lat)));
            out.add(copy);
        }
        return out;
    }
    public static double metersToLongitude(double lat, long meters){
        double rlat = lat * Math.PI/180;
        double a = meters /(111412.84 * Math.cos(rlat)- 93.5 * Math.cos(3*rlat));
        return a;
    }
    public static double metersToLatitude(double lat, long meters){
        double rlat = lat * Math.PI/180;
        double a = meters/(111132.92 - 559.82 * Math.cos(2 * rlat) + 1.175 * Math.cos(4 *rlat));
        return a;
    }

    private double distance = 0.0;
    @Override
    public boolean loadArguments(String arguments) {
        //right now only distance


        String value = arguments.substring(arguments.indexOf("\"")+1, arguments.lastIndexOf("\""));
        Double v = Double.parseDouble(value);
        if(arguments.toLowerCase().startsWith("meters")){
            distance = v;
        }else if(arguments.toLowerCase().startsWith("kilometers")){
            distance = v*1000;
        }else if(arguments.toLowerCase().startsWith("feet")){
            distance = v * .3048;
        }else if(arguments.toLowerCase().startsWith("yards")){
            distance = (v/3) * .3048;
        }else if(arguments.toLowerCase().startsWith("miles")){
            distance = v * 1609.34;
        }

        return true;
    }
}
