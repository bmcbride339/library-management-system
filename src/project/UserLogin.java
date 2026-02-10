package project;

import java.util.*;
import java.io.*;

public class UserLogin {
	
	private Map<String, User> users;
	private final String filePath;
	
	
	public UserLogin(String filePath) {
		this.filePath=filePath;
		
		users = new HashMap<>();
		
		loadFile();
	}
	
	public User createUser(String ID, String name, boolean isAdmin) {
		if(users.containsKey(ID)) {
			System.out.println("User already exists");
			return null;
		}
		User newUser = new User(ID, name, isAdmin);
		users.put(ID, newUser);
		saveUser();
		
		return newUser;
	}
	
	public User getUser(String ID) {
		return users.get(ID);
	}
	
	public boolean userExists(String ID) {
		return users.containsKey(ID);
	}
	
	public Collection<User> getAllUsers(){
		return users.values();
	}
	
	public void loadFile() {
		File file = new File(filePath);
		
		if(!file.exists()) {
			return;
		}
		
		try (BufferedReader reader = new BufferedReader(new FileReader(file))){
			String line;
			while((line = reader.readLine()) != null) {
				User user = User.fileToString(line);
				if (user != null) {
					users.put(user.getID(), user);
				}
			}
		} catch (IOException e) {
			System.err.println("Error loading users: " + e.getMessage());
		}			
	}
	
	public void saveUser() {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))){
			for (User user : users.values()) {
				writer.write(user.toFile());
				writer.newLine();
			}
		}catch (IOException e) {
			System.err.println("Error saving users: " + e.getMessage());
		}
	}
}
