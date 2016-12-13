package cz.cvut.kbss.jopa.environment;

import cz.cvut.kbss.jopa.model.annotations.*;

import java.lang.reflect.Field;

@OWLClass(iri = Vocabulary.C_OWLClassR)
public class OWLClassR extends OWLClassS {

    @OWLDataProperty(iri = Vocabulary.P_R_STRING_ATTRIBUTE)
    private String stringAtt;

    @OWLObjectProperty(iri = Vocabulary.P_HAS_A, cascade = {CascadeType.PERSIST})
    private OWLClassA owlClassA;

    public String getStringAtt() {
        return stringAtt;
    }

    public void setStringAtt(String stringAtt) {
        this.stringAtt = stringAtt;
    }

    public OWLClassA getOwlClassA() {
        return owlClassA;
    }

    public void setOwlClassA(OWLClassA owlClassA) {
        this.owlClassA = owlClassA;
    }

    @Override
    public String toString() {
        return "OWLClassR{" +
                "owlClassA=" + owlClassA +
                ", stringAtt='" + stringAtt + '\'' +
                "} " + super.toString();
    }

    public static String getClassIri() {
        return OWLClassR.class.getDeclaredAnnotation(OWLClass.class).iri();
    }

    public static Field getStringAttField() throws NoSuchFieldException {
        return OWLClassR.class.getDeclaredField("stringAtt");
    }

    public static Field getOwlClassAField() throws NoSuchFieldException {
        return OWLClassR.class.getDeclaredField("owlClassA");
    }
}
