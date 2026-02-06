//arquivo de implementação da classe FloorTile
//por enquanto essa classe não tem nada que a torne diferente, ela existe apenas por questões de boas práticas e organização

package com.dirtyCode.world;//pacotes

//importação de recursos do java
import java.awt.image.BufferedImage;

//codigo da classe FloorTile
public class FloorTile extends Tile{//herda da classe Tile

	//contrutor com base no Super que vem de Tile.java
	public FloorTile(int x, int y, BufferedImage sprite) {
		super(x, y, sprite);
	}

}
