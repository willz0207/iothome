package com.example.iothome;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {

    ToggleButton buttonLampu1;
    ToggleButton buttonLampu2;


    TextView statuspir;
    TextView statusLdr;

    String valueButton;
    String valueLdr;
    String valueLampu1;
    String valueLampu2;

    DatabaseReference dref;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonLampu1 = (ToggleButton) findViewById(R.id.toggleButtonlampu1);
        buttonLampu2 = (ToggleButton) findViewById(R.id.toggleButtonlampu2);

        statusLdr = (TextView) findViewById(R.id.txtView_valueLdr);
        statuspir = (TextView) findViewById(R.id.txtView_valuepirSensor);


        dref = FirebaseDatabase.getInstance().getReference();
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                valueLdr = dataSnapshot.child("Node1/ldr").getValue().toString();
                statusLdr.setText(valueLdr);

                valueButton = dataSnapshot.child("Node1/pir").getValue().toString();
                if(valueButton.equals("0"))
                    statuspir.setText("no motion");
                else
                    statuspir.setText("motion detect ");

                valueLampu1 = dataSnapshot.child("Node1/led").getValue().toString();
                if(valueLampu1.equals("0"))
                    buttonLampu1.setChecked(false);
                else
                    buttonLampu1.setChecked(true);

                valueLampu2 = dataSnapshot.child("Node1/led1").getValue().toString();
                if(valueLampu2.equals("0"))
                    buttonLampu2.setChecked(false);
                else
                    buttonLampu2.setChecked(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        buttonLampu1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    DatabaseReference lampu1Ref = FirebaseDatabase.getInstance().getReference("Node1/led");
                    lampu1Ref.setValue(1);
                }
                else
                {
                    DatabaseReference lampu1Ref = FirebaseDatabase.getInstance().getReference("Node1/led");
                    lampu1Ref.setValue(0);
                }
            }
        });

        buttonLampu2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    DatabaseReference lampu2Ref = FirebaseDatabase.getInstance().getReference("Node1/led1");
                    lampu2Ref.keepSynced(true);

                    lampu2Ref.setValue(1);
                }
                else
                {
                    DatabaseReference lampu2Ref = FirebaseDatabase.getInstance().getReference("Node1/led1");
                    lampu2Ref.keepSynced(true);

                    lampu2Ref.setValue(0);
                }
            }
        });
    }
}