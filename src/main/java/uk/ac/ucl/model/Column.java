package uk.ac.ucl.model;

import java.util.ArrayList;

public class Column {
    String name;
    ArrayList<String> rows;

    public Column(String name){ this.name = name;}

    public String getName() { return name;}

    public int getSize(){ return rows.size();}

    public String getRowValue(int index){ return rows.get(index);}

    public void setRowValue(int index, String row){ rows.set(index,row);}

    public void addRowValue(String row){ rows.add(row);}
}
