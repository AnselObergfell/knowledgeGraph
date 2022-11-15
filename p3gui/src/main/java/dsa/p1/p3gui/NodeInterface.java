package dsa.p1.p3gui;

import java.util.ArrayList;

import dsa.p1.p3gui.DataNode.DataEdge;

interface NodeInterface {
	String[] getData();

	boolean hasEdge();

	ArrayList<DataEdge> getEdges();
}
