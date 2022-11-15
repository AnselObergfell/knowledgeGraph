package dsa.p1.p3gui;

import java.util.ArrayList;

public class DataNode implements NodeInterface {
	private String cui, name, psem;
	private ArrayList<String> sem;
	private String[] data;
	private ArrayList<DataEdge> edges;

	public DataNode(String data) {
		sem = new ArrayList<>();
		this.data = data.split("\\|", -1);
		for (String s : this.data) {
			if (s.equals("") || s == null) {
				s = "N/A";
			}
		}
		cui = this.data[0];
		name = this.data[1];
		psem = this.data[3];
		for (String el : this.data[2].split(",", -1)) {
			sem.add(el);
		}
		edges = new ArrayList<>();
	}

	public DataNode(String data, String relationship, DataNode n) {
		sem = new ArrayList<>();
		this.data = data.split("\\|", -1);
		cui = this.data[0];
		name = this.data[1];
		psem = this.data[3];
		for (String el : this.data[2].split(",", -1)) {
			sem.add(el);
		}
		edges = new ArrayList<>();
		addEdge(relationship, n);
	}

	@Override
	public String[] getData() {
		return data;
	}

	public String getName() {
		return name;
	}

	public String getCui() {
		return cui;
	}

	public String getPsem() {
		return psem;
	}

	public ArrayList<String> getSem() {
		return sem;
	}

	@Override
	public ArrayList<DataEdge> getEdges() {
		return edges;
	}

	public void addEdge(DataEdge edge) {
		if (edges.indexOf(edge) == -1)
			edges.add(edge);
	}

	public void addEdge(String relation, DataNode node) {
		addEdge(new DataEdge(relation, node));
	}

	public void addEdge(String relation, String data) {
		addEdge(relation, new DataNode(data));
	}

	@Override
	public boolean hasEdge() {
		return edges.size() > 0;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof DataNode))
			return false;
		return this.getName().equals(((DataNode) o).getName());
	}

	/*
	 * @Override public String toString() { return data + "\n Edges " + edges; }
	 */
	public class DataEdge implements EdgeInterface {

		private String relation;
		private DataNode node;

		public DataEdge(String relation, DataNode node) {
			this.relation = relation;
			this.node = node;
		}

		@Override
		public String getRelationship() {
			return relation;
		}

		@Override
		public DataNode getNextNode() {
			return node;
		}

		@Override
		public boolean equals(Object o) {
			return this.getRelationship().equals(((DataEdge) o).getRelationship())
					&& this.getNextNode().equals(((DataEdge) o).getNextNode());
		}

		@Override
		public String toString() {
			return relation + " " + node.toString();
		}
	}
}
