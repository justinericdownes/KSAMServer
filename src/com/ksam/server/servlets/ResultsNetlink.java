package com.ksam.server.servlets;

import com.ksam.server.storage.SpatialManager;
import com.ksam.server.storage.SpatialRecord;
import com.ksam.server.workflow.Query;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jdownes on 4/10/2016.
 */
public class ResultsNetlink extends HttpServlet {

    public static final String USER_ID="userId";
    //given a query id spit out the kml results
    public static final String BBOX ="BBOX";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //super.doGet(req, resp);
        try {
            String client = req.getParameter(USER_ID);
            System.out.println("Received results request for : " + client);
            if (client.equalsIgnoreCase("all")) {
                String wkt = getBboxWKT(req);
                Query q = new Query();
                List<SpatialRecord> start = new ArrayList<>();
                SpatialRecord s = new SpatialRecord();
                s.setWkt(wkt);
                start.add(s);
                List<SpatialRecord> hits = q.execute(start);
                ResultsKMLBuilder builder = new ResultsKMLBuilder();
                String kml = builder.buildKML(hits);
                resp.setContentType("application/vnd.google-earth.kml+xml");
                PrintWriter writer = resp.getWriter();
                writer.write(kml);
                writer.flush();
                writer.close();
            } else {
                SpatialManager sm = new SpatialManager();
                Map<String, List<SpatialRecord>> results = sm.getOperationResults(client);
                ResultsKMLBuilder builder = new ResultsKMLBuilder();
                String kml = builder.buildKML(results);
                System.out.println("KML = " + kml);
                resp.setContentType("application/vnd.google-earth.kml+xml");
                PrintWriter writer = resp.getWriter();
                writer.write(kml);
                writer.flush();
                writer.close();
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    public String getBboxWKT(HttpServletRequest req){
        String wkt=null;
        String bboxStr = req.getParameter(BBOX);
        Map<String, String[]> params = req.getParameterMap();
        for(String key: params.keySet()){
            System.out.println("key="+key);
        }
        if(bboxStr !=null){
            //lower left lon, lower left lat, upper right lon, upper right lat
            String[] corners = bboxStr.split(",");
            if(corners.length==4){
                wkt = "POLYGON((" +
                        corners[0]+" "+corners[1]+","+
                        corners[0]+" "+corners[3]+","+
                        corners[2]+" "+corners[3]+","+
                        corners[2]+" "+corners[1]+","+

                        corners[0]+" "+corners[1]+
                        "))";
            }
            System.out.println("bbox detected = "+bboxStr);
            System.out.println("WKT detected ="+wkt);
        }

        return wkt;
    }
}
