package sankoff;

/**
* @author Abel Oria Echevarria
* @author Lionel Larifla
*
*/

public class Sankoff {

	/**
	 * @param root
	 * @param weightMatrix
	 * @param alphabet
	 * @param sequenceSize
	 * @return le poids de l'arbre enraciné à root selon l'algorithme Sankoff
	 */
	public static int getPoids(Node root, int[][] weightMatrix, 
			String alphabet, int sequenceSize) {
		
		int weight = 0;
		
		// Calcule du poids minimum pour chaque index des sequences
		for(int i = 0; i < sequenceSize ; i++){
			weight += getWeightSankoff(root, weightMatrix, alphabet, i); 
		}
		
		return weight;
	}

	
	/**
	 * @param root
	 * @param weightMatrix
	 * @param alphabet
	 * @param index
	 * @return le poids minimum pour un index donnée des sequences del'arbre
	 */
	private static int getWeightSankoff(Node root,
			int[][] weightMatrix, String alphabet, int index) {

		int min = 0;

		// Vector contenant les poids de l'arbre d'un index donnee des sequences
		int weightsVector[] = getWeightsVector(root, weightMatrix, alphabet, 
				index);
		
		// anulation des position avec "-"  dans au moins une sequence
		if(weightsVector[0]==-1)
			return min;
		
		min = getMin(weightsVector);
		return min;
	}


	/**
	 * @param node
	 * @param weightMatrix
	 * @param alphabet
	 * @param index
	 * @return le vector des poids posibles pour un noeud en fonction 
	 * 			des caracteres de l'alphabet utilisé
	 */
	private static int[] getWeightsVector(Node node,
			int[][] weightMatrix, String alphabet, int index) {
		
		if(node.getLeftChild() == null){
			return getLeafVector(node, alphabet, index);
		}
		
		int weightVector_LeftChild[] = getWeightsVector(node.getLeftChild(), 
				weightMatrix, alphabet, index);
		int weightVector_RightChild[] = getWeightsVector(node.getRightChild(),
				weightMatrix, alphabet, index);
		
		// anulation des position avec "-"  dans au moins une sequence
		if (weightVector_LeftChild[0]==-1 || weightVector_RightChild[0] ==-1){
			weightVector_LeftChild[0] = -1;
			return weightVector_LeftChild;
		}
		
		return getWeightVectorCurrent(weightVector_LeftChild, 
				weightVector_RightChild, weightMatrix);
	}


	/**
	 * @param weightVector_LeftChild
	 * @param weightVector_RightChild
	 * @param weightMatrix
	 * @return le vector des poids possibles pour un noeud en fonction des
	 *  deux enfants et du cout d'aller du noeud currant aux enfants
	 */
	private static int[] getWeightVectorCurrent(int[] weightVector_LeftChild,
			int[] weightVector_RightChild, int[][] weightMatrix) {

		int [] currentVector = new int[weightVector_LeftChild.length];
		
		for(int i = 0 ; i < currentVector.length ; i++)
			currentVector[i] = getMinCurrentCharacter(i, weightVector_LeftChild, weightMatrix)
			+ getMinCurrentCharacter(i, weightVector_RightChild, weightMatrix);
			
		return currentVector;
	}


	/**
	 * @param alphabetIndex
	 * @param weightVector
	 * @param weightMatrix
	 * @return La poids minimum pour l'utilisation d'une caracter dans ne noeud
	 * 		courant
	 */
	private static int getMinCurrentCharacter(int alphabetIndex, int[] weightVector, 
			int [][]weightMatrix) {
		
		int toMinimize[] = new int[weightVector.length];
		
		for(int i = 0; i < weightVector.length ; i++){
			// Ne pas addicionner sur:  Integer.MAX_VALUE;
			if(weightVector[i] !=  Integer.MAX_VALUE){
				toMinimize[i] = weightVector[i] + weightMatrix[alphabetIndex][i];
			}
			else{
				toMinimize[i] = weightVector[i];
			}
		}
		
		return getMin(toMinimize);
	}


	/**
	 * @param node
	 * @param alphabet
	 * @param index
	 * @return un vector de poids d'une feuille
	 */
	private static int[] getLeafVector(Node node, String alphabet,
			int index) {
		
		int  weightVector[] = new int[alphabet.length()];
		String currentCharacter = node.getSequence().substring(index, index+1);
			
		for(int i  = 0 ; i < alphabet.length() ; i++)
			weightVector[i] = (int)Integer.MAX_VALUE;		
		
		// anulation des position avec "-"  dans au moins une sequence
		if(alphabet.indexOf(currentCharacter) == -1)
			weightVector[0] = -1;
		else
			weightVector[alphabet.indexOf(currentCharacter)] = 0;
		
		return weightVector;
	}



	/**
	 * @param weightsVector
	 * @return la valeur minimum dans une vector
	 */
	private static int getMin(int[] weightsVector) {
		int min = (int)Integer.MAX_VALUE;
		
		for(int i = 0 ; i < weightsVector.length ; i++)
			if(weightsVector[i] < min)
				min = weightsVector[i];
		
		return min;
	}

}
