package fag.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class NewsUrlBuilder {

        private final String baseUrl = "https://servicodados.ibge.gov.br/api/v3/noticias";

        public enum SearchType {
                TEXTO("busca"),
                PALAVRA_CHAVE("busca");

                private final String parameterName;

                SearchType(String parameterName) {
                        this.parameterName = parameterName;
                }

                public String getParameterName() {
                        return parameterName;
                }
        }

        public String buildUrl(String searchTerm, SearchType type) {
                String encodedSearchTerm = URLEncoder.encode(searchTerm, StandardCharsets.UTF_8);
                return baseUrl + "?" + type.getParameterName() + "=" + encodedSearchTerm;
        }

        public String buildUrlByDate(String date) {
                try {
                        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        LocalDate parsedDate = LocalDate.parse(date, inputFormatter);
                        DateTimeFormatter apiFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
                        String formattedDate = parsedDate.format(apiFormatter);

                        String encodedDate = URLEncoder.encode(formattedDate, StandardCharsets.UTF_8);
                        return baseUrl + "?de=" + encodedDate + "&ate=" + encodedDate;
                } catch (DateTimeParseException e) {
                        System.err.println("Erro de formato de data: A data deve estar no formato DD/MM/AAAA. " + e.getMessage());
                        return null;
                }
        }

        public String getJson(String urlString) throws IOException {
                if (urlString == null || urlString.isEmpty()) {
                        throw new IllegalArgumentException("A URL não pode ser nula ou vazia.");
                }

                URL url = new URL(urlString);
                HttpURLConnection connection = null;
                try {
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setRequestProperty("Accept", "application/json");

                        int responseCode = connection.getResponseCode();
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                                StringBuilder response = new StringBuilder();
                                String inputLine;
                                while ((inputLine = in.readLine()) != null) {
                                        response.append(inputLine);
                                }
                                in.close();
                                return response.toString();
                        } else {
                                System.err.println("Erro na requisição HTTP. Código de resposta: " + responseCode);
                                try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
                                        StringBuilder errorResponse = new StringBuilder();
                                        String errorLine;
                                        while ((errorLine = errorReader.readLine()) != null) {
                                                errorResponse.append(errorLine);
                                        }
                                        System.err.println("Corpo do erro: " + errorResponse.toString());
                                } catch (Exception ex) {
                                }
                                throw new IOException("Falha na requisição HTTP com código: " + responseCode);
                        }
                } finally {
                        if (connection != null) {
                                connection.disconnect();
                        }
                }
        }

        public String buildUrlByText(String text) {
                return buildUrl(text, SearchType.TEXTO);
        }

        public String buildUrlByKeyword(String keyword) {
                return buildUrl(keyword, SearchType.PALAVRA_CHAVE);
        }
}