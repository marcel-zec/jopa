package cz.cvut.kbss.jopa.query.criteria.expressions;

import cz.cvut.kbss.jopa.model.metamodel.Metamodel;
import cz.cvut.kbss.jopa.query.criteria.CriteriaParameterFiller;

public class ExpressionEntityImpl<Y> extends AbstractPathExpression<Y> {


    public ExpressionEntityImpl(Class<Y> type, AbstractPathExpression pathSource, Metamodel metamodel) {
        super(type, pathSource, metamodel);
    }

    @Override
    public void setExpressionToQuery(StringBuilder query, CriteriaParameterFiller parameterFiller) {
        if (this.pathSource != null){
            this.pathSource.setExpressionToQuery(query, parameterFiller);
            query.append("." + type.getSimpleName().toLowerCase());
        } else {
            query.append(type.getSimpleName().toLowerCase());
        }
    }
}

