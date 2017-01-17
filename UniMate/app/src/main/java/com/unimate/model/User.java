package com.unimate.model;

import java.util.ArrayList;

/**
 * Created by Hans Vader on 31.10.2016.
 */
public class User {

    private String name;
    // TODO: attribute ausdenken ..
    private ArrayList<Modul> modules;

    public User(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Modul> getModules() {
        return modules;
    }

    public void setModules(ArrayList<Modul> modules) {
        this.modules = modules;
    }
}
