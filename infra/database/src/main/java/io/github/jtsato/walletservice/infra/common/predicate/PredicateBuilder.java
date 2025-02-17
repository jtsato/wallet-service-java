package io.github.jtsato.bookstore.dataprovider.common.predicate;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.List;

/**
 * @author Jorge Takeshi Sato
 */

public interface PredicateBuilder<Q> {

    List<BooleanExpression> buildBooleanExpressions(final Q query);

    BooleanBuilder buildBooleanBuilder(final Q query);
}
