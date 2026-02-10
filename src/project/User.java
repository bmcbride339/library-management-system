package project;

import java.util.*;

public class User {
	
	private String ID;
	private String name;
	private ArrayList<String> checkedOut;
	private boolean isAdmin;
	private double lateFee;
	
	public User(String ID, String name, boolean isAdmin) {
		this.setID(ID);
		this.setName(name);
		this.setAdmin(isAdmin);
		this.checkedOut = new ArrayList<>();
		this.setLateFee(0.0);
		
	}

	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<String> getCheckedOut() {
		return checkedOut;
	}
	
	public boolean isAdmin() {
		return isAdmin;
	}
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	public double getLateFee() {
		return lateFee;
	}
	public void setLateFee(double lateFee) {
		this.lateFee = lateFee;
	}
	
	
	public void addItem(String itemID) {
		if (!checkedOut.contains(itemID)) {
			checkedOut.add(itemID);
		}
	}
	public void removeItem(String itemID) {
		if(checkedOut.contains(itemID)) {
			checkedOut.remove(itemID);
		}
	}
	public void lateFee(double amount) {
		lateFee += amount;
	}
	
	
	public String toFile() {
		 return String.format("USER|%s|%s|%b|%s|%.2f", ID, name, isAdmin, String.join(",", checkedOut), lateFee);
	}
	
	public static User fileToString(String input) {
		
		String[] info = input.split("\\|");
		
		if (info.length<6) {
			return null; // if info has less than 6 inputs, user is invalid
		}
		
		User user = new User(info[1], info[2], Boolean.parseBoolean(info[3])); // convert the string boolean into an actual boolean
		
		if(!info[4].isEmpty()) {
			String[] itemId = info[4].split(",");
			for (String ID : itemId) {
				user.addItem(ID);
			}
			
		}
		user.lateFee = Double.parseDouble(info[5]); //convert string LateFee into a double
		return user;
	}

	public void feeClear() {
		lateFee = 0.0;
		}

}
