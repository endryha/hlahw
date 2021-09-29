package com.hla.books;

import com.github.javafaker.Book;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RequestMapping("/books")
@RestController
public class BooksController {
    private static final Logger LOG = LoggerFactory.getLogger(BooksController.class);

    private static final int CURRENT_YEAR = LocalDate.now().getYear();
    private static final int YEARS_LOOK_BACK = 75;
    private static final int[] CATEGORIES = new int[]{1, 2, 3};

    private final JdbcTemplate jdbcTemplate;
    private final Faker faker;

    public BooksController(JdbcTemplate jdbcTemplate, Faker faker) {
        this.jdbcTemplate = jdbcTemplate;
        this.faker = faker;
    }

    @PostMapping("/bulk_insert")
    public ResponseEntity<String> bulkInsert(@RequestParam("n") int n,
                                             @RequestParam("c") int c) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(c);

        LOG.info("Start bulk insert concurrency={}, total={} ", c, c * n);

        CountDownLatch countDownLatch = new CountDownLatch(c);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = 0; i < c; i++) {
            executor.submit(() -> {
                for (int j = 0; j < n; j++) {
                    insertRandomBook();
                }
                countDownLatch.countDown();
            });
        }

        countDownLatch.await();

        stopWatch.stop();

        LOG.info("Time elapsed {} sec", stopWatch.getTotalTimeSeconds());

        String result = new DecimalFormat("#.##").format(stopWatch.getTotalTimeSeconds());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/random")
    public ResponseEntity<?> insertRandom() {
        insertRandomBook();

        return ResponseEntity.ok().build();
    }

    private void insertRandomBook() {
        BookInfo bookInfo = randomBookInfo();
        jdbcTemplate.update("insert into books (id, category_id, author, title, year)" +
                        " values (nextval('books_seq'), ?, ?, ?, ?)",
                bookInfo.getCategoryId(), bookInfo.getAuthor(), bookInfo.getTitle(), bookInfo.getYear());
    }

    private BookInfo randomBookInfo() {
        Book book = faker.book();

        String author = book.author();
        String title = book.title();
        int year = getYear();
        int categoryId = getCategoryId(book);

        return new BookInfo(author, title, year, categoryId);
    }

    private static int getCategoryId(Book book) {
        return CATEGORIES[(Math.abs(book.genre().hashCode() % 3) + 1) - 1];
    }

    private static int getYear() {
        int yearHigh = CURRENT_YEAR;
        int yearLow = yearHigh - YEARS_LOOK_BACK;
        return new Random().nextInt(yearHigh - yearLow) + yearLow;
    }
}
