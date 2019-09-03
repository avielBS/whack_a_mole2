package com.example.whack_a_mole2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

        SQLiteDatabase mydatabase = openOrCreateDatabase(getString(R.string.DBname),MODE_PRIVATE,null);



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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch(item.getItemId()){
            case R.id.statistics:
                 intent = new Intent(this,Statistics.class);
                startActivity(intent);
                return true;
            case R.id.maps:
                 intent = new Intent(this,Maps.class);
                startActivity(intent);
                return true;

                default:
                    return super.onOptionsItemSelected(item);

        }

    }
}
