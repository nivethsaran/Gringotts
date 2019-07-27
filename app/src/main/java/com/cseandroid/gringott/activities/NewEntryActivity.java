package com.cseandroid.gringott.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.cseandroid.gringott.R;

public class NewEntryActivity extends AppCompatActivity implements View.OnClickListener {
    EditText et1,et2,et3,et4;
    Button b1,b2,b3;
    CheckBox b4;
    String activityType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);

        Intent typeIntent=getIntent();
        activityType=typeIntent.getStringExtra("type");


        et1=findViewById(R.id.fbet1);
        et2=findViewById(R.id.fbet2);
        et3=findViewById(R.id.fbet3);
        et4=findViewById(R.id.fbet4);
        b1=findViewById(R.id.button1);
        b2=findViewById(R.id.button2);
        b3=findViewById(R.id.button3);
        b4=findViewById(R.id.button4);

        b4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        if(activityType.equals("add"))
        {
            et1.setText("");
            et2.setText("");
            et4.setText("");
            b2.setVisibility(View.VISIBLE);
            b1.setVisibility(View.INVISIBLE);
            b3.setVisibility(View.INVISIBLE);

        }
        else if (activityType.equals("edit"))
        {
            String uname=typeIntent.getStringExtra("username");
            String sitename=typeIntent.getStringExtra("site");
            et1.setText(uname);
            et2.setText("from database");
            et4.setText(sitename);
            b3.setVisibility(View.INVISIBLE);
            b1.setVisibility(View.VISIBLE);
            b2.setVisibility(View.INVISIBLE);
        }
        else if (activityType.equals("view"))
        {String uname=typeIntent.getStringExtra("username");
            String sitename=typeIntent.getStringExtra("site");
            et1.setText(uname);
            et2.setText("from database");
            et4.setText(sitename);
            et1.setEnabled(false);
            et2.setEnabled(false);
            et4.setEnabled(false);
            b3.setVisibility(View.VISIBLE);
            b2.setVisibility(View.INVISIBLE);
            b1.setVisibility(View.INVISIBLE);
        }


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == b1.getId())
        {
            Intent intent=new Intent(NewEntryActivity.this,MainActivity.class);
            startActivity(intent);
//            ((EditText) findViewById(R.id.fbet1)).getText().clear();
//            ((EditText) findViewById(R.id.fbet2)).getText().clear();
//            ((EditText) findViewById(R.id.fbet3)).getText().clear();
        }
        if(v.getId()==b3.getId())
        {
            et1.setEnabled(true);
            et2.setEnabled(true);
            et4.setEnabled(true);
            b1.setVisibility(View.VISIBLE);
            b3.setVisibility(View.INVISIBLE);
            b2.setVisibility(View.INVISIBLE);

        }
            //if(PasswordStrength.calculateStrength(mViewData.mRegisterData.password). getValue() < PasswordStrength.STRONG.getValue())
//{
//    message = "Password should contain min of 6 characters and at least 1 lowercase, 1 uppercase and 1 numeric value";
//    return null;
//}
    }


}

//public enum PasswordStrength
//{
//
//    WEAK(0, Color.RED), MEDIUM(1, Color.argb(255, 220, 185, 0)), STRONG(2, Color.GREEN), VERY_STRONG(3, Color.BLUE);
//
//    static int REQUIRED_LENGTH = 6;
//    static int MAXIMUM_LENGTH = 6;
//    static boolean REQUIRE_SPECIAL_CHARACTERS = true;
//    static boolean REQUIRE_DIGITS = true;
//    static boolean REQUIRE_LOWER_CASE = true;
//    static boolean REQUIRE_UPPER_CASE = true;
//
//    int resId;
//    int color;
//
//    PasswordStrength(int resId, int color)
//    {
//        this.resId = resId;
//        this.color = color;
//    }
//
//    public int getValue()
//    {
//        return resId;
//    }
//
//    public int getColor()
//    {
//        return color;
//    }
//
//    public static PasswordStrength calculateStrength(String password)
//    {
//        int currentScore = 0;
//        boolean sawUpper = false;
//        boolean sawLower = false;
//        boolean sawDigit = false;
//        boolean sawSpecial = false;
//
//        for (int i = 0; i < password.length(); i++)
//        {
//            char c = password.charAt(i);
//
//            if (!sawSpecial && !Character.isLetterOrDigit(c))
//            {
//                currentScore += 1;
//                sawSpecial = true;
//            }
//            else
//            {
//                if (!sawDigit && Character.isDigit(c))
//                {
//                    currentScore += 1;
//                    sawDigit = true;
//                }
//                else
//                {
//                    if (!sawUpper || !sawLower)
//                    {
//                        if (Character.isUpperCase(c))
//                            sawUpper = true;
//                        else
//                            sawLower = true;
//                        if (sawUpper && sawLower)
//                            currentScore += 1;
//                    }
//                }
//            }
//        }
//
//        if (password.length() > REQUIRED_LENGTH)
//        {
//            if ((REQUIRE_SPECIAL_CHARACTERS && !sawSpecial) || (REQUIRE_UPPER_CASE && !sawUpper) || (REQUIRE_LOWER_CASE && !sawLower) || (REQUIRE_DIGITS && !sawDigit))
//            {
//                currentScore = 1;
//            }
//            else
//            {
//                currentScore = 2;
//                if (password.length() > MAXIMUM_LENGTH)
//                {
//                    currentScore = 3;
//                }
//            }
//        }
//        else
//        {
//            currentScore = 0;
//        }
//
//        switch (currentScore)
//        {
//            case 0:
//                return WEAK;
//            case 1:
//                return MEDIUM;
//            case 2:
//                return STRONG;
//            case 3:
//                return VERY_STRONG;
//            default:
//        }
//
//        return VERY_STRONG;
//    }
//
//}