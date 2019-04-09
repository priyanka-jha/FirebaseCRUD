package com.android.priyanka.firebasecrud;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.nio.channels.AcceptPendingException;
import java.util.List;

public class PersonList extends ArrayAdapter<Person> {

   private Activity context;
   private List<Person> personList;

   public PersonList(Activity context,List<Person> personList) {
       super(context,R.layout.listdata,personList);

       this.context = context;
       this.personList = personList;



   }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.listdata,null,true);

        TextView tvName = listViewItem.findViewById(R.id.listname);
        TextView tvQual = listViewItem.findViewById(R.id.listqual);

        Person person = personList.get(position);

        tvName.setText(person.getPersonName());
        tvQual.setText(person.getPersonQualification());

        return listViewItem;



    }
}
