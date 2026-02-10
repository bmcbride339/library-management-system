package project;

public class Newspaper extends Magazine {
	
	private String publicationDate;
	
	public Newspaper(String id, String status, String title, String dueDate, String publicationDate, String issue) {
	    super(id, status, title, dueDate, "NONE", issue); 
	    this.setPublicationDate(publicationDate);
	}


	public String getPublicationDate() {
		return publicationDate;
	}
	public void setPublicationDate(String publicationDate) {
		this.publicationDate = publicationDate;
	}
	
	public String getType() {
		return "NEWSPAPER";
	}
	
	public String toFileString() {
	      return "NEWSPAPER|" + getID() + "|" + getStatus() + "|" + getTitle() + "|" + getDueDate() + 
	    		  "|" + publicationDate + "|" + getIssue();
	    }

	}
	
