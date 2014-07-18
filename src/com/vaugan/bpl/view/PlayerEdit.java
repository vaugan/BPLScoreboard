package com.vaugan.bpl.view;

import com.vaugan.bpl.R;
import com.vaugan.bpl.model.PlayerDbAdapter;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PlayerEdit extends Activity {

    private EditText mTitleText;
    private EditText mBodyText;
    private EditText mCellText;
    private Long mRowId;
    private PlayerDbAdapter mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDbHelper = new PlayerDbAdapter(this);
        mDbHelper.open();

        setContentView(R.layout.player_edit);
        setTitle(R.string.edit_note);

        mTitleText = (EditText) findViewById(R.id.title);
        mBodyText = (EditText) findViewById(R.id.body);
        mCellText = (EditText) findViewById(R.id.cell);

        Button confirmButton = (Button) findViewById(R.id.confirm);

        mRowId = (savedInstanceState == null) ? null :
            (Long) savedInstanceState.getSerializable(PlayerDbAdapter.KEY_ROWID);
		if (mRowId == null) {
			Bundle extras = getIntent().getExtras();
			mRowId = extras != null ? extras.getLong(PlayerDbAdapter.KEY_ROWID)
									: null;
		}

		populateFields();

        confirmButton.setOnClickListener(new View.OnClickListener() {

            @Override
			public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }

        });
    }

    private void populateFields() {
        if (mRowId != null) {
            Cursor note = mDbHelper.fetchPlayer(mRowId);
            startManagingCursor(note);
            mTitleText.setText(note.getString(
                    note.getColumnIndexOrThrow(PlayerDbAdapter.KEY_NAME)));
            mBodyText.setText(note.getString(
                    note.getColumnIndexOrThrow(PlayerDbAdapter.KEY_PICTURE)));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putSerializable(PlayerDbAdapter.KEY_ROWID, mRowId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }

    private void saveState() {
        String name = mTitleText.getText().toString();
        String picture = mBodyText.getText().toString();

        if (mRowId == null) {
            long id = mDbHelper.createPlayer(name, picture);
            if (id > 0) {
                mRowId = id;
            }
        } else {
            mDbHelper.updatePlayer(mRowId, name, picture);
        }
    }

}
