//arquivo de implementação da classe NulletShoot

package com.dirtyCode.entities;//pacotes

//importando recursos do java
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

//importando recursos do próprio projeto
import com.dirtyCode.main.Game;
import com.dirtyCode.world.Camera;

//codigo da classe BulletShoot
public class BulletShoot extends Entity { // herda da classe Entity

	//variaveis de controle de movimentação da bullet
	private double dx; //direção em x
	private double dy; //direção em y
	private double speed = 4; //velocidade de deslocamento
	
	//variaveis de controle do tempo de vida da bala
	private int life = 30, curLife = 0;
	
	//metodo construtor
	public BulletShoot(int x, int y, int width, int height, BufferedImage sprite, double dx, double dy) {
		super(x, y, width, height, sprite); //direciona para o construtor de entity
		
		//atribui valores de dx e dy
		this.dx = dx;
		this.dy = dy;
	}
	
	//metodo de update
	public void tick() {
		//calculo de deslocamento em X e Y
		x += dx * speed;
		y += dy * speed;
		curLife++; // incrementa a vida corrente da bala
		if(curLife >= life) { // caso a vida corrente desta instancia de bala seja maior ou igual a vida total de si mesma
			Game.bullets.remove(this); // destroi esta instancia de bala
			return; // retorna
		}
	}
	
	//metodo de render
	public void render(Graphics g) {
		g.setColor(Color.YELLOW); //configura para cor amarela
		g.fillOval(this.getX() - Camera.x , this.getY() - Camera.y, 3, 3); //desenha uma bola apartir dos pontos x e y da bala, calculando offset da camera
	}
}
