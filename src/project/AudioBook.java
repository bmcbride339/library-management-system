package project;

public class AudioBook extends Book { 
	
	private String duration;
	
	public AudioBook(String ID, String status, String title, String dueDate, String author, String duration) {
	    super(ID, status, title, dueDate, author);
	    this.setDuration(duration);
	}

	// setters and getters for AudioBook
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	
	public String getType() {
		return "AUDIOBOOK";
	}
	
	public String toFileString() {
        return "AUDIOBOOK|" + getID() + "|" + getStatus() + "|" + getTitle() + "|" + getDueDate() + "|" + getAuthor() + "|" + duration;
    }

}
