package cz.cvut.kbss.jopa.oom;

import cz.cvut.kbss.jopa.environment.OWLClassA;
import cz.cvut.kbss.jopa.environment.utils.Generators;
import cz.cvut.kbss.jopa.environment.utils.MetamodelMocks;
import cz.cvut.kbss.jopa.model.JOPAPersistenceProperties;
import cz.cvut.kbss.jopa.model.descriptors.Descriptor;
import cz.cvut.kbss.jopa.model.descriptors.EntityDescriptor;
import cz.cvut.kbss.jopa.model.metamodel.EntityType;
import cz.cvut.kbss.jopa.model.metamodel.Identifier;
import cz.cvut.kbss.jopa.utils.Configuration;
import cz.cvut.kbss.ontodriver.descriptor.AxiomValueDescriptor;
import cz.cvut.kbss.ontodriver.model.Assertion;
import cz.cvut.kbss.ontodriver.model.NamedResource;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.URI;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class IdentifierFieldStrategyTest {

    private static final URI PK = Generators.createIndividualIdentifier();
    private static final NamedResource INDIVIDUAL = NamedResource.create(PK);


    @Mock
    private EntityMappingHelper mapperMock;

    private AxiomValueGatherer gatherer;

    private Descriptor descriptor = new EntityDescriptor();

    private FieldStrategy<Identifier<? super OWLClassA, ?>, OWLClassA> strategy;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        final Configuration configuration = new Configuration(
                Collections.singletonMap(JOPAPersistenceProperties.LANG, "en"));
        when(mapperMock.getConfiguration()).thenReturn(configuration);

        this.gatherer = new AxiomValueGatherer(INDIVIDUAL, null);
        final MetamodelMocks mocks = new MetamodelMocks();
        final EntityType<OWLClassA> et = mocks.forOwlClassA().entityType();
        when(mapperMock.getEntityType(OWLClassA.class)).thenReturn(et);

        this.strategy = new IdentifierFieldStrategy<>(et, et.getIdentifier(), descriptor, mapperMock);
    }

    @Test
    public void createAssertionCreatesClassAssertion() {
        final Assertion assertion = strategy.createAssertion();
        assertTrue(assertion.isClassAssertion());
    }

    @Test
    public void buildAxiomsFromValueCreatesClassAssertionForRepresentedIndividual() throws Exception {
        final OWLClassA instance = Generators.generateOwlClassAInstance();
        strategy.buildAxiomValuesFromInstance(instance, gatherer);
        final AxiomValueDescriptor valueDescriptor = OOMTestUtils.getAxiomValueDescriptor(gatherer);
        assertEquals(1, valueDescriptor.getAssertions().size());
        assertTrue(valueDescriptor.getAssertions().contains(strategy.createAssertion()));
    }
}