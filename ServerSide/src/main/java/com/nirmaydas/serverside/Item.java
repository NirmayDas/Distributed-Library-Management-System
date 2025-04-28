package com.nirmaydas.serverside;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Item implements Serializable {
    private String type;
    private String title;
    private String author;
    private int id;
    private boolean isAvailable;
    private String borrowedBy;
    private String imageUrl;
    private List<String> tags;

    public Item(String type, String title, String author, int id, boolean isAvailable, String borrowedBy, String imageUrl) {
        this.type = type;
        this.title = title;
        this.author = author;
        this.id = id;
        this.isAvailable = isAvailable;
        this.borrowedBy = borrowedBy;
        this.imageUrl = imageUrl;
        this.tags = new ArrayList<>();
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getId() {
        return id;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public String getBorrowedBy() {
        return borrowedBy;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public void setBorrowedBy(String borrowedBy) {
        this.borrowedBy = borrowedBy;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return type + ": " + title + " by " + author + " (ID: " + id + ", Available: " + isAvailable + ")";
    }

    public String toFileFormat() {
        String borrowedValue;
        if (borrowedBy != null) {
            borrowedValue = this.borrowedBy;
        } else {
            borrowedValue = "null";
        }
        return type
             + "," + title
             + "," + author
             + "," + id
             + "," + isAvailable
             + "," + borrowedValue
             + ", " +  imageUrl;
    }
    
}