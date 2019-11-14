package sk.hlavco.hladacBytov;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PropertyFile {

    private static final Logger LOGGER = Logger.getLogger( PropertyFile.class.getName() );

    private String advertisementFileN = "Inzeraty_nehnutelnosti.properties";
    private String advertisementFileT = "Inzeraty_topreality.properties";
    private String configFile = "Config.properties";

    private String getAdvertisementFileName(int portal){

        if(portal == 1){
            return advertisementFileN;

        } else {
            return advertisementFileT;
        }
    }

    public boolean isInAdvertisementFile(String key, int portal){

        return isInPropertyFile(key, getAdvertisementFileName(portal));
    }

    public String findInAdvertisementFile(String key, int portal){
        return findInPropertyFile(key, getAdvertisementFileName(portal));
    }

    public void saveToAdvertisementFile(String key, String value, int portal){
        saveToPropertyFile(key, value, getAdvertisementFileName(portal));
    }

    public boolean isInConfigFile(String key){
        return isInPropertyFile(key, configFile);
    }

    public String findInConfigFile(String key){
        return findInPropertyFile(key, configFile);
    }

    public String getPathToFile(String fileName){

        File dir = new File(PropertyFile.class.getProtectionDomain().getCodeSource().getLocation().getPath());

//        System.out.println("TATO TRIEDA JE V ADRESARI: " + dir.toString());

        if(dir.toString().contains(".jar")) {
            File filePath = new File(dir.getParentFile(), fileName);

            System.out.println("PRACUJEM SO SUBOROM: " + filePath.toString());

            return filePath.toString();
        }
        return fileName;
    }

    private boolean isInPropertyFile(String key, String file){
        try {

            FileInputStream in = new FileInputStream(getPathToFile(file));
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
            FileInputStream in = new FileInputStream(getPathToFile(file));
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
//            String filePath = getClass().getClassLoader().getResource(file).getFile();
            FileInputStream in = new FileInputStream(getPathToFile(file));
            Properties props = new Properties();
            props.load(in);
            in.close();

            //write
            FileOutputStream out = new FileOutputStream(getPathToFile(file));
            props.setProperty(key, value);
            props.store(out, null);
            out.close();

            LOGGER.log(Level.INFO, "property with key: " + key + " stored with value: " + value + " to file: " + file);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
