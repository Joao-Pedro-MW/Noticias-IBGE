package fag.comparators;

import fag.models.News;

import java.util.Comparator;

public class NewsTypeComparator implements Comparator<News> {

    @Override
    public int compare(News news1, News news2) {
        String name1 = (news1 != null && news1.getTipo() != null) ? news1.getTipo() : "";
        String name2 = (news2 != null && news2.getTipo() != null) ? news2.getTipo() : "";

        return name1.compareTo(name2);
    }
}
