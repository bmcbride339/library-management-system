package project;

public class Book implements Item {
	private String ID;
	private String status;
	private String dueDate;
	private String author;
	private String title;
	
	public Book(String ID, String status, String title, String dueDate, String author) {
	    this.setID(ID);
	    this.setStatus(status);
	    this.setTitle(title);
	    this.setDueDate(dueDate);
	    this.setAuthor(author);
	}
	

	public String getID() {
		return ID;
	}
	public void setID(String ID) {
		this.ID = ID;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	// return the type of the object
	
	public String getType() {
		
		return "BOOK";
	}
	
	
	//return a string that can printed to a file
	
	 public String toFileString() {
	        return "BOOK|" + getID() + "|" + getStatus() + "|" + getTitle() + "|" + getDueDate() + "|" + author;
	    }
}
