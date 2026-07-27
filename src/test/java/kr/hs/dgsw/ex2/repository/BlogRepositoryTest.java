package kr.hs.dgsw.ex2.repository;

import kr.hs.dgsw.ex2.domain.Article;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
class BlogRepositoryTest {

    @Autowired // DI
    private BlogRepository blogRepository;

    @Test
    void test2() {
        Pageable pageable
                = PageRequest.of(
                        0,
                        10,
                        Sort.by(Sort.Direction.DESC, "id")
                    );
        Page<Article> page = blogRepository.findAll(pageable);
        System.out.println(page.getTotalElements());
        System.out.println(page.getTotalPages());
        System.out.println(page.getNumber());
        System.out.println(page.getSize());
        page.getContent().forEach(System.out::println); // 매서드 참조
    }

    @Test
    @Transactional
    @Commit
    void test() {
        Optional<Article> result = blogRepository.findById(100L);
        // SQL : select * from articles where id = 100;
        if(result.isPresent()){
            Article article = result.get();
            System.out.println(article);
            System.out.println("=======================");
            article.update("Update Title3", "Update Content3");
            System.out.println("======================== UPDATE");
            System.out.println(article.getId());
//            blogRepository.save(article);
        }
    }

    @Test
    void insert() {
        List<Article> articles = IntStream
                .rangeClosed(1, 100)
                .mapToObj(i -> {
                    return Article.builder()
                        .title("Title" + i)
                        .content("Content" + i)
                        .build();
                })
                .toList();
        blogRepository.saveAll(articles);
    }
}