package Server.Domain;
import java.util.ArrayList;

public class Landlord extends User{

    public ArrayList<Property> ownedProperties;

    public Landlord(String userName, String firstName, String lastName, String email, String password, ArrayList<Property> properties){
        super(userName, firstName, lastName, email, password, "Landlord");
        ownedProperties = properties;
    }
}