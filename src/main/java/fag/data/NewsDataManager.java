package fag.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fag.data.NewsData;

import java.io.File;
import java.io.IOException;

public class NewsDataManager {

    private final String filePath;
    private final ObjectMapper objectMapper;

    public NewsDataManager(String fileName) {
        this.filePath = fileName;
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public void saveNewsData(NewsData newsData) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), newsData);
            System.out.println("Dados de notícias salvos em: " + filePath);
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados de notícias: " + e.getMessage());
        }
    }

    public NewsData loadNewsData() {
        File file = new File(filePath);
        if (file.exists()) {
            try {
                return objectMapper.readValue(file, NewsData.class);
            } catch (IOException e) {
                System.err.println("Erro ao carregar dados de notícias: " + e.getMessage());
                return new NewsData();
            }
        } else {
            System.out.println("Arquivo de dados de notícias não encontrado. Criando um novo.");
            return new NewsData();
        }
    }
}
