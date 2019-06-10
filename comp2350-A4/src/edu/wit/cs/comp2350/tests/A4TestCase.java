package edu.wit.cs.comp2350.tests;

import java.security.Permission;
import java.util.ArrayList;
import java.util.Collections;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import edu.wit.cs.comp2350.DiskLocation;
import edu.wit.cs.comp2350.LocationHolder;
import edu.wit.cs.comp2350.BST;

public class A4TestCase{


	@Rule
	public Timeout globalTimeout = Timeout.seconds(15);

	@SuppressWarnings("serial")
	private static class ExitException extends SecurityException {}

	private static class NoExitSecurityManager extends SecurityManager 
	{
		@Override
		public void checkPermission(Permission perm) {}

		@Override
		public void checkPermission(Permission perm, Object context) {}

		@Override
		public void checkExit(int status) { super.checkExit(status); throw new ExitException(); }
	}

	@Before
	public void setUp() throws Exception 
	{
		System.setSecurityManager(new NoExitSecurityManager());
	}

	@After
	public void tearDown() throws Exception 
	{
		System.setSecurityManager(null);
	}


	private void _testInsert(LocationHolder T, DiskLocation[] vals) {
		try {
			for (int i = 0; i < vals.length; i++) {
				T.insert(vals[i]);
			}
			assertEquals("Incorrect number of nodes in tree", vals.length, T.size());
		} catch (ExitException e) {}
	}

	private void _testNext(LocationHolder T, DiskLocation target, DiskLocation expected) {
		DiskLocation actual = new DiskLocation(-1, -1);
		try {
			actual = T.next(target);
		} catch (ExitException e) {}
		assertNotNull("Next value is unexpectedly null", actual);
		assertEquals("Location after " + target.toString() + " is incorrect", expected, actual);
	}

	private void _testPrev(LocationHolder T, DiskLocation target, DiskLocation expected) {
		DiskLocation actual = new DiskLocation(-1, -1);
		try {
			actual = T.prev(target);
		} catch (ExitException e) {}
		assertNotNull("Prev value is unexpectedly null", actual);
		assertEquals("Location before " + target.toString() + " is incorrect", expected, actual);
	}

	private void _testHeight(LocationHolder T, int h) {
		int actual = Integer.MAX_VALUE;
		try {
			actual = T.height();
		} catch (ExitException e) {}
		assertEquals(h, actual);

	}

	@Test
	public void testInsertSmall() {
		_testInsert(new BST(), new DiskLocation[] {new DiskLocation(1, 2)});
		_testInsert(new BST(), new DiskLocation[] {new DiskLocation(1, 2), new DiskLocation(1, 3)});
		_testInsert(new BST(), new DiskLocation[] {new DiskLocation(1, 2), new DiskLocation(1, 1), new DiskLocation(1, 3)});
		_testInsert(new BST(), new DiskLocation[] {new DiskLocation(1, 3), new DiskLocation(1, 2), new DiskLocation(1, 1)});
	}

	@Test
	public void testInsertMedium() {
		_testInsert(new BST(), new DiskLocation[] {new DiskLocation(1, 3), new DiskLocation(1, 1), new DiskLocation(1, 2), new DiskLocation(1, 4)});
		_testInsert(new BST(), new DiskLocation[] {new DiskLocation(1, 3), new DiskLocation(1, 1), new DiskLocation(1, 2), new DiskLocation(1, 5), new DiskLocation(1, 4)});
		_testInsert(new BST(), new DiskLocation[] {new DiskLocation(1, 2), new DiskLocation(2, 1), new DiskLocation(2, 3), new DiskLocation(1, 4), new DiskLocation(1, 1), new DiskLocation(2, 4)});
		_testInsert(new BST(), new DiskLocation[] {new DiskLocation(2, 3), new DiskLocation(1, 3), new DiskLocation(2, 2), new DiskLocation(1, 2), new DiskLocation(1, 1), new DiskLocation(2, 1)});
	}

	@Test
	public void testInsertSeq() {
		for (int i = 1; i < 100; i*=10) {
			DiskLocation[] d = new DiskLocation[i];
			for (int j = 0; j < i; j++)
				d[j] = new DiskLocation(1, j);
			_testInsert(new BST(), d);
		}
	}

	@Test
	public void testInsertRand() {

		for (int track = 10; track <= 100; track += 10) {
			for (int sector = 10; sector <= 100; sector += 10) {
				ArrayList<DiskLocation> arrD = new ArrayList<DiskLocation>(track*sector);
				for (int i = 0; i < track; i++) {
					for (int j = 0; j < sector; j++) 
						arrD.add(new DiskLocation(i, j));
				}
				DiskLocation[] d = new DiskLocation[track*sector];
				Collections.shuffle(arrD);		// randomize array
				_testInsert(new BST(), arrD.toArray(d));
			}
		}
	}

