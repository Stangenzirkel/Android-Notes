package stangenzirkel.notes;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.NoteViewHolder> {
    String tag = "RVAdapterTag";
    Activity activity;

    public static class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener,
            AdapterView.OnClickListener,
            MenuItem.OnMenuItemClickListener {
        String tag = "NoteViewHolderTag";
        ShowActivity activity;

        final int DELETE = 1, EDIT = 2;

        CardView cv;
        TextView dateTV;
        TextView headerTV;
        TextView bodyTV;

        Note note;

        NoteViewHolder(View itemView, ShowActivity activity) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            dateTV = itemView.findViewById(R.id.cardDateTextView);
            headerTV = itemView.findViewById(R.id.cardHeaderTextView);
            bodyTV = itemView.findViewById(R.id.cardBodyTextView);
            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnClickListener(this);

            this.activity = activity;
        }

        public void setNote(Note note) {
            this.note = note;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            Log.d(tag, "onCreateContextMenu");
            menu.add(0, DELETE, 0, "Delete").setOnMenuItemClickListener(this);
            menu.add(0, EDIT, 0, "Edit").setOnMenuItemClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d(tag, "click to note " + note.getId());
            activity.openNote((int) note.getId());
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            Log.d(tag, "menu item click to note " + note.getId());
            switch (item.getItemId()) {
                case (DELETE):
                    Log.d(tag, "delete note " + note.getId());
                    activity.deleteNote((int) note.getId());
                    break;
                case (EDIT):
                    Log.d(tag, "edit note " + note.getId());
                    activity.openNote((int) note.getId());
                    break;
            }
            return false;
        }
    }

    List<Note> notes;

    RVAdapter(List<Note> notes, Activity activity){
        this.notes = notes;
        this.activity = activity;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card, viewGroup, false);
        NoteViewHolder noteViewHolder = new NoteViewHolder(v, (ShowActivity) activity);
        return noteViewHolder;
    }

    @Override
    public void onBindViewHolder(NoteViewHolder noteViewHolder, int i) {
        noteViewHolder.dateTV.setText(notes.get(i).getDate());
        noteViewHolder.headerTV.setText(notes.get(i).getHeader());
        noteViewHolder.bodyTV.setText(notes.get(i).getBody());
        noteViewHolder.setNote(notes.get(i));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }
}