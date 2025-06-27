package fag.ui;

import fag.api.NewsMapper;
import fag.api.NewsUrlBuilder;
import fag.managers.FavoritesManager;
import fag.managers.NewsListManager;
import fag.managers.WatchedManager;
import fag.managers.WatchlistManager;
import fag.models.News;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class UserInterface {

    private final NewsMapper newsMapper;
    private final NewsUrlBuilder newsUrlBuilder;
    private final FavoritesManager favorites;
    private final WatchedManager watched;
    private final WatchlistManager watchlist;
    private final Scanner scanner;
    private String userName;

    public String titulo =
            " \n" +
                    "  ___  ___   ___  ___   _  _       _    _      _           \n" +
                    " |_ _|| _ ) / __|| __| | \\| | ___ | |_ (_) __ (_) __ _  ___\n" +
                    "  | | | _ \\| (_ || _|  | .` |/ _ \\|  _|| |/ _|| |/ _` |(_-<\n" +
                    " |___||___/ \\___||___| |_|\\_|\\___/ \\__||_|\\__||_|\\__,_|/__/\n" +
                    "                                                           \n";

    public UserInterface(NewsMapper newsMapper, NewsUrlBuilder newsUrlBuilder,
                         FavoritesManager favorites, WatchedManager watched, WatchlistManager watchlist) {
        this.newsMapper = newsMapper;
        this.newsUrlBuilder = newsUrlBuilder;
        this.favorites = favorites;
        this.watched = watched;
        this.watchlist = watchlist;
        this.scanner = new Scanner(System.in);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private void displayMenu() {
        System.out.println(titulo);
        System.out.println("\n--- Gerenciador de Notícias ---");
        System.out.println("[1] - Buscar Notícias");
        System.out.println("[2] - Notícias Favoritas");
        System.out.println("[3] - Notícias Lidas");
        System.out.println("[4] - Notícias para Ler Depois");
        System.out.println("[0] - Sair");
        System.out.print("Escolha uma opção: ");
    }

    private int getUserChoice() {
        while (!scanner.hasNextInt()) {
            System.out.println("Entrada inválida. Por favor, digite um número.");
            scanner.next();
            System.out.print("Escolha uma opção: ");
        }
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }

    private List<News> newsSearch() {
        System.out.println("\n--- Buscar Notícias ---");
        System.out.println("[1] - Por Título/Busca Geral");
        System.out.println("[2] - Por Palavra-Chave (Busca)");
        System.out.println("[3] - Por Data");
        System.out.print("Escolha o tipo de busca: ");
        int searchTypeChoice = getUserChoice();

        String searchTerm;
        String apiUrl;

        try {
            switch (searchTypeChoice) {
                case 1 -> {
                    System.out.print("Digite o termo de busca (título ou geral): ");
                    searchTerm = scanner.nextLine();
                    searchTerm = searchTerm.replaceAll("[^\\p{L}\\p{N}\\s]", "");
                    apiUrl = newsUrlBuilder.buildUrl(searchTerm, NewsUrlBuilder.SearchType.TEXTO);
                }
                case 2 -> {
                    System.out.print("Digite a palavra-chave (busca): ");
                    searchTerm = scanner.nextLine();
                    searchTerm = searchTerm.replaceAll("[^\\p{L}\\p{N}\\s]", "");
                    apiUrl = newsUrlBuilder.buildUrl(searchTerm, NewsUrlBuilder.SearchType.PALAVRA_CHAVE);
                }
                case 3 -> {
                    System.out.print("Digite a data (formato DD/MM/AAAA): ");
                    searchTerm = scanner.nextLine();
                    apiUrl = newsUrlBuilder.buildUrlByDate(searchTerm);
                    if (apiUrl == null) {
                        System.out.println("Formato de data inválido. Não foi possível realizar a busca.");
                        return Collections.emptyList();
                    }
                }
                default -> {
                    System.out.println("Opção de busca inválida.");
                    return Collections.emptyList();
                }
            }

            if (apiUrl == null || apiUrl.isEmpty()) {
                System.out.println("Não foi possível construir a URL da API para a busca.");
                return Collections.emptyList();
            }

            String jsonResponse = newsUrlBuilder.getJson(apiUrl);

            List<News> foundNews = newsMapper.mapJsonToNewsList(jsonResponse);
            if (foundNews.isEmpty()) {
                System.out.println("Nenhuma notícia encontrada com os critérios.");
                return Collections.emptyList();
            } else {
                System.out.println("\nNotícias encontradas:");
                for (int i = 0; i < foundNews.size(); i++) {
                    News n = foundNews.get(i);
                    System.out.println((i + 1) + ". " + n.getTitulo() + " (ID: " + n.getId() + ") - Data: " + n.getDataPublicacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                }
                return foundNews;
            }
        } catch (IOException e) {
            System.err.println("Erro de comunicação com a API do IBGE: " + e.getMessage());
            return Collections.emptyList();
        } catch (Exception e) {
            System.err.println("Erro inesperado ao buscar notícias: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    private void addNewsFromSearchResults(List<News> searchResults, NewsListManager targetManager) {
        if (searchResults.isEmpty()) {
            System.out.println("Não há notícias nos resultados da busca para adicionar.");
            return;
        }

        System.out.println("\n--- Adicionar Notícia por ID ---");
        System.out.print("Digite o ID da notícia que deseja adicionar: ");
        int newsId;
        try {
            newsId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ID inválido. Por favor, digite um número.");
            return;
        }

        Optional<News> newsToAdd = searchResults.stream()
                .filter(n -> n.getId() == newsId)
                .findFirst();

        if (newsToAdd.isPresent()) {
            targetManager.addNews(newsToAdd.get());
            System.out.println("Notícia '" + newsToAdd.get().getTitulo() + "' adicionada à lista com sucesso!");
        } else {
            System.out.println("ID de notícia não encontrado nos resultados da busca. Verifique o ID e tente novamente.");
        }
    }

    private void manageNewsList(List<News> newsContext, NewsListManager manager) {
        System.out.println("\n--- Gerenciar Notícias ---");
        System.out.println("[1] - Adicionar notícia (dos resultados da busca)");
        System.out.println("[2] - Remover notícia da lista (por ID)");
        System.out.println("[3] - Exibir lista atual");
        System.out.println("[4] - Ordenar lista atual");
        System.out.println("[0] - Voltar");
        System.out.print("Escolha uma opção: ");

        int choice = getUserChoice();
        switch (choice) {
            case 1 -> addNewsFromSearchResults(newsContext, manager);
            case 2 -> {
                System.out.print("Digite o ID da notícia para remover: ");
                int newsIdRemove;
                try {
                    newsIdRemove = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("ID inválido. Por favor, digite um número.");
                    return;
                }
                manager.removeNews(newsIdRemove);
                System.out.println("Notícia removida (se encontrada).");
            }
            case 3 -> manager.showNewsList();
            case 4 -> orderListMenu(manager);
            case 0 -> {}
            default -> System.out.println("Opção inválida.");
        }
    }

    private void orderListMenu(NewsListManager manager) {
        System.out.println("\n--- Ordenar Lista por ---");
        System.out.println("[1] - Título (A-Z)");
        System.out.println("[2] - Data de Publicação");
        System.out.println("[3] - Tipo/Categoria");
        System.out.println("[0] - Voltar");
        System.out.print("Escolha uma opção: ");

        int choice = getUserChoice();
        switch (choice) {
            case 1 -> { manager.orderByTitle(); System.out.println("Lista ordenada por título."); }
            case 2 -> { manager.orderByPublishedDate(); System.out.println("Lista ordenada por data de publicação."); }
            case 3 -> { manager.orderByType(); System.out.println("Lista ordenada por tipo/categoria."); }
            case 0 -> {}
            default -> System.out.println("Opção inválida. Por favor, tente novamente.");
        }
        manager.showNewsList();
    }

    public void start() {
        if (this.userName == null || this.userName.isEmpty()) {
            System.out.print("Bem-vindo! Por favor, digite seu nome: ");
            this.userName = scanner.nextLine();
        }
        System.out.println("Olá, " + (this.userName != null ? this.userName : "Usuário") + "!");

        int choice;
        do {
            displayMenu();
            choice = getUserChoice();

            switch (choice) {
                case 1 -> {
                    List<News> searchResults = newsSearch();
                    if (!searchResults.isEmpty()) {
                        System.out.println("\nNotícias encontradas. O que deseja fazer?");
                        System.out.println("[1] Gerenciar essas notícias em uma lista");
                        System.out.println("[0] Voltar ao menu principal");
                        System.out.print("Escolha: ");
                        int manageLists = getUserChoice();
                        switch (manageLists) {
                            case 1 -> {
                                System.out.println("Qual lista você deseja gerenciar?");
                                System.out.println("[1] Favoritas");
                                System.out.println("[2] Lidas");
                                System.out.println("[3] Para Ler Depois");
                                System.out.print("Escolha: ");
                                int listChoice = getUserChoice();
                                switch (listChoice) {
                                    case 1 -> manageNewsList(searchResults, favorites);
                                    case 2 -> manageNewsList(searchResults, watched);
                                    case 3 -> manageNewsList(searchResults, watchlist);
                                    default -> System.out.println("Opção de lista inválida.");
                                }
                            }
                            case 0 -> System.out.println("Voltando...");
                            default -> System.out.println("Opção inválida. Por favor, tente novamente.");
                        }
                    }
                }
                case 2 -> {
                    favorites.showNewsList();
                    manageNewsList(favorites.getNewsList(), favorites);
                }
                case 3 -> {
                    watched.showNewsList();
                    manageNewsList(watched.getNewsList(), watched);
                }
                case 4 -> {
                    watchlist.showNewsList();
                    manageNewsList(watchlist.getNewsList(), watchlist);
                }
                case 0 -> System.out.println("Saindo do programa. Até logo!");
                default -> System.out.println("Opção inválida. Por favor, tente novamente.");
            }
            System.out.println("\n----------------------------------\n");
        } while (choice != 0);
        scanner.close();
    }
}