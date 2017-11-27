package ca.yorku.eecs.mack.demolunarlanderplusmcmaceac;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * Created by mcmaceac on 2017-10-23.
 */

public class Results extends Activity {
    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results);

        Bundle b = getIntent().getExtras();
        int trials = b.getInt("trials");
        String difficulty = b.getString("difficulty");
        String time = b.getString("time");
        int wins = b.getInt("wins");

        TextView trialsTV = (TextView) findViewById(R.id.paramTrials);
        TextView difficultyTV = (TextView) findViewById(R.id.paramDifficulty);
        TextView timeTV = (TextView) findViewById(R.id.paramTime);
        TextView winsTV = (TextView) findViewById(R.id.paramWins);

        trialsTV.setText("Trials = " + trials);
        difficultyTV.setText("Difficulty = " + difficulty);
        timeTV.setText("Time = " + time + " (mean/trial)");
        winsTV.setText("Wins = " + wins);

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
