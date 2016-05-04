package com.ksam.server.servlets;

import com.ksam.server.storage.SpatialManager;
import com.ksam.server.storage.SpatialRecord;
import com.sun.deploy.util.ArrayUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by jdownes on 4/10/2016.
 *
 * submit kml reports with a unique source id
 */
public class SubmitServlet extends HttpServlet {
    public static final String WKT="wkt";
    public static final String SOURCE_ID="sourceId";
    public static final String RECORD_ID="recordId";
    public static final String CONTENTS="contents";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //super.doGet(req, resp);
        try {

            System.out.println("Do get hit");
            Map<String, String[]> params = req.getParameterMap();
            for (String key : params.keySet()) {
                System.out.println(key + ": " + params.get(key)[0]);
            }
            String source = params.get(SOURCE_ID)[0];
            String record = params.get(RECORD_ID)[0];
            String contents = params.get(CONTENTS)[0];
            String wkt = params.get(WKT)[0];


            SpatialManager manager = new SpatialManager();

            SpatialRecord sr = new SpatialRecord();


            sr.setSourceId(source);
            sr.setRecordId(record);
            sr.setText(contents);
            sr.setWkt(wkt);


            boolean result =manager.write(sr);
            System.out.println("Received record " + sr.getRecordId());

           /* resp.getOutputStream().print(Boolean.toString(result));
            resp.getOutputStream().flush();
            resp.getOutputStream().close();*/
        }catch(Exception e){
            e.printStackTrace();
            resp.getOutputStream().print("false");
            resp.getOutputStream().close();
        }
    }


}
