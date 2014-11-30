package sankoff;

/**
* @author Abel Oria Echevarria
* @author Lionel Larifla
*
*/

public class Node {
	private String name;
	private Node leftChild;
	private Node rightChild;
	private String sequence;

	public Node (Node leftChild, Node rigthChild, String name, String sequence){
		this.leftChild = leftChild;
		this.rightChild = rigthChild;
		this.name = name;
		this.sequence = sequence;
	}
		
	public Node getLeftChild() {
		return leftChild;
	}

	public Node getRightChild() {
		return rightChild;
	}
	
	public String getName(){
		return name;
	}
	
	public String getSequence(){
		return sequence;
	}
}