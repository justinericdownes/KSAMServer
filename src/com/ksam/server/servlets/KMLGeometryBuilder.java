package com.ksam.server.servlets;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import java.text.DecimalFormat;

/**
 * Created by jdownes on 5/3/2016.
 */
public class KMLGeometryBuilder {

    public String getKMLGeometry(String wkt){
        StringBuilder sb = new StringBuilder();

        WKTReader reader = new WKTReader();
        try {
            Geometry geo = reader.read(wkt);
            String type = geo.getGeometryType();

            switch(type){
                case "Point":{
                    addPoint(geo, sb);
                    break;
                }
                case "LineString":{
                    addLineString(geo, sb);
                    break;
                }
                case "Polygon":{
                    //addLineString(geo, sb);
                    addPolygon(geo, sb);
                    break;
                }
                //TODO the rest of the geometries
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return sb.toString();
    }

    public String getKMLGeometry(Geometry geo){
        StringBuilder sb = new StringBuilder();

        String type = geo.getGeometryType();

        switch(type){
            case "Point":{
                addPoint(geo, sb);
                break;
            }
            case "LineString":{
                addLineString(geo, sb);
                break;
            }
            case "Polygon":{
                addPolygon(geo, sb);
                break;
            }
            //TODO the rest of the geometries
        }
        return sb.toString();
    }

    public void addPoint(Geometry geo, StringBuilder sb){
        sb.append("<Point><coordinates>"+geo.getCoordinate().x+","+geo.getCoordinate().y+"</coordinates></Point>");
    }
    public void addLineString(Geometry geo, StringBuilder sb){
        sb.append("<LineString>");
        sb.append("<extrude>1</extrude>");
        sb.append("<tessellate>1</tessellate>");
        sb.append("<coordinates>");
        for(Coordinate c : geo.getCoordinates()){

            sb.append(c.x+","+c.y+" ");
        }
        sb.deleteCharAt(sb.length()-1);

        sb.append("</coordinates>");
        sb.append("</LineString>");
    }
    public void addPolygon(Geometry geo, StringBuilder sb){
        sb.append("<Polygon>");
        sb.append("<extrude>1</extrude>");
        sb.append("<tessellate>1</tessellate>");
        sb.append("<altitudeMode>clampToGround</altitudeMode>");
        sb.append("<outerBoundaryIs>");
        sb.append("<LinearRing>");
        sb.append("<coordinates>");
        for(Coordinate c : geo.getCoordinates()){
            sb.append(c.x+","+c.y+" ");
        }
        sb.deleteCharAt(sb.length()-1);

        sb.append("</coordinates>");
        sb.append("</LinearRing>");
        sb.append("</outerBoundaryIs>");
        sb.append("</Polygon>");
    }

    public DecimalFormat format = new DecimalFormat("###.########");


}
