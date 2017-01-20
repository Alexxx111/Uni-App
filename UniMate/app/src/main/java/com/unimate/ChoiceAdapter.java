package com.unimate;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.unimate.model.Message;
import com.unimate.model.Modul;
import com.unimate.model.User;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Hans Vader on 09.12.2016.
 */

public class ChoiceAdapter extends BaseAdapter {

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]

    private FirebaseAuth mAuth;

    private Activity activity;
    private ArrayList<Modul> modules;
    private ArrayList<Modul> alreadySelectedModules;
    private ArrayList<Modul> selectedModules;
    private static LayoutInflater inflater=null;

    private ArrayList<String> symbolOfSelectedModules;

    private Switch aSwitch;

    public ChoiceAdapter(Activity a, ArrayList<Modul> modules,ArrayList<Modul> alreadySelectedModules) {
        activity = a;
        this.modules = modules;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.alreadySelectedModules = alreadySelectedModules;

        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        selectedModules = new ArrayList<>();

       for(Modul m: alreadySelectedModules){
           selectedModules.add(m);
       }

        symbolOfSelectedModules = new ArrayList<>();

        for(Modul m: alreadySelectedModules){
            symbolOfSelectedModules.add(m.getSymbol());
        }
    }

    public int getCount() {
        return modules.size();
    }

    public Object getItem(int position) {
        return modules.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi=convertView;

        if(convertView==null)
            vi = inflater.inflate(R.layout.modules_element, null);

        aSwitch = (Switch)vi.findViewById(R.id.aswitch);
        final TextView switchText = (TextView)vi.findViewById(R.id.switch_text);

        System.out.println("already selected: " + alreadySelectedModules);

        if(symbolOfSelectedModules.contains(modules.get(position).getSymbol())){
            System.out.println("switch !!!");
            aSwitch.setChecked(true);
        }
        else{
            aSwitch.setChecked(false);
        }

        switchText.setText(modules.get(position).getName());

        aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Modul m = modules.get(position);

                System.out.println("module .. pressing modulSymbol:: " + m.getSymbol());

                        if(symbolOfSelectedModules.contains(m.getSymbol())){

                            System.out.println("module .. modulID:: " + m.getId());

                            if(!selectedModules.isEmpty()) {
                                Modul toBeDeleted = new Modul();

                                for (Modul m1 : selectedModules) {
                                    if (m1.getId().equals(modules.get(position).getId())) {

                                        toBeDeleted = m1;
                                        System.out.println("module .. your trying to delete: " + m1 + " removing " + selectedModules.size());

                                        System.out.println("module ..  in the list are: ");
                                    }
                                    System.out.println("module ..  -- " + m1);
                                }

                                selectedModules.remove(toBeDeleted);
                            }
                            symbolOfSelectedModules.remove(m.getSymbol());

                            System.out.println("module .. removing ids::  " + symbolOfSelectedModules.size());
                        }
                        else{
                            System.out.println("module .. adding " + selectedModules.size());
                            selectedModules.add(m);
                            symbolOfSelectedModules.add(m.getSymbol());

                        }



            }
        });

        return vi;
    }

    public ArrayList<Modul> getSelectedModules() {

        System.out.println("selectedModules size:: " + selectedModules.size());

        return selectedModules;
    }
}
