package com.hla.books;

class BookInfo {
    private final String author;
    private final String title;
    private final int year;
    private final int categoryId;

    public BookInfo(String author, String title, int year, int categoryId) {
        this.author = author;
        this.title = title;
        this.year = year;
        this.categoryId = categoryId;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public int getCategoryId() {
        return categoryId;
    }
}
