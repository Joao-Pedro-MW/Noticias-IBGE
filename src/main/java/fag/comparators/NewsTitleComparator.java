package fag.comparators;

import fag.models.News;

import java.util.Comparator;

public class NewsTitleComparator implements Comparator<News> {

    @Override
    public int compare(News news1, News news2) {
        String name1 = (news1 != null && news1.getTitulo() != null) ? news1.getTitulo() : "";
        String name2 = (news2 != null && news2.getTitulo() != null) ? news2.getTitulo() : "";

        return name1.compareTo(name2);
    }
}
