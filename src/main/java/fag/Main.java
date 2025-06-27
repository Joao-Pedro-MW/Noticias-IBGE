package fag;

import fag.api.NewsMapper;
import fag.api.NewsUrlBuilder;
import fag.managers.FavoritesManager;
import fag.managers.WatchedManager;
import fag.managers.WatchlistManager;
import fag.ui.UserInterface;

public class Main {

    public static void main(String[] args) {
        NewsMapper newsMapper = new NewsMapper();
        NewsUrlBuilder newsUrlBuilder = new NewsUrlBuilder();
        FavoritesManager favoritesManager = new FavoritesManager();
        WatchedManager readManager = new WatchedManager();
        WatchlistManager toReadLaterManager = new WatchlistManager();

        UserInterface ui = new UserInterface(newsMapper, newsUrlBuilder,
                favoritesManager, readManager, toReadLaterManager);

        ui.start();
    }
}