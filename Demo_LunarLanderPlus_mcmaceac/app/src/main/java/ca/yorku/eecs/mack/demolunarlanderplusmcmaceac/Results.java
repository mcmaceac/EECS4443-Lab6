package ca.yorku.eecs.mack.demolunarlanderplusmcmaceac;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Locale;

/**
 * Created by mcmaceac on 2017-10-23.
 */

public class Results extends Activity {
    final String SD1_HEADER = "App,Participant,Session,Block,Group,Condition,Trials,Difficulty";
    final String APP = "LunarLander";
    final String WORKING_DIRECTORY = "/LunarData/";


    public BufferedWriter sd1;
    public File f1;
    public String sd1Leader;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results);

        Bundle b = getIntent().getExtras();
        int trials = b.getInt("trials");
        String difficulty = b.getString("difficulty");
        String time = b.getString("time");
        int wins = b.getInt("wins");
        String participantCode = b.getString("participant");
        String sessionCode = b.getString("session");
        String groupCode = b.getString("group");
        String conditionCode = b.getString("condition");

        TextView trialsTV = (TextView) findViewById(R.id.paramTrials);
        TextView difficultyTV = (TextView) findViewById(R.id.paramDifficulty);
        TextView timeTV = (TextView) findViewById(R.id.paramTime);
        TextView winsTV = (TextView) findViewById(R.id.paramWins);

        trialsTV.setText("Trials = " + trials);
        difficultyTV.setText("Difficulty = " + difficulty);
        timeTV.setText("Time = " + time + " (mean/trial)");
        winsTV.setText("Wins = " + wins);

        File dataDirectory = new File(Environment.getExternalStorageDirectory() + WORKING_DIRECTORY);
        if (!dataDirectory.exists() && !dataDirectory.mkdirs())
        {
            Log.e("MYDEBUG", "ERROR --> FAILED TO CREATE DIRECTORY: " + WORKING_DIRECTORY);
            super.onDestroy(); // cleanup
            this.finish(); // terminate
        }
        try {
            dataDirectory.createNewFile();
        } catch (IOException e) {}

        int blockNumber = 0;
        do {
            ++blockNumber;
            String blockCode = String.format(Locale.CANADA, "B%02d", blockNumber);
            String baseFileName = String.format("%s-%s-%s-%s-%s-%s-%s", APP, participantCode, sessionCode,
                    blockCode, groupCode, conditionCode, difficulty);
            f1 = new File(dataDirectory, baseFileName + ".sd2");
            sd1Leader = String.format("%s,%s,%s,%s,%s,%s,%s", APP, participantCode, sessionCode,
                    blockCode, groupCode, conditionCode, difficulty);
        } while (f1.exists());



        Log.i("MYDEBUG", "Working directory=" + dataDirectory);


        try {
            sd1 = new BufferedWriter(new FileWriter(f1));
        } catch (IOException e) {
            Log.e("MYDEBUG", "ERROR OPENING DATA FILES! e=" + e.toString());
            super.onDestroy();
            this.finish();
        }

        StringBuilder sd1Data = new StringBuilder(100);
        sd1Data.append(String.format("%s,", sd1Leader));
        sd1Data.append(String.format(Locale.CANADA, "%d,", trials));
        sd1Data.append(String.format(Locale.CANADA, "%d,", wins));
        sd1Data.append(String.format("%s,", time));

        try {
            sd1.write(SD1_HEADER, 0, SD1_HEADER.length());
            sd1.flush();
            sd1.write(sd1Data.toString(), 0, sd1Data.length());
            sd1.flush();
        } catch (IOException e) {
            Log.e("MYDEBUG", "ERROR WRITING TO DATA FILE!\n" + e);
            super.onDestroy();
            this.finish();
        }

        //refreshes the file in explorer
        MediaScannerConnection.scanFile(this, new String[]{f1.getAbsolutePath()}, null, null);
    }

    // called when the "Results" button is pressed
    public void clickSetup(View view) {
        Intent i = new Intent(getApplicationContext(), LunarSetup.class);
        //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    /** Called when the "Exit" button is pressed. */
    public void clickExit(View view) {
        super.onDestroy(); // cleanup
        this.finish(); // terminate
        //System.exit(0);
    }
}
