package fag.managers;

import fag.comparators.NewsPublishedDateComparator;
import fag.comparators.NewsTitleComparator;
import fag.comparators.NewsTypeComparator;
import fag.models.News;

import java.util.ArrayList;
import java.util.List;

public abstract class NewsListManager {
    private List<News> newsList;

    public NewsListManager() {
        this.newsList = new ArrayList<>();
    }

    public NewsListManager(List<News> initialNewsList) {
        this.newsList = (initialNewsList != null) ? new ArrayList<>(initialNewsList) : new ArrayList<>();
    }

    public void addNews(News news){
        newsList.add(news);
    }

    public void removeNews(int id_news_str) {
        try {
            newsList.removeIf(news -> news.getId() == id_news_str);
            System.out.println("Tentativa de remover notícia com ID " + id_news_str + " (se encontrada).");
        } catch (NumberFormatException e) {
            System.err.println("Erro: O ID '" + id_news_str + "' não é um número válido.");
        }
    }

    public void showNewsList(){
        if (newsList.isEmpty()) {
            System.out.println("A lista está vazia.");
            return;
        }
        for(News news: newsList){
            System.out.println(news.toString());
        }
    }

    public void orderByTitle(){
        NewsTitleComparator nameComparator = new NewsTitleComparator();
        this.newsList.sort(nameComparator);
    }

    public void orderByType(){
        NewsTypeComparator ratingComparator = new NewsTypeComparator();
        this.newsList.sort(ratingComparator);
    }

    public void orderByPublishedDate(){
        NewsPublishedDateComparator statusComparator = new NewsPublishedDateComparator();
        this.newsList.sort(statusComparator);
    }

    public List<News> getNewsList() {
        return new ArrayList<>(this.newsList);
    }

    public void setNewsList(List<News> newsList) {
        this.newsList = newsList;
    }
}