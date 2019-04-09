package com.android.priyanka.firebasecrud;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText edName;
    Spinner spQualification;
    Button btnSave;
    String name,qualification;
    DatabaseReference databasePerson;

    ListView listViewPerson;
    List<Person> personList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databasePerson = FirebaseDatabase.getInstance().getReference("person");

       edName = findViewById(R.id.name);
       spQualification = findViewById(R.id.qualification);

       listViewPerson = findViewById(R.id.personlist);

       personList = new ArrayList<>();
       btnSave = findViewById(R.id.save);

       btnSave.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               addPersonInfo();
           }
       });

       listViewPerson.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
           @Override
           public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

               Person person = personList.get(position);
               showUpdateDialog(person.getPersonId(),person.getPersonName());
               return false;
           }
       });

    }


    @Override
    protected void onStart() {
        super.onStart();

        databasePerson.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                personList.clear();
                for(DataSnapshot personSnapshot: dataSnapshot.getChildren()) {
                    Person person = personSnapshot.getValue(Person.class);

                    personList.add(person);
                }

                PersonList adapter = new PersonList(MainActivity.this,personList);
                listViewPerson.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {



            }
        });


    }

    public void addPersonInfo() {

        name = edName.getText().toString();
        qualification = spQualification.getSelectedItem().toString();

        if (!TextUtils.isEmpty(name)) {

            String id = databasePerson.push().getKey();
            Person person = new Person(id,name,qualification);
            databasePerson.child(id).setValue(person);
         Toast.makeText(getApplicationContext(),"Info added!",Toast.LENGTH_LONG).show();

        }
        else {
            Toast.makeText(getApplicationContext(), "Please enter the Name!", Toast.LENGTH_SHORT).show();

        }


    }



    private void showUpdateDialog(final String personId, final String personName) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog,null);
        dialogBuilder.setView(dialogView);


        final EditText editTextName = dialogView.findViewById(R.id.edName);
        final Button btnUpdate = dialogView.findViewById(R.id.update);
        final Button btnDelete = dialogView.findViewById(R.id.delete);
        final Spinner spinQual = dialogView.findViewById(R.id.qualification);

        dialogBuilder.setTitle("Updating Artist "+personName);

        final AlertDialog alertDialog = dialogBuilder.create();

        alertDialog.show();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               String name = editTextName.getText().toString();
               String  qualification = spinQual.getSelectedItem().toString();

                if (TextUtils.isEmpty(name)) {

                    editTextName.setError("Name required!");
                    return;
                }
                updateInfo(personId,name,qualification);
                alertDialog.dismiss();
            }
        });

btnDelete.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        deleteInfo(personId);

    }
});

    }

    private void deleteInfo(String personId) {
        DatabaseReference refrence = FirebaseDatabase.getInstance().getReference("person").child(personId);
        refrence.removeValue();
        Toast.makeText(getApplicationContext(),"Data deleted",Toast.LENGTH_LONG).show();

    }

    private boolean updateInfo(String id,String name,String qualification) {

        DatabaseReference refrence = FirebaseDatabase.getInstance().getReference("person").child(id);
        Person person = new Person(id,name,qualification);
        refrence.setValue(person);

        Toast.makeText(getApplicationContext(),"Data Updated",Toast.LENGTH_LONG).show();

        return true;
    }
}
