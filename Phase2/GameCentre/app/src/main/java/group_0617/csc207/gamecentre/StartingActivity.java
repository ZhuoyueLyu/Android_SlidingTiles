package group_0617.csc207.gamecentre;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The initial activity for the sliding puzzle tile game.
 */
public class StartingActivity extends AppCompatActivity {

    public static String gameComplexity = "medium";
    /**
     * counting the times user click the arrow, start with medium which is index 1
     */
    private int positionOfChoice = 1;

    /**
     * The main save file.
     */
    private String SAVE_FILENAME = "save_file_" + GameChoiceActivity.currentGame + "_" +
            Board.NUM_COLS + "_" + LoginActivity.currentUser;
    //(Change from Board)

    /**
     * A temporary save file.
     */
    public static String TEMP_SAVE_FILENAME = "save_file_tmp_" + GameChoiceActivity.currentGame + "_" +
            Board.NUM_COLS + "_" + LoginActivity.currentUser;
    /**
     * The board manager.
     */
    //private BoardManager boardManager;

    private BoardManager boardManager;
    private BoardManagerSlidingtiles boardManagerSlidingtiles;
    private BoardManager2048 boardManager2048;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (GameChoiceActivity.currentGame == "2048"){
            boardManager2048 = new BoardManager2048();
        }
        else if (GameChoiceActivity.currentGame == "Slidingtiles") {
            boardManagerSlidingtiles = new BoardManagerSlidingtiles();
        }
        boardManager = new BoardManager();
        saveToFile(TEMP_SAVE_FILENAME);

        setContentView(R.layout.activity_starting_);
        addStartButtonListener();
        addLoadButtonListener();
        addSaveButtonListener();
        addLeaderBoardButtonListener();
        addRulesButtonListener();
        addLeftArrowButtonListener();
        addRightArrowButtonListener();
    }

    /**
     * Activate the left arrow button.
     */

    private void addLeftArrowButtonListener() {


        ImageButton Button = findViewById(R.id.leftArrow);
        Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView complexity = (TextView) findViewById(R.id.Complexity);
                if (positionOfChoice - 1 >= 0) {
                    positionOfChoice--;
                    ChooseComplexity(complexity);
                }
            }
        });

    }

    /**
     * Activate the right arrow button.
     */
    private void addRightArrowButtonListener() {

        ImageButton Button = findViewById(R.id.rightArrow);
        Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView complexity = (TextView) findViewById(R.id.Complexity);
                if (positionOfChoice + 1 <= 2) {
                    positionOfChoice++;
                    ChooseComplexity(complexity);

                }
            }
        });


    }

/**
 * Choose the complexity of the game
 */
    @SuppressLint("SetTextI18n")
    private void ChooseComplexity(TextView complexity) {
        switch (positionOfChoice){
            case 0:
                Board.NUM_COLS = 3;
                Board.NUM_ROWS = 3;
                gameComplexity = "easy";
                complexity.setText("Easy (3x3)");break;

            case 1:
                Board.NUM_COLS = 4;
                Board.NUM_ROWS = 4;
                gameComplexity = "medium";
                complexity.setText("Medium (4x4)");break;
            case 2:
                Board.NUM_COLS = 5;
                Board.NUM_ROWS = 5;
                gameComplexity = "hard";
                complexity.setText("Hard (5x5)");break;
            default:
                Board.NUM_COLS = 4;
                Board.NUM_ROWS = 4;
                gameComplexity = "medium";
                complexity.setText("Medium (4x4)");

        }
    }

    /**
     * Activate the start button.
     */
    private void addStartButtonListener() {
        Button startButton = findViewById(R.id.StartButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //boardManager = new BoardManager();
                boardManager = new BoardManager();
                switchToGame(GameChoiceActivity.currentGame);
            }
        });
    }

    /**
     * Activate the load button.
     */
    private void addLoadButtonListener() {
        Button loadButton = findViewById(R.id.LoadButton);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFromFile(SAVE_FILENAME);
                saveToFile(TEMP_SAVE_FILENAME);
                makeToastLoadedText();
                switchToGame(GameChoiceActivity.currentGame);
            }
        });
    }

    /**
     * Display that a game was loaded successfully.
     */
    private void makeToastLoadedText() {
        Toast.makeText(this,"Loaded Game",Toast.LENGTH_SHORT).show();
    }

    /**
     * Activate the save button.
     */
    private void addSaveButtonListener() {
        Button saveButton = findViewById(R.id.SaveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToFile(SAVE_FILENAME);
                saveToFile(TEMP_SAVE_FILENAME);
                makeToastSavedText();
            }
        });
    }

    /**
     * Activate the Leaderboard button.
     */
    private void addLeaderBoardButtonListener() {
        ImageButton leaderboardButton = findViewById(R.id.leaderboard);
        leaderboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gameScoreboardScreen =
                        new Intent(StartingActivity.this,LeaderboardActivity.class);
                startActivity(gameScoreboardScreen);
            }
        });
    }

    /**
     * Activate the rules button.
     */
    private void addRulesButtonListener() {
        ImageButton Button = findViewById(R.id.gameRulesOfST);
        Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gameScoreboardScreen =
                        new Intent(StartingActivity.this,GameRulesActivity.class);
                startActivity(gameScoreboardScreen);
            }
        });
    }


    /**
     * Display that a game was saved successfully.
     */
    private void makeToastSavedText() {
        Toast.makeText(this,"Game Saved",Toast.LENGTH_SHORT).show();
    }

    /**
     * Read the temporary board from disk.
     */
    @Override
    protected void onResume() {
        super.onResume();
        loadFromFile(TEMP_SAVE_FILENAME);
    }

    /**
     * Switch to the GameActivity view to play the game.
     */
    private void switchToGame(String currentGame) {
        Intent tmp = null;
        switch (currentGame) {
            case "Slidingtiles":
                tmp = new Intent(this,GameActivity.class);
                break;
            case "2048":
                tmp = new Intent(this, Game2048Activity.class);
                break;
        }
        saveToFile(StartingActivity.TEMP_SAVE_FILENAME);
        startActivity(tmp);
    }

    /**
     * Load the board manager from fileName.
     *
     * @param fileName the name of the file
     */
    private void loadFromFile(String fileName) {

        try {
            InputStream inputStream = this.openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                //boardManager = (BoardManager) input.readObject();
                boardManagerSlidingtiles = (BoardManagerSlidingtiles) input.readObject();
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity","File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity","Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("login activity","File contained unexpected data type: " + e.toString());
        }
    }

    /**
     * Save the board manager to fileName.
     *
     * @param fileName the name of the file
     */
    public void saveToFile(String fileName) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput(fileName,MODE_PRIVATE));
            switch (GameChoiceActivity.currentGame){
                case "Slidingtiles":
                    outputStream.writeObject(boardManagerSlidingtiles);
                    break;
                case "2048":
                    outputStream.writeObject(boardManager2048);
                    break;
            }
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception","File write failed: " + e.toString());
        }
    }

}
