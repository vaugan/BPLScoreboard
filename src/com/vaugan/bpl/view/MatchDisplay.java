package com.vaugan.bpl.view;

import com.vaugan.bpl.R;
import com.vaugan.bpl.model.FrameCodeAPI;
import com.vaugan.bpl.model.IBPLConstants;
import com.vaugan.bpl.model.MatchDbAdapter;
import com.vaugan.bpl.model.PlayerDbAdapter;
import com.vaugan.bpl.model.SetLogic;
import com.vaugan.bpl.model.MatchLogic;
import com.vaugan.bpl.presenter.MatchPresenter;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MatchDisplay extends Activity {

    // Model/Presenter Stuff - move out later to presentation layer

    // End of Model/Presenter stuff

    private static final String TAG = "MatchDisplay";

    private EditText mP1Text;

    private EditText mP2Text;

    private int aSetsUI[] = new int[IBPLConstants.MAX_SETS_IN_MATCH];

    // Match
    private TextView p1MatchScore;

    private TextView p2MatchScore;

    private TextView p1CurrentSetScore;

    private TextView p2CurrentSetScore;

    // Set1
    private GridView gvS1P1FrameCodes;

    private GridView gvS1P2FrameCodes;

    private TextView etS1P1Score;

    private TextView etS1P2Score;

    // Set2
    private GridView gvS2P1FrameCodes;

    private GridView gvS2P2FrameCodes;

    private TextView etS2P1Score;

    private TextView etS2P2Score;

    // Set3
    private GridView gvS3P1FrameCodes;

    private GridView gvS3P2FrameCodes;

    private TextView etS3P1Score;

    private TextView etS3P2Score;

    private ImageView mP1Image;

    private ImageView mP2Image;

    private MatchPresenter mp;

    protected static final int ACTIVITY_FRAME_IMAGE_SELECTOR = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Bundle extras = this.getIntent().getExtras();
        long mRowId = extras.getLong("com.vaugan.csf.match.rowid");
        long mP1RowId = extras.getLong("com.vaugan.csf.match.p1rowid");
        long mP2RowId = extras.getLong("com.vaugan.csf.match.p2rowid");

        mp = MatchPresenter.getInstance(this.getApplicationContext());
        mp.initialiseMatch(this, mRowId, mP1RowId, mP2RowId);

        aSetsUI[0] = R.id.Set1;
        aSetsUI[1] = R.id.Set2;
        aSetsUI[2] = R.id.Set3;

        Log.v(TAG, "mRowId=" + mRowId);

        setContentView(R.layout.match_display);
        setTitle(R.string.edit_note);

        // ##############################
        // ##### Match Fields #####
        // ##############################
        mP1Text = (EditText) findViewById(R.id.player1name);
        mP2Text = (EditText) findViewById(R.id.player2name);
        mP1Image = (ImageView) findViewById(R.id.player1Image);
        mP2Image = (ImageView) findViewById(R.id.player2Image);
        p1MatchScore = (TextView) findViewById(R.id.matchP1Score);
        p2MatchScore = (TextView) findViewById(R.id.matchP2Score);
        p1CurrentSetScore = (TextView) findViewById(R.id.currentSetP1Score);
        p2CurrentSetScore = (TextView) findViewById(R.id.currentSetP2Score);

        // ##############################
        // ##### Set 1 #####
        // ##############################
        // Player1 Scorecard
        etS1P1Score = (TextView) findViewById(R.id.set1Player1Score);
        gvS1P1FrameCodes = (GridView) findViewById(R.id.set1Player1FrameCodes);
        gvS1P1FrameCodes.setNumColumns(IBPLConstants.MAX_FRAMES_IN_SET);
        gvS1P1FrameCodes.setAdapter(mp.getSet(0, 0));

        gvS1P1FrameCodes.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                    int position, long id) {

                Intent i = new Intent(MatchDisplay.this,
                        FrameCodeSelector.class);
                i.putExtra("player", IBPLConstants.HOME_PLAYER);
                i.putExtra("pos", position);
                i.putExtra("set", IBPLConstants.SET_ONE);
                startActivityForResult(i, ACTIVITY_FRAME_IMAGE_SELECTOR);
            }
        });

        // Player2 Scorecard
        etS1P2Score = (TextView) findViewById(R.id.set1Player2Score);
        gvS1P2FrameCodes = (GridView) findViewById(R.id.set1Player2FrameCodes);
        gvS1P2FrameCodes.setNumColumns(IBPLConstants.MAX_FRAMES_IN_SET);
        gvS1P2FrameCodes.setAdapter(mp.getSet(1, 0));

        gvS1P2FrameCodes.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                    int position, long id) {
                Intent i = new Intent(MatchDisplay.this,
                        FrameCodeSelector.class);
                i.putExtra("player", IBPLConstants.AWAY_PLAYER);
                i.putExtra("pos", position);
                i.putExtra("set", IBPLConstants.SET_ONE);
                startActivityForResult(i, ACTIVITY_FRAME_IMAGE_SELECTOR);
            }
        });

        // ##############################
        // ##### Set 2 #####
        // ##############################
        // Player1
        etS2P1Score = (TextView) findViewById(R.id.set2Player1Score);
        gvS2P1FrameCodes = (GridView) findViewById(R.id.set2Player1FrameCodes);
        gvS2P1FrameCodes.setNumColumns(IBPLConstants.MAX_FRAMES_IN_SET);
        gvS2P1FrameCodes.setAdapter(mp.getSet(0, 1));

        gvS2P1FrameCodes.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                    int position, long id) {

                Intent i = new Intent(MatchDisplay.this,
                        FrameCodeSelector.class);
                i.putExtra("player", IBPLConstants.HOME_PLAYER);
                i.putExtra("pos", position);
                i.putExtra("set", IBPLConstants.SET_TWO);
                startActivityForResult(i, ACTIVITY_FRAME_IMAGE_SELECTOR);
            }
        });

        // Player2
        etS2P2Score = (TextView) findViewById(R.id.set2Player2Score);
        gvS2P2FrameCodes = (GridView) findViewById(R.id.set2Player2FrameCodes);
        gvS2P2FrameCodes.setNumColumns(IBPLConstants.MAX_FRAMES_IN_SET);
        gvS2P2FrameCodes.setAdapter(mp.getSet(1, 1));

        gvS2P2FrameCodes.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                    int position, long id) {
                Intent i = new Intent(MatchDisplay.this,
                        FrameCodeSelector.class);
                i.putExtra("player", IBPLConstants.AWAY_PLAYER);
                i.putExtra("pos", position);
                i.putExtra("set", IBPLConstants.SET_TWO);
                startActivityForResult(i, ACTIVITY_FRAME_IMAGE_SELECTOR);
            }
        });

        // ##############################
        // ##### Set 3 #####
        // ##############################
        // Player1 Scorecard
        etS3P1Score = (TextView) findViewById(R.id.set3Player1Score);
        gvS3P1FrameCodes = (GridView) findViewById(R.id.set3Player1FrameCodes);
        gvS3P1FrameCodes.setNumColumns(IBPLConstants.MAX_FRAMES_IN_SET);
        gvS3P1FrameCodes.setAdapter(mp.getSet(0, 2));

        gvS3P1FrameCodes.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                    int position, long id) {

                Intent i = new Intent(MatchDisplay.this,
                        FrameCodeSelector.class);
                i.putExtra("player", IBPLConstants.HOME_PLAYER);
                i.putExtra("pos", position);
                i.putExtra("set", IBPLConstants.SET_THREE);
                startActivityForResult(i, ACTIVITY_FRAME_IMAGE_SELECTOR);
            }
        });

        // Player2 Scorecard
        etS3P2Score = (TextView) findViewById(R.id.set3Player2Score);
        gvS3P2FrameCodes = (GridView) findViewById(R.id.set3Player2FrameCodes);
        gvS3P2FrameCodes.setNumColumns(IBPLConstants.MAX_FRAMES_IN_SET);
        gvS3P2FrameCodes.setAdapter(mp.getSet(1, 2));

        gvS3P2FrameCodes.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                    int position, long id) {
                Intent i = new Intent(MatchDisplay.this,
                        FrameCodeSelector.class);
                i.putExtra("player", IBPLConstants.AWAY_PLAYER);
                i.putExtra("pos", position);
                i.putExtra("set", IBPLConstants.SET_THREE);
                startActivityForResult(i, ACTIVITY_FRAME_IMAGE_SELECTOR);
            }
        });

        populateFields();
        refreshDisplay();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
            Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == Activity.RESULT_OK) {

            Bundle extras = intent.getExtras();
            int frameCode = 0;
            int player = extras.getInt("player");
            int pos = extras.getInt("pos");
            int set = extras.getInt("set");
            frameCode = extras.getInt("selected_icon");

            Log.v(TAG,
                    "Return this value from Frame Selector dialog"
                            + extras.toString());

            switch (requestCode) {
            case ACTIVITY_FRAME_IMAGE_SELECTOR:

                mp.updateSetScore(player, pos, set, frameCode);

                etS1P1Score.setText(mp.getSetScore(IBPLConstants.SET_ONE,
                        IBPLConstants.HOME_PLAYER));
                etS2P1Score.setText(mp.getSetScore(IBPLConstants.SET_TWO,
                        IBPLConstants.HOME_PLAYER));
                etS3P1Score.setText(mp.getSetScore(IBPLConstants.SET_THREE,
                        IBPLConstants.HOME_PLAYER));

                etS1P2Score.setText(mp.getSetScore(IBPLConstants.SET_ONE,
                        IBPLConstants.AWAY_PLAYER));
                etS2P2Score.setText(mp.getSetScore(IBPLConstants.SET_TWO,
                        IBPLConstants.AWAY_PLAYER));
                etS3P2Score.setText(mp.getSetScore(IBPLConstants.SET_THREE,
                        IBPLConstants.AWAY_PLAYER));

                break;
            // case ACTIVITY_EDIT:
            // Long mRowId = extras.getLong(NotesDbAdapter.KEY_ROWID);
            // if (mRowId != null) {
            // String editTitle = extras.getString(NotesDbAdapter.KEY_TITLE);
            // String editBody = extras.getString(NotesDbAdapter.KEY_BODY);
            // mDbHelper.updateNote(mRowId, editTitle, editBody);
            // }
            // fillData();
            // break;
            }

            populateFields();
            updateSetVisibility(set);
        } else {
            Log.v(TAG,
                    "Returned from frameselector activity! ERROR!!!! resultCode["
                            + resultCode);
        }
    }

    private void updateMatchStatus() {
        // if (MatchLogic.isMatchOver(aP1Sets, aP2Sets)) {
        // // Match is over so disable everything and put up message.
        // Toast.makeText(getApplicationContext(), "Game Over!",
        // Toast.LENGTH_LONG);
        // }

        // p1CurrentSetScore.setText(Integer.toString(p1SetScore));
        // p2CurrentSetScore.setText(Integer.toString(p2SetScore));
        //
        // p1CurrentSetScore.setAnimation(AnimationUtils.loadAnimation(MatchDisplay.this,
        // android.R.anim.fade_in));
        // p2CurrentSetScore.setAnimation(AnimationUtils.loadAnimation(MatchDisplay.this,
        // android.R.anim.fade_in));
        //
        // p1MatchScore.setText(Integer.toString(mp.getMatchScore(homePlayer)));
        // p2MatchScore.setText(Integer.toString(mp.getMatchScore(awayPlayer)));

    }

    /**
     * If a set is over, show it and disable it
     * If current set, show it
     * over.
     * 
     * @param set
     */
    private void updateSetVisibility(int set) {

        if (!mp.isMatchFinished()) {
            // Match is still in progress.

            if (mp.isCurrentSet(set)) {
                findViewById(aSetsUI[set]).setVisibility(View.VISIBLE);
            }

            // If the set is finished, show it and disable.
            if (mp.isSetFinished(set)) {
                Log.v(TAG, "Set[" + set + " is finished. Disabling input...");

                findViewById(aSetsUI[set]).setVisibility(View.VISIBLE);

                LinearLayout myLayout = (LinearLayout) findViewById(aSetsUI[set]);
                for (int i = 0; i < myLayout.getChildCount(); i++) {
                    View view = myLayout.getChildAt(i);
                    // view.setEnabled(false);
                    if (view instanceof GridView) {
                        ((GridView) view)
                                .setOnItemClickListener(new OnItemClickListener() {
                                    public void onItemClick(
                                            AdapterView<?> parent, View v,
                                            int position, long id) {
                                        Toast.makeText(getApplicationContext(),
                                                "This set is over",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            }
        } else {
            // Match is over. Show all sets that are finished.
            // If the set is finished, show it and disable.
            if (mp.isSetFinished(set)) {
                Log.v(TAG, "Set[" + set + " is finished. Disabling input...");

                findViewById(aSetsUI[set]).setVisibility(View.VISIBLE);

                LinearLayout myLayout = (LinearLayout) findViewById(aSetsUI[set]);
                for (int i = 0; i < myLayout.getChildCount(); i++) {
                    View view = myLayout.getChildAt(i);
                    // view.setEnabled(false);
                    if (view instanceof GridView) {
                        ((GridView) view)
                                .setOnItemClickListener(new OnItemClickListener() {
                                    public void onItemClick(
                                            AdapterView<?> parent, View v,
                                            int position, long id) {
                                        Toast.makeText(getApplicationContext(),
                                                "This set is over",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            }

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        // outState.putSerializable(MatchDbAdapter.KEY_ROWID, mRowId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();

    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshDisplay();
        populateFields();

    }

    @Override
    protected void onDestroy() {
        super.onResume();

    }

    private void saveState() {
        mp.saveMatch();
    }

    private void populateFields() {

        // Player Info
        mP1Text.setText(mp.getPlayerNameUsingHomeOrAway(IBPLConstants.HOME_PLAYER));
        mP2Text.setText(mp.getPlayerNameUsingHomeOrAway(IBPLConstants.AWAY_PLAYER));
        mP1Image.setImageBitmap(mp.getPlayerImage(IBPLConstants.HOME_PLAYER));
        mP2Image.setImageBitmap(mp.getPlayerImage(IBPLConstants.AWAY_PLAYER));

        // Set Grid Score
        etS1P1Score.setText(mp.getSetScore(IBPLConstants.SET_ONE,
                IBPLConstants.HOME_PLAYER));
        etS2P1Score.setText(mp.getSetScore(IBPLConstants.SET_TWO,
                IBPLConstants.HOME_PLAYER));
        etS3P1Score.setText(mp.getSetScore(IBPLConstants.SET_THREE,
                IBPLConstants.HOME_PLAYER));

        etS1P2Score.setText(mp.getSetScore(IBPLConstants.SET_ONE,
                IBPLConstants.AWAY_PLAYER));
        etS2P2Score.setText(mp.getSetScore(IBPLConstants.SET_TWO,
                IBPLConstants.AWAY_PLAYER));
        etS3P2Score.setText(mp.getSetScore(IBPLConstants.SET_THREE,
                IBPLConstants.AWAY_PLAYER));

        // Current Set Score
        p1CurrentSetScore.setText(mp
                .getCurrentSetScore(IBPLConstants.HOME_PLAYER));
        p2CurrentSetScore.setText(mp
                .getCurrentSetScore(IBPLConstants.AWAY_PLAYER));

        // Match Score
        p1MatchScore.setText(Integer.toString(mp
                .getMatchScore(IBPLConstants.HOME_PLAYER)));
        p2MatchScore.setText(Integer.toString(mp
                .getMatchScore(IBPLConstants.AWAY_PLAYER)));
    }

    public void refreshDisplay() {
        for (int i = 0; i < IBPLConstants.MAX_SETS_IN_MATCH; i++) {
            updateSetVisibility(i);
        }
    }

}
