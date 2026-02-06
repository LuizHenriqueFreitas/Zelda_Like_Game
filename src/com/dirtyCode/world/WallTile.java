//arquivo de implementação da classe WallTile
//por enquanto essa classe não tem nada que a torne diferente, ela existe apenas por questões de boas práticas e organização

package com.dirtyCode.world;//pacotes

//recursos do java
import java.awt.image.BufferedImage;

//codigo da classe WallTile
public class WallTile extends Tile{//herda da classe Tile

	//metodo construtor baseado no Super de Tile.java
	public WallTile(int x, int y, BufferedImage sprite) {
		super(x, y, sprite);
	}

}
