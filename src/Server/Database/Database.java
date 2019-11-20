package Server.Database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Server.Domain.Property;
import Server.Domain.User;

public class Database{
    public Connection getConn() throws ClassNotFoundException, SQLException{
        Connection c = DriverManager.getConnection("jdbc:sqlite:C:/Users/Youup/Documents/GitHub/ENSF480-Final-Project/src/Server/Database.db");
        return c;
    }

    public boolean addProperty(Property p){
    	boolean success = true;
    	Connection conn = null;
    	PreparedStatement addProperty = null;
    	String addPropertyString = "INSERT INTO Property values (?, ?, ?, ? ,? ,?, ?, ?)";
    	try {
    		conn = getConn();
    		if(conn != null) {
    			addProperty = conn.prepareStatement(addPropertyString);
    			addProperty.setInt(1, p.getID());
    			addProperty.setString(2, p.getType());
    			addProperty.setInt(3, p.getNumOfBedrooms());
    			addProperty.setInt(4, p.getNumOfBathrooms());
    			addProperty.setBoolean(5, p.isFurnished());
    			addProperty.setString(6, p.getCityQuadrant());
    			addProperty.setString(7, p.getListingState());
    			addProperty.setDouble(8, p.getFee().getAmount());
    			addProperty.executeUpdate();
    			System.out.println("Added property");
    		}
    		conn.close();
    	} catch(ClassNotFoundException | SQLException e) {
    		e.printStackTrace();
    		return false;
    	}
    	return success;
    }

    public boolean removeProperty(Property p){
    	Connection conn = null;
    	PreparedStatement deleteProperty = null;
    	String deletePropertyString = "DELETE FROM Property WHERE ID = ?";
    	try {
    		conn = getConn();
    		if(conn != null) {
    			deleteProperty = conn.prepareStatement(deletePropertyString);
    			deleteProperty.setInt(1, p.getID());
    			deleteProperty.executeUpdate();
    			conn.close();
    			return true;
    		}
    		conn.close();
    	} catch(ClassNotFoundException | SQLException e) {
    		e.printStackTrace();
    	}
    	return false;
    }
    
    public boolean changeState(String newState, int ID) {
    	Connection conn = null;
    	PreparedStatement changeState = null;
    	String changeStateString = "UPDATE Property SET listingState = ? WHERE ID = ?";
    	try {
    		conn = getConn();
    		if(conn != null) {
    			changeState = conn.prepareStatement(changeStateString);
    			changeState.setString(1, newState);
    			changeState.setInt(2, ID);
    			changeState.executeUpdate();
    			conn.close();
    			return true;
    		}
    		conn.close();
    	} catch(ClassNotFoundException | SQLException e) {
    		e.printStackTrace();
    	}
    	return false;
    }
    
    public Property getProperty(int ID) {
    	Connection conn = null;
    	PreparedStatement getProperty = null;
    	String getPropertyString = "SELECT * from Property WHERE ID = ?";
    	try {
    		conn = getConn();
    		if(conn != null) {
    			getProperty = conn.prepareStatement(getPropertyString);
    			getProperty.setInt(1, ID);
    			ResultSet rs = getProperty.executeQuery();
    			Property p = new Property(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4),
        					rs.getBoolean(5), rs.getString(6), rs.getString(7));
    			conn.close();
    			return p;
    		}
    		conn.close();
    	} catch(ClassNotFoundException | SQLException e) {
    		e.printStackTrace();
    	}
    	return null;
    }

    public ArrayList<Property> getProperties(){
        Connection conn = null;
        PreparedStatement getAllProperties = null;
        String getAllPropertiesString = "SELECT * from Property";
        ArrayList<Property> temp = new ArrayList<Property>();
        try {
        	conn = getConn();
        	if(conn != null) {
        		getAllProperties = conn.prepareStatement(getAllPropertiesString);
        		ResultSet rs = getAllProperties.executeQuery();
        		while(rs.next()) {
        			Property p = new Property(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4),
        					rs.getBoolean(5), rs.getString(6), rs.getString(7));
        			temp.add(p);
        		}
        		conn.close();
        		return temp;
        	}
        	conn.close();
        } catch(ClassNotFoundException | SQLException e) {
        	e.printStackTrace();
        }
        return null;
    }
    
    
    public boolean addUser(User u){
    	boolean success = true;
    	Connection conn = null;
    	PreparedStatement addUser = null;
    	String addUserString = "INSERT INTO User values (?, ?, ?, ? ,? ,?, ?)";
    	try {
    		conn = getConn();
    		if(conn != null) {
    			addUser = conn.prepareStatement(addUserString);
    			addUser.setInt(1, u.getID());
    			addUser.setString(2, u.getType());
    			addUser.setString(3, u.getUserName());
    			addUser.setString(4, u.getFirstName());
    			addUser.setString(5, u.getLastName());
    			addUser.setString(6, u.getEmail());
    			addUser.setString(7, u.getPassword());
    			addUser.executeUpdate();
    			System.out.println("Added User");
    		}
    		conn.close();
    	} catch(ClassNotFoundException | SQLException e) {
    		e.printStackTrace();
    		return false;
    	}
    	return success;
    }

    public boolean removeUser(User u){
    	Connection conn = null;
    	PreparedStatement deleteUser = null;
    	String deleteUserString = "DELETE FROM User WHERE ID = ?";
    	try {
    		conn = getConn();
    		if(conn != null) {
    			deleteUser = conn.prepareStatement(deleteUserString);
    			deleteUser.setInt(1, u.getID());
    			deleteUser.executeUpdate();
    			conn.close();
    			return true;
    		}
    		conn.close();
    	} catch(ClassNotFoundException | SQLException e) {
    		e.printStackTrace();
    	}
    	return false;
    }
    
    public User getUser(String username) {
    	Connection conn = null;
    	PreparedStatement getUser = null;
    	String getUserString = "SELECT * from User WHERE userName = ?";
    	try {
    		conn = getConn();
    		if(conn != null) {
    			getUser = conn.prepareStatement(getUserString);
    			getUser.setString(1, username);
    			ResultSet rs = getUser.executeQuery();
    			User u = new User(rs.getInt(1), rs.getString(3), rs.getString(4), rs.getString(5),
        					rs.getString(6), rs.getString(7), rs.getString(2));
    			conn.close();
    			return u;
    		}
    		conn.close();
    	} catch(ClassNotFoundException | SQLException e){
    		e.printStackTrace();
    	}
    	return null;
    }
    
    public ArrayList<User> getUsers(){
        Connection conn = null;
        PreparedStatement getAllUsers = null;
        String getAllUsersString = "SELECT * from User";
        ArrayList<User> temp = new ArrayList<User>();
        try {
        	conn = getConn();
        	if(conn != null) {
        		getAllUsers = conn.prepareStatement(getAllUsersString);
        		ResultSet rs = getAllUsers.executeQuery();
        		while(rs.next()) {
        			User u = new User(rs.getInt(1), rs.getString(3), rs.getString(4), rs.getString(5),
        					rs.getString(6), rs.getString(7), rs.getString(2));
        			temp.add(u);
        		}
        		conn.close();
        		return temp;
        	}
        	conn.close();
        } catch(ClassNotFoundException | SQLException e) {
        	e.printStackTrace();
        }
        return null;
    }
    
    public boolean updateFee()

//    public User validateLogin(String username, String password){
//        
//    }
}