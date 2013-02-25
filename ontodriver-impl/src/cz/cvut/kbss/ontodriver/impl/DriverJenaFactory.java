package cz.cvut.kbss.ontodriver.impl;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import cz.cvut.kbss.ontodriver.Context;
import cz.cvut.kbss.ontodriver.DriverAbstractFactory;
import cz.cvut.kbss.ontodriver.OntoDriverException;
import cz.cvut.kbss.ontodriver.OntologyConnectorType;
import cz.cvut.kbss.ontodriver.OntologyStorageProperties;
import cz.cvut.kbss.ontodriver.StorageModule;

public class DriverJenaFactory extends DriverAbstractFactory {

	static {
		try {
			OntoDriverImpl
					.registerFactoryClass(OntologyConnectorType.JENA, DriverJenaFactory.class);
		} catch (OntoDriverException e) {
			LOG.severe("Unable to register " + DriverOwlapiFactory.class
					+ " at the driver. Message: " + e.getMessage());
		}
	}

	public DriverJenaFactory(List<OntologyStorageProperties> storageProperties,
			Map<String, String> properties) throws OntoDriverException {
		super(storageProperties, properties);
	}

	@Override
	public StorageModule createStorageModule(Context ctx, boolean autoCommit)
			throws OntoDriverException {
		ensureState(ctx);
		if (LOG.isLoggable(Level.FINER)) {
			LOG.finer("Creating Jena storage module.");
		}
		final JenaStorageModule m = new JenaStorageModule(ctx, this);
		registerModule(m);
		return m;
	}

	@Override
	public JenaStorageConnector createStorageConnector(Context ctx, boolean autoCommit)
			throws OntoDriverException {
		ensureState(ctx);
		if (LOG.isLoggable(Level.FINER)) {
			LOG.finer("Creating Jena storage connector.");
		}
		final JenaStorageConnector c = new JenaStorageConnector(contextsToProperties.get(ctx));
		registerConnector(c);
		return c;
	}
}
