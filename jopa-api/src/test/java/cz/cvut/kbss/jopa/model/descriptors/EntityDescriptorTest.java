package cz.cvut.kbss.jopa.model.descriptors;

import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.jopa.model.metamodel.Attribute;
import org.junit.Test;

import java.lang.reflect.Field;
import java.net.URI;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EntityDescriptorTest {

    private static final URI CONTEXT_ONE = URI.create("http://krizik.felk.cvut.cz/ontologies/jopa/contextOne");
    private static final URI CONTEXT_TWO = URI.create("http://krizik.felk.cvut.cz/ontologies/jopa/contextTwo");
    private static final String LANG = "en";

    @Test
    public void fieldDescriptorByDefaultInheritsEntityContext() throws Exception {
        final EntityDescriptor descriptor = new EntityDescriptor(CONTEXT_ONE);
        final Attribute<TestClass, String> att = mock(Attribute.class);
        when(att.getJavaField()).thenReturn(TestClass.stringAttField());
        final Descriptor result = descriptor.getAttributeDescriptor(att);
        assertEquals(CONTEXT_ONE, result.getContext());
    }

    @Test
    public void fieldDescriptorHasItsOwnContextWhenItIsSetForIt() throws Exception {
        final EntityDescriptor descriptor = new EntityDescriptor(CONTEXT_ONE);
        final Attribute<TestClass, String> att = mock(Attribute.class);
        when(att.getJavaField()).thenReturn(TestClass.stringAttField());
        descriptor.addAttributeContext(att.getJavaField(), CONTEXT_TWO);

        final Descriptor result = descriptor.getAttributeDescriptor(att);
        assertEquals(CONTEXT_TWO, result.getContext());
    }

    @Test
    public void fieldDescriptorByDefaultInheritsEntityLanguageTag() throws Exception {
        final EntityDescriptor descriptor = new EntityDescriptor();
        final Attribute<TestClass, String> att = mock(Attribute.class);
        when(att.getJavaField()).thenReturn(TestClass.stringAttField());
        descriptor.setLanguage(LANG);
        assertTrue(descriptor.hasLanguage());

        final Descriptor result = descriptor.getAttributeDescriptor(att);
        assertTrue(result.hasLanguage());
        assertEquals(LANG, result.getLanguage());
    }

    @Test
    public void fieldDescriptorInheritsChangeOfLanguageTagFromEntityDescriptor() throws Exception {
        final EntityDescriptor descriptor = new EntityDescriptor();
        final Attribute<TestClass, String> att = mock(Attribute.class);
        when(att.getJavaField()).thenReturn(TestClass.stringAttField());
        descriptor.setLanguage(LANG);
        final String newLang = "cs";
        descriptor.setLanguage(newLang);
        final Descriptor result = descriptor.getAttributeDescriptor(att);
        assertTrue(result.hasLanguage());
        assertEquals(newLang, result.getLanguage());
    }

    @Test
    public void fieldDescriptorHasLanguageSetToItThroughEntityDescriptor() throws Exception {
        final EntityDescriptor descriptor = new EntityDescriptor();
        final Attribute<TestClass, String> att = mock(Attribute.class);
        when(att.getJavaField()).thenReturn(TestClass.stringAttField());
        descriptor.setLanguage(LANG);
        final String newLang = "cs";
        descriptor.setAttributeLanguage(att.getJavaField(), newLang);

        final Descriptor result = descriptor.getAttributeDescriptor(att);
        assertTrue(result.hasLanguage());
        assertEquals(newLang, result.getLanguage());
    }

    @Test
    public void twoEntityDescriptorsAreEqualWhenTheirFieldDescriptorsHaveTheSameContexts() throws Exception {
        final EntityDescriptor dOne = new EntityDescriptor(CONTEXT_ONE);
        final EntityDescriptor dTwo = new EntityDescriptor(CONTEXT_ONE);
        dOne.addAttributeContext(TestClass.stringAttField(), CONTEXT_TWO);
        dTwo.addAttributeContext(TestClass.stringAttField(), CONTEXT_TWO);
        dOne.addAttributeDescriptor(TestClass.intAttField(), new FieldDescriptor(CONTEXT_ONE, TestClass.intAttField()));
        dTwo.addAttributeDescriptor(TestClass.intAttField(), new FieldDescriptor(CONTEXT_ONE, TestClass.intAttField()));

        assertTrue(dOne.equals(dTwo));
        assertTrue(dTwo.equals(dOne));
        assertEquals(dOne.hashCode(), dTwo.hashCode());
    }

    @Test
    public void twoEntityDescriptorsAreNotEqualWhenTheyDifferInFieldContext() throws Exception {
        final EntityDescriptor dOne = new EntityDescriptor(CONTEXT_ONE);
        final EntityDescriptor dTwo = new EntityDescriptor(CONTEXT_ONE);
        dOne.addAttributeContext(TestClass.stringAttField(), CONTEXT_TWO);
        dTwo.addAttributeContext(TestClass.stringAttField(), CONTEXT_ONE);

        assertFalse(dOne.equals(dTwo));
        assertNotEquals(dOne.hashCode(), dTwo.hashCode());
    }

    @SuppressWarnings("unused")
    private static class TestClass {

        @OWLDataProperty(iri = "http://krizik.felk.cvut.cz/ontologies/jopa/attributes/stringAtt")
        private String stringAtt;

        @OWLDataProperty(iri = "http://krizik.felk.cvut.cz/ontologies/jopa/attributes/intAtt")
        private Integer intAtt;

        private static Field stringAttField() throws NoSuchFieldException {
            return TestClass.class.getDeclaredField("stringAtt");
        }

        private static Field intAttField() throws NoSuchFieldException {
            return TestClass.class.getDeclaredField("intAtt");
        }
    }

    @Test
    public void hasLanguageReturnsTrueForLanguageSetExplicitlyToNull() {
        final Descriptor descriptor = new EntityDescriptor();
        assertFalse(descriptor.hasLanguage());
        descriptor.setLanguage(null);
        assertTrue(descriptor.hasLanguage());
        assertNull(descriptor.getLanguage());
    }

    @Test
    public void gettingFieldDescriptorFromEntityDescriptorLeavesItsHasLanguageStatusEmpty() throws Exception {
        final Descriptor descriptor = new EntityDescriptor();
        final Attribute<TestClass, String> att = mock(Attribute.class);
        when(att.getJavaField()).thenReturn(TestClass.stringAttField());
        final Descriptor fieldDescriptor = descriptor.getAttributeDescriptor(att);
        assertFalse(fieldDescriptor.hasLanguage());
    }
}