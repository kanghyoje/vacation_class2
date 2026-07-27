package kr.hs.dgsw.ex2.service;

import kr.hs.dgsw.ex2.domain.Article;
import kr.hs.dgsw.ex2.dto.request.ArticleRequest;
import kr.hs.dgsw.ex2.dto.response.ArticleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BlogService {
    ArticleResponse create(ArticleRequest request);

    ArticleResponse findById(Long id);

    Page<ArticleResponse> findAll(Pageable pageable);

    ArticleResponse update(
            Long id,
            ArticleRequest request
    );

    void deleteById(Long id);
}
