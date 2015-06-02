package cz.cvut.kbss.jopa.test.runners;

import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.descriptors.Descriptor;
import cz.cvut.kbss.jopa.model.descriptors.EntityDescriptor;
import cz.cvut.kbss.jopa.model.descriptors.ObjectPropertyCollectionDescriptor;
import cz.cvut.kbss.jopa.test.OWLClassA;
import cz.cvut.kbss.jopa.test.OWLClassB;
import cz.cvut.kbss.jopa.test.OWLClassC;
import cz.cvut.kbss.jopa.test.OWLClassI;
import cz.cvut.kbss.jopa.test.environment.Generators;
import cz.cvut.kbss.jopa.test.environment.TestEnvironmentUtils;

import java.lang.reflect.Field;
import java.util.logging.Logger;

import static org.junit.Assert.*;

public class RetrieveOperationsMultiContextRunner extends BaseRunner {

    public RetrieveOperationsMultiContextRunner(Logger logger) {
        super(logger);
    }

    public void retrieveSimilarFromTwoContexts(EntityManager em) throws Exception {
        logger.config(
                "Test: persist entities with the same URI but different attributes into two contexts and then retrieve them.");
        final OWLClassA entityATwo = new OWLClassA();
        entityATwo.setUri(entityA.getUri());
        entityATwo.setStringAttribute("SomeCompletelyDifferentStringAttribute");
        final Descriptor aDescriptor = new EntityDescriptor(CONTEXT_ONE);
        final Descriptor aTwoDescriptor = new EntityDescriptor(CONTEXT_TWO);
        em.getTransaction().begin();
        em.persist(entityA, aDescriptor);
        em.persist(entityATwo, aTwoDescriptor);
        em.getTransaction().commit();

        final OWLClassA resOne = em.find(OWLClassA.class, entityA.getUri(), aDescriptor);
        assertNotNull(resOne);
        assertEquals(entityA.getStringAttribute(), resOne.getStringAttribute());
        final OWLClassA resTwo = em.find(OWLClassA.class, entityATwo.getUri(), aTwoDescriptor);
        assertNotNull(resTwo);
        assertEquals(entityATwo.getStringAttribute(), resTwo.getStringAttribute());
    }

    public void retrieveSimpleListFromContext(EntityManager em) throws Exception {
        logger.config("Test: retrieve simple list and its values from a different context.");
        entityC.setSimpleList(Generators.createSimpleList(10));
        final Descriptor cDescriptor = new EntityDescriptor();
        final Descriptor listDescriptor = new ObjectPropertyCollectionDescriptor(CONTEXT_ONE,
                OWLClassC.class.getDeclaredField("simpleList"));
        cDescriptor.addAttributeDescriptor(OWLClassC.class.getDeclaredField("simpleList"), listDescriptor);
        em.getTransaction().begin();
        em.persist(entityC, cDescriptor);
        for (OWLClassA a : entityC.getSimpleList()) {
            em.persist(a, listDescriptor);
        }
        em.getTransaction().commit();

        final OWLClassC resC = em.find(OWLClassC.class, entityC.getUri(), cDescriptor);
        assertNotNull(resC);
        assertNotNull(resC.getSimpleList());
        assertEquals(entityC.getSimpleList().size(), resC.getSimpleList().size());
        for (OWLClassA a : entityC.getSimpleList()) {
            final OWLClassA resA = em.find(OWLClassA.class, a.getUri(), listDescriptor);
            assertNotNull(resA);
            assertEquals(a.getUri(), resA.getUri());
            assertEquals(a.getStringAttribute(), resA.getStringAttribute());
        }
    }

    public void retrieveReferencedlistFromContext(EntityManager em) throws Exception {
        logger.config("Test: retrieve referenced list and its values from a different context.");
        entityC.setReferencedList(Generators.createReferencedList(15));
        final Descriptor cDescriptor = new EntityDescriptor();
        final Descriptor listDescriptor = new ObjectPropertyCollectionDescriptor(CONTEXT_ONE,
                OWLClassC.class.getDeclaredField("referencedList"));
        cDescriptor.addAttributeDescriptor(OWLClassC.class.getDeclaredField("referencedList"), listDescriptor);
        em.getTransaction().begin();
        em.persist(entityC, cDescriptor);
        for (OWLClassA a : entityC.getReferencedList()) {
            em.persist(a, listDescriptor);
        }
        em.getTransaction().commit();

        final OWLClassC resC = em.find(OWLClassC.class, entityC.getUri(), cDescriptor);
        assertNotNull(resC);
        assertNotNull(resC.getReferencedList());
        assertEquals(entityC.getReferencedList().size(), resC.getReferencedList().size());
        for (OWLClassA a : entityC.getReferencedList()) {
            final OWLClassA resA = em.find(OWLClassA.class, a.getUri(), listDescriptor);
            assertNotNull(resA);
            assertEquals(a.getUri(), resA.getUri());
            assertEquals(a.getStringAttribute(), resA.getStringAttribute());
        }
    }

    public void retrieveLazyReferenceFromContext(EntityManager em) throws Exception {
        logger.config("Test: retrieve entity with lazy loaded reference in another context.");
        final Descriptor iDescriptor = new EntityDescriptor(CONTEXT_ONE);
        final Descriptor aDescriptor = new EntityDescriptor(CONTEXT_TWO);
        aDescriptor.addAttributeContext(OWLClassA.class.getDeclaredField("stringAttribute"), CONTEXT_ONE);
        iDescriptor.addAttributeDescriptor(OWLClassI.class.getDeclaredField("owlClassA"), aDescriptor);
        em.getTransaction().begin();
        // The relationship is CascadeType.PERSIST
        em.persist(entityI, iDescriptor);
        em.getTransaction().commit();

        final OWLClassI resI = em.find(OWLClassI.class, entityI.getUri(), iDescriptor);
        assertNotNull(resI);
        final Field refAField = OWLClassI.class.getDeclaredField("owlClassA");
        refAField.setAccessible(true);
        assertNull(refAField.get(resI));
        assertNotNull(resI.getOwlClassA());
        final OWLClassA resA = em.find(OWLClassA.class, entityA.getUri(), aDescriptor);
        assertNotNull(resA);
        // If we were using cache, ref.getOwlClassA() and resA would be the same
        assertEquals(resI.getOwlClassA().getStringAttribute(), resA.getStringAttribute());
    }

    public void retrievePropertiesFromContext(EntityManager em) throws Exception {
        logger.config("Test: retrieve entity properties from a context.");
        entityB.setProperties(Generators.createProperties(50));
        final Descriptor bDescriptor = new EntityDescriptor(CONTEXT_ONE);
        bDescriptor.addAttributeContext(OWLClassB.class.getDeclaredField("properties"), CONTEXT_TWO);
        bDescriptor.addAttributeContext(OWLClassB.class.getDeclaredField("stringAttribute"), null);
        em.getTransaction().begin();
        em.persist(entityB, bDescriptor);
        em.getTransaction().commit();

        final OWLClassB res = em.find(OWLClassB.class, entityB.getUri(), bDescriptor);
        assertNotNull(res);
        assertEquals(entityB.getStringAttribute(), res.getStringAttribute());
        assertTrue(TestEnvironmentUtils.arePropertiesEqual(entityB.getProperties(),
                res.getProperties()));
    }
}
