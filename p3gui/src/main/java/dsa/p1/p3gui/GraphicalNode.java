package dsa.p1.p3gui;

import java.util.ArrayList;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.StrokeType;
import jfxtras.labs.util.event.MouseControlUtil;

public class GraphicalNode {
	private DataNode dataNode;
	private ArrayList<GraphicalEdge> edges;
	private Label graphicalNode;
	private int index, numColumns;
	private static int xOffset = 75, yOffset = 25, xSpacing = 500, ySpacing = 155;

	public GraphicalNode(DataNode dataNode, int index, int size) {
		edges = new ArrayList<>();
		this.index = index;
		numColumns = (int) (Math.sqrt(size) * .75 + 1);
		this.dataNode = dataNode;
		graphicalNode = makeNode();
	}

	public void addEdge(GraphicalEdge edge) {
		edges.add(edge);
	}

	public Label getGraphicalNode() {
		return graphicalNode;
	}

	public ArrayList<GraphicalEdge> getEdges() {
		return edges;
	}

	public String name() {
		return dataNode.getName();
	}

	public int getIndex() {
		return index;
	}

	private Label makeNode() {
		Ellipse ellipse = new Ellipse(125, 50);
		ellipse.setStrokeType(StrokeType.OUTSIDE);
		ellipse.setStroke(Color.BLACK);
		ellipse.setStrokeWidth(4);
		ellipse.setFill(Color.TEAL);

		Label node = new Label(dataNode.getName(), ellipse);
		node.setContentDisplay(ContentDisplay.CENTER);
		node.setLayoutX(index % numColumns * xSpacing + xOffset);
		node.setLayoutY(index / numColumns * ySpacing + yOffset);
		MouseControlUtil.makeDraggable(node);

		node.layoutXProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<?> o, Object oldVal, Object newVal) {
				for (GraphicalEdge edge : edges) {
					edge.notifyX(index, newVal);
				}
			}
		});
		node.layoutYProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<?> o, Object oldVal, Object newVal) {
				for (GraphicalEdge edge : edges) {
					edge.notifyY(index, newVal);
				}
			}
		});

		return node;
	}
}
