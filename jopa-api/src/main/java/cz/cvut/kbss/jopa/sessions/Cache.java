package cz.cvut.kbss.jopa.sessions;

import java.net.URI;

/**
 * Interface used to interact with the second-level cache. If a cache is not in
 * use, the methods of this interface have no effect, except for contains, which
 * returns false.
 * 
 * Taken from JPA 2.
 * 
 * @author kidney
 * 
 */
public interface Cache {

	/**
	 * Checks whether the cache contains data for the given entity. </p>
	 * 
	 * This method searches all the available contexts and returns true on
	 * finding the first occurence of matching entity.
	 * 
	 * @param cls
	 *            entity class
	 * @param primaryKey
	 *            primary key
	 * @return {@code boolean} indicating whether the entity is in the cache
	 * @see #contains(URI, Class, Object)
	 */
	public boolean contains(Class<?> cls, Object primaryKey);

	/**
	 * Checks whether the cache contains data for the given entity (in the given
	 * context only).
	 * 
	 * @param contextUri
	 *            URI of context
	 * @param cls
	 *            Entity class
	 * @param primaryKey
	 *            Primary key
	 * @return {@code boolean} indicating whether the entity is in the cache
	 */
	public boolean contains(URI contextUri, Class<?> cls, Object primaryKey);

	/**
	 * Removes the data for the given entity from the cache. </p>
	 * 
	 * This method removes all occurrences of the matching entity from all the
	 * available contexts.
	 * 
	 * @param cls
	 *            entity class
	 * @param primaryKey
	 *            primary key
	 */
	public void evict(Class<?> cls, Object primaryKey);

	/**
	 * Removes the data for the given entity from the cache.
	 * 
	 * @param contextUri
	 *            URI of context
	 * @param cls
	 *            Entity class
	 * @param primaryKey
	 *            Primary key
	 */
	public void evict(URI contextUri, Class<?> cls, Object primaryKey);

	/**
	 * Removes the data for entities of the specified class (and its subclasses)
	 * from the cache. </p>
	 * 
	 * This method removes the entities from all available contexts.
	 * 
	 * @param cls
	 *            entity class
	 */
	public void evict(Class<?> cls);

	/**
	 * Removes the data for entities of the specified context from the cache.
	 * 
	 * @param contextUri
	 *            URI of {@code Context}
	 */
	public void evict(URI contextUri);

	/**
	 * Clears the cache.
	 */
	public void evictAll();

}
