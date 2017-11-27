package ca.yorku.eecs.mack.demolunarlanderplusmcmaceac;

/**
 * Created by mcmaceac on 2017-11-23.
 */

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.util.Log;

@SuppressWarnings("unused")
public class LunarSetup extends Activity implements View.OnClickListener
{
    final static String MYDEBUG = "MYDEBUG"; // for Log.i messages
    final static String TITLE = "GraffitiSetup";

    /*
     * The following arrays are used to fill the spinners in the set up dialog. The first entries will be replaced by
     * corresponding values in the app's shared preferences, if any exist. In order for a value to exit as a shared
     * preference, the app must have been run at least once with the "Save" button tapped.
     */
    String[] participantCode = {"P99", "P01", "P02", "P03", "P04", "P05", "P06", "P07", "P08", "P09", "P10", "P11",
            "P12", "P13", "P14", "P15", "P16", "P17", "P18", "P19", "P20", "P21", "P22", "P23", "P24", "P25"};
    String[] sessionCode = {"S99", "S01", "S02", "S03", "S04", "S05", "S06", "S07", "S08", "S09", "S10", "S11", "S12",
            "S13", "S14", "S15", "S16", "S17", "S18", "S19", "S20", "S21", "S22", "S23", "S24", "S25"};
    String[] blockCode = {"(auto)"};
    String[] groupCode = {"G99", "G01", "G02", "G03", "G04", "G05", "G06", "G07", "G08", "G09", "G10", "G11", "G12",
            "G13", "G14", "G15", "G16", "G17", "G18", "G19", "G20", "G21", "G22", "G23", "G24", "G25"};
    String[] conditionCode = {"C99", "C01", "C02", "C03", "C04", "C05", "C06", "C07", "C08", "C09", "C10", "C11",
            "C12", "C13", "C14", "C15", "C16", "C17", "C18", "C19", "C20", "C21", "C22", "C23", "C24", "C25"};
    String[] numberOfTrials = {"5", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    String[] difficulty = {"Easy", "Medium", "Hard"};
    // default values for EditText fields (may be different if shared preferences saved)

    SharedPreferences sp;
    SharedPreferences.Editor spe;
    Button ok, save, exit;

    private Spinner spinParticipantCode;
    private Spinner spinSessionCode, spinGroupCode;
    private Spinner spinNumberOfTrials, spinDifficulty;
    private Spinner spinConditionCode, spinBlockCode;
    // end set up parameters

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.setup);

        // get a reference to a SharedPreferences object (used to store, retrieve, and save setup parameters)
        sp = this.getPreferences(MODE_PRIVATE);

        // overwrite 1st entry from shared preferences, if corresponding value exits
        participantCode[0] = sp.getString("participantCode", participantCode[0]);
        sessionCode[0] = sp.getString("sessionCode", sessionCode[0]);
        blockCode[0] = sp.getString("blockCode", blockCode[0]);
        groupCode[0] = sp.getString("groupCode", groupCode[0]);
        conditionCode[0] = sp.getString("conditionCode", conditionCode[0]);
        numberOfTrials[0] = sp.getString("numberOfPhrases", numberOfTrials[0]);
        difficulty[0] = sp.getString("difficulty", difficulty[0]);


        // get references to widgets in setup dialog
        spinParticipantCode = (Spinner)findViewById(R.id.spinParticipantCode);
        spinSessionCode = (Spinner)findViewById(R.id.spinSessionCode);
        spinBlockCode = (Spinner)findViewById(R.id.spinBlockCode);
        spinGroupCode = (Spinner)findViewById(R.id.spinGroupCode);
        spinConditionCode = (Spinner)findViewById(R.id.conditionCode);
        spinNumberOfTrials = (Spinner)findViewById(R.id.numberOfTrials);
        spinDifficulty = (Spinner)findViewById(R.id.difficulty);

        // get references to OK, SAVE, and EXIT buttons
        ok = (Button)findViewById(R.id.ok);
        save = (Button)findViewById(R.id.save);
        exit = (Button)findViewById(R.id.exit);

