package edu.wit.cs.comp2350;

// @Override tags tell the compiler that the method is implementing a method
// inherited from the abstract parent class
/*The node has parent, left, and right references for binary trees (DiskLocation)
* copied from lab a4 BST
*/
public class BST extends LocationHolder {
	
	//build tree and set root to nil
	public BST() {
		root=nil;
	}
	
	@Override
	//find a particular node in tree
	public DiskLocation find(DiskLocation d) {
		return _find(root,d);
	}

	@Override
	//next node in tree
	public DiskLocation next(DiskLocation d) {
		if(!d.right.equals(nil)) {return min(d.right);}
		
		return up(d);
	}
	
	@Override
	//prev node in tree
	public DiskLocation prev(DiskLocation d) {
		if(!d.left.equals(nil)) {return min(d.left);}
		
		return down(d);
	}
	
	@Override
	//insert new nodes into tree
	public void insert(DiskLocation d) {
		d.right = nil;
		d.left = nil;
		d.parent = nil;
		d.parent = findParent(d, root, nil);
		
		if (d.parent == nil) {
			root = d;
			
			return;
		}
		else {
			if (!d.isGreaterThan(d.parent)) {d.parent.left = d;}

			else {d.parent.right = d;}
		}	
	}

	@Override
	//gets height of tree
	public int height() {
		return _height(root) -1;
	}
	
	/* _________________Private Helper Methods_________________ */
	
	//help method for pulling node up
	private DiskLocation up(DiskLocation d) {
		DiskLocation par = d.parent;
				
		if (par.equals(nil) || d == par.left) {return par;}
		
		return up(par);
	}
			
	//help method for pulling node down	
	private DiskLocation down(DiskLocation d) {
		DiskLocation par = d.parent;
		
		if (par.equals(nil) || d == par.right) {return par;}

		return down(par);
	}
	
	//help method for finding minimum
	private DiskLocation min(DiskLocation d) {
		if (d == nil) {return nil;}
		
		if (d.left == nil) {return d;}
			
		return min(d.left);
	}

	//help method for recursive search operation
	private DiskLocation _find(DiskLocation curr, DiskLocation x) {
		if(curr.equals(x) || curr.equals(nil)) {return curr;}
		
		if (x.isGreaterThan(curr)) {return _find(curr.right, x);}
		
		return _find(curr.left, x);
	}

	//help method for finding parent of node being evaluated
	private DiskLocation findParent(DiskLocation n, DiskLocation curr,DiskLocation parent) {
		if (curr.equals(nil)) {return parent;}
		
		if (!n.isGreaterThan(curr)) {return findParent(n, curr.left,curr);}
		
		return findParent(n,curr.right,curr);
	}

	//help method for getting height of tree
	private int _height(DiskLocation d) {
		if (d.equals(nil)) {return 0;}
		
		int left = _height(d.left);
		int right = _height(d.right);
		
		if (left > right) {return left+1;}
		
		return right+1;
	}
}
