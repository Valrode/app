package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MAIN extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference User = database.getReference("User");
    DatabaseReference IMG = User.child("IMG");
    DatabaseReference DESC = User.child("DESC");
    public ArrayList<Integer> IDs = new ArrayList<>(Arrays.asList(R.id.Inv1,R.id.Inv2,R.id.Inv3,R.id.Inv4,R.id.Inv5,R.id.Inv6,R.id.Inv7,R.id.Inv8,R.id.Inv9,R.id.Inv10,R.id.Inv11,R.id.Inv12,R.id.Inv13,R.id.Inv14,R.id.Inv15,R.id.Inv16));
    public ArrayList<Integer> Armes = new ArrayList<>(Arrays.asList(R.id.ArmeDroite,R.id.ArmeGauche));
    public ArrayList<Integer> Anneaux = new ArrayList<>(Arrays.asList(R.id.AnneauDroite,R.id.AnneauGauche));
    public ArrayList<Integer> Gants = new ArrayList<>(Arrays.asList(R.id.GantDroite,R.id.GantGauche));
    public ArrayList<Integer> Collier = new ArrayList<>(Arrays.asList(R.id.Collier));
    public ArrayList<Integer> Torse = new ArrayList<>(Arrays.asList(R.id.Torse));
    public ArrayList<Integer> Haume = new ArrayList<>(Arrays.asList(R.id.Haume));
    public ArrayList<Integer> Botte = new ArrayList<>(Arrays.asList(R.id.Botte));
    public ArrayList<Integer> Ceinture = new ArrayList<>(Arrays.asList(R.id.Ceinture));

    public ArrayList<Integer> KeyForDesc = new ArrayList<>();
    public int tempInc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        Button ButtonStats = findViewById(R.id.button);

        ButtonStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Intent intent;
                intent = new Intent(MAIN.this, Stats.class);
                MAIN.this.startActivity(intent);
            }
        });

        Button ButtonDice = findViewById(R.id.button2);

        ButtonDice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Intent intent;
                intent = new Intent(MAIN.this, Dice.class);
                MAIN.this.startActivity(intent);
            }
        });

        Button ButtonSpell = findViewById(R.id.button3);

        ButtonSpell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Intent intent;
                intent = new Intent(MAIN.this, Spell.class);
                MAIN.this.startActivity(intent);
            }
        });

        Button ButtonNotebook = findViewById(R.id.button4);

        ButtonNotebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Intent intent;
                intent = new Intent(MAIN.this, Notebook.class);
                MAIN.this.startActivity(intent);
            }
        });

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(MAIN.this);

        if(account != null){
            NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
            View headerView = navView.getHeaderView(0);
            String Name = account.getDisplayName();
            String Email = account.getEmail();
            Uri Image = account.getPhotoUrl();
            ((TextView)headerView.findViewById(R.id.EmailUser)).setText(Email);
            ((TextView)headerView.findViewById(R.id.NameUser)).setText(Name);
            ((ImageView)headerView.findViewById(R.id.ImageUser)).setImageURI(Image);
        }

        thread.start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    Thread thread = new Thread(new Runnable() {

        ImageButton ImageButton;
        Drawable Image;
        @Override
        public void run() {
            try {
                File test = File.createTempFile("IMG", "png");
                IMG.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(MAIN.this, "Error Loading Image", Toast.LENGTH_SHORT).show();
                        } else {
                            Integer Inc = -1;
                            for (Object i : task.getResult().getChildren()) {
                                Inc += 1;
                                KeyForDesc.add(Inc+1);
                                Integer Value = Integer.parseInt(i.toString().substring(i.toString().length()-3,i.toString().length()-2));
                                StorageReference data = FirebaseStorage.getInstance().getReference().child("IMG_OBJ").child(Value.toString()+".png");
                                Integer finalInc = Inc;
                                ((ImageButton)findViewById(IDs.get(finalInc))).setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {
                                        DescItem(view,finalInc);
                                        return true;
                                    }
                                });

                                ((ImageButton)findViewById(IDs.get(finalInc))).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (Image == null) {
                                            Image = ((ImageButton)findViewById(IDs.get(finalInc))).getDrawable();
                                            ImageButton = (ImageButton) findViewById(IDs.get(finalInc));
                                            tempInc = finalInc;
                                            PlaceItem(finalInc);

                                        }
                                        else {
                                            ImageButton.setImageDrawable((((ImageButton) findViewById(IDs.get(finalInc))).getDrawable()));
                                            ((ImageButton)findViewById(IDs.get(finalInc))).setImageDrawable(Image);
                                            Image = null;
                                            Collections.swap(KeyForDesc,tempInc,finalInc);
                                        }
                                    }
                                });
                                data.getFile(test).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        SetIMG(test,finalInc);
                                    }
                                });
                            }
                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    });

    public void DescItem (View view, int finalInc) {
        AlertDialog.Builder Popup_Builder = new AlertDialog.Builder(view.getContext());
        final View PopUp = getLayoutInflater().inflate(R.layout.popup,null);
        DESC.child(KeyForDesc.get(finalInc).toString()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    ((TextView) findViewById(R.id.TextPopup)).setText("Error");
                }
                else {
                    ((TextView) PopUp.findViewById(R.id.TextPopup)).setText(task.getResult().getValue().toString());
                }
            }
        });
        Popup_Builder.setView(PopUp);
        Popup_Builder.create();
        Popup_Builder.show();
    }

    public void SetIMG (File test,int finalInc) {
        try {
            Bitmap img = BitmapFactory.decodeFile(test.getAbsolutePath());
            ((ImageButton) findViewById(IDs.get(finalInc))).setImageBitmap(img);
        } catch (Exception e) {
            Toast.makeText(MAIN.this, "Error Loading Image", Toast.LENGTH_SHORT).show();
        }
    }

    void PlaceItem (int finalInc) {
        ArrayList<ArrayList> tempo = new ArrayList<>();
        Drawable croix = getDrawable(R.drawable.croix);
        switch (finalInc) {
            case 3 :
                for (int i:Ceinture) {
                    ((ImageButton) findViewById(i)).setImageDrawable(croix);
                }
                for (int i:Anneaux) {
                    ((ImageButton) findViewById(i)).setImageDrawable(croix);
                }
                for (int i:Collier) {

                    ImageButton Collier = findViewById(i);

                    Collier.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View V) {
                            Drawable IMGtemp = ((ImageButton)findViewById(IDs.get(finalInc))).getDrawable();
                            Collier.setImageDrawable(IMGtemp);
                            int tempID = IDs.get(5);
                            ((ImageButton)findViewById(IDs.get(finalInc))).setImageDrawable(((ImageButton) findViewById(tempID)).getDrawable());
                            for (int i:Ceinture) {
                                Drawable ceinture = getDrawable(R.drawable.ceinture);
                                ((ImageButton) findViewById(i)).setImageDrawable(ceinture);
                            }
                            for (int i:Anneaux) {
                                Drawable Anneaux = getDrawable(R.drawable.ringslots);
                                ((ImageButton) findViewById(i)).setImageDrawable(Anneaux);
                            }
                        }
                    });

                }
        }
    }
}