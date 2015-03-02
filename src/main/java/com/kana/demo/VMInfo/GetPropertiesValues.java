package com.kana.demo.VMInfo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by aditya on 02/03/15.
 */
public class GetPropertiesValues {


    Properties prop = new Properties();

    public boolean checkPropertiesFile() throws IOException{

        String propFIleName = "config.properties";

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFIleName);

        if (inputStream != null) {
            prop.load(inputStream);
            return true;
        }else {
            throw new FileNotFoundException("property file " + propFIleName + " not found");
        }

    }


    public String getURLProperties() {

        return prop.getProperty("url");

    }

    public String getUserNameProperties() {

        return prop.getProperty("userName");

    }

    public String getPasswordProperties() {

        return prop.getProperty("password");

    }
}
