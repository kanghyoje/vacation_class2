package kr.hs.dgsw.ex2.service;

import kr.hs.dgsw.ex2.domain.Article;
import kr.hs.dgsw.ex2.dto.request.ArticleRequest;
import kr.hs.dgsw.ex2.dto.response.ArticleResponse;
import kr.hs.dgsw.ex2.repository.BlogRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional // 실제 데이터 건들지 않고 롤백, id값은 증가
class BlogServiceTest {

    @Autowired
    private BlogService blogService;

    @Autowired
    private BlogRepository blogRepository;

    @Test
    @DisplayName("게시글 생성 요청 시 성공적으로 저장되고 ArticleResponse를 반환한다")
    void create_success() {
        // given (준비)
        ArticleRequest request
                = new ArticleRequest("새로운 제목", "새로운 내용");

        // when (실행)
        ArticleResponse articleResponse
                = blogService.create(request);

        // then (검증)
        assertThat(articleResponse).isNotNull();
//        assertThat(articleResponse.getId()).isNotNull();
        assertThat(articleResponse.getId()).isGreaterThan(0);
        assertThat(articleResponse.getTitle()).isEqualTo("새로운 제목");
        assertThat(articleResponse.getContent()).isEqualTo("새로운 내용");
    }

    @Test
    @DisplayName("findById success")
    void findById_success() {
        // given (준비)
        Article article = Article.builder()
                .title("새로운 제목")
                .content("새로운 내용")
                .build();
        Article saved = blogRepository.save(article);
        // when (실행)
        ArticleResponse articleResponse
                = blogService.findById(saved.getId());

        // then (검증)
        assertThat(articleResponse).isNotNull();

        assertThat(articleResponse.getId())
                .isEqualTo(saved.getId());

        assertThat(articleResponse.getTitle())
                .isEqualTo(saved.getTitle());
    }

    @Test
    @DisplayName("findAll_success")
    void findAll_success() {
        // given (준비)
        Pageable pageable = PageRequest.of(0, 10);

        // when (실행)
        Page<ArticleResponse> responses = blogService.findAll(pageable);

        // then (검증)
        assertThat(responses).isNotNull();
        assertThat(responses.getContent()).hasSize(10);

        List<ArticleResponse> content = responses.getContent();
    }

    @Test
    @DisplayName("update_success")
    void update_success() {
        // given (준비)
        Article saved = blogRepository.save(
                Article.builder()
                        .title("제목")
                        .content("내용")
                        .build()
        );
        long id = saved.getId();
        ArticleRequest articleRequest
                = new ArticleRequest("수정 제목", "수정 내용");

        // when (실행)
        ArticleResponse articleResponse = blogService.update(id, articleRequest);

        // then (검증)
        assertThat(articleResponse).isNotNull();
        assertThat(articleResponse.getTitle()).isEqualTo("수정 제목");
        assertThat(articleResponse.getContent()).isEqualTo("수정 내용");
    }

    @Test
    @DisplayName("deleteById_success")
    void deleteById_success() {
        // given
        Article saved = blogRepository.save(
                Article.builder()
                        .title("제목")
                        .content("내용")
                        .build()
        );
        long id = saved.getId();
        // when
        blogService.deleteById(id);
        // then
        Optional<Article> result = blogRepository.findById(id);
        assertThat(result).isEmpty();
    }
}