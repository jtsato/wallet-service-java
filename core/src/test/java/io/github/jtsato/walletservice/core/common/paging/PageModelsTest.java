package io.github.jtsato.walletservice.core.common.paging;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Paging Models Test")
class PageModelsTest {

    @DisplayName("Pageable should expose the expected values")
    @Test
    void pageableShouldExposeTheExpectedValues() {
        final Pageable pageable = new Pageable(1, 2, 3, 4L, 5);

        assertThat(pageable.getPage()).isEqualTo(1);
        assertThat(pageable.getSize()).isEqualTo(2);
        assertThat(pageable.getNumberOfElements()).isEqualTo(3);
        assertThat(pageable.getTotalOfElements()).isEqualTo(4L);
        assertThat(pageable.getTotalPages()).isEqualTo(5);
    }

    @DisplayName("PageImpl should expose content and pageable")
    @Test
    void pageImplShouldExposeContentAndPageable() {
        final List<String> content = List.of("a", "b");
        final Pageable pageable = new Pageable(0, 10, 2, 2L, 1);

        final PageImpl<String> page = new PageImpl<>(content, pageable);

        assertThat(page.getContent()).containsExactlyElementsOf(content);
        assertThat(page.getPageable()).isSameAs(pageable);
    }
}

