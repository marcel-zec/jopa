package cz.cvut.kbss.jopa.adapters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cz.cvut.kbss.jopa.sessions.UnitOfWorkImpl;
import cz.cvut.kbss.jopa.test.OWLClassA;
import cz.cvut.kbss.jopa.test.OWLClassC;

public class IndirectListTest {

	private static List<OWLClassA> list;
	private static List<OWLClassA> backupList;
	private static OWLClassC owner;
	private static Field ownerField;

	@Mock
	private UnitOfWorkImpl uow;

	private IndirectList<OWLClassA> target;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		owner = new OWLClassC();
		owner.setUri(URI.create("http://C"));
		ownerField = OWLClassC.class.getDeclaredField("referencedList");
		backupList = new ArrayList<OWLClassA>();
		list = new ArrayList<OWLClassA>();
		for (byte i = 0; i < 10; i++) {
			OWLClassA a = new OWLClassA();
			a.setUri(URI.create("http://a" + i));
			a.setStringAttribute("testString");
			backupList.add(a);
		}
		list.addAll(backupList);
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		when(uow.isInTransaction()).thenReturn(Boolean.TRUE);
		target = new IndirectList<OWLClassA>(owner, ownerField, uow, list);
		list.clear();
		list.addAll(backupList);
		owner.setReferencedList(target);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructorNull() {
		@SuppressWarnings("unused")
		final IndirectList<OWLClassA> l = new IndirectList<OWLClassA>(owner, ownerField, uow, null);
		fail("This line should not have been reached.");
	}

	@Test(expected = NullPointerException.class)
	public void testConstructorNullTwo() {
		@SuppressWarnings("unused")
		final IndirectList<OWLClassA> l = new IndirectList<OWLClassA>(owner, ownerField, null, list);
		fail("This line should not have been reached.");
	}

	@Test
	public void testAdd() {
		final OWLClassA added = new OWLClassA();
		added.setUri(URI.create("http://added"));
		owner.getReferencedList().add(added);
		verify(uow).attributeChanged(owner, ownerField);
		assertEquals(backupList.size() + 1, target.size());
	}

	@Test
	public void testAddNull() {
		// Adding null is possible for some list types (ArrayList in our case
		// permits them)
		owner.getReferencedList().add(null);
		verify(uow).attributeChanged(owner, ownerField);
		assertEquals(backupList.size() + 1, target.size());
	}

	@Test
	public void testAddAtIndex() {
		final OWLClassA added = new OWLClassA();
		added.setUri(URI.create("http://added"));
		owner.getReferencedList().add(list.size() / 2, added);
		verify(uow).attributeChanged(owner, ownerField);
		assertEquals(backupList.size() + 1, target.size());
	}

	@Test
	public void testAddAll() {
		final List<OWLClassA> toAdd = new ArrayList<OWLClassA>();
		for (int i = 0; i < 5; i++) {
			final OWLClassA a = new OWLClassA();
			a.setUri(URI.create("http://addedA" + i));
			toAdd.add(a);
		}
		owner.getReferencedList().addAll(toAdd);
		verify(uow).attributeChanged(owner, ownerField);
		assertEquals(backupList.size() + toAdd.size(), target.size());
	}

	@Test
	public void testAddAllAtIndex() {
		final List<OWLClassA> toAdd = new ArrayList<OWLClassA>();
		for (int i = 0; i < 5; i++) {
			final OWLClassA a = new OWLClassA();
			a.setUri(URI.create("http://addedA" + i));
			toAdd.add(a);
		}
		final int index = 4;
		owner.getReferencedList().addAll(index, toAdd);
		verify(uow).attributeChanged(owner, ownerField);
		assertEquals(backupList.size() + toAdd.size(), target.size());
		assertEquals(toAdd.get(0), target.get(index));
	}

	@Test
	public void testClear() {
		owner.getReferencedList().clear();
		verify(uow).attributeChanged(owner, ownerField);
		assertTrue(target.isEmpty());
	}

	@Test
	public void testRemoveObject() {
		owner.getReferencedList().remove(list.get(0));
		verify(uow).attributeChanged(owner, ownerField);
		assertEquals(backupList.size() - 1, target.size());
	}

	@Test
	public void testRemoveNull() {
		owner.getReferencedList().remove(null);
		verify(uow, never()).attributeChanged(any(), any(Field.class));
	}

	@Test
	public void testRemoveAtIndex() {
		owner.getReferencedList().remove(list.size() - 1);
		verify(uow).attributeChanged(owner, ownerField);
		assertEquals(backupList.size() - 1, target.size());
	}

	@Test
	public void testRemoveAll() {
		List<OWLClassA> toRemove = list.subList(2, 5);
		final int toRemoveSize = toRemove.size();
		owner.getReferencedList().removeAll(toRemove);
		verify(uow).attributeChanged(owner, ownerField);
		assertEquals(backupList.size() - toRemoveSize, target.size());
	}

	@Test
	public void testRetainAll() {
		List<OWLClassA> toRetain = list.subList(2, 5);
		final int toRetainSize = toRetain.size();
		owner.getReferencedList().retainAll(toRetain);
		verify(uow).attributeChanged(owner, ownerField);
		assertEquals(toRetainSize, target.size());
	}

	@Test
	public void testSet() {
		final OWLClassA a = new OWLClassA();
		a.setUri(URI.create("http://setA"));
		owner.getReferencedList().set(0, a);
		verify(uow).attributeChanged(owner, ownerField);
		assertEquals(backupList.size(), target.size());
		assertTrue(target.contains(a));
	}

	@Test
	public void testIteratorRemove() {
		int cnt = 3;
		final OWLClassA toRemove = owner.getReferencedList().get(cnt);
		final Iterator<OWLClassA> it = owner.getReferencedList().iterator();
		int i = 0;
		while (it.hasNext() && i < cnt + 1) {
			i++;
			it.next();
		}
		it.remove();
		verify(uow).attributeChanged(owner, ownerField);
		assertFalse(owner.getReferencedList().contains(toRemove));
	}

	@Test
	public void testListIteratorAdd() {
		final OWLClassA addA = new OWLClassA();
		addA.setUri(URI.create("http://addA"));
		final ListIterator<OWLClassA> lit = owner.getReferencedList().listIterator();
		int i = 0;
		int pos = 4;
		while (lit.hasNext() && i < pos) {
			i++;
			lit.next();
		}
		lit.add(addA);
		verify(uow).attributeChanged(owner, ownerField);
		assertEquals(owner.getReferencedList().get(pos), addA);
	}

	@Test
	public void testListIteratorSetReverse() {
		final OWLClassA a = new OWLClassA();
		a.setUri(URI.create("http://setA"));
		final ListIterator<OWLClassA> lit = owner.getReferencedList().listIterator(5);
		while (lit.hasPrevious()) {
			lit.previous();
		}
		lit.set(a);
		verify(uow).attributeChanged(owner, ownerField);
		assertEquals(owner.getReferencedList().get(0), a);
	}

	@Test
	public void testListIteratorRemove() {
		int cnt = 3;
		final OWLClassA toRemove = owner.getReferencedList().get(cnt);
		final ListIterator<OWLClassA> it = owner.getReferencedList().listIterator();
		int i = 0;
		while (it.hasNext() && i < cnt + 1) {
			i++;
			it.next();
		}
		it.remove();
		verify(uow).attributeChanged(owner, ownerField);
		assertFalse(owner.getReferencedList().contains(toRemove));
		assertEquals(cnt - 1, it.previousIndex());
		assertEquals(cnt, it.nextIndex());
	}
}