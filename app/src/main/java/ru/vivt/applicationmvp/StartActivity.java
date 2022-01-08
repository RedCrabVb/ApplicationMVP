package ru.vivt.applicationmvp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import ru.vivt.applicationmvp.ui.repository.MemoryValues;
import ru.vivt.applicationmvp.ui.repository.Server;

public class StartActivity extends AppCompatActivity {
    private EditText editTextIp;
    private static final String server = "10.0.2.2:8082"; // for test 10.0.2.2:8082
                                                        // for prod servermvp.ru:49207

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        Thread thread = new Thread(() -> {{
            Server server = Server.getInstance(editTextIp.getText().toString());

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