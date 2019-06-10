package edu.wit.cs.comp2350;


public class RBTree extends LocationHolder {

	/** sets a disk location's color to red.
	 * 
	 * Use this method on fix-insert instead of directly
	 * coloring nodes red to avoid setting nil as red.
	 * 
	 * Properties of RB Trees
	 * nodes = red || black
	 * root = black
	 * every leaf = black
	 * if node red then both children our black
	 * For each node all simple paths from the node to descendant 
	 * leaves contain the same number of black nodes
	 */
	
	//sentinel = nil
	//init tree set root and nil to being black
	public RBTree() {
		root = nil;	
		root.color = RB.BLACK;
		nil.color = RB.BLACK;
	}
	
	//set a nodes color to red
	private void setRed(DiskLocation z) {
		if (z != nil)
			z.color = RB.RED;
	}
	
	@Override
	//search for node in tree
	public DiskLocation find(DiskLocation d) {
			return _find(root,d);
	}

	@Override
	//traverse forwards
	public DiskLocation next(DiskLocation d) {
		if(!d.right.equals(nil)) {return min(d.right);}
		
		return up(d);
	}

	@Override
	//traverse backwards
	public DiskLocation prev(DiskLocation d) {
		if(!d.left.equals(nil)) {return min(d.left);}
		
		return down(d);
	}
	
	@Override
	//insert new val into tree
	public void insert(DiskLocation d) {
		d.parent = findParent(d,root,nil);
		
		if (d.parent == nil) {root = d;}
		
		else {
			if (!d.isGreaterThan(d.parent)) {d.parent.left = d;}
			
			else {d.parent.right = d;}
		}
		d.left = nil; 
		d.right = nil;
		setRed(d);
		insertFix(d);
	}

	@Override
	//gets the height of the tree and returns it
	public int height() {
		return _height(root) -1;
	}

	/* _________________Private Helper Methods_________________ */
	//carefully going line by line and seeing if code says same thing as pseudo code

	//help method to fix the insert post operation
	private void insertFix(DiskLocation z) {
		DiskLocation y = nil;
		while (z.parent.color == RB.RED) {
			if (z.parent.equals(z.parent.parent.left)) {
				y = z.parent.parent.right;
				if (y.color == RB.RED) {
					z.parent.color = RB.BLACK;
					y.color = RB.BLACK;
					z.parent.parent.color = RB.RED;
					z = z.parent.parent;
				}
				else {
					if(z.equals(z.parent.right)) {
						z = z.parent;
						rotateLeft(z);
					}
					z.parent.color = RB.BLACK;
					z.parent.parent.color = RB.RED;
					rotateRight(z.parent.parent);
				}	
			}
			else {
				y = z.parent.parent.left;
				if (y.color == RB.RED) {
					z.parent.color = RB.BLACK;
					y.color = RB.BLACK;
					z.parent.parent.color = RB.RED;
					z = z.parent.parent;
				}
				else {
					if(z.equals(z.parent.left)) {
						z = z.parent;
						rotateRight(z);
					}
					z.parent.color = RB.BLACK;
					z.parent.parent.color = RB.RED;
					rotateLeft(z.parent.parent);
				}	
			}
		}
		root.color = RB.BLACK;
	}

	//Rotation Methods left = y,d | right = x,d
	//rotate subtree left
	private void rotateLeft(DiskLocation d) {
		//left rotation on node x | PAGE 313
		DiskLocation y = d.right;
		d.right = y.left;
		
		if (!y.left.equals(nil)) {y.left.parent = d;}
	
		y.parent = d.parent;
		
		if (d.parent.equals(nil)) {root = y;}
		
		else if (d.parent.left.equals(d)) {d.parent.left = y;}
		
		else {d.parent.right = y;}
		
		y.left = d;
		d.parent = y;
	}
	
	//rotate subtree right
	private void rotateRight(DiskLocation d) {
		//right rotation on node x | PAGE 313 
		DiskLocation x = d.left;
		d.left = x.right;
		
		if (!x.right.equals(nil)) {x.right.parent = d;}
		
		x.parent = d.parent;
		
		if (d.parent.equals(nil)) {root = x;}
		
		else if (d.parent.right.equals(d)) {d.parent.right = x;}
		
		else {d.parent.left = x;}
		
		x.right = d;
		d.parent = x;
	}
	
	//help method to find the parent of the current node
	private DiskLocation findParent(DiskLocation n, DiskLocation curr,DiskLocation parent) {
		if (curr.equals(nil)) {return parent;}
			
		if (!n.isGreaterThan(curr)) {return findParent(n, curr.left,curr);}
			
		return findParent(n,curr.right,curr);
	}
	
	//help method for searching finding particular node in tree.
	private DiskLocation _find(DiskLocation curr, DiskLocation x) {
		if(curr.equals(x) || curr.equals(nil)) {return curr;}
	
		if (x.isGreaterThan(curr)) {return _find(curr.right, x);}
	
		return _find(curr.left, x);
	}
	
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
		
	//help method for getting height of tree
	private int _height(DiskLocation d) {
		if (d.equals(nil)) {return 0;}
			
		int left = _height(d.left);
		int right = _height(d.right);
			
		if (left > right) {return left+1;}
			
		return right+1;
	}
}