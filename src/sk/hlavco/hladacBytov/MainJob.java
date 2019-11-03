package sk.hlavco.hladacBytov;

import javax.mail.MessagingException;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainJob {

    static PropertyFile propFile = new PropertyFile();
    GoogleMail gMail = new GoogleMail();


    boolean mailSendEnable = true;

    private static final Logger LOGGER = Logger.getLogger( MainJob.class.getName() );

    public static void main(String[] args) throws IOException {

        FileHandler fh;
        fh = new FileHandler(propFile.getPathToFile("LOGS.log"));
        LOGGER.addHandler(fh);

        LOGGER.log(Level.INFO, "Start iteration");

        MainJob mainJob = new MainJob();
        mainJob.oneIteration();
    }

    public void oneIteration() {
        webSearch("searchUrl1");
        webSearch("searchUrl2");
        webSearch("searchUrl3");
        webSearch("searchUrl4");
    }

    public void advertisementDecide(String id, String url) {

        if (!propFile.isInAdvertisementFile(id)){

            if(mailSendEnable) {

                String mailUsername = propFile.findInConfigFile("mailUsername");
                String mailPassword = propFile.findInConfigFile("mailPassword");
                String mailRecipient = propFile.findInConfigFile("mailRecipient");
                String mailTitle = propFile.findInConfigFile("mailTitle");

                //poslanie mailu
                try {
                    gMail.Send(mailUsername, mailPassword, mailRecipient, mailTitle, url);

                    //ulozenie do properties file
                    propFile.saveToAdvertisementFile(id, url);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public void webSearch(String searchUrl) {
        URL url;
        InputStream is = null;
        DataInputStream dis;
        String site;
        String line;
        String lineUtf;
        int chIndexStart;
        int chIndexFinish;
        String inzeratId = null;
        String inzeratUrl = null;


        try {
            if(!propFile.isInConfigFile(searchUrl)){
                return;
            }

            url = new URL(propFile.findInConfigFile(searchUrl));
            is = url.openStream();  // throws an IOException
            dis = new DataInputStream(new BufferedInputStream(is));


            while ((line = dis.readLine()) != null) {

                byte[] ptext = line.getBytes("ISO-8859-1");


                lineUtf = (new String(ptext, "UTF-8"));

                //praca s ID inzeratu
                if(lineUtf.contains("<div class=\"advertisement")){
                    if(lineUtf.contains("id=\"adv-")){
                     chIndexStart = lineUtf.indexOf("id=\"adv-");
                     inzeratId = lineUtf.substring(chIndexStart + 8, chIndexStart + 15);
                    }
                }

                //praca s URL inzeratu
                if(inzeratId != null) {
                    if (lineUtf.contains("www.nehnutelnosti.sk/" + inzeratId)) {
                        if (lineUtf.contains("<a href=")) {
                            chIndexStart = lineUtf.indexOf("<a href=");

                            chIndexStart += 9;

                            if(lineUtf.contains("\" target=")){
                                chIndexFinish = lineUtf.indexOf("\" target=");
                                inzeratUrl = lineUtf.substring(chIndexStart, chIndexFinish);

                                advertisementDecide(inzeratId, inzeratUrl);
                                inzeratId = null;
                                inzeratUrl = null;
                            }
                        }
                    }
                }
//                System.out.println(lineUtf);

            }
            is.close();

        } catch (MalformedURLException mue) {
            mue.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();

        }
    }
}
