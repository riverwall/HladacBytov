package sk.hlavco.hladacBytov;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PropertyFile {

    private static final Logger LOGGER = Logger.getLogger( PropertyFile.class.getName() );

    private String advertisementFile = "src/sk/hlavco/hladacBytov/resources/Advertisements.properties";
    private String configFile = "src/sk/hlavco/hladacBytov/resources/Config.properties";

    public boolean isInAdvertisementFile(String key){
        return isInPropertyFile(key, advertisementFile);
    }

    public String findInAdvertisementFile(String key){
        return findInPropertyFile(key, advertisementFile);
    }

    public void saveToAdvertisementFile(String key, String value){
        saveToPropertyFile(key, value, advertisementFile);
    }

    public boolean isInConfigFile(String key){
        return isInPropertyFile(key, configFile);
    }

    public String findInConfigFile(String key){
        return findInPropertyFile(key, configFile);
    }

    private boolean isInPropertyFile(String key, String file){
        try {
            FileInputStream in = new FileInputStream(file);
            Properties props = new Properties();
            props.load(in);
            in.close();

            if(props.containsKey(key)) {
                return true;

            } else {
                return false;
            }


        }catch (IOException ex){
            ex.printStackTrace();
            return false;
        }


    }

    private String findInPropertyFile(String key, String file){
        try {
            FileInputStream in = new FileInputStream(file);
            Properties props = new Properties();
            props.load(in);
            in.close();

            String value = props.getProperty(key);

            if(!value.isEmpty()) {
                LOGGER.log(Level.INFO, "Searching criteria : " + value);
                return value;

            }

            return null;


        }catch (IOException ex){
            ex.printStackTrace();
            return null;
        }


    }

    private void saveToPropertyFile(String key, String value, String file){

        try {
            //read
            FileInputStream in = new FileInputStream(file);
            Properties props = new Properties();
            props.load(in);
            in.close();

            //write
            FileOutputStream out = new FileOutputStream(file);
            props.setProperty(key, value);
            props.store(out, null);
            out.close();

            LOGGER.log(Level.INFO, "property with key: " + key + " stored with value: " + value + " to file: " + file);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
