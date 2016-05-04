package com.ksam.server.storage;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;



/**
 * Created by jdownes on 4/10/2016.
 */
public class SpatialRecord implements java.io.Serializable {
    //private Geometry geo;
    private String text;
    private String sourceId;
    private String recordId;
    private String wkt;
    public SpatialRecord(){

    }
    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String toKML(){
        return null;
    }

    public static Geometry asGEO(SpatialRecord sr) {
        WKTReader reader = new WKTReader();
        try {
            return reader.read(sr.getWkt());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }



    public String getWkt() {
        return wkt;
    }

    public void setWkt(String wkt) {
        this.wkt = wkt;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public SpatialRecord copy(){
        SpatialRecord c = new SpatialRecord();
        c.setWkt(wkt);
        c.setText(text);
        c.setSourceId(sourceId);
        c.setRecordId(recordId);
        return c;
    }
    public SpatialRecord merge(SpatialRecord other){
        SpatialRecord m = new SpatialRecord();
        m.setText(this.text+"\n"+other.getText());
        return m;
    }

}
