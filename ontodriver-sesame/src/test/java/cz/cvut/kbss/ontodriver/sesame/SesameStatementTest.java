package cz.cvut.kbss.ontodriver.sesame;

import cz.cvut.kbss.ontodriver.sesame.connector.StatementExecutor;
import cz.cvut.kbss.ontodriver.sesame.query.AskResultSet;
import cz.cvut.kbss.ontodriver.sesame.query.SelectResultSet;
import cz.cvut.kbss.ontodriver.sesame.query.SesameStatement;
import cz.cvut.kbss.ontodriver_new.ResultSet;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openrdf.query.TupleQueryResult;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author kidney
 */
public class SesameStatementTest {

    private static final String SELECT_ENTITY_QUERY = "SELECT ?x WHERE { ?x a <http://krizik.felk.cvut.cz/ontologies/jopa/entities#OWLClassA> . }";
    private static final String ASK_BOOLEAN_QUERY = "ASK { ?x a <http://krizik.felk.cvut.cz/ontologies/jopa/entities#OWLClassA> . }";

    @Mock
    private StatementExecutor executorMock;

    private SesameStatement statement;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.statement = new SesameStatement(executorMock);
    }

    @Test
    public void executeSelectQueryReturnsSelectResultSet() throws Exception {
        when(executorMock.executeSelectQuery(SELECT_ENTITY_QUERY)).thenReturn(mock(TupleQueryResult.class));
        final ResultSet rs = statement.executeQuery(SELECT_ENTITY_QUERY);
        assertNotNull(rs);
        assertTrue(rs instanceof SelectResultSet);
        verify(executorMock).executeSelectQuery(SELECT_ENTITY_QUERY);
    }

    @Test
    public void executeAskQueryReturnsAskResultSet() throws Exception {
        when(executorMock.executeBooleanQuery(ASK_BOOLEAN_QUERY)).thenReturn(true);
        final ResultSet rs = statement.executeQuery(ASK_BOOLEAN_QUERY);
        assertNotNull(rs);
        assertTrue(rs instanceof AskResultSet);
        verify(executorMock).executeBooleanQuery(ASK_BOOLEAN_QUERY);
    }
}