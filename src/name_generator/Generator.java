package name_generator;

import java.util.ArrayList;

public class Generator {
	
	Trainer trainer;
	
	public Generator(String fileName) {
		trainer = new Trainer(fileName);
    }
	
	
	/**
	 * Generateur d'un nombre aléatoire entre 0 et 1
	 * @return le nombre aléatoire
	 */
	public float generateRandomNumber() {
		float randomNumber = (float) Math.random();
		
        System.out.println("***** Generate random number " + randomNumber + " *****");
		
		return randomNumber;
	}
	
	/**
	 * Generateur du nom aléatoire
	 * @return le nom géneré
	 */
	public String generateRandomName() {
		int columnIndex = 0;
		int rowIndex = 0;
		String generated = "";
		float randomNumber = this.generateRandomNumber();
		ArrayList<Character> characters = this.trainer.getCharacterList();
		Float[][] normalizedTransformationMatrix = this.trainer.getNormalizedTransformationMatrix();
		float sum = normalizedTransformationMatrix[rowIndex][columnIndex];
		boolean endReached = false;
		while(!endReached) {
			while(rowIndex < normalizedTransformationMatrix.length && 
					(
							normalizedTransformationMatrix[rowIndex][columnIndex] == 0 
							|| Math.floor(randomNumber / sum) > 0
					)
			) {
				rowIndex++;
				sum += normalizedTransformationMatrix[rowIndex][columnIndex];
			}
			if(rowIndex == 0) {
				endReached = true;
			} else {
		        System.out.println("***** Random number " + randomNumber + " consists of the letter: " + characters.get(rowIndex - 1) + " *****");
				generated += characters.get(rowIndex - 1);
				columnIndex = rowIndex;
				rowIndex = 0;
				randomNumber = this.generateRandomNumber();
				sum = normalizedTransformationMatrix[rowIndex][columnIndex];
			}
		}
		
		return generated;
	}

}

