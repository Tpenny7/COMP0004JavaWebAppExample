package uk.ac.ucl.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Column implements Iterable<String>{
    String name;
    ArrayList<String> rows = new ArrayList<>();

    public Column(String name){
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Column name must not be null/blank");
        this.name = name;
    }

    public String getName() { return name;}

    @Override
    public Iterator<String> iterator(){
        return Collections.unmodifiableList(rows).iterator();
    }

    public int getSize(){ return rows.size();}

    public String getRowValue(int index){ return rows.get(index);}

    public void setRowValue(int index, String row){ rows.set(index,row);}

    public void addRowValue(String row){ rows.add(row);}
}
