package io.github.jtsato.walletservice.infra.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import java.util.Arrays;
import java.util.List;

/**
 * @author Jorge Takeshi Sato
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageRequestHelper {

    private static final String FIELD_DELIMITER = ",";
    private static final String DELIMITER = ":";

    public static PageRequest buildPageRequest(final Integer page, final Integer size, final String orderBy) {

        final int realPage = page == null ? 0 : page;
        final int realSize = size == null || size <= 0 ? 10 : size;

        return PageRequest.of(realPage, realSize, getSort(orderBy));
    }

    private static Sort getSort(final String rawSortBy) {

        if (StringUtils.isBlank(rawSortBy)) {
            return Sort.unsorted();
        }

        final String orderBy = StringUtils.deleteWhitespace(rawSortBy);

        final List<String> parameters = Arrays.stream(StringUtils.split(orderBy, FIELD_DELIMITER))
                                              .map(element -> StringUtils.substringBefore(element, DELIMITER))
                                              .toList();

        return Sort.by(parameters.stream().map(propertyName -> getOrder(propertyName, orderBy)).toList());
    }

    private static Order getOrder(final String propertyName, final String orderBy) {
        return new Order(getDirection(propertyName, orderBy), propertyName);
    }

    private static Direction getDirection(final String propertyName, final String orderBy) {
        final String direction = StringUtils.substringBefore(StringUtils.substringAfter(orderBy, propertyName + DELIMITER), FIELD_DELIMITER);
        return direction.equalsIgnoreCase(Direction.DESC.toString()) ? Direction.DESC : Direction.ASC;
    }
}
