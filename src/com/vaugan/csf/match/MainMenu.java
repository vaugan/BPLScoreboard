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

package com.vaugan.csf.match;

import com.vaugan.csf.match.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends Activity {


    protected static final int ACTIVITY_MATCH_DISPLAY = 0;
	protected static final int ACTIVITY_PLAYER_DISPLAY = 1;
	protected static final int ACTIVITY_MATCH_CREATE = 2;
	protected static final int ACTIVITY_MATCH_LIST = 3;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_menu);
        setTitle(R.string.app_name);

        Button newMatchButton = (Button) findViewById(R.id.match);
        Button viewStatsButton = (Button) findViewById(R.id.stats);        
        Button viewPlayersButton = (Button) findViewById(R.id.players);


        newMatchButton.setOnClickListener(new View.OnClickListener() {

            @Override
			public void onClick(View view) {
                Intent i = new Intent(MainMenu.this, MatchCreate.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivityForResult(i, ACTIVITY_MATCH_CREATE);        
          	
            	setResult(RESULT_OK);
               // finish();
            }
        });    

        viewStatsButton.setOnClickListener(new View.OnClickListener() {

	        @Override
			public void onClick(View view) {
                Intent i = new Intent(MainMenu.this, MatchList.class);
                startActivityForResult(i, ACTIVITY_MATCH_LIST);	        	
//	            setResult(RESULT_OK);
//	            Toast.makeText(MainMenu.this, "tbd - view stats activity", Toast.LENGTH_SHORT).show();
	            //finish();
	        }
        });
        
        viewPlayersButton.setOnClickListener(new View.OnClickListener() {

            @Override
			public void onClick(View view) {
                setResult(RESULT_OK);
                Intent i = new Intent(MainMenu.this, PlayerList.class);
                startActivityForResult(i, ACTIVITY_PLAYER_DISPLAY);                
                //Toast.makeText(MainMenu.this, "tbd - view players activity", Toast.LENGTH_SHORT).show();
                //finish();
            }

        });
    }



}
