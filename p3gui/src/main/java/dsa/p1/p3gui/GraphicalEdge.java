package dsa.p1.p3gui;

import dsa.p1.p3gui.DataNode.DataEdge;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class GraphicalEdge {
	private Label edgeLabel;
	private DataEdge dataEdge;
	private Arrow graphicalEdge;
	private int index0, colornum, numColumns;
	private Group root;
	private double y0, yf, x0, xf;
	private static int xOffset = 75, yOffset = 25, xSpacing = 500, ySpacing = 155;
	private static Color[] palet = dsa.p1.p3gui.palet.palet;

	public GraphicalEdge(DataEdge dEdge, int index0, int indexf, int colornum, Group group2, int size) {
		this.root = group2;
		this.dataEdge = dEdge;
		this.index0 = index0;
		this.colornum = colornum % palet.length;
		numColumns = (int) (Math.sqrt(size) * .75 + 1);
		x0 = index0 % numColumns * xSpacing + xOffset;
		y0 = index0 / numColumns * ySpacing + yOffset;
		xf = indexf % numColumns * xSpacing + xOffset;
		yf = indexf / numColumns * ySpacing + yOffset;
		graphicalEdge = makeEdge();
	}

	public DataEdge getDataEdge() {
		return dataEdge;
	}

	public Arrow getGraphicalEdge() {
		return graphicalEdge;
	}

	public int getStart() {
		return index0;
	}

	private Arrow makeEdge() {
		Arrow edge = new Arrow(x0, y0, xf, yf, palet[colornum]);
		return edge;
	}

	public void notifyX(int index, Object newVal) {
		if (index == index0)
			x0 = (double) newVal;
		else
			xf = (double) newVal;
		Arrow gEdge = makeEdge();
		root.getChildren().set(root.getChildren().indexOf(this.graphicalEdge), gEdge);
		this.graphicalEdge = gEdge;
	}

	public void notifyY(int index, Object newVal) {
		if (index == index0)
			y0 = (double) newVal;
		else
			yf = (double) newVal;
		Arrow gEdge = makeEdge();
		root.getChildren().set(root.getChildren().indexOf(this.graphicalEdge), gEdge);
		this.graphicalEdge = gEdge;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof GraphicalEdge))
			return false;
		return getStart() == ((GraphicalEdge) o).getStart()
				&& getDataEdge().getNextNode().equals(((GraphicalEdge) o).getDataEdge().getNextNode())
				&& getDataEdge().getRelationship().equals(((GraphicalEdge) o).getDataEdge().getRelationship());
	}
}