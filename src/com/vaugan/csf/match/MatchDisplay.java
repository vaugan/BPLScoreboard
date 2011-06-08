package com.vaugan.csf.match;

/*
 * Copyright (C) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.vaugan.csf.match.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

public class MatchDisplay extends Activity {

    private EditText mTitleText;
    private EditText mBodyText;
    private EditText mCellText;
    private Long mRowId;
    private MatchDbAdapter mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDbHelper = new MatchDbAdapter(this);
        mDbHelper.open();

        setContentView(R.layout.match_display);
        setTitle(R.string.edit_note);


    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        saveState();
//        outState.putSerializable(MatchDbAdapter.KEY_ROWID, mRowId);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        saveState();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        populateFields();
    }

    private void saveState() {
//        String title = mTitleText.getText().toString();
//        String body = mBodyText.getText().toString();
//        String cell = mCellText.getText().toString();
//
//        if (mRowId == null) {
//            long id = mDbHelper.createNote(title, body, cell);
//            if (id > 0) {
//                mRowId = id;
//            }
//        } else {
//            mDbHelper.updateNote(mRowId, title, body, cell);
//        }
    }

}
