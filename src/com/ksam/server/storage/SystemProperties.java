package com.ksam.server.storage;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by jdownes on 4/10/2016.
 */
public class SystemProperties {
    private static final String propFile ="./config.properties";
    private static SystemProperties sp = new SystemProperties();
    public static SystemProperties getInstance(){
        return sp;
    }
    private Properties props;
    private SystemProperties(){
        //read from prop file and load info
        props = new Properties();
        try {
            props.load(new FileInputStream(propFile));
        } catch (IOException e) {
            e.printStackTrace();
            props = null;
        }
    }


}
