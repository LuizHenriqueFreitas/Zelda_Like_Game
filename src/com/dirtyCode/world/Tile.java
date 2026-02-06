//arquivo de implementação da classe generica Tile

package com.dirtyCode.world;//pacotes

//importação de recursos do java
import java.awt.Graphics;
import java.awt.image.BufferedImage;

//importando recursos criados no projeto
import com.dirtyCode.main.Game;

//Codigo da classe Tile
public class Tile {

	//declaração das sprites padrao para FLOOR e WALL em modo estático para facilitar acesso em outros scripts
	public static BufferedImage TILE_FLOOR = Game.spritesheet.getSprite(16, 32, 16, 16);
	public static BufferedImage TILE_WALL = Game.spritesheet.getSprite(32, 32, 16, 16);
	
	//declaração das variaveis internas
	private BufferedImage sprite;
	private int x, y;
	
	//metodo construtor simples
	public Tile(int x, int y, BufferedImage sprite){
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}
	
	//metodo de renderização de tiles
	public void render(Graphics g) {
		//os recursos de "- Camera.x" e "- Camera.y" são usados para balancear com a camera inteligente que segue o personagem
		g.drawImage(sprite, x  - Camera.x, y  - Camera.y, null);
	}
}
