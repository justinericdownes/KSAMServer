package com.ksam.server.servlets;

import com.ksam.server.storage.SpatialManager;
import com.ksam.server.workflow.OperationPipeline;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by jdownes on 4/10/2016.
 */
public class QueryServlet extends HttpServlet{
    public static final String CLIENT_ID="clientId";
    public static final String OPERATION_ID="opId";
    public static final String START_WKTS="startWKTs";
    public static final String QUERY_STRING="queryString";
    //this can also have starting geometries or start with a query op


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //super.doGet(req, resp);
        System.out.println("Received operation:");
        try {
            String clientId = req.getParameter(CLIENT_ID);
            String startWKTs = req.getParameter(START_WKTS);
            String queryString = req.getParameter(QUERY_STRING);
            String operationId = req.getParameter(OPERATION_ID);
            OperationPipeline op = OperationPipeline.parseScript(queryString);
            op.setClientId(clientId);
            op.setOperationId(operationId);
            if (startWKTs != null && startWKTs.length() > 0) {
                String[] wkts = startWKTs.split(";");
                for (String wkt : wkts) {
                    if (!wkt.trim().isEmpty()) {
                        op.getStartingWKTs().add(wkt);
                    }
                }
            }

            System.out.println("name: " + op.getOperationId());
            System.out.println("query: " + queryString);
            SpatialManager manager = new SpatialManager();
            manager.writeOperation(op);
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