	private BST _buildSmallTree() {
		BST T = new BST();

		T.insert(new DiskLocation(6, 1));
		T.insert(new DiskLocation(4, 1));
		T.insert(new DiskLocation(9, 1));
		T.insert(new DiskLocation(2, 1));
		T.insert(new DiskLocation(5, 1));
		T.insert(new DiskLocation(8, 1));
		T.insert(new DiskLocation(11, 1));

		return T;
	}

	private BST _buildSmolTree() {
		BST T = new BST();

		T.insert(new DiskLocation(6, 1));
		T.insert(new DiskLocation(4, 1));
		T.insert(new DiskLocation(9, 1));

		return T;
	}

	@Test
	public void testHeightSmall() {
		BST T = _buildSmolTree();
		_testHeight(T, 1);

		T = _buildSmallTree();
		_testHeight(T, 2);
	}

	@Test
	public void testNextSmall() {
		BST T = _buildSmolTree();

		_testNext(T, T.find(new DiskLocation(4, 1)), new DiskLocation(6, 1));
		_testNext(T, T.find(new DiskLocation(6, 1)), new DiskLocation(9, 1));

		T = _buildSmallTree();
		_testNext(T, T.find(new DiskLocation(9, 1)), new DiskLocation(11, 1));
		_testNext(T, T.find(new DiskLocation(4, 1)), new DiskLocation(5, 1));
	}

	@Test
	public void testNextLarge() {
		BST T = new BST();

		for (int i = 0; i < 300; i++)
			T.insert(new DiskLocation(i, 1));
		_testNext(T, T.find(new DiskLocation(91, 1)), new DiskLocation(92, 1));
		_testNext(T, T.find(new DiskLocation(140, 1)), new DiskLocation(141, 1));
	}

	@Test
	public void testPrevSmall() {
		BST T = _buildSmolTree();

		_testPrev(T, T.find(new DiskLocation(9, 1)), new DiskLocation(6, 1));
		_testPrev(T, T.find(new DiskLocation(6, 1)), new DiskLocation(4, 1));

		T = _buildSmallTree();
		_testPrev(T, T.find(new DiskLocation(11, 1)), new DiskLocation(9, 1));
		_testPrev(T, T.find(new DiskLocation(5, 1)), new DiskLocation(4, 1));
	}

	@Test
	public void testPrevLarge() {
		BST T = new BST();

		for (int i = 0; i < 300; i++)
			T.insert(new DiskLocation(i, 1));
		_testPrev(T, T.find(new DiskLocation(92, 1)), new DiskLocation(91, 1));
		_testPrev(T, T.find(new DiskLocation(141, 1)), new DiskLocation(140, 1));
	}

	@Test
	public void testFind() {
		BST T = _buildSmolTree();
		DiskLocation d;

		d = T.find(new DiskLocation(9, 1));
		assertNotNull("Find value is unexpectedly null", d);
		assertTrue("Find is not working correctly", d.equals(new DiskLocation(9, 1)));

		d = T.find(new DiskLocation(4, 1));
		assertNotNull("Find value is unexpectedly null", d);
		assertTrue("Find is not working correctly", d.equals(new DiskLocation(4, 1)));

		T = _buildSmallTree();

		d = T.find(new DiskLocation(4, 1));
		assertNotNull("Find value is unexpectedly null", d);
		assertTrue("Find is not working correctly", d.equals(new DiskLocation(4, 1)));

		d = T.find(new DiskLocation(11, 1));
		assertNotNull("Find value is unexpectedly null", d);
		assertTrue("Find is not working correctly", d.equals(new DiskLocation(11, 1)));
	}


	private int _treeSize(BST T, DiskLocation min) {
		DiskLocation curr = T.find(min);
		int count = 0;
		try {
			while (curr != BST.nil && curr != null) {
				curr = T.next(curr);
				count++;
			}
		} catch (NullPointerException ex) {
		}

		return count;
	}

	@Test
	public void testSize() {
		BST T = _buildSmolTree();
		assertEquals("Tree has wrong number of nodes", 3, _treeSize(T, new DiskLocation(4, 1)));

		T = _buildSmallTree();
		assertEquals("Tree has wrong number of nodes", 7, _treeSize(T, new DiskLocation(2, 1)));


		T = new BST();
		ArrayList<DiskLocation> arrD = new ArrayList<DiskLocation>(200);
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 10; j++) 
				arrD.add(new DiskLocation(i, j));
		}
		DiskLocation[] d = new DiskLocation[200];
		Collections.shuffle(arrD);		// randomize array
		_testInsert(T, arrD.toArray(d));

		assertEquals("Tree has wrong number of nodes", 200, _treeSize(T, new DiskLocation(0, 0)));
	}
}
