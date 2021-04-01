/**
 * Copyright (C) 2020 Czech Technical University in Prague
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cvut.kbss.jopa.model;

import cz.cvut.kbss.jopa.model.metamodel.EntityType;
import cz.cvut.kbss.jopa.model.metamodel.Metamodel;
import cz.cvut.kbss.jopa.model.query.criteria.*;
import cz.cvut.kbss.jopa.query.criteria.*;
import cz.cvut.kbss.jopa.query.criteria.expressions.AbstractAggregateFunctionExpression;
import cz.cvut.kbss.jopa.query.criteria.expressions.AbstractExpression;
import cz.cvut.kbss.jopa.query.criteria.expressions.AbstractPathExpression;
import cz.cvut.kbss.jopa.query.criteria.expressions.ExpressionEntityImpl;
import cz.cvut.kbss.jopa.sessions.CriteriaFactory;
import cz.cvut.kbss.jopa.utils.ErrorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Objects;

//TODO PRO - CriteriaQueryImpl methods implementation
public class CriteriaQueryImpl<T> implements CriteriaQuery<T> {

    private static final Logger LOG = LoggerFactory.getLogger(CriteriaQuery.class);

    protected final CriteriaQueryHolder<T> query;
    private final Metamodel metamodel;
    private final CriteriaFactory factory;


    public CriteriaQueryImpl(CriteriaQueryHolder<T> query, Metamodel metamodel, CriteriaFactory factory) {
        this.query = Objects.requireNonNull(query, ErrorUtils.getNPXMessageSupplier("query"));
        this.metamodel = metamodel;
        this.factory = factory;
    }

    @Override
    public <X> Root<X> from(Class<X> entityClass) {
        RootImpl<X> root = new RootImpl<>(metamodel, new ExpressionEntityImpl<>(entityClass, null, metamodel), entityClass);
        query.setRoot(root);
        return root;
    }

    @Override
    public <X> Root<X> from(EntityType<X> entity) {
        return null;
    }

    @Override
    public CriteriaQuery<T> select(Selection<? extends T> selection){
        query.setSelection((SelectionImpl<? extends T>) selection);
        return this;
    }


    //TODO - BAKALARKA - KONZULTACIA
    // automaticky kazdy Expression ktory nie je boolean sa pretvori na
    // equals(expression, null)?
    // ake to ma realne vyuzitie?
    @Override
    public CriteriaQuery<T> where(Expression<Boolean> expression) {
//        AbstractExpression<Boolean> abstractExpression = (AbstractExpression<Boolean>) expression;
        query.setWhere((AbstractExpression<Boolean>) expression);
        return this;
    }

    @Override
    public CriteriaQuery<T> where(Predicate... predicates) {
        query.setWhere((AbstractExpression<Boolean>) factory.and(predicates));
        return this;
    }

    @Override
    public Class<T> getResultType() {
        return null;
    }

    @Override
    public CriteriaQuery<T> distinct(boolean b) {
        query.setDistinct(b);
        return this;
    }

    @Override
    public boolean isDistinct() {
        return query.isDistinct();
    }

    @Override
    public Selection<T> getSelection() {
        return (Selection<T>) query.getSelection();
    }

    @Override
    public Predicate getRestriction() {
        return null;
    }

    public String translateQuery(CriteriaParameterFiller parameterFiller){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT ");
        if (query.isDistinct()) stringBuilder.append("DISTINCT ");
        translateSelection(stringBuilder,parameterFiller,query.getSelection());
        stringBuilder.append(" FROM ");
        query.getRoot().setExpressionToQuery(stringBuilder, parameterFiller);
        if (query.getWhere() != null){
            stringBuilder.append(" WHERE ");
            query.getWhere().setExpressionToQuery(stringBuilder, parameterFiller);
        }
        return stringBuilder.toString();
    }

    private void translateSelection(StringBuilder stringBuilder, CriteriaParameterFiller parameterFiller, SelectionImpl<? extends T> selection) {
        AbstractExpression expression;
        if (selection instanceof AbstractAggregateFunctionExpression){
            expression = (AbstractAggregateFunctionExpression) selection;
        } else {
            PathImpl pathSelection = (PathImpl) selection;
            expression = (AbstractPathExpression) pathSelection.getParentPath();
        }
        expression.setExpressionToQuery(stringBuilder, parameterFiller);
    }
}