        // initialise spinner adapters
        ArrayAdapter<CharSequence> adapterPC = new ArrayAdapter<CharSequence>(this, R.layout.spinnerstyle,
                participantCode);
        spinParticipantCode.setAdapter(adapterPC);

        ArrayAdapter<CharSequence> adapterSC = new ArrayAdapter<CharSequence>(this, R.layout.spinnerstyle,
                sessionCode);
        spinSessionCode.setAdapter(adapterSC);

        ArrayAdapter<CharSequence> adapterBC = new ArrayAdapter<CharSequence>(this, R.layout.spinnerstyle,
                blockCode);
        spinBlockCode.setAdapter(adapterBC);

        ArrayAdapter<CharSequence> adapterGC = new ArrayAdapter<CharSequence>(this, R.layout.spinnerstyle,
                groupCode);
        spinGroupCode.setAdapter(adapterGC);

        ArrayAdapter<CharSequence> adapterNOP = new ArrayAdapter<CharSequence>(this, R.layout.spinnerstyle,
                numberOfTrials);
        spinNumberOfTrials.setAdapter(adapterNOP);

        ArrayAdapter<CharSequence> adapterPF = new ArrayAdapter<CharSequence>(this, R.layout.spinnerstyle,
                difficulty);
        spinDifficulty.setAdapter(adapterPF);

        ArrayAdapter<CharSequence> adapterCC = new ArrayAdapter<CharSequence>(this, R.layout.spinnerstyle,
                conditionCode);
        spinConditionCode.setAdapter(adapterCC);
        // initialize EditText setup items

        // prevent soft keyboard from popping up when activity launches
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onClick(View v)
    {
        if (v == ok)
        {
            // get user's choices
            String part = participantCode[spinParticipantCode.getSelectedItemPosition()];
            String sess = sessionCode[spinSessionCode.getSelectedItemPosition()];
            String block = blockCode[spinBlockCode.getSelectedItemPosition()];
            String group = groupCode[spinGroupCode.getSelectedItemPosition()];
            String cond = conditionCode[spinConditionCode.getSelectedItemPosition()];
            int num = Integer.parseInt(numberOfTrials[spinNumberOfTrials.getSelectedItemPosition()]);
            String difficultySetting = difficulty[spinDifficulty.getSelectedItemPosition()];

            // package the user's choices in a bundle
            Bundle b = new Bundle();
            b.putString("participantCode", part);
            b.putString("sessionCode", sess);
            b.putString("blockCode", block);
            b.putString("groupCode", group);
            b.putString("conditionCode", cond);
            b.putInt("numberOfTrials", num);
            b.putString("difficulty", difficultySetting);

            // start experiment activity (sending the bundle with the user's choices)
            Intent i = new Intent(getApplicationContext(), DemoLunarLanderPlusmcmaceacActivity.class);
            i.putExtras(b);
            startActivity(i);
            finish();

        } else if (v == save)
        {
            spe = sp.edit();
            spe.putString("participantCode", participantCode[spinParticipantCode.getSelectedItemPosition()]);
            spe.putString("sessionCode", sessionCode[spinSessionCode.getSelectedItemPosition()]);
            spe.putString("groupCode", groupCode[spinGroupCode.getSelectedItemPosition()]);
            spe.putString("blockCode", blockCode[spinBlockCode.getSelectedItemPosition()]);
            spe.putString("conditionCode", conditionCode[spinConditionCode.getSelectedItemPosition()]);
            spe.putString("numberOfTrials", numberOfTrials[spinNumberOfTrials.getSelectedItemPosition()]);
            spe.putString("difficulty", difficulty[spinDifficulty.getSelectedItemPosition()]);
            spe.apply();
            Toast.makeText(this, "Preferences saved!", Toast.LENGTH_SHORT).show();

        } else if (v == exit)
        {
            this.finish(); // terminate
        }
    }
}
