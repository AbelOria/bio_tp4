package sankoff;

/**
* @author Abel Oria Echevarria
* @author Lionel Larifla
*
*/

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Tp4 {

	//Optenu a partir de la fonction getSequencesMap
	private static int sequenceSize;

	private static String alphabet = "ARNDCQEGHILKMFPSTWYVBZX";
	private static int weightMatrix[][] = 
		{
		{15, 19, 17, 17, 19, 17, 17, 16, 18, 18, 19, 18, 18, 20, 16, 16, 16, 23, 20, 17, 17, 17, 17},
		{19, 11, 17, 18, 21, 16, 18, 20, 15, 19, 20, 14, 17, 21, 17, 17, 18, 15, 21, 19, 18, 17, 18},
		{17, 17, 15, 15, 21, 16, 16, 17, 15, 19, 20, 16, 19, 20, 17, 16, 17, 21, 19, 19, 15, 16, 17},
		{17, 18, 15, 13, 22, 15, 14, 16, 16, 19, 21, 17, 20, 23, 18, 17, 17, 24, 21, 19, 14, 14, 18},
		{19, 21, 21, 22,  5, 22, 22, 20, 20, 19, 23, 22, 22, 21, 20, 17, 19, 25, 17, 19, 21, 22, 20},
		{17, 16, 16, 15, 22, 13, 15, 18, 14, 19, 19, 16, 18, 22, 17, 18, 18, 22, 21, 19, 16, 14, 18},
		{17, 18, 16, 14, 22, 15, 13, 17, 16, 19, 20, 17, 19, 22, 18, 17, 17, 24, 21, 19, 14, 14, 18},
		{16, 20, 17, 16, 20, 18, 17, 12, 19, 20, 21, 19, 20, 22, 17, 16, 17, 24, 22, 18, 17, 17, 18},
		{18, 15, 15, 16, 20, 14, 16, 19, 11, 19, 19, 17, 19, 19, 17, 18, 18, 20, 17, 19, 16, 15, 18},
		{18, 19, 19, 19, 19, 19, 19, 20, 19, 12, 15, 19, 15, 16, 19, 18, 17, 22, 18, 13, 19, 19, 18},
		{19, 20, 20, 21, 23, 19, 20, 21, 19, 15, 11, 20, 13, 15, 20, 20, 19, 19, 18, 15, 20, 20, 18},
		{18, 14, 16, 17, 22, 16, 17, 19, 17, 19, 20, 12, 17, 22, 18, 17, 17, 20, 21, 19, 16, 17, 18},
		{18, 17, 19, 20, 22, 18, 19, 20, 19, 15, 13, 17, 11, 17, 19, 19, 18, 21, 19, 15, 19, 19, 18},
		{20, 21, 20, 23, 21, 22, 22, 22, 19, 16, 15, 22, 17,  8, 22, 20, 20, 17, 10, 18, 21, 22, 19},
		{16, 17, 17, 18, 20, 17, 18, 17, 17, 19, 20, 18, 19, 22, 11, 16, 17, 23, 22, 18, 18, 17, 18},
		{16, 17, 16, 17, 17, 18, 17, 16, 18, 18, 20, 17, 19, 20, 16, 15, 16, 19, 20, 18, 17, 17, 17},
		{16, 18, 17, 17, 19, 18, 17, 17, 18, 17, 19, 17, 18, 20, 17, 16, 14, 22, 20, 17, 17, 18, 17},
		{23, 15, 21, 24, 25, 22, 24, 24, 20, 22, 19, 20, 21, 17, 23, 19, 22,  0, 17, 23, 22, 23, 21},
		{20, 21, 19, 21, 17, 21, 21, 22, 17, 18, 18, 21, 19, 10, 22, 20, 20, 17,  7, 19, 20, 21, 19},
		{17, 19, 19, 19, 19, 19, 19, 18, 19, 13, 15, 19, 15, 18, 18, 18, 17, 23, 19, 13, 19, 19, 18},
		{17, 18, 15, 14, 21, 16, 14, 17, 16, 19, 20, 16, 19, 21, 18, 17, 17, 22, 20, 19, 14, 15, 18},
		{17, 17, 16, 14, 22, 14, 14, 17, 15, 19, 20, 17, 19, 22, 17, 17, 18, 23, 21, 19, 15, 14, 18},
		{17, 18, 17, 18, 20, 18, 18, 18, 18, 18, 18, 18, 18, 19, 18, 17, 17, 21, 19, 18, 18, 18, 18}
		};


	public static void main(String[] arg){

		String path = System.getProperty("user.dir" );

//		String addressNewick = path+ "\\src\\sankoff\\arbres.newick";
//		String addressFasta = path+"\\src\\sankoff\\proteines.fa";

		String addressNewick = path+ "\\src\\sankoff\\"+arg[1];
		String addressFasta = path+"\\src\\sankoff\\"+arg[0];

		String sequences= readFile(addressFasta);
		Map<String, String> sequencesMap = getSequencesMap(sequences);		

		String newicks = readFile(addressNewick);				
		String [] newicksTable = newicks.split("\n");

		Tree []trees = new Tree[newicksTable.length];

		for(int i = 0 ; i < newicksTable.length ; i++){
			trees[i] = new Tree(newicksTable[i], weightMatrix, alphabet, 
					sequencesMap, sequenceSize);
			trees[i].print();
		}

		int arbreChoisit= 0;
		int poidsMinimum = Integer.MAX_VALUE;

		System.out.println("Poids de chaque arbre");
		for(int i =0; i < trees.length ; i++){
			int weight = trees[i].getWeight();

			if(weight < poidsMinimum){
				poidsMinimum = weight;
				arbreChoisit = i+1;  
			}

			int arbreCourant = i+1;
			System.out.println("Arbre "+ arbreCourant +" : " + weight );
		}

		System.out.println("Arbre choisit:  Arbre "+arbreChoisit);
	}


	/**
	 * Transformation d'une String en format fasta aligné en Map<String, String>
	 * @param sequences
	 * @return Map,  key = nom de la sequence,  value = sequence
	 */
	private static Map<String, String> getSequencesMap(String sequences) {
		Map<String, String> sequencesMap = new HashMap<String, String>();

		String[] sequencesTable = sequences.split("\n");

		String key = "";
		String sequence = "";

		int i = 0;
		boolean next = true;		
		while(next){
			if(!sequencesTable[i+1].substring(0,1).equals(">"))
				i++;
			else
				next = false;
		}

		int j = 0;
		while(j < sequencesTable.length){
			String first = sequencesTable[j].substring(0,1);

			if(first.equals(">")){
				key = sequencesTable[j].substring(1);
				j++;
			}
			int k = 0;
			while(k < i){
				sequence = sequence + sequencesTable[j];
				k++;
				j++;
			}
			sequencesMap.put(key, sequence);
			sequenceSize = sequence.length();
			key = "";
			sequence = "";
		}

		return sequencesMap;
	}


	/**
	 * Lire un fichier et le transformer en String
	 * @param adresse
	 * @return String contenat le texte du fichier passe en parametre
	 */
	private static String readFile(String adresse) {

		String texte = "";

		try{
			InputStream is =  new FileInputStream(adresse);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String ligne = "";  
			while((ligne = br.readLine())!=null){
				texte += ligne +"\n";
			}
		}catch(IOException e){System.err.println("File non trouve");
		texte = "Fichier non trouve";
		}		
		return texte;
	}
}
