package fag.comparators;

import fag.models.News;

import java.time.LocalDate;
import java.util.Comparator;

public class NewsPublishedDateComparator implements Comparator<News> {

    @Override
    public int compare(News news1, News news2) {
        LocalDate date1 = LocalDate.from(news1.getDataPublicacao());
        LocalDate date2 = LocalDate.from(news2.getDataPublicacao());

        if (date1 == null && date2 == null) {
            return 0;
        }
        if (date1 == null) {
            return -1;
        }
        if (date2 == null) {
            return 1;
        }
        return date1.compareTo(date2);
    }
}
