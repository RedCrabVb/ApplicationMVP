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

import ru.vivt.applicationmvp.ui.repository.Server;

public class StartActivity extends AppCompatActivity {
    private EditText editTextIp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        Thread thread = new Thread(() -> {{
            Server server = Server.getInstance(editTextIp.getText().toString());

            try (DataInputStream inputStream = new DataInputStream(new FileInputStream(new File(this.getFilesDir(), Server.fileNameConfig)))) {
                String str = inputStream.readUTF();
                JsonObject json = new JsonParser().parse(str).getAsJsonObject();
                server.setTokenConnection(json.get(Server.token).getAsString());
                server.setQrCodeConntion(json.get(Server.qrCode).getAsString());
            } catch (FileNotFoundException e) {
                new File(getFilesDir(), Server.fileNameConfig).delete();
                if (!server.registration()) {
                    Toast.makeText(StartActivity.this, "False to connection server", Toast.LENGTH_LONG).show();
                    System.exit(-1);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (!server.tokenActive()) {
                new File(this.getFilesDir(), Server.fileNameConfig).delete();
                server.registration();
                try (DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(new File(getFilesDir(), Server.fileNameConfig)))){
                    JsonObject json = new JsonObject();
                    json.addProperty(Server.qrCode, server.getQrCodeConntion());
                    json.addProperty(Server.token, server.getTokenConnection());
                    outputStream.writeUTF(json.toString());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
        }});

        editTextIp = findViewById(R.id.editTextIp);
        findViewById(R.id.enterIP).setOnClickListener(v -> {
            thread.start();
        });
    }
}