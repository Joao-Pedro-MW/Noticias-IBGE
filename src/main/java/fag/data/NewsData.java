package fag.data;

import fag.models.News; // Importa a sua classe News
import java.util.ArrayList;
import java.util.List;

public class NewsData {
    private String userName;
    private List<News> favoriteNews;
    private List<News> readNews;
    private List<News> savedNews;

    public NewsData() {
        this.favoriteNews = new ArrayList<>();
        this.readNews = new ArrayList<>();
        this.savedNews = new ArrayList<>();
    }

    public NewsData(String userName, List<News> favoriteNews, List<News> readNews, List<News> savedNews) {
        this.userName = userName;
        this.favoriteNews = favoriteNews != null ? new ArrayList<>(favoriteNews) : new ArrayList<>();
        this.readNews = readNews != null ? new ArrayList<>(readNews) : new ArrayList<>();
        this.savedNews = savedNews != null ? new ArrayList<>(savedNews) : new ArrayList<>();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<News> getFavoriteNews() {
        return favoriteNews;
    }

    public void setFavoriteNews(List<News> favoriteNews) {
        this.favoriteNews = favoriteNews;
    }

    public List<News> getReadNews() {
        return readNews;
    }

    public void setReadNews(List<News> readNews) {
        this.readNews = readNews;
    }

    public List<News> getSavedNews() {
        return savedNews;
    }

    public void setSavedNews(List<News> savedNews) {
        this.savedNews = savedNews;
    }
}