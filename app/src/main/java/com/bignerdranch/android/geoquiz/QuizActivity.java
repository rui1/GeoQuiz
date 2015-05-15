package com.bignerdranch.android.geoquiz;

import android.app.Dialog;
import android.content.Intent;
import android.media.session.MediaSession;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class QuizActivity extends ActionBarActivity {
    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private Button mCheatButton;

    private boolean cheated;

    private List<TrueFalse> mQuestions;

    private int mCurrentQuestion;

    private static final String KEY_QUESTION = "question";
    private static final String KEY_CHEATED = "cheated";
    private static final int CHEATED_REQUEST = 0;
    private static final  int REPLAY_REQUEST=1;

    private final String TAG = QuizActivity.class.getName();

    private void logStateChange(String methodName) {
        Log.d(TAG, methodName + " called!");
    }

    @Override
    protected void onSaveInstanceState(Bundle onState) {
        onState.putInt(KEY_QUESTION, mCurrentQuestion);
        onState.putBoolean(KEY_CHEATED, cheated);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==CHEATED_REQUEST)cheated = data.getBooleanExtra(CheatActivity.EXTRA_ANSWER_SHOWN, false);
    }

    private void toastResult(boolean actual) {
        boolean expected = mQuestions.get(mCurrentQuestion).isTrueQuestion();
        String message;
        if(cheated) message = getString(R.string.judgment_toast);
        else if(expected == actual) message = "Correct!";
        else message = "Incorrect!";
        Toast.makeText(QuizActivity.this, message, Toast.LENGTH_SHORT).show();
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        logStateChange("onCreate");

        setContentView(R.layout.activity_quiz);

        mTrueButton = (Button) findViewById(R.id.true_button);
        mFalseButton = (Button) findViewById(R.id.false_button);
        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mCheatButton = (Button) findViewById(R.id.cheat_button);

        mQuestions = new ArrayList<TrueFalse>();
        mQuestions.add(new TrueFalse(R.string.q1, true));
        mQuestions.add(new TrueFalse(R.string.q2, false));
        mQuestions.add(new TrueFalse(R.string.q3, false));

        mCurrentQuestion = 0;
        if(savedInstanceState != null) {
            mCurrentQuestion = savedInstanceState.getInt(KEY_QUESTION);
            cheated = savedInstanceState.getBoolean(KEY_CHEATED);
        }

        TextView view = (TextView) findViewById(R.id.question_text_view);
        view.setText(mQuestions.get(mCurrentQuestion).getQuestion());

        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toastResult(true);
            }
        });
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toastResult(false);
            }
        });
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCurrentQuestion+1>= mQuestions.size()){
                    final Dialog dialog = new Dialog(QuizActivity.this);
                    dialog.setContentView(R.layout.activity_replay);
                    dialog.setCancelable(true);
                    TextView scView = (TextView) dialog.findViewById(R.id.score_view);
                    scView.setText(R.string.end_text);
                    Button replay = (Button) dialog.findViewById(R.id.replay_button);
                    Button exit = (Button)dialog.findViewById(R.id.exit_button);
                    replay.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            startActivity(getIntent());
                            mCurrentQuestion=-1;
                            cheated = false;
                        }
                    });
                    exit.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                } else if (mCurrentQuestion+1 <mQuestions.size() ){
                mCurrentQuestion = (mCurrentQuestion + 1) % mQuestions.size();
                TextView view = (TextView) findViewById(R.id.question_text_view);
                view.setText(mQuestions.get(mCurrentQuestion).getQuestion());
                cheated = false;}
            }
        });
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizActivity.this, CheatActivity.class);
                intent.putExtra(CheatActivity.EXTRA_ANSWER_IS_TRUE, mQuestions.get(mCurrentQuestion).isTrueQuestion());
                intent.putExtra(CheatActivity.CURRENT_QUESTION,mQuestions.get(mCurrentQuestion).getQuestion());
                startActivityForResult(intent, CHEATED_REQUEST);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        logStateChange("onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        logStateChange("onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        logStateChange("onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        logStateChange("onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        logStateChange("onStart");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quiz, menu);
        return true;
    }

}
