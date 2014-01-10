package cz.cvut.kbss.jopa.sessions;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.cvut.kbss.jopa.sessions.ChangeRecord;
import cz.cvut.kbss.jopa.sessions.ObjectChangeSet;
import cz.cvut.kbss.jopa.sessions.UnitOfWorkChangeSet;

public class ObjectChangeSetImpl implements Serializable, ObjectChangeSet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1316908575232588565L;

	// Class this ObjectChangeSet represents
	private transient Class<?> objectClass;

	// The object the changes are bound to
	private Object changedObject;

	// Reference to the clone
	private transient Object cloneObject;

	// Set of changes
	private List<ChangeRecord> changes;

	// A map of attributeName-changeRecord pairs to easily find the attributes
	// to change
	private transient Map<String, ChangeRecord> attributesToChange;

	// The UOWchangeSet this ObjectChangeSet belongs to
	private transient UnitOfWorkChangeSet uowChangeSet;

	// Does this change set represent a new object
	private boolean isNew;

	// URI of the ontology context the object belongs to
	private URI entityContext;

	protected ObjectChangeSetImpl() {
	}

	/**
	 * 
	 * @param changedObject
	 * @param objectClass
	 * @param isNew
	 *            boolean The isNew parameter marks whether the change set
	 *            represents a new object
	 * @param uowChangeSet
	 */
	public ObjectChangeSetImpl(Object changedObject, Object cloneObject, boolean isNew,
			UnitOfWorkChangeSet uowChangeSet) {
		super();
		this.changedObject = changedObject;
		this.cloneObject = cloneObject;
		this.objectClass = cloneObject.getClass();
		this.isNew = isNew;
		this.uowChangeSet = uowChangeSet;
	}

	public ObjectChangeSetImpl(Object changedObject, Object cloneObject, boolean isNew,
			UnitOfWorkChangeSet uowChangeSet, URI contextUri) {
		this(changedObject, cloneObject, isNew, uowChangeSet);
		if (contextUri == null) {
			throw new NullPointerException();
		}
		this.entityContext = contextUri;
	}

	/**
	 * Add a change record to this change set
	 * 
	 * @param record
	 *            ChangeRecord
	 */
	public void addChangeRecord(ChangeRecord record) {
		if (record == null)
			return;
		String attributeName = record.getAttributeName();
		ChangeRecord existing = getAttributesToChange().get(attributeName);
		if (existing != null) {
			getChanges().remove(existing);
		}
		getChanges().add(record);
		getAttributesToChange().put(attributeName, record);
	}

	/**
	 * Returns the change records collections.
	 * 
	 * @return java.util.List<ChangeRecord>
	 */
	public List<ChangeRecord> getChanges() {
		if (this.changes == null) {
			this.changes = new ArrayList<ChangeRecord>();
		}
		return this.changes;
	}

	/**
	 * Returns the map with attribute names and changes made to them.
	 * 
	 * @return java.util.Map<String, ChangeRecord>
	 */
	public Map<String, ChangeRecord> getAttributesToChange() {
		if (this.attributesToChange == null) {
			this.attributesToChange = new HashMap<String, ChangeRecord>();
		}
		return this.attributesToChange;
	}

	public Class<?> getObjectClass() {
		return this.objectClass;
	}

	public Object getChangedObject() {
		return this.changedObject;
	}

	public Object getCloneObject() {
		return cloneObject;
	}

	public void setCloneObject(Object cloneObject) {
		this.cloneObject = cloneObject;
	}

	/**
	 * Set the change record list.
	 * 
	 * @param changes
	 *            List<ChangeRecord>
	 */
	public void setChanges(List<ChangeRecord> changes) {
		this.changes = changes;
	}

	/**
	 * Get the owner of this changeSet
	 * 
	 * @return UnitOfWorkChangeSet
	 */
	public UnitOfWorkChangeSet getUowChangeSet() {
		return this.uowChangeSet;
	}

	/**
	 * Set the owner of this change set.
	 * 
	 * @param uowChangeSet
	 *            UnitOfWorkChangeSet
	 */
	public void setUowChangeSet(UnitOfWorkChangeSet uowChangeSet) {
		this.uowChangeSet = uowChangeSet;
	}

	/**
	 * Returns true if this change set represents a new object
	 * 
	 * @return boolean
	 */
	public boolean isNew() {
		return this.isNew;
	}

	/**
	 * Retrieves context the referenced entity belongs to
	 */
	@Override
	public URI getEntityContext() {
		return entityContext;
	}
}
