package Functionality;

import java.io.Serializable;
import java.util.ArrayList;

public class PropertyListing implements Serializable{
	private static final long serialVersionUID = 1L;

    private ArrayList<Property> properties = new ArrayList<Property>();

    public PropertyListing(ArrayList<Property> properties){
        this.properties = new ArrayList<Property>();
    }

    public PropertyListing() {

	}

	public void addNewListing(Property p){
        properties.add(p);
    }

    public void removeListing(Property p){
        properties.remove(p);
    }
}