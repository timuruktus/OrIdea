package ru.timuruktus.oridea.Model.JSONFragments;



public class Post {

    private String text;
    private String newsImageUrl;
    private String authorImageUrl;
    private String category;
    private String title;
    private String author; //Author- authId

    public Post() {
    }

    public Post(String text, String newsImageUrl, String authorImageUrl, String category,
                String title, String author) {
        this.text = text;
        this.newsImageUrl = newsImageUrl;
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
        return newsImageUrl;
    }

    public void setPostImageUrl(String newsImageUrl) {
        this.newsImageUrl = newsImageUrl;
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
