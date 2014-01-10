package cz.cvut.kbss.ontodriver.impl.owlapi;

import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

public final class OwlapiConnectorDataHolder {

	private final OWLOntology workingOntology;
	private final OWLOntologyManager ontologyManager;
	private final OWLDataFactory dataFactory;
	private final OWLReasoner reasoner;
	private final String lang;

	OwlapiConnectorDataHolder(DataHolderBuilder builder) {
		this.workingOntology = builder.workingOntology;
		this.ontologyManager = builder.ontologyManager;
		this.dataFactory = builder.dataFactory;
		this.reasoner = builder.reasoner;
		this.lang = builder.language;
	}

	public OWLOntology getWorkingOntology() {
		return workingOntology;
	}

	public OWLOntologyManager getOntologyManager() {
		return ontologyManager;
	}

	public OWLDataFactory getDataFactory() {
		return dataFactory;
	}

	public OWLReasoner getReasoner() {
		return reasoner;
	}

	public String getLanguage() {
		return lang;
	}

	public static DataHolderBuilder workingOntology(OWLOntology ontology) {
		return new DataHolderBuilder().workingOntology(ontology);
	}

	public static DataHolderBuilder ontologyManager(OWLOntologyManager manager) {
		return new DataHolderBuilder().ontologyManager(manager);
	}

	public static DataHolderBuilder dataFactory(OWLDataFactory factory) {
		return new DataHolderBuilder().dataFactory(factory);
	}

	public static DataHolderBuilder reasoner(OWLReasoner reasoner) {
		return new DataHolderBuilder().reasoner(reasoner);
	}

	public static DataHolderBuilder language(String language) {
		return new DataHolderBuilder().language(language);
	}

	public static class DataHolderBuilder {
		private OWLOntology workingOntology;
		private OWLOntologyManager ontologyManager;
		private OWLDataFactory dataFactory;
		private OWLReasoner reasoner;
		private String language;

		DataHolderBuilder() {
		}

		public DataHolderBuilder workingOntology(OWLOntology ontology) {
			this.workingOntology = ontology;
			return this;
		}

		public DataHolderBuilder ontologyManager(OWLOntologyManager manager) {
			this.ontologyManager = manager;
			return this;
		}

		public DataHolderBuilder dataFactory(OWLDataFactory dataFactory) {
			this.dataFactory = dataFactory;
			return this;
		}

		public DataHolderBuilder reasoner(OWLReasoner reasoner) {
			this.reasoner = reasoner;
			return this;
		}

		public DataHolderBuilder language(String language) {
			this.language = language;
			return this;
		}

		public OwlapiConnectorDataHolder build() {
			return new OwlapiConnectorDataHolder(this);
		}
	}
}
