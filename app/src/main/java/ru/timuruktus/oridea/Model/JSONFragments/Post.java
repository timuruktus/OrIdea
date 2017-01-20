package ru.timuruktus.oridea.Model.JSONFragments;


public class Post {

    private String text;
    private String postImageUrl;
    private String authorImageUrl;
    private String category;
    private String title;
    private String author; //Author- authId

    // TODO: CHANGE ALL STRINGS TO ONE MAP CAUSE OF DB STRUCTURE!
    public Post() {
    }

    public Post(String text, String postImageUrl, String authorImageUrl, String category,
                String title, String author) {
        this.text = text;
        this.postImageUrl = postImageUrl;
        this.authorImageUrl = authorImageUrl;
        this.category = category;
        this.title = title;
        this.author = author;
    }

    public Post(String text, String authorImageUrl, String category,
                String title, String author) {
        this.text = text;
        this.authorImageUrl = authorImageUrl;
        this.category = category;
        this.title = title;
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public void setPostImageUrl(String newsImageUrl) {
        this.postImageUrl = newsImageUrl;
    }

    public String getAuthorImageUrl() {
        return authorImageUrl;
    }

    public void setAuthorImageUrl(String authorImageUrl) {
        this.authorImageUrl = authorImageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String tag) {
        this.category = category;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


}
