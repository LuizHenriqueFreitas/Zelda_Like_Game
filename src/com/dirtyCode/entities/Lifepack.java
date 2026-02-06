//arquivo de implementação da classe Lifepack
//por enquanto essa classe não tem nada que a torne diferente, ela existe apenas por questões de boas práticas e organização

package com.dirtyCode.entities;//pacotes

//importando recursos do java
import java.awt.image.BufferedImage;

//codigo da classe Lifepack
public class Lifepack extends Entity{//herda da classe Entity

	//contrutor com base no Super da classe pai Entity.java
	public Lifepack(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
	}

}
