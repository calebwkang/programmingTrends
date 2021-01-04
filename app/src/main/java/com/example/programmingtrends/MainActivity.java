package com.example.programmingtrends;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.TextView;
import org.json.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private String[] cities = {"Boston", "San Francisco", "Los Angeles", "Denver", "Boulder", "Chicago", "New York", "Raleigh"};
    private String[] languages = {"Java", "C#", "Python", "Swift", "Objective-C", "Ruby", "Kotlin", "Go", "C++", "Scala"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        displayData();
    }

    private void displayData() {
        for (String city: cities) {
            displayCity(city);
        }
    }

    private void displayCity(String city) {
        Thread resultThread = makeRequest(city, null, null);
        resultThread.start();
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

                    while ((inputLine = in.readLine()) != null) {
                        inputLine = "{ jobs: "+inputLine+"}";

                        JSONObject jsonObject = new JSONObject(inputLine);

                        if (lang == null && totalJobs == null) {
                            handleCityRequest(jsonObject, city);
                        } else if (lang != null && totalJobs != null) {
                            handleCityLangRequest(jsonObject, city, lang, totalJobs);
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

    // Handles response of GET request with just location param
    private void handleCityRequest(JSONObject jsonObject, String city) {
        try {
            JSONArray array = (JSONArray) jsonObject.get("jobs");
            int jobTotal = array.length();

            TextView textView = getTextView(city);
            if (jobTotal>0) {
                textView.setText(city + ":");
            } else {
                textView.setText(city + ": No Jobs");
            }

            for (String lang: languages) {
                Thread langThread = makeRequest(city, lang, jobTotal);
                langThread.start();
            }

        } catch (JSONException je) { System.out.println("JSON Exception error converting"); }

    }

    // Handles response of GET request with location and description params
    private void handleCityLangRequest(JSONObject jsonObject, String city, String lang, Integer totalJobs) {
        try {
            JSONArray array = (JSONArray) jsonObject.get("jobs");
            TextView textView = getTextView(city);
            int cityLangJobs = array.length();

            if (totalJobs > 0 && cityLangJobs > 0) {
                double percentage = (cityLangJobs * 100.0) / (totalJobs);

                DecimalFormat df = new DecimalFormat("###.#");
                textView.setText(textView.getText() + " " + lang + " " + df.format(percentage) + "%;");
            }

        } catch (JSONException je) {System.out.println("Error converting JSON at lang request level");}
    }

    // Returns the respective TextView for a city string
    private TextView getTextView(String city) {
        switch (city) {
            case "Boston":
                return this.findViewById(R.id.city0);
            case "San Francisco":
                return this.findViewById(R.id.city1);
            case "Los Angeles":
                return this.findViewById(R.id.city2);
            case "Denver":
                return this.findViewById(R.id.city3);
            case "Boulder":
                return this.findViewById(R.id.city4);
            case "Chicago":
                return this.findViewById(R.id.city5);
            case "New York":
                return this.findViewById(R.id.city6);
            case "Raleigh":
                return this.findViewById(R.id.city7);
            default:
                return null;
        }
    }
}