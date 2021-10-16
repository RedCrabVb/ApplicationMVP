package ru.vivt.applicationmvp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

import ru.vivt.applicationmvp.ui.repository.MemoryValues;
import ru.vivt.applicationmvp.ui.repository.Server;

public class StartActivity extends AppCompatActivity {
    private EditText editTextIp;
    private final String server = "servermvp.ru:49379"; // for test 10.0.2.2:8082
                                                        // for prod servermvp.ru:49379

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        Thread thread = new Thread(() -> {{
            Server server = Server.getInstance(editTextIp.getText().toString(), StartActivity.this.getApplicationContext());

            MemoryValues memoryValues = MemoryValues.init(getApplicationContext());
            String tokenInMemory = memoryValues.getToken();
            if (tokenInMemory != null) {
                server.setTokenConnection(tokenInMemory);
            } else {
                server.registration();
            }

            if (!server.tokenActive()) {
                server.registration();
            }

            server.saveDataInMemory(memoryValues);

            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
        }});

        editTextIp = findViewById(R.id.editTextIp);
        editTextIp.setText(server);
        thread.start();
    }
}