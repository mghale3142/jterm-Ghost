/* Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String TAG = "GhostActivity";

    private static final String KEY_USER_TURN = "keyUserTurn";
    private static final String KEY_CURRENT_WORD = "keyCurrentWord";
    private static final String KEY_SAVED_STATUS = "keySavedStatus";

    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    private String currentWord = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");

            dictionary = new SimpleDictionary(inputStream);
                      // new FastDictionary(inputStream);
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, R.string.load_dictionary_failed, Toast.LENGTH_LONG);
            toast.show();
        }

        // TODO(you): Handle Bundle savedInstanceState
        onStart(null);
    }

    // TODO(you): Handle onSaveInstanceState

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        int unicode = event.getUnicodeChar();
        if (('A' <= unicode && unicode <= 'Z') || 'a' <= unicode && unicode <= 'z') {
            String character = ((char) unicode + "").toLowerCase();
            currentWord += character;
            ((TextView) findViewById(R.id.ghostText)).setText(currentWord);
            computerTurn();
        }
        return super.onKeyUp(keyCode, event);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(R.string.user_turn);
        } else {
            label.setText(R.string.computer_turn);
            computerTurn();
        }
        return true;
    }

    /**
     * Challenges the current word. Returns true if the challenge was successful, false otherwise.
     * @param fromUser
     * @return
     */
    private boolean doChallenge(boolean fromUser) {
        TextView status = (TextView) findViewById(R.id.gameStatus);
        if (dictionary.isWord(currentWord)) {
            if (!fromUser) {
                // It is a word! The user loses.
                status.setText(String.format("%s is a word. The computer wins!", currentWord));
                return true;
            } else {
                // The computer loses, it has formed a word. --> because the player issued the challenge
                status.setText(String.format("%s is a word. You win!", currentWord));
                return true;
            }
        } else if (TextUtils.isEmpty(dictionary.getAnyWordStartingWith(currentWord))) {
            //invalid word case
            if (!fromUser) {
                // This is not a valid word prefix. The user loses.
                status.setText(String.format("%s is an invalid prefix. The computer wins!",
                        currentWord));
                return true;
            } else {
                status.setText(String.format("%s is an invalid prefix. You win!", currentWord));
                return true;
            }
        }
        if (fromUser) {
            // We've challenged and failed. The user loses.
            status.setText(String.format("%s is a valid prefix and not a word. The computer wins!",
                    currentWord));
        }
        return false;
    }

    private void computerTurn() {
        TextView status = (TextView) findViewById(R.id.gameStatus);

        // Checks if the user's currentWord is a full word, or if it is an invalid prefix.
        boolean challengeSuccessful = doChallenge(/* from the computer */ false);
        if (challengeSuccessful) {
            return;
        }

        userTurn = false;

        // Do computer turn stuff then make it the user's turn again
        status.setText(R.string.computer_turn);
        // odd number so that hte computer can win
        String next = dictionary.getGoodWordStartingWith(currentWord);
        currentWord += next.charAt(currentWord.length());
        // We can now update the text field with the computer's word.
        ((TextView) findViewById(R.id.ghostText)).setText(currentWord);

        // Keep playing...
        userTurn = true;
        status.setText(R.string.user_turn);
    }
}
