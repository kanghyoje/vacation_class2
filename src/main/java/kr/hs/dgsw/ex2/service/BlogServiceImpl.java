package kr.hs.dgsw.ex2.service;

import kr.hs.dgsw.ex2.domain.Article;
import kr.hs.dgsw.ex2.dto.request.ArticleRequest;
import kr.hs.dgsw.ex2.dto.response.ArticleResponse;
import kr.hs.dgsw.ex2.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService{

    private final BlogRepository blogRepository;

    @Override
    public ArticleResponse create(ArticleRequest request) {

        Article article = Article.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .build();

        Article savedArticle = blogRepository.save(article);
        return ArticleResponse.from(savedArticle);
    }

    @Override
    public ArticleResponse findById(Long id) {
        Article article = blogRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
        return ArticleResponse.from(article);
    }

    @Override
    public Page<ArticleResponse> findAll(Pageable pageable) {
        Page<Article> page = blogRepository.findAll(pageable);
        // Article(Entity) -> ArticleResponse(DTO)
        // R apply(T t) == ArticleResponse.from(T t);

        //page.map(article ->  ArticleResponse.from(article));
        return page.map(ArticleResponse::from);

    }

    @Override
    public ArticleResponse update(
            Long id,
            ArticleRequest request
    ) {
        Article article = blogRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
        article.update(request.getTitle(), request.getContent());
        return ArticleResponse.from(article);
    } // 영속성 컨텍스트 유지 <- 트랜잭션 단위, open session in view

    @Override
    public void deleteById(Long id) {
        Article article = blogRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
        blogRepository.delete(article);
    }
}
