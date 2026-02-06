//arquivo de implementação da classe Weapon
//por enquanto essa classe não tem nada que a torne diferente, ela existe apenas por questões de boas práticas e organização

package com.dirtyCode.entities;//pacotes

//importando recursos do java
import java.awt.image.BufferedImage;

//codigo da classe Weapon
public class Weapon extends Entity{//herda da classe Entity

	//contrutor com base no Super da classe pai Entity.java
	public Weapon(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
	}

}
