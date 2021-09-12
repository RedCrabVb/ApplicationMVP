package ru.vivt.applicationmvp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import ru.vivt.applicationmvp.ui.repository.Server;

import static android.provider.Telephony.Mms.Part.FILENAME;

public class StartActivity extends AppCompatActivity {
    private EditText editTextIp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        Thread thread = new Thread(() -> {{
            Server server = Server.getInstance(editTextIp.getText().toString());

            try (DataInputStream inputStream = new DataInputStream(new FileInputStream(new File(getFilesDir(), "config.json")))) {
                System.out.println(getFilesDir());
                String str = inputStream.readUTF();
                JsonObject json = new JsonParser().parse(str).getAsJsonObject();
                server.setToken(json.get("token").getAsString());
                server.setQrCode(json.get("qrCode").getAsString());
            } catch (Exception e) {
                if (!server.registration()) {
                    Toast.makeText(StartActivity.this, "False to connection server", Toast.LENGTH_LONG).show();
                    System.exit(-1);
                }
                e.printStackTrace();
            }

            if (!server.tokenActive()) {
                new File(this.getFilesDir(), "config.json").delete();
                server.registration();
            }

            try (DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(new File(getFilesDir(), "config.json")))){
                JsonObject json = new JsonObject();
                json.addProperty("qrCode", server.getQrCode());
                json.addProperty("token", server.getToken());
                outputStream.writeUTF(json.toString());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
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