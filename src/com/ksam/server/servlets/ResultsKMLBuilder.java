package com.ksam.server.servlets;

import com.ksam.server.storage.SpatialRecord;

import java.util.List;
import java.util.Map;

/**
 * Created by jdownes on 5/3/2016.
 */
public class ResultsKMLBuilder {
    public String buildKML(Map<String, List<SpatialRecord>> results){
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append("<kml xmlns=\"http://www.opengis.net/kml/2.2\">");
        sb.append("<Document>");
        sb.append("<Style id=\"polyStyle\"><PolyStyle><color>ff00ff00</color><fill>0</fill><outline>1</outline></PolyStyle><LineStyle><color>ff00ff00</color><width>4</width></LineStyle></Style>");
        for(String opName:results.keySet()){
            sb.append("<Folder><name>"+opName+"</name>");
            for(SpatialRecord record:results.get(opName)){
                sb.append("<Placemark><name>"+record.getRecordId()+"</name>");
                sb.append("<styleUrl>#polyStyle</styleUrl>");
                sb.append("<description>"+record.getText()+"</description>");
                KMLGeometryBuilder builder = new KMLGeometryBuilder();
                sb.append(builder.getKMLGeometry(record.getWkt()));
                sb.append("</Placemark>");
            }
            sb.append("</Folder>");
        }
        sb.append("</Document>");
        sb.append("</kml>");
        return sb.toString();
    }
    public String buildKML(List<SpatialRecord> results){
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append("<kml xmlns=\"http://www.opengis.net/kml/2.2\">");
        sb.append("<Document>");
        sb.append("<Style id=\"polyStyle\"><PolyStyle><color>ff00ff00</color><fill>0</fill><outline>1</outline></PolyStyle><LineStyle><color>ff00ff00</color><width>4</width></LineStyle></Style>");

        sb.append("<Folder><name>Records</name>");
        for(SpatialRecord record:results){
            sb.append("<Placemark><name>"+record.getRecordId()+"</name>");
            sb.append("<styleUrl>#polyStyle</styleUrl>");
            sb.append("<description>"+record.getText()+"</description>");
            KMLGeometryBuilder builder = new KMLGeometryBuilder();
            sb.append(builder.getKMLGeometry(record.getWkt()));
            sb.append("</Placemark>");
        }
        sb.append("</Folder>");

        sb.append("</Document>");
        sb.append("</kml>");
        return sb.toString();
    }
}
