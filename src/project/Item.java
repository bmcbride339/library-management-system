package project;

public interface Item {
	String getID();
	String getTitle();
	String getAuthor();
	String getStatus();
	String getDueDate();
	String getType();
	String toFileString();
	void setStatus(String status);
	void setDueDate(String dueDate);
	
}
