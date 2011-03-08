package cz.cvut.kbss.owlpersistence.owlapi;

import java.net.URI;

import cz.cvut.kbss.owlpersistence.model.annotations.FetchType;
import cz.cvut.kbss.owlpersistence.model.annotations.Id;
import cz.cvut.kbss.owlpersistence.model.annotations.OWLClass;
import cz.cvut.kbss.owlpersistence.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.owlpersistence.model.annotations.ParticipationConstraint;
import cz.cvut.kbss.owlpersistence.model.annotations.ParticipationConstraints;

@OWLClass(iri = "http://new.owl#OWLClassD")
public class OWLClassD {

	@Id
	private URI uri;

	@OWLObjectProperty(iri = "http://hasA", fetch = FetchType.EAGER)
//	@ParticipationConstraints({
//		@ParticipationConstraint(owlObjectIRI="http://new.owl#OWLClassA", min=1, max=1)
//	})
	private OWLClassA owlClassA;

	/**
	 * @param uri
	 *            the uri to set
	 */
	public void setUri(URI uri) {
		this.uri = uri;
	}

	/**
	 * @return the uri
	 */
	public URI getUri() {
		return uri;
	}

	public void setOwlClassA(OWLClassA owlClassA) {
		this.owlClassA = owlClassA;
	}

	public OWLClassA getOwlClassA() {
		return owlClassA;
	}
}