//arquivo de implementação da classe genérica entity

package com.dirtyCode.entities;//pacotes

//iomportando recursos do java
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

//importando recursos do proprio projeto
import com.dirtyCode.main.Game;
import com.dirtyCode.world.Camera;

//cpdigo da classe Entity
public class Entity {
	//declaração das sprites padrao para os 4 tipos de entidades (alem do player) em modo estático(static) para facilitar acesso em outros scripts
	public static BufferedImage LIFEPACK_EN = Game.spritesheet.getSprite(16*4, 16, 16, 16);
	public static BufferedImage BULLET_EN = Game.spritesheet.getSprite(16*4, 32, 16, 16);
	public static BufferedImage WEAPON_EN = Game.spritesheet.getSprite(16*3, 32, 16, 16);
	public static BufferedImage ENEMY_EN = Game.spritesheet.getSprite(16*4, 32+16, 16, 16);
	public static BufferedImage ENEMY_FEEDBACK = Game.spritesheet.getSprite(16*3,16*4, 16, 16);
	
	//declaração das variaveis internas
	protected double x;
	protected double y;
	protected int width;
	protected int height;
	
	//variavel de armazenamento para sprite
	protected BufferedImage sprite; 

	//variaveis de controle para mascara de hitbox da entidade
	private int maskx, masky, maskw, maskh;
	
	//metodo construtor simples
	public Entity(int x, int y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
		
		this.maskx = 0;
		this.masky = 0;
		this.maskw = width;
		this.maskh = height;
	}
	
	//metodo de configuração da mascara
	public void setMask(int maskx, int masky, int maskw, int maskh) {
		this.maskx = maskx;
		this.masky = masky;
		this.maskw = maskw;
		this.maskh = maskh;
	}
	
	//metodos setters para valores de x e y
	public void setX(double newX) {
		this.x = newX;
	}
	
	public void setY(double newY) {
		this.y = newY;
	}
	
	//metodos getters para valores de cada variavel numerica
	public int getX() {
		return (int)this.x;
	}
	
	public int getY() {
		return (int)this.y;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	//metodo tick, por padrao vazio
	public void tick() {
		
	}
	
	//metodo de verificação de colisão
	public static boolean isColidding(Entity e1, Entity e2) {
		//cria uma hitbox invisivel na forma de um retangulo
		Rectangle e1Mask = new Rectangle(e1.getX() + e1.maskx, e1.getY() + e1.masky, e1.maskw, e1.maskh);// com esses parametros a hitbox cobre o tamnho do sprite 16x16 do inimigo
		Rectangle e2Mask = new Rectangle(e2.getX() + e2.maskx, e2.getY() + e2.masky, e2.maskw, e2.maskh);// com esses parametros a hitbox cobre o tamnho do sprite 16x16 do player
		//verifica se há colisão entre uma entidade e a outra
		return e1Mask.intersects(e2Mask);
	}
	
	//metodo de renderização de entidades
	public void render(Graphics g) {
		//os recursos de "- Camera.x" e "- Camera.y" são usados para balancear com a camera inteligente que segue o personagem
		g.drawImage(sprite, this.getX() - Camera.x, this.getY() - Camera.y, null);
	}
}
