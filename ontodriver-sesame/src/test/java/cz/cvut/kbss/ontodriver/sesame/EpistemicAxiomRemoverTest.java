/**
 * Copyright (C) 2016 Czech Technical University in Prague
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
package cz.cvut.kbss.ontodriver.sesame;

import cz.cvut.kbss.ontodriver.descriptor.AxiomDescriptor;
import cz.cvut.kbss.ontodriver.model.Assertion;
import cz.cvut.kbss.ontodriver.model.NamedResource;
import cz.cvut.kbss.ontodriver.sesame.connector.Connector;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openrdf.model.Statement;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;

import java.net.URI;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class EpistemicAxiomRemoverTest {

    private static final NamedResource SUBJECT = NamedResource
            .create("http://krizik.felk.cvut.cz/ontologies/jopa/entityX");

    private static final String PROPERTY = "http://krizik.felk.cvut.cz/ontologies/jopa/propertyOne";

    private AxiomDescriptor descriptor;

    @Mock
    private Connector connectorMock;

    private ValueFactory vf;

    private EpistemicAxiomRemover axiomRemover;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.vf = new ValueFactoryImpl();
        this.descriptor = new AxiomDescriptor(SUBJECT);

        this.axiomRemover = new EpistemicAxiomRemover(connectorMock, vf, "en");
    }

    @Test
    public void removeSkipsInferredAssertions() throws Exception {
        final Assertion ass = Assertion.createObjectPropertyAssertion(URI.create(PROPERTY), true);
        descriptor.addAssertion(ass);

        axiomRemover.remove(descriptor);
        verify(connectorMock, never())
                .findStatements(eq(vf.createURI(SUBJECT.toString())), eq(vf.createURI(PROPERTY)), any(), anyBoolean());
        verify(connectorMock, never())
                .findStatements(eq(vf.createURI(SUBJECT.toString())), eq(vf.createURI(PROPERTY)), any(), anyBoolean(),
                        anyVararg());
    }

    @Test
    public void removeWithAssertionContextSearchesInContext() throws Exception {
        final String context = "http://krizik.felk.cvut.cz/ontologies/jopa/contexts#One";
        final Assertion ass = Assertion.createObjectPropertyAssertion(URI.create(PROPERTY), false);
        descriptor.addAssertion(ass);
        descriptor.setAssertionContext(ass, URI.create(context));

        axiomRemover.remove(descriptor);

        verify(connectorMock).findStatements(vf.createURI(SUBJECT.toString()), vf.createURI(PROPERTY), null, false,
                vf.createURI(context));
        verify(connectorMock).removeStatements(anyCollectionOf(Statement.class));
    }

    @Test
    public void removeCallsFindStatementsWithoutContextsWhenItIsNotSpecifiedForAssertion() throws Exception {
        final Assertion ass = Assertion.createObjectPropertyAssertion(URI.create(PROPERTY), false);
        descriptor.addAssertion(ass);

        axiomRemover.remove(descriptor);

        verify(connectorMock).findStatements(vf.createURI(SUBJECT.toString()), vf.createURI(PROPERTY), null, false);
        verify(connectorMock).removeStatements(anyCollectionOf(Statement.class));
    }
}