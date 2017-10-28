package cz.cvut.kbss.jopa.query.mapper;

import cz.cvut.kbss.jopa.environment.OWLClassA;
import cz.cvut.kbss.jopa.model.metamodel.EntityType;
import cz.cvut.kbss.jopa.sessions.UnitOfWorkImpl;
import cz.cvut.kbss.ontodriver.ResultSet;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class EntityResultMapperTest {

    @Mock
    private ResultSet resultSetMock;

    @Mock
    private UnitOfWorkImpl uowMock;

    @Mock
    private EntityType<OWLClassA> etMock;

    private EntityResultMapper<OWLClassA> mapper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mapper = new EntityResultMapper<>(etMock);
        when(etMock.getJavaType()).thenReturn(OWLClassA.class);
    }

    @Test
    public void mapCreatesNewInstanceOfTargetTypeAndRegistersItInUOW() {
        final OWLClassA clone = new OWLClassA();
        when(uowMock.registerExistingObject(any(), any())).thenReturn(clone);
        final OWLClassA result = mapper.map(resultSetMock, uowMock);
        assertNotNull(result);
        verify(uowMock).registerExistingObject(any(), any());
    }

    @Test
    public void mapUsesFieldMappersToPopulateEntityFields() {
        final FieldResultMapper fOne = mock(FieldResultMapper.class);
        final FieldResultMapper fTwo = mock(FieldResultMapper.class);
        mapper.addFieldMapper(fOne);
        mapper.addFieldMapper(fTwo);

        mapper.map(resultSetMock, uowMock);
        verify(fOne).map(eq(resultSetMock), any(), eq(uowMock));
        verify(fTwo).map(eq(resultSetMock), any(), eq(uowMock));
    }
}