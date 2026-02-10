package project;

import java.io.*;
import java.util.*;
import java.util.*;

public class Manager {
	
	private Map<String, Item> items;
	
	private final String filePath;
	
	public Manager(String filePath) {
		this.filePath = filePath;
		items = new HashMap<>();
		loadItemsFromFile();
	}
	
	public Item getItem(String ID) {
		return items.get(ID);
	}
	
	
	public boolean addItem(Item item) {
	    if (items.containsKey(item.getID())) {
	        System.out.println("Item already exists");
	        return false;
	    }

	    item.setStatus("AVAILABLE");
	    item.setDueDate("NONE");

	    items.put(item.getID(), item);
	    saveItem();
	    return true;
	}


	
	public boolean removeItem(String ID) {
		if (items.containsKey(ID)) {
			items.remove(ID);
			saveItem();
			return true;
			
		}
		return false;
	}
	
	public boolean checkOutItem(String itemId, User user, String dueDate) {
		
		if (itemId == null || user == null || dueDate == null) {
		    return false; 
		}
		Item item = items.get(itemId.toUpperCase());
	    if (item != null) {
	        String status = item.getStatus().toUpperCase();
	        if (status.equals("AVAILABLE") || (user.isAdmin() && status.equals("RESERVED"))) {
	            item.setStatus("CHECKED_OUT");
	            item.setDueDate(dueDate);
	            user.addItem(itemId.toUpperCase()); // always store IDs in uppercase
	            saveItem();
	            return true;
	        }
	    }
	    return false;
	}

	
	public boolean returnItem(String itemId, User user) {
		Item item = items.get(itemId);
		if (item != null && user.getCheckedOut().contains(itemId)) {
			item.setStatus("AVAILABLE");
			item.setDueDate("NONE");
			user.removeItem(itemId);
			saveItem();
			return true;
		}
		return false;
	}
	
	public boolean reserveItem(String itemId, User user) {
		Item item = items.get(itemId);
		
		if (item != null && item.getStatus().equals("AVAILABLE")) {
			item.setStatus("RESERVED");
			item.setDueDate("RESERVED");
			user.addItem(itemId);
			saveItem();
			return true;
		}
		return false;
	}
	
	public void loadItemsFromFile() {
		File file = new File(getFilePath());
		
		if (!file.exists()) return;
		int counter=0;
		try (BufferedReader reader = new BufferedReader(new FileReader(file))){
			String line;
			while ((line = reader.readLine()) != null) {
				Item item = createItemFromFile(line);
				if (item!= null) {
					items.put(item.getID(), item);
					counter++;
				}
				if (counter >=100000) {
					break;
				}
			}
		}catch (IOException e) {
			System.err.println("Error loading item: " + e.getMessage());
		}
	}
	
	public static Item createItemFromFile(String line) {
	    if (line == null || line.trim().isEmpty()) {
	        return null;
	    }

	    String[] input = line.split("\\|");
	    String type = input[0];

	    try {
	        if (type.equalsIgnoreCase("BOOK") && input.length >= 6) {
	            return new Book(input[1], input[2], input[3], input[4], input[5]);
	        } else if (type.equalsIgnoreCase("AUDIOBOOK") && input.length >= 7) {
	            return new AudioBook(input[1], input[2], input[3], input[4], input[5], input[6]);
	        } else if (type.equalsIgnoreCase("MAGAZINE") && input.length >= 7) {
	            return new Magazine(input[1], input[2], input[3], input[4], input[5], input[6]);
	        } else if (type.equalsIgnoreCase("NEWSPAPER") && input.length >= 7) {
	            return new Newspaper(input[1], input[2], input[3], input[4], input[5], input[6]);
	        } else {
	            System.out.println("Unknown or invalid item format: " + line);
	            return null;
	        }
	    } catch (Exception e) {
	        System.err.println("Failed to parse line: " + line + " Error: " + e.getMessage());
	        return null;
	    }
	}

	
	
	public void saveItem() {
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(getFilePath()))){
			for (Item item : items.values()) {
				writer.write(item.toFileString());
				writer.newLine();
				
			}
		}catch (IOException e) {
			System.err.println("Error saving item: " + e.getMessage());
		}
	}

	public String getFilePath() {
		return filePath;
	}
}
