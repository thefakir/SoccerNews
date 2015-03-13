package com.example.fidelbarcaya.soccernews;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Formatter;

/**
 * Created by fidel.barcaya on 3/10/2015.
 */
public class Utility {
    private static final String LOG_TAG = Utility.class.getSimpleName();

    public static String getJsonStringFromNetwork() {
        Log.d(LOG_TAG, "Starting network connection");
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String team = "81";
        String timeFrame = "p30";

        try {
            final String FIXTURE_BASE_URL = "http://api.football-data.org/alpha/teams/";
            final String FIXTURE_PATH = "fixtures";
            final String TIME_FRAME_PARAMETER = "timeFrame";

            Uri builtUri = Uri.parse(FIXTURE_BASE_URL).buildUpon()
                    .appendPath(team)
                    .appendPath(FIXTURE_PATH)
                    .appendQueryParameter(TIME_FRAME_PARAMETER, timeFrame)
                    .build();
            URL url = new URL(builtUri.toString());

            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            if (inputStream == null)
                return "";
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;

            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }

            if (buffer.length() == 0)
                return "";

            return buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                    e.printStackTrace();
                }
            }
        }

        return "";
    }
    public static String[] parseFixtureJson(String fixtureJson) throws JSONException {
        JSONObject jsonObject = new JSONObject(fixtureJson);
        ArrayList<String> result = new ArrayList<>();

        final String LIST = "fixtures";
        final String HOME_TEAM = "homeTeamName";
        final String AWAY_TEAM = "awayTeamName";
        final String RESULT = "result";
        final String HOME_SCORE = "goalsHomeTeam";
        final String AWAY_SCORE = "goalsAwayTeam";

        JSONArray fixturesArray = jsonObject.getJSONArray(LIST);

        for (int i = 0; i < fixturesArray.length(); i++) {
            String homeTeam;
            String awayTeam;
            int homeScore;
            int awayScore;
            JSONObject matchObject = fixturesArray.getJSONObject(i);
            JSONObject resultObject = matchObject.getJSONObject(RESULT);

            homeTeam = matchObject.getString(HOME_TEAM);
            awayTeam = matchObject.getString(AWAY_TEAM);
            homeScore = resultObject.getInt(HOME_SCORE);
            awayScore = resultObject.getInt(AWAY_SCORE);

            String resultString = new Formatter().format("%s: %d - %s: %d", homeTeam, homeScore, awayTeam, awayScore).toString();
            result.add(resultString);
        }
        return result.toArray(new String[result.size()]);
    }
}
