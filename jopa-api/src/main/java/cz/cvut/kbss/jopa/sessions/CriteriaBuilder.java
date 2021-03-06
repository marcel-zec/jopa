package cz.cvut.kbss.jopa.sessions;

import cz.cvut.kbss.jopa.model.query.criteria.*;

public interface CriteriaBuilder extends PredicateFactory {


    /**
     * Create a CriteriaQuery object with the specified result type.
     * @param resultClass type of the query result
     * @return criteria query object
     */
    <T> CriteriaQuery<T> createQuery(Class<T> resultClass);

    /**
     * Create an aggregate expression applying the count operation.
     * Return type of count function in SPARQL is xsd:integer which JOPA internally represents as Integer.
     * @param x expression representing input value to count operation
     * @return count expression
     */
    Expression<Integer> count(Expression<?> x);

    /**
     * Create a parameter expression.
     * @param paramClass - parameter class
     * @return parameter expression
     */
    <T> ParameterExpression<T> parameter(Class<T> paramClass);

    /**
     * Create a parameter expression with the given name.
     * @param paramClass - parameter class
     * @param name - name that can be used to refer to the parameter
     * @return parameter expression
     */
    <T> ParameterExpression<T> parameter(Class<T> paramClass, String name);

    /**
     * Create an expression for a literal.
     *
     * @param value - value represented by the expression
     * @return expression literal
     */
    <T> Expression<T> literal(T value);

    /**
     * Create an expression for a string literal with language tag.
     * @param value - string value represented by the expression
     * @param languageTag - string language tag
     * @return expression literal
     */
    Expression<String> literal(String value, String languageTag);

    /**
     * Create an ordering by the ascending value of the expression.
     * @param x expression used to define the ordering
     * @return ascending ordering corresponding to the expression
     */
    Order asc(Expression<?> x);

    /**
     * Create an ordering by the descending value of the expression.
     * @param x expression used to define the ordering
     * @return descending ordering corresponding to the expression
     */
    Order desc(Expression<?> x);
}
