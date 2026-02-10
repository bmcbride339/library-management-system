package project;

public class Magazine extends Book{
	
	private String issue;
	
	public Magazine(String id, String status, String title, String dueDate, String author, String issue) {
	    super(id, status, title, dueDate, author);
	    this.setIssue(issue); 
	}


	public String getIssue() {
		return issue;
	}
	public void setIssue(String issue) {
		this.issue = issue;
	}
	
	public String getType() {
		return "MAGAZINE";
	}
	public String toFileString() {
        return "MAGAZINE|" + getID() + "|" + getStatus() + "|" + getTitle() + "|" + getDueDate() + "|" + getAuthor() + "|" + issue;
    }

}
