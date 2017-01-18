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
    private ArrayList<Modul> selectedModules;
    private static LayoutInflater inflater=null;

    private Switch aSwitch;

    public ChoiceAdapter(Activity a, ArrayList<Modul> modules) {
        activity = a;
        this.modules = modules;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);



        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        selectedModules = new ArrayList<>();
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

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;

        if(convertView==null)
            vi = inflater.inflate(R.layout.modules_element, null);

        aSwitch = (Switch)vi.findViewById(R.id.aswitch);
        final TextView switchText = (TextView)vi.findViewById(R.id.switch_text);

        aSwitch.setText("");
        switchText.setText(modules.get(position).getName());

        aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for(Modul m: modules){
                    if(m.getName().equals(switchText.getText().toString())){
                        selectedModules.add(m);
                    }
                }

            }
        });

        return vi;
    }

    public ArrayList<Modul> getSelectedModules() {
        return selectedModules;
    }
}
