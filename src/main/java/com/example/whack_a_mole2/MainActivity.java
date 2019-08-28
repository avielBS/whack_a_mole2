package com.example.whack_a_mole2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;




public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = findViewById(R.id.start_btn);
        editText = findViewById(R.id.edit_text_name);

        startButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String name = editText.getText().toString();

                if(!name.isEmpty() )
                    openGameActivity(name);
                else
                    enterValidNameMessage();
            }
        });


    }

    private void enterValidNameMessage() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String message = "";

        builder.setTitle("Error").setMessage(message);
        builder.setMessage("Please Enter Valid Name");
        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });
        builder.show();
    }

    private void openGameActivity(String name) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("PlayerName",name);
        startActivity(intent);
    }

}
