package dsa.p1.p3gui;

import java.util.ArrayList;

import dsa.p1.p3gui.DataNode.DataEdge;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class TestGui extends Application {

	private static Color[] palet = dsa.p1.p3gui.palet.palet;
/**
 * backup in case start fails
 * @param args
 */
	public static void main(String[] args) {
		Application.launch(args);
	}

	private ArrayList<DataNode> allDataNodes = new ArrayList<>();
	private ArrayList<GraphicalNode> allGraphicalNodes = new ArrayList<>();
	private ArrayList<String> relations = new ArrayList<>();
	private NodeGen ng = new NodeGen();
	private ArrayList<DataNode> currentdataNodes = ng.StartingNodes();
	private AnchorPane root;
	private Group group = new Group();
	private ScrollPane scrollPane = new ScrollPane();
	private Stage pStage;
	PanAndZoomPane panAndZoomPane;
	private DoubleProperty deltaY = new SimpleDoubleProperty(0.0d);
	private DoubleProperty zoomProperty = new SimpleDoubleProperty(1.0d);
/**
 * @param previousGraphicalEdge last edge parsed
 * @param index current location of node
 * @param size total number of nodes to be represented
 * @return last used index for management of graphical nodes
 */
	private int depthFirstGenerator(GraphicalEdge previousGraphicalEdge, int index, int size) {

		int index0 = index, indexf = index + 1;
		DataNode currentDataNode = previousGraphicalEdge.getDataEdge().getNextNode();
		GraphicalNode currentGraphicalNode = null;
		if (allDataNodes.contains(currentDataNode)) {
			currentGraphicalNode = allGraphicalNodes
					.get(allDataNodes.indexOf(previousGraphicalEdge.getDataEdge().getNextNode()));
			index0 = currentGraphicalNode.getIndex();
			index--;
		} else {
			currentGraphicalNode = new GraphicalNode(currentDataNode, index0, size);
			allDataNodes.add(currentDataNode);
			allGraphicalNodes.add(currentGraphicalNode);
		}
		if (!currentGraphicalNode.getEdges().contains(previousGraphicalEdge)) {
			currentGraphicalNode.addEdge(previousGraphicalEdge);
			group.getChildren().add(previousGraphicalEdge.getGraphicalEdge());
		}

		if (currentDataNode.hasEdge()) {
			for (DataEdge edge : currentDataNode.getEdges()) {
				if (allDataNodes.contains(edge.getNextNode()))
					indexf = allGraphicalNodes.get(allDataNodes.indexOf(edge.getNextNode())).getIndex();
				else
					indexf = index + 1;
				if (!relations.contains(edge.getRelationship()))
					relations.add(edge.getRelationship());
				GraphicalEdge nextEdge = new GraphicalEdge(edge, index0, indexf,
						relations.indexOf(edge.getRelationship()), group, size);
				if (!currentGraphicalNode.getEdges().contains(nextEdge)) {
					currentGraphicalNode.addEdge(nextEdge);
					index = depthFirstGenerator(nextEdge, index + 1, size);
				}
			}
		}
		return index;
	}
/**
 * generates visual representation of data
 */
	private void generateGraph() {
		ArrayList<DataNode> nodes = currentdataNodes;
		int size = nodes.size();
		GraphicalNode leadNode = null;
		GraphicalEdge leadEdge = null;
		int absIndex = 0, relIndex = 0;
		for (DataNode node : nodes) {
			if (allDataNodes.contains(node)) {
				leadNode = allGraphicalNodes.get(allDataNodes.indexOf(node));
				relIndex--;
			} else {
				leadNode = new GraphicalNode(node, relIndex, size);
				allDataNodes.add(node);
				allGraphicalNodes.add(leadNode);
			}
			if (node.hasEdge()) {
				for (DataEdge edge : node.getEdges()) {

					if (!relations.contains(edge.getRelationship()))
						relations.add(edge.getRelationship());

					if (allDataNodes.contains(edge.getNextNode()))
						leadEdge = new GraphicalEdge(edge, leadNode.getIndex(),
								allGraphicalNodes.get(allDataNodes.indexOf(edge.getNextNode())).getIndex(),
								relations.indexOf(edge.getRelationship()), group, size);

					else
						leadEdge = new GraphicalEdge(edge, absIndex, relIndex + 1,
								relations.indexOf(edge.getRelationship()), group, size);

					if (!leadNode.getEdges().contains(leadEdge)) {
						leadNode.addEdge(leadEdge);
						relIndex = depthFirstGenerator(leadEdge, relIndex + 1, size);
					}
				}
			}
			relIndex++;
			absIndex = relIndex;

		}

		for (GraphicalNode node : allGraphicalNodes)
			group.getChildren().add(node.getGraphicalNode());

	}
/**
 * forms legend
 */
	private void makeLegend() {
		int i = 0;
		int y = 2 * 16;
		Label key = new Label("Key");
		key.setLayoutY(y);
		y += 15;
		root.getChildren().add(key);
		for (String relation : relations) {
			Rectangle rectangle = new Rectangle(15, 15, palet[i % palet.length]);
			ToggleButton tb = new ToggleButton("", rectangle);
			key = new Label(relation, rectangle);
			key.setLayoutY(i * 15 + y);
			i++;
			root.getChildren().add(key);
		}
	}
	/**
	 * Creates Search bar and search button
	 */
	private void makeSearch() {
		Group g = new Group();
		Button b = new Button("🔍");
		b.setDefaultButton(true);

		TextField t = new TextField();
		b.setOnAction(new EventHandler() {
			@Override
			public void handle(Event event) {
				search(t.getText());
			}
		});
		g.getChildren().add(b);
		t.setTranslateX(30);
		g.getChildren().add(t);
		root.getChildren().add(g);
	}
	/**
	 * Prints menu of search field options
	 */
	private void menu() {
		System.out.println("Search Functions:"
				+ "\n adding \":r\" to the end of a search will reduce the search to only primary nodes"
				+ "\n \"name:\" -(default)" + "\n \"all\" - returns to full view" + "\n \"cui:\" - searches by CUI"
				+ "\n \"psem:\" - searches by primary semantic type" + "\n \"sem:\" - searches by semantic type"
				+ "\n \"rel:\" - searches by relationship shared"
				+ "\n \"count:\" - searches by number of connections from the node");

	}
	/**
	 * Necessary resets on variables in order to produce a new view when a search is performed
	 * @param cdn arraylist of datanodes to be used
	 */
	private void reset(ArrayList<DataNode> cdn) {
		allDataNodes = new ArrayList<>();
		allGraphicalNodes = new ArrayList<>();
		relations = new ArrayList<>();
		currentdataNodes = cdn;
		group = new Group();
		root = setUpAnchorPane();
		zoomAndScrollSetUp(root);
		generateGraph();
		makeLegend();
		makeSearch();
		Scene scene = new Scene(root, 1600, 900);
		pStage.setScene(scene);
		pStage.show();
	}
	/**
	 * Parses input string and calls the respective search function and passes the correct data
	 * @param term full string of normal format "searchformat":"searchterm":rflag
	 */
	private void search(String term) {
		String command = "all";
		String[] terms = null;
		boolean r = true;
		ArrayList<DataNode> newSNodes = null;
		long count = term.chars().filter(ch -> ch == ':').count();
		if (!term.equals("all")) {
			if ((count == 0 || (count == 1 && term.split(":", 2)[1].trim().toLowerCase().equals("r")))) {
				term = "name:" + term;
				count++;
			}
			terms = term.split(":", (int) (count + 1));
			command = terms[0].toLowerCase();
		}
		if (count == 2) {
			r = false;
		}

		switch (command) {
		case "name":
			newSNodes = ng.searchName(terms[1].toLowerCase().trim(), r);
			break;
		case "all":
			newSNodes = ng.StartingNodes();
			break;
		case "cui":
			newSNodes = ng.searchCUI(terms[1].toLowerCase().trim(), r);
			break;
		case "psem":
			newSNodes = ng.searchPSem(terms[1].toLowerCase().trim(), r);
			break;
		case "sem":
			newSNodes = ng.searchSem(terms[1].toLowerCase().trim(), r);
			break;
		case "rel":
			newSNodes = ng.searchRel(terms[1].toLowerCase().trim());
			break;
		case "count":
			newSNodes = ng.searchCount(Integer.parseInt(terms[1].toLowerCase().trim()));
		}
		reset(newSNodes);
	}
/**
 * Used to enable zoomability
 * @return AnchorPane object
 */
	private AnchorPane setUpAnchorPane() {
		scrollPane.setPannable(true);
		scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
		AnchorPane.setTopAnchor(scrollPane, 0d);
		AnchorPane.setRightAnchor(scrollPane, 0d);
		AnchorPane.setBottomAnchor(scrollPane, 0d);
		AnchorPane.setLeftAnchor(scrollPane, 0d);
		return new AnchorPane();
	}
/**
 * First method to be ran, sets up and displays initial offering
 */
	@Override
	public void start(Stage primaryStage) {
		menu();
		pStage = primaryStage;
		root = setUpAnchorPane();
		generateGraph();
		zoomAndScrollSetUp(root);
		Scene scene = new Scene(root, 1600, 900);
		makeLegend();
		makeSearch();
		pStage.setScene(scene);
		pStage.show();

	}
/**
 * Sets up zoom and scroll functionalities
 * @param root base of gui to add components to
 */
	private void zoomAndScrollSetUp(AnchorPane root) {
		PanAndZoomPane panAndZoomPane = new PanAndZoomPane();
		zoomProperty.bind(panAndZoomPane.myScale);
		deltaY.bind(panAndZoomPane.deltaY);
		panAndZoomPane.getChildren().add(group);

		SceneGestures sceneGestures = new SceneGestures(panAndZoomPane);

		scrollPane.setContent(panAndZoomPane);
		panAndZoomPane.toBack();
		scrollPane.addEventFilter(MouseEvent.MOUSE_CLICKED, sceneGestures.getOnMouseClickedEventHandler());
		scrollPane.addEventFilter(MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
		scrollPane.addEventFilter(MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnMouseDraggedEventHandler());
		scrollPane.addEventFilter(ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());

		root.getChildren().add(scrollPane);
	}
}