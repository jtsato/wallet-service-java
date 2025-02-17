package io.github.jtsato.walletservice.core.common.paging;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Jorge Takeshi Sato
 */

@Getter
@AllArgsConstructor
@ToString
public class Pageable {

    private final Integer page;

    private final Integer size;

    private final Integer numberOfElements;

    private final Long totalOfElements;

    private final Integer totalPages;
}
