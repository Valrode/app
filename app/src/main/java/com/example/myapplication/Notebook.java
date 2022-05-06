package com.example.myapplication;

import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;

import com.example.myapplication.databinding.ActivityNotebookBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Notebook extends AppCompatActivity {

    private ActivityNotebookBinding binding;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference User = database.getReference("User");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            ((EditText) findViewById(R.id.text_notebook)).setText(savedInstanceState.getString("notebook_text"));
        }
        binding = ActivityNotebookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle(getTitle());
        User.child("TextNotebook").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    ((EditText) findViewById(R.id.text_notebook)).setText("Error");
                } else {
                    ((EditText) findViewById(R.id.text_notebook)).setText(String.valueOf(task.getResult().getValue()));
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        User.child("TextNotebook").setValue(((EditText) findViewById(R.id.text_notebook)).getText().toString());
        finish();
    }
}