/**
 * 
 */
package pl.chilczuk;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

/**
 * @author Michał Chilczuk
 *
 */
public class Dominikanie
{
    public static void main(String[] args)
    {
        FileFormConfig config = new FileFormConfig();
        String formURL = "https://docs.google.com/forms/d/" + config.getFormID() + "/formResponse?ifq";
        for (Entry<String, String> field : config.getFields().entrySet())
        {
            formURL += "&" + field.getKey() + "=" + field.getValue();
        }
        formURL += "&submit=Submit";

        try
        {
            Random rand = new Random(System.currentTimeMillis());
            while (true)
            {
                submitForm(formURL);
                Thread.sleep(rand.nextInt(10000) % 10);
            }
        } catch (IOException e)
        {
            System.err.println(e.getMessage());
        } catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static void submitForm(String formURL) throws MalformedURLException, IOException, ProtocolException
    {
        URL url = new URL(formURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.97 Safari/537.36");
        int responseCode = conn.getResponseCode();
        if (responseCode != 200)
        {
            System.err.println("ERROR HTTP CODE: " + responseCode);
        } else
        {
            System.out.println("Form submitted - OK");
        }
    }

    private static class FileFormConfig
    {

        private Properties config;

        public FileFormConfig()
        {
            File configFile = new File("config.properties");

            try (FileInputStream configFileStream = new FileInputStream(configFile))
            {
                config = new Properties();
                config.load(configFileStream);
            } catch (IOException e)
            {
                System.err.println(e.getMessage());
                return;
            }
        }

        public String getFormID()
        {
            return (String) config.getOrDefault("formID", "1fmILMIWovOG9K2gltCtRyQSeTCEAWol467d2LWBZxYQ");
        }

        public Map<String, String> getFields()
        {
            Set<Object> keys = config.keySet();
            keys.remove("formID");

            Map<String, String> fields = new HashMap<>();
            for (Object key : keys)
            {
                fields.put((String) key, config.getProperty((String) key));
            }

            return fields;
        }
    }
}
