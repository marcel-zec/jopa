package cz.cvut.kbss.jopa.sessions;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URI;

abstract class AbstractInstanceBuilder {

	protected final CloneBuilderImpl builder;
	protected final UnitOfWork uow;

	AbstractInstanceBuilder() {
		this.builder = null;
		this.uow = null;
	}

	public AbstractInstanceBuilder(CloneBuilderImpl builder, UnitOfWork uow) {
		super();
		this.builder = builder;
		this.uow = uow;
	}

	/**
	 * Builds new instance from the original. </p>
	 * 
	 * For some implementations this may mean creating an empty object, others
	 * might choose to initialize it using the original data.
	 * 
	 * @param origCls
	 *            Type of the original object
	 * @param original
	 *            The original object
	 * @param contextUri
	 *            Context URI
	 * @return
	 */
	abstract Object buildClone(Class<?> origCls, Object original, URI contextUri);

	/**
	 * Return the declared constructor for the specified class. If the
	 * constructor is not accessible, it is set accessible. If there is no
	 * constructor corresponding to the specified argument list, null is
	 * returned.
	 * 
	 * @param javaClass
	 *            The class of the constructor.
	 * @param args
	 *            An Array of classes, which should take the constructor as
	 *            parameters.
	 * @return Constructor<?>
	 * @throws SecurityException
	 *             If the security check denies access to the constructor.
	 */
	protected static Constructor<?> getDeclaredConstructorFor(
			final Class<?> javaClass, Class<?>[] args) throws SecurityException {
		Constructor<?> c = null;
		try {
			c = javaClass.getDeclaredConstructor(args);
			if (c == null) {
				return null;
			}
			if (!c.isAccessible()) {
				c.setAccessible(true);
			}
		} catch (NoSuchMethodException e) {
			return null;
		}
		return c;
	}

	/**
	 * This helper method returns the first declared constructor of the
	 * specified class. It may be used only in cases when the caller knows
	 * exactly which constructor is the first one declared by the class. A use
	 * case may be a class with only one declared constructor, which is not a
	 * zero argument one.
	 * 
	 * @param javaClass
	 *            The class whose constructors should be searched.
	 * @return The first declared constructor of the specified class.
	 */
	protected static Constructor<?> getFirstDeclaredConstructorFor(
			Class<?> javaClass) {
		Constructor<?>[] ctors = javaClass.getDeclaredConstructors();
		return ctors[0];
	}

	/**
	 * Checks if the specified field was declared static in its class.
	 * 
	 * @param f
	 *            The field to examine.
	 * @return True when the Field is static.
	 */
	protected static boolean isFieldStatic(final Field f) {
		return Modifier.isStatic(f.getModifiers());
	}
}
