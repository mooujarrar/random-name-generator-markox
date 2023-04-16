package name_generator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class Trainer {
	
    private Set<String> names; // Ensemble de noms, on utilise un Set pour s'assurer de l'unicité
    private ArrayList<Character> characters; // Ensemble des characteres a utiliser pour la matrice de transformation
    private Integer[][] transformationMatrix; // Matrice de transformation
    private Float[][] normalizedTransformationMatrix; // Matrice de transformation normalisée
    
	public Trainer(String fileName) {
		names = new LinkedHashSet<>();
		String fileName_ = fileName;
		// Si l'utilisateur 
		if(fileName_ == "") {
			fileName_ = "names.txt";
		}
		try {
			 // Charger les noms à partir d'un fichier
			this.loadNamesFromFile(fileName_);
		} catch (IOException e) {
			e.printStackTrace();
			// On quitte le programme immediatemment s'il ya une erreur pour acceder au fichier des noms
			System.exit(0);
		}
		
		/*
		 *  Pas d'exception levée, on commence l'entrainement:
		 *  	1- Extraction des occurences uniques des characteres presentes dans tout les noms
		 *  	2- Remplissage de la matrice de transformation
		 *  	3- Normalization de la matrice de transformation
		 */
	
		System.out.println("Names are " + this.names);
		
		this.extractCharacters();
		System.out.println("Characters are " + this.characters);
		System.out.println();
		
		this.fillTransformationMatrix();
		System.out.println("**** Transformation Matrix is *****");
		printMatrix(this.transformationMatrix);
        System.out.println();
        
        this.normalizeColumns();
        System.out.println("**** Normalized Matrix is *****");
		printMatrix(this.normalizedTransformationMatrix);
    }
	
	/**
	 * Getter of the normalized transformation matrix, this will be used in the Generator class to generate random names
	 * @return The transformation matrix
	 */
	public Float[][] getNormalizedTransformationMatrix() {
		return this.normalizedTransformationMatrix;
	}
	
	/**
	 * Getter de la list des characters, utilisée pour la generation des noms aléatoires
	 * @return la liste des characteres
	 */
	public ArrayList<Character> getCharacterList() {
		return this.characters;
	}
	
	/**
	 * Charger une liste de noms à partir d'un fichier
	 * @param fileName nom/path du fichier texte d'entrée
	 * @throws IOException Si la lecture du fichier échoue à cause d'une raison ou une autre (cf. fichier introuvable):
	 * 					   Une exception va être levée
	 */
    public void loadNamesFromFile(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] nameList = line.split("\\s+"); // Diviser les noms par espace
            for (String name : nameList) {
                names.add(name);
            }
        }
        reader.close();
    }
    
    /**
	 * Cette méthode sert à parcourir toute la liste des noms et éxtraire l'uniques occurences des characteres sur la globalité de la liste
	 * Exemple: entrée [simon, tim, emile, lin] => resultat: ['s', 'i', 'm', 'o', 'n', 't', 'e', 'l']
	 */
	public void extractCharacters() {
		// On crée un Set pour assure l'unicité des characters dans la matrice
		Set<Character> charactersSet = new LinkedHashSet<>();
		for (String name : names) {
			// Traverse the string
	        for (int i = 0; i < name.length(); i++) {
	        	charactersSet.add(name.charAt(i));
	        }
		}
		// Enfin on stocke les characters dans une liste, pour un accés facile a tout les elements par la suite
		characters = new ArrayList<>(charactersSet);
	}
	
	/**
	 * Population de la matrice de transformation par le nombre d'occurence de deux characteres suivient dans chaque mot
	 * Comme décrit dans l'exercice, ça prends en compte le rien du début et de la fin du mot.
	 */
	public void fillTransformationMatrix() {
		int charSetSize = characters.size();
		transformationMatrix = new Integer[charSetSize + 1][charSetSize + 1];
		for (Integer[] row: transformationMatrix)
		    Arrays.fill(row, 0);
		for (String name : names) {
			// Traverse the string
			int i = 0;
			int j = 1;
			while (j < name.length()) {
        		int indexI = characters.indexOf(name.charAt(i));
        		int indexJ = characters.indexOf(name.charAt(j));
				if(i == 0) {
					transformationMatrix[indexI + 1][0] += 1; 
				}
				transformationMatrix[indexJ + 1][indexI + 1] += 1;
				if(j == name.length() - 1) {
					transformationMatrix[0][indexJ + 1] += 1; 
				}
				i++;
				j++;
			}
		}
	}
	
	/**
	 * Normalisation de la matrice de transformation
	 */
	 public void normalizeColumns() {
	        int rows = transformationMatrix.length;
	        int cols = transformationMatrix[0].length;
	        
	        // Allouer statiquement la memoire pour la matrice normalisée
	        normalizedTransformationMatrix = new Float[rows][cols];
	        
	        // Calculer la somme de chaque colonne
	        int[] colSums = new int[cols];
	        for (int j = 0; j < cols; j++) {
	            for (int i = 0; i < rows; i++) {
	                colSums[j] += transformationMatrix[i][j];
	            }
	        }

	        // Normaliser chaque colonne: diviser la case par la somme de la colonne
	        for (int j = 0; j < cols; j++) {
	            for (int i = 0; i < rows; i++) {
	            	normalizedTransformationMatrix[i][j] = (float) transformationMatrix[i][j] / colSums[j];
	            }
	        }
	 }
	 
		
	 /**
	  * Méthode static générique pour pouvoir écrire dans la console les matrices de tout type Integer ou Float
	  * @param <T> Type de la matrice Integer pour la matrice de transformation, Float le cas de la matrice noramlisée
	  * @param matrix la matrice à écrire
	  */
	public static <T> void printMatrix(T[][] matrix) {
		for (int i = 0; i < matrix.length; i++) { //this equals to the row in our matrix.
	         for (int j = 0; j < matrix[i].length; j++) { //this equals to the column in each row.
	            System.out.print(matrix[i][j] + " ");
	         }
	         System.out.println(); //change line on console as row comes to end in the matrix.
      }
	}
}
