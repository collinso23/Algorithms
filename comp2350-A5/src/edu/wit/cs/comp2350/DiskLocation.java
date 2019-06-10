package edu.wit.cs.comp2350;

/*
 * A class that represents a binary tree node to hold disk location information
 * The node has parent, left, and right references for binary trees and color
 * for red black trees
 */
public class DiskLocation {
	private int track;
	private int sector;
	public DiskLocation left;
	public DiskLocation right;
	public DiskLocation parent;
	public RB color;

	public boolean isGreaterThan(DiskLocation that) {
		if (this.track > that.track)
			return true;
		else if (this.track == that.track && this.sector > that.sector)
			return true;
		return false;
	}

	public boolean equals(Object that) {
		if (that.getClass() != this.getClass())
			return false;
		DiskLocation d = (DiskLocation) that;
		return (this.track == d.track && this.sector == d.sector);
	}

	public DiskLocation(int t, int s) {
		track = t;
		sector = s;
		left = null;
		right = null;
		parent = null;
	}

	public String toString() {
		if (track < 0)
			return new String("");
		return String.format("Track: %d, Sector: %d", track, sector);
	}
}
