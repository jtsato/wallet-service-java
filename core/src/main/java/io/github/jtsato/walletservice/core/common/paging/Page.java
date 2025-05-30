package io.github.jtsato.walletservice.core.common.paging;


import java.util.List;

/**
 * @author Jorge Takeshi Sato
 */

public interface Page<T> {

    List<T> getContent();

    Pageable getPageable();
}
