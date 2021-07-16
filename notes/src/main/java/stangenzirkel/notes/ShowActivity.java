package stangenzirkel.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ShowActivity extends AppCompatActivity implements View.OnClickListener {
    private String tag = "ShowActivityTag";
    private List<Note> notes;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        rv = findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        initializeData();
        // initializeAdapter();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(tag, "onstart");

        initializeData();
        initializeAdapter();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fbtnAdd: {
                Log.d(tag, "fbtnAdd clicked");
                Toast.makeText(this, "New note", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, NoteActivity.class);
                startActivity(intent);

                break;
            }
        }
    }

    public void openNote(int id) {
        Intent intent = new Intent(this, NoteActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    public void deleteNote(int id) {
        Log.d(tag, "deleting note " + id);
        new DBHelper(this).getWritableDatabase().delete("mytable", "id = " + id, null);

        initializeData();
        initializeAdapter();
    }

    private void initializeData(){
        notes = new ArrayList<>();
//        notes.add(new Note(1, 0, "Note1", "NoteText1"));
//        notes.add(new Note(1, 0, "Note2", "NoteText2"));
//        notes.add(new Note(1, 0, "Note3", "NoteText3"));

        Cursor c =  new DBHelper(this).getWritableDatabase().query("mytable", null, null, null, null, null, null);

        if (c.moveToFirst()) {
            int idColIndex = c.getColumnIndex("id");
            int timeColIndex = c.getColumnIndex("time");
            int headerColIndex = c.getColumnIndex("header");
            int bodyColIndex = c.getColumnIndex("body");

            do {
                notes.add(new Note(c.getLong(idColIndex),
                        c.getLong(timeColIndex),
                        c.getString(headerColIndex),
                        c.getString(bodyColIndex)));
            } while (c.moveToNext());
        }
        c.close();
    }

    private void initializeAdapter(){
        RVAdapter adapter = new RVAdapter(notes, this);
        rv.setAdapter(adapter);
    }
}