package stangenzirkel.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class NoteActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    String tag = "MainActivityTag";

    TextView headerTE;
    TextView bodyTE;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // getSupportActionBar().setDisplayShowTitleEnabled(false);
        // getSupportActionBar().setTitle("Adding note...");

        headerTE = findViewById(R.id.headerTE);
        bodyTE = findViewById(R.id.bodyTE);
        id = getIntent().getIntExtra("id", 0);
        if (id != 0) {
            String selection = "id = " + id;
            Cursor c =  new DBHelper(this).getWritableDatabase().query("mytable",
                    null,
                    selection,
                    null,
                    null,
                    null,
                    null,
                    "1");
            if (c.moveToFirst()) {
                headerTE.setText(c.getString(c.getColumnIndex("header")));
                bodyTE.setText(c.getString(c.getColumnIndex("body")));
            } else
                Log.d(tag, "row with id " + id + "not find");
            c.close();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                Log.d(tag, "btnSave clicked");

                //printDB();
                saveAll();

                break;

            case R.id.btnExit:
                Log.d(tag, "btnExit clicked");

                saveAll();
                closeActivity();
                break;
            case R.id.btnDelete:
                Log.d(tag, "btnDelete clicked");
                deleteAll();
                closeActivity();
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveAll();
    }

    private void closeActivity() {
        startActivity(new Intent(this, ShowActivity.class));
    }

    private void saveAll() {
        long time = System.currentTimeMillis();
        String header = headerTE.getText().toString();
        String body = bodyTE.getText().toString();

        if (header.isEmpty() && body.isEmpty()) {
            Toast.makeText(this, "Cannot save empty note", Toast.LENGTH_SHORT).show();
            return;
        }

        if (id == 0) {
            ContentValues cv = new ContentValues();

            cv.put("time", time);
            cv.put("header", header);
            cv.put("body", body);

            id = (int) new DBHelper(this).getWritableDatabase().insert("mytable", null, cv);
        } else {
            ContentValues cv = new ContentValues();

            cv.put("time", time);
            cv.put("header", header);
            cv.put("body", body);

            // обновляем по id
            new DBHelper(this).getWritableDatabase().update("mytable", cv, "id = ?",
                    new String[] { Integer.toString(id) });
        }
        Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
    }

    private void deleteAll() {
        Toast.makeText(this, "Deleting...", Toast.LENGTH_SHORT).show();

        new DBHelper(this).getWritableDatabase().delete("mytable", "id = " + id, null);
    }

    private void printDB() {
        Log.d(tag, "read mytable");
        Cursor c =  new DBHelper(this).getWritableDatabase().query("mytable",
                null,
                null,
                null,
                null,
                null,
                null);

        if (c.moveToFirst()) {
            int idColIndex = c.getColumnIndex("id");
            int timeColIndex = c.getColumnIndex("time");
            int headerColIndex = c.getColumnIndex("header");
            int bodyColIndex = c.getColumnIndex("body");

            do {
                Log.d(tag,
                        "ID = " + c.getInt(idColIndex) +
                                ", time = " + c.getString(timeColIndex) +
                                ", header = " + c.getString(headerColIndex) +
                                ", body = " + c.getString(bodyColIndex));
            } while (c.moveToNext());
        } else
            Log.d(tag, "0 rows");
        c.close();
    }
}