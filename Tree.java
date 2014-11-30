package sankoff;
import java.util.ArrayList;
import java.util.Map;

/**
* @author Abel Oria Echevarria
* @author Lionel Larifla
*
*/

public class Tree {

	private Node root;
	private int weight;

	public Tree(String newick, 
			int[][] weightMatrix, 
			String alphabet,
			Map<String, String> sequences,
			int sequenceSize){

		int alphabetsize = weightMatrix[0].length;
		newick = newick.replace(" ", "");
		root = (Node) builtTree(newick, sequences, alphabetsize).get(0);
		
		weight = Sankoff.getPoids(root, weightMatrix, alphabet, 
				sequenceSize);
	}

	public int getWeight(){
		return weight;
	}
	
	
	/**
	 * Affichage de l'arbre enracine dans root
	 */
	public void print(){
		print(root, "");
	}
	private void print(Node node, String indent){
		if(node != null){
			System.out.println(indent + node.getName() + ": " + node.getSequence());
			indent =indent + "    "; 
			print(node.getLeftChild(), indent);
			print(node.getRightChild(), indent);			
		}
		
	}
	
	
	/**
	 * Contruction d'un en fontion d'un arbre  especifié en format newick
	 * @param newick
	 * @param sequences
	 * @param alphabetSize
	 * @return racine de l'arbre
	 */
	private ArrayList<Object> builtTree(String newick, 
			Map<String,String> sequences, 
			int alphabetSize) {
		
		String action = "";
		
		int indexParentheses = newick.indexOf("(");
		
		if(indexParentheses == 0){
			action="(";
			newick = newick.substring(1);
		}
		else{
			int index = newick.indexOf(",");
			int indexParenthese = newick.indexOf(")");
			
			if(index > indexParenthese || index == -1)
				index = indexParenthese;
			
			action = newick.substring(0, index);
			newick = newick.substring(index);
		}
		
		
		ArrayList<Object> node_newick = new ArrayList<Object>();

		if(action.equals("(")){
			//obtenir l'enfant gauche et le nouveau newick
			ArrayList<Object> leftChild_newick = 
					builtTree(newick, sequences, alphabetSize);
			Node leftChild = (Node) leftChild_newick.get(0);
			newick = (String) leftChild_newick.get(1);

			//effacer la virgule
			newick  = newick.substring(1);
			
			//obtenir l'enfant droite et le nouveau newick
			ArrayList<Object> rightChild_newick = 
					builtTree(newick, sequences, alphabetSize);
			Node rightChild = (Node) rightChild_newick.get(0);
			newick = (String) rightChild_newick.get(1);
			
			//effacer le parenthèse ")"
			newick = newick.substring(1);
			
			//retouner le noeud courant et le nouveau newick
			node_newick.add(new Node(leftChild, rightChild,"", ""));
			node_newick.add(newick);
		}
		
		else{
			// L'action est le nom de la feuille
			String name = action;
			
			//retouner  la feuille et le nouveau newick
			node_newick.add(new Node(null, null,name, sequences.get(name)));
			node_newick.add(newick);
		}
		
		return node_newick;
	}
}