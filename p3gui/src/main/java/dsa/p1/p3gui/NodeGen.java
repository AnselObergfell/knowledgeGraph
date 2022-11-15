package dsa.p1.p3gui;

import java.util.ArrayList;

import dsa.p1.p3gui.DataNode.DataEdge;

public class NodeGen implements NodeCollectionInterface {
	ArrayList<DataNode> dNodes;

	public NodeGen() {
		DataReader r = new DataReader();
		ArrayList<String[]> data = r.tsvr();
		dNodes = new ArrayList<>();
		String nA, ed, nC;
		DataNode node;
		int inNode;
		for (String[] line : data) {
			nA = line[0];
			ed = line[1];
			nC = line[2];
			if (!nA.equals(nC)) {
				node = new DataNode(nA);
				inNode = dNodes.indexOf(node);
				if (inNode == -1) {

					node.addEdge(ed, nC);
					dNodes.add(node);
				} else {
					node = dNodes.get(inNode);
					node.addEdge(ed, nC);
				}
			}
		}
	}

	public ArrayList<DataNode> searchCUI(String term, boolean r) {
		ArrayList<DataNode> searchNodes = new ArrayList<>();
		for (DataNode n : dNodes) {
			if (n.getCui().toLowerCase().trim().equals(term)) {
				searchNodes.add(n);
			}
			if (r)
				for (DataEdge e : n.getEdges()) {
					if (e.getNextNode().getCui().toLowerCase().trim().equals(term)) {
						searchNodes
								.add(new DataNode(String.join("|", n.getData()), e.getRelationship(), e.getNextNode()));
					}
				}
		}
		return searchNodes;
	}

	public ArrayList<DataNode> searchName(String term, boolean r) {
		ArrayList<DataNode> searchNodes = new ArrayList<>();
		for (DataNode n : dNodes) {
			if (r)
				for (DataEdge e : n.getEdges()) {
					if (e.getNextNode().getName().toLowerCase().trim().equals(term)) {
						searchNodes
								.add(new DataNode(String.join("|", n.getData()), e.getRelationship(), e.getNextNode()));
					}
				}
			if (n.getName().toLowerCase().trim().equals(term)) {
				searchNodes.add(n);
			}
			
		}
		return searchNodes;
	}

	public ArrayList<DataNode> searchPSem(String term, boolean r) {
		ArrayList<DataNode> searchNodes = new ArrayList<>();
		for (DataNode n : dNodes) {
			if (n.getPsem().toLowerCase().trim().equals(term)) {
				searchNodes.add(n);
			}
			if (r)
				for (DataEdge e : n.getEdges()) {
					if (e.getNextNode().getPsem().toLowerCase().trim().equals(term)) {
						searchNodes
								.add(new DataNode(String.join("|", n.getData()), e.getRelationship(), e.getNextNode()));
					}
				}
		}
		return searchNodes;
	}

	public ArrayList<DataNode> searchSem(String term, boolean r) {
		ArrayList<DataNode> searchNodes = new ArrayList<>();
		for (DataNode n : dNodes) {
			for (String s : n.getSem()) {
				if (s.toLowerCase().trim().equals(term)) {
					searchNodes.add(n);
					break;
				}
			}
			if (r)
				for (DataEdge e : n.getEdges()) {
					for (String s : e.getNextNode().getSem()) {
						if (s.toLowerCase().trim().equals(term)) {
							searchNodes.add(
									new DataNode(String.join("|", n.getData()), e.getRelationship(), e.getNextNode()));
							break;
						}
					}
					if (e.getNextNode().getPsem().toLowerCase().trim().equals(term)) {
						searchNodes
								.add(new DataNode(String.join("|", n.getData()), e.getRelationship(), e.getNextNode()));
					}
				}
		}
		return searchNodes;
	}

	public ArrayList<DataNode> searchRel(String term) {
		ArrayList<DataNode> searchNodes = new ArrayList<>();
		for (DataNode n : dNodes) {
			for (DataEdge e : n.getEdges()) {
				if (e.getRelationship().toLowerCase().trim().equals(term)) {
					searchNodes.add(new DataNode(String.join("|", n.getData()), e.getRelationship(), e.getNextNode()));
				}
			}
		}
		return searchNodes;
	}

	@Override
	public ArrayList<DataNode> StartingNodes() {
		return dNodes;
	}

	public ArrayList<DataNode> searchCount(int term) {
		ArrayList<DataNode> searchNodes = new ArrayList<>();
		for (DataNode n : dNodes)
			if (n.getEdges().size() == term)
				searchNodes.add(n);
		return searchNodes;
	}
}
