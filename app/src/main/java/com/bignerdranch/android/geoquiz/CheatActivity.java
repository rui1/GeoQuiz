package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Rui on 1/2/15.
 */
public class CheatActivity extends Activity {

    public static final String EXTRA_ANSWER_IS_TRUE = "is_true";
    public static final String EXTRA_ANSWER_SHOWN = "answer_shown";
    public static final String CURRENT_QUESTION = "current_question";
    private boolean mAnswerIsTrue;
    private Button mShowAnswerButton;
    private Button mBackButton;
    private TextView mAnswerTextView;
    private static final String KEY_CHEATED = "cheated";
    private boolean mCheated;

    @Override
    protected void onSaveInstanceState(Bundle onState) {
        onState.putBoolean(KEY_CHEATED, mCheated);
    }

    private void setAnswerShown(boolean answerShown) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_ANSWER_SHOWN, answerShown);
        setResult(RESULT_OK, intent);
        mCheated = true;
    }

    private void displayAnswer() {
        String message = "The correct answer is " + Boolean.toString(mAnswerIsTrue);
        mAnswerTextView.setText(message);
        setAnswerShown(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cheat);
        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        mShowAnswerButton = (Button) findViewById(R.id.showAnswerButton);
        mBackButton = (Button) findViewById(R.id.backButton);
        mAnswerTextView = (TextView) findViewById(R.id.answerTextView);

        if(savedInstanceState != null)
            mCheated = savedInstanceState.getBoolean(KEY_CHEATED);
        if(!mCheated)
            setAnswerShown(false);
        else
            setAnswerShown(true);

        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAnswer();
            }
        });
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //TextView view = (TextView) findViewById(R.id.question_text_view);
                //System.out.println("back here");
                //view.setText("here");

            }
        });
    }
}
