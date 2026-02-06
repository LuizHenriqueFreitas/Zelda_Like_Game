//arquivo de implementação da classe Enemy

package com.dirtyCode.entities;//pacotes

//importando recursos do java
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

//importando rescusos do proprio projeto
import com.dirtyCode.main.Game;
import com.dirtyCode.world.Camera;
import com.dirtyCode.world.Camera;
import com.dirtyCode.world.World;

//codigo da classe Enemy
public class Enemy extends Entity{//herda da classe Entity

	//atributos gerais dos inimigos
	private double speed = 1;//variavel que controla a velocidade de movimento dos inimigos
	private int life = 10; //variavel que controla o valor de vida dos inimigos
	
	//variaveis de controle da mascara de hitbox
	private int maskx = 0, masky = 0, maskw = 16, maskh =16;
	
	//declaração de variaveis de controle das animações
	private int frames = 0, maxFrames = 5, index = 0, maxIndex = 1; //parametros de animação
	private boolean isDamaged = false; //informa se o inimigo tomou dano
	private int damagedFrames = 10, damageCurrent = 0; //variaveis para controle da animação do feedback de dano
	private BufferedImage[] sprites;//armazena os aprites de animação do inimigos
	
	//contrutor com base no Super da classe pai Entity.java
	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);//chama o construtor da classe pai
		sprites = new BufferedImage[2];//instancia o tamanho da variavel sprite como um array de 2 posições
		sprites[0] = Game.spritesheet.getSprite(64, 48, World.TILE_SIZE, World.TILE_SIZE);//aloca sprite 1 da animação do inimigo
		sprites[1] = Game.spritesheet.getSprite(64, 64, World.TILE_SIZE, World.TILE_SIZE);//aloca sprite 2 da animação do inimigo
	}

	//metodo de update do inimigo - o que tem rodando aqui é um algoritmo de perseguição ao player
	public void tick() {
		if(this.isColiddingWithPlayer() == false) {
		//caso x do inimigo < que o x do player E NAO haja colisão, move o inimigo para a direita
		if((int)x < Game.player.getX() && World.isFree((int)(x+speed), this.getY()) &&
				!isColidding((int)(x+speed), this.getY()))
			x+=speed;
		//caso x do inimigo > que o x do player E NAO haja colisão, move o inimigo para a esquerda
		else if((int)x > Game.player.getX() && World.isFree((int)(x-speed), this.getY()) &&
				!isColidding((int)(x-speed), this.getY()))
			x-=speed;
		//caso y do inimigo < que o y do player E NAO haja colisão, move o inimigo para baixo
		if((int)y < Game.player.getY() && World.isFree(this.getX(), (int)(y+speed)) &&
				!isColidding(this.getX(), (int)(y+speed)))
			y+=speed;
		//caso y do inimigo > que o y do player E NAO haja colisão, move o inimigo para cima
		else if((int)y > Game.player.getY() && World.isFree(this.getX(), (int)(y-speed)) &&
				!isColidding(this.getX(), (int)(y-speed)))
			y-=speed;
		}else {//sistema de calculo de dano do player ao encostar num inimigo
			if(Game.rand.nextInt(100) < 10){//calcula 10% de chance
				Game.player.life -= Game.rand.nextInt(3);//dano randomico de no maximo 3
				Game.player.isDamaged = true; //player sofreu dano
				if(Game.player.life <= 0) //caso a vida do player fique zerada
					System.out.println("DERROTA"); //debug 
			}
		}
		
		//script da animação dos inimigos
		frames++;//incrementa o contador de frames
		//caso chegue no maximo de frames
		if(frames == maxFrames) {
			frames = 0;//zera o contador
			index++;//incrementa o index da animação
			//caso index seja maior que o index maximo
			if(index > maxIndex)
				index = 0;//zera o contador
		}
		
		//chama a função de verificação para colisão com balas
		isColiddinBullet();
		
		//verifica se a vida do inimigo é menor ou igual a zero
		if(life <= 0) {
			//caso seja, chama o metodo "destroySelf();"
			destroySelf();
			return;//retorna
		}
		
		//caso o inimigo esteja sob dano - controle da animação de feddback
		if(isDamaged) {
			this.damageCurrent++;//incrementa o "damageCurrent" desta instancia de inimigo
			if(this.damageCurrent >= this.damagedFrames) { // caso o dano corrente seja maior ou igual ao frame de dano
				this.damageCurrent = 0; //damageCurrent é zerado
				this.isDamaged = false; //isDamage é resetado e colocado como falso
			}
		}
	}
	
	//metodo de autodestrução
	public void destroySelf() {
		Game.enemies.remove(this);//destroi esta instancia de inimigo pela lista de inimgos
		Game.entities.remove(this);//destroi esta instancia de inimigo pela lista de entidades
	}
	
	//meotodo que verifica colisão desta instancia de inimigo com o player
	public boolean isColiddingWithPlayer() {
		//cria uma hitbox invisivel na forma de um retangulo
		Rectangle enemyCurrent = new Rectangle(this.getX() + maskx, this.getY() + masky, maskw, maskh);// com esses parametros a hitbox cobre o tamnho do sprite 16x16 do inimigo
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), maskw, maskh);// com esses parametros a hitbox cobre o tamnho do sprite 16x16 do player
		//verifica se existe colisao entre o inimigo e o player
		return enemyCurrent.intersects(player);
	}
	
	//metodo que verifica a colisão desta instancia de inimigo com bullets
	public void isColiddinBullet() {
		for(int i = 0; i < Game.bullets.size(); i++) {//esse laço percorre toda a lista de bullets até o fim
			Entity e = Game.bullets.get(i); //variavel "e" 'aponta' para a bullet atual do laço
			if(Entity.isColidding(this, e)) { // caso esta instancia de inimigo esteja colidindo com a bullet atual do laço
				isDamaged = true; //inimigo passa a estar sob dano
				life--; //decrementa a vida desta instancia de inimigo
				Game.bullets.remove(e); //destroi a bala por meio da variavel e
				return;// retorna
			}
		}
	}
	
	//metodo de verificação de colisões entre inimigos
	public boolean isColidding(int xNext, int yNext) {
		//cria uma hitbox invisivel na forma de um retangulo
		Rectangle enemyCurrent = new Rectangle(xNext + maskx, yNext + masky, maskw, maskh);// com esses parametros a hitbox cobre o tamnho do sprite 16x16 do inimigo
		//esse laço percorre toda a lista de inimigos até o fim
		for(int i = 0; i < Game.enemies.size(); i++) {
			//varivavel local "e" seleciona o inimigo ativo no laço
			Enemy e = Game.enemies.get(i);
			//caso o inimigo ativo no momento seja si mesmo, continue o laço diretamente para o proximo
			if(e == this)
				continue;
			//senão, instancia um segundo retangulo com base no inimigo ativo "e"
			Rectangle targetEnemy = new Rectangle(e.getX() + maskx, e.getY() + masky, maskw, maskh);
			//verifica se existe intersecção entre os 2 retangulos - caso sim retorna true
			if(enemyCurrent.intersects(targetEnemy)) {
				return true;
			}
		}
		//caso nao colida com nenhum inimigo retorna falso
		return false;
	}
	
	//metodo de renderização de inimigos
	public void render(Graphics g) {
		if(!isDamaged) {//caso NAO esteja sob dano
			g.drawImage(sprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);//desenha a animação comum do inimigo
		}else {//caso esteja sob dano
			g.drawImage(Entity.ENEMY_FEEDBACK, this.getX() - Camera.x, this.getY() - Camera.y, null);//desenha o feddback de dano
		}
		
		//as 2 linhas abaixo representam codigo de deBug que desenha na tela os retangulos invisíveis das hitboxes
		//g.setColor(Color.BLUE);
		//g.fillRect(this.getX() + maskx - Camera.x, this.getY() + masky - Camera.y, maskw, maskh);
	}
}
