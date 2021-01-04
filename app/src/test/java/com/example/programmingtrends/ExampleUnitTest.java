package com.example.programmingtrends;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void testTotalJobNumbers () {
        String city = "Boston"; String lang = "Kotlin";

        Thread testJobNumbersThread = makeRequest(city, null, null);
        testJobNumbersThread.start();
    }

    @Test
    public void testRounding() {
        DecimalFormat df = new DecimalFormat("###.#");

        double num = 2.0/3.0;
        assertTrue(df.format(num).length() <= 4);

        num = 0.25;
        assertTrue(df.format(num).length() <= 4);
    }

    private Thread makeRequest(String city, String lang, Integer totalJobs) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    URL jobs = lang == null ? new URL("https://jobs.github.com/positions.json?location="+city): new URL("https://jobs.github.com/positions.json?location="+city+"&description="+lang);
                    URLConnection yc = jobs.openConnection();
                    BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                    String inputLine;
                    int lineCount = 0;

                    while ((inputLine = in.readLine()) != null) {
                        inputLine = "{ jobs: "+inputLine+"}";

                        JSONObject jsonObject = new JSONObject(inputLine);

                        if (lang == null && totalJobs == null) {
                            handleCityRequest(jsonObject, city);
                        } else if (lang != null && totalJobs != null) {
                            handleCityLangRequest(jsonObject, city, lang, totalJobs);
                        } else {
                            fail("Can't reach point where one of lang or totalJobs is null and one isn't!");
                        }

                    }
                    in.close();

                } catch (Exception e) {
                    System.out.println(e);
                }

            }
        };

        return new Thread(runnable);
    }

    private void handleCityRequest(JSONObject jsonObject, String city) {
        try {
            JSONArray array = (JSONArray) jsonObject.get("jobs");
            int jobTotal = array.length();

            assertTrue(jobTotal>=0);

            String lang = "Kotlin";
            Thread langThread = makeRequest(city, lang, jobTotal);
            langThread.start();

        } catch (JSONException je) {
            fail("JSON exception error parseing to json");
        }

    }

    private void handleCityLangRequest(JSONObject jsonObject, String city, String lang, Integer totalJobs) {
        try {
            JSONArray array = (JSONArray) jsonObject.get("jobs");
            int cityLangJobs = array.length();

            assertTrue(cityLangJobs>=0);

            if (cityLangJobs > 0 && totalJobs > 0) {
                assertTrue(cityLangJobs<totalJobs);
            }
        } catch (JSONException je) {System.out.println("Error converting JSON at lang request level");}
    }
}