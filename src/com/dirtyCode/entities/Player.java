//arquivo de implementação da classe player

package com.dirtyCode.entities;//pacotes

//importando recursos do java
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.dirtyCode.graphics.Spritesheet;
//importando recursos do proprio projeto
import com.dirtyCode.main.Game;
import com.dirtyCode.world.Camera;
import com.dirtyCode.world.World;

//codigo da classe Player
public class Player extends Entity{//herda da classe Entity
	
	//declarando variaveis de controle;
	public boolean moved = false; //verifica se moveu
	public boolean right, up, left, down; //verifica direção
	public int rightDir = 0, leftDir = 1, upDir = 2, downDir = 3; //seletores de animação
	public int dir = rightDir; //seletor de direção
	public double speed = 1.5; //velocidade de movimentação do player
	public double life = 100, maxLife = 100; //variaveis de controle da vida do player
	public int ammo = 0; //variaveis de controle da munição do player
	public boolean isDamaged = false; //variavel de controle, verifica se o player tomou dano ou não
	private int damagedFrames = 0; //variavel de controle para o feedback de dano
	private boolean hasGun; //variavel de controle, verifica se o player tem uma arma
	public boolean shoot = false; //verifica se o player esta atirando - TECLADO
	public boolean mouseShoot = false; //verifica se o player esta atirando - MOUSE
	public int mx, my; //variaveis de controle para encontrar a posição do mouse no canvas
	
	//declaração de variaveis de controle das animações
	private int frames = 0, maxFrames = 5, index = 0, maxIndex = 3; //parametros de animação
	private BufferedImage[] playerWalkRight; //carrega a animação de andar para a direita
	private BufferedImage[] playerWalkLeft;	//carrega a animação de andar para a esquerda
	private BufferedImage[] playerWalkUp; //carrega a animação de andar para cima
	private BufferedImage[] playerWalkDown; //carrega a animação de andar para baixo
	private BufferedImage playerDamaged; //carrega a animação de feedback de dano
	
	//metodo construtor
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);//variavis comuns declaradas pelo super da classe Entity
		
		//instancia os Arrays de BufferedImages para cara uma das 4 animações
		playerWalkRight = new BufferedImage[4]; //animação com 4 sprites
		playerWalkLeft = new BufferedImage[4]; //animação com 4 sprites
		playerWalkUp = new BufferedImage[3]; //animação com 3 sprites
		playerWalkDown = new BufferedImage[3]; //animação com 3 sprites
		playerDamaged = Game.spritesheet.getSprite(0, 64, 16, 16); //seleciona o sprite de feedback de dano direto da spritesheet
		
		//faz a alocação do recorte de cada sprite da spritesheet de acordo com o frame de cada animação - feito com o uso de laços for
		for(int i = 0; i < 4; i++)
			playerWalkRight[i] = Game.spritesheet.getSprite(16 + (i * 16), 0, 16, 16);
		for(int i = 0; i < 4; i++)
			playerWalkLeft[i] = Game.spritesheet.getSprite(0 + (i * 16), 16*3, 16, 16);
		for(int i = 0; i < 3; i++)
			playerWalkUp[i] = Game.spritesheet.getSprite(16 + (i * 16), 16, 16, 16);
		for(int i = 0; i < 3; i++)
			playerWalkDown[i] = Game.spritesheet.getSprite(0, 0 + (i * 16), 16, 16);	
	}

	//metodo de update do player
	public void tick() {
		moved = false;//comeca com o movimento em falso
		
		//caso direita seja verdadeiro E tenha caminho livre
		if(right && World.isFree((int)(x+speed), this.getY())) {
			moved = true; //moveu-se fica verdadeiro
			dir = rightDir; //direção = direita
			x += speed; //move o player de fato
		}
		//caso esquerda seja verdadeiro E tenha caminho livre
		else if(left && World.isFree((int)(x-speed), this.getY())) {
			moved = true; //moveu-se fica verdadeiro
			dir = leftDir; //direção = esquerda
			x -= speed; //move o player de fato
		}
		//caso pra cima seja verdadeiro E tenha caminho livre
		if(up && World.isFree(this.getX(), (int)(y-speed))) {
			moved = true; //moveu-se fica verdadeiro
			dir = upDir; //direção = cima
			y -= speed; //move o player de fato
		}
		//caso pra baixo seja verdadeiro E tenha caminho livre
		else if(down && World.isFree(this.getX(), (int)(y+speed))) {
			moved = true; //moveu-se fica verdadeiro
			dir = downDir; //direção = baixo
			y += speed; //move o player de fato
		}
		//caso moveu seja verdadeiro
		if(moved) {
			frames++;//incrementa o contador de frames
			//caso chegue no maximo de frames
			if(frames == maxFrames) {
				frames = 0;//zera o contador
				index++;//incrementa o index da animação
				//caso mova na horizontal
				if(dir == rightDir || dir == leftDir)
					maxIndex = 3;//index maximo = 3
				else//caso mova na vertical
					maxIndex = 2;//index maximo =2
				//esse controle de index maximo existe pois as animações de andar na vertical estao com apenas 3 sprites enquanto as da horizontal tem 4 sprites
				//caso index seja maior que o index maximo
				if(index > maxIndex-1)
					index = 0;//zera o contador
			}
		}
		//metodos de verificação de colisão com itens
		checkCollisionLifePack();
		checkCollisionBullet();
		checkCollisionWeapon();
		
		//caso tenha sofrido dano
		if (isDamaged) {
			this.damagedFrames++;//conta os frames do feedback visual
			if(this.damagedFrames == 8) {//limite de 8 frames para a animação visual
				this.damagedFrames = 0;//zera o contador
				isDamaged = false;//limpa o estado de "tomou dano"
			}
		}
		
		//caso o player tenha atirado - TECLADO
		if(shoot) {
			shoot = false;//torna "shoot" falso, para evitar spam
			//caso player tenha uma arma e mais de 0 munições
			if(hasGun && (ammo > 0)) {
				ammo--;//decrementa 1 munição a cada tiro
				//variáveis de direcionamento da bala
				int dx = 0;
				int dy = 0;
				//variáveis de offset, para ajuste visual
				int px = 0;
				int py = 0;
				//caso atire para direita
				if(dir == rightDir) {
					px = 16;
					py = 3;
					dx = 1;//faz a bala ir para direita
				}else if(dir == leftDir){//caso atire para esquerda
					px = -6;
					py = 3;
					dx = -1;//faz a bala ir para esquerda
				}else if(dir == upDir) {//caso atire para cima
					px = 4;
					py = -4;
					dy = -1;//faz a bala ir para cima
				}else {//caso atire para baixo
					px = 4;
					py = 12;
					dy = 1;//faz a bala ir para baixo
				}
				
				//instancia uma nova bala
				BulletShoot bullet = new BulletShoot(this.getX() + px, this.getY() + py, 3, 3, null, dx, dy);
				Game.bullets.add(bullet);//adiciona a nova bala na lista de bullets
			}
		}
		
		//caso player tenha atirado - MOUSE
		if(mouseShoot) {
			mouseShoot = false;//torna "mouseShoot" falso, para evitar spam
			//caso player tenha uma arma e mais de 0 munições
			if( hasGun && (ammo > 0)) {
				ammo--;//decrementa 1 munição a cada tiro
				//variáveis de offset, para ajuste visual
				int px = 0;
				int py = 0;
				//variável de direcionamento da bala
				double angle = 0;
				//configurando offsets de acordo com a direção do player
				if(dir == rightDir) {//caso player esteja virado para direita
					px = 16;
					py = 3;
				}else if(dir == leftDir){ //caso player esteja virado para esquerda
					px = -6;
					py = 3;
				}else if(dir == upDir) { //caso player esteja virado para cima
					px = 4;
					py = -4;
				}else { //caso player esteja virado para baixo
					px = 4;
					py = 12;
				}
				//calcula o angulo do mouse em relação ao player
				angle = Math.atan2(my - this.getY(), mx - this.getX());
				double dx = Math.cos(angle);//dx recebe o valor do cosseno de "angle"
				double dy = Math.sin(angle);//dy recene o valor do seno de "angle"
				
				//instancia uma nova bala
				BulletShoot bullet = new BulletShoot(this.getX() + px, this.getY() + py, 3, 3, null, dx, dy);
				Game.bullets.add(bullet);//adiciona a nova bala na lista de bullets
			}
		}
		
		//caso a vida seja igual ou menor que 0
		if(life <= 0) {//reinicia o mundo - é literalmente o mesmo codigo presente na classe Game
			life = 0; //coloca a vida do jogador sempre igual a zero para que ele não fique com vida negativa na UI apos morrer
			Game.gameState = "GAME_OVER"; //tornar o estado de jogo em "GAME_OVER"
		}
		
		//chama o metodo de update da camera
		updateCamera();
	}
	
	//metodo de update da camera
	public void updateCamera() {
		/*
		  configura os eixos da camera para seguirem o player
		  usando o metodo clamp, o x do plauer - metade da tela se torna o atual,
		  enquanto 0 é o minimo,
		  e o tamnho maximo é o tamanho do world em pixels - o tamnho do game.
		  o mesmo se repete para y
		 */
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2), 0, World.WIDTH*World.TILE_SIZE - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2), 0, World.HEIGHT*World.TILE_SIZE - Game.HEIGHT);
	}
	
	//declaração do metodo de colisão com itens do tipo arma
	public void checkCollisionWeapon() {
		/*
		 Uma ideia interessante é colocar todas as armas, LifePacks ou Munições em suas próprias 
		 listas particulares alem da lista geral de entidades, assim como fizemos com os inimigos.
		 Isso ajuda muito na organização, escalabilidade e modularidade do projeto.
		 */
		for(int i = 0; i < Game.entities.size(); i++) { //roda todas as entidades do jogo
			Entity atual = Game.entities.get(i); //coloca o indice atual do laço na variavel atual do tipo Entity
			if(atual instanceof Weapon) { //verifica se a entidade é do tipo Weapon
				if(Entity.isColidding(this, atual)) { //caso esteja colidindo, verificado pelo metodo "isColidding();" da classe Entity
					hasGun = true; //informa que o player tem a arma em posse
					Game.entities.remove(atual); //destroi a arma do mapa qual o player colidiu
				}
			}
		}
	}
	
	//declaração do metodo de colisão com itens do tipo Munição
	public void checkCollisionBullet() {
		/*
		 Uma ideia interessante é colocar todas as armas, LifePacks ou Munições em suas próprias 
		 listas particulares alem da lista geral de entidades, assim como fizemos com os inimigos.
		 Isso ajuda muito na organização, escalabilidade e modularidade do projeto.
		 */
		for(int i = 0; i < Game.entities.size(); i++) { //roda todas as entidades do jogo
			Entity atual = Game.entities.get(i); //coloca o indice atual do laço na variavel atual do tipo Entity
			if(atual instanceof Bullet) { //verifica se a entidade é do tipo Bullet
				if(Entity.isColidding(this, atual)) { //caso esteja colidindo, verificado pelo metodo "isColidding();" da classe Entity
					Game.player.ammo += 30; //incrementa +10 unidades de munição atual do player
					Game.entities.remove(atual); //destroi a munição do mapa qual o player colidiu
				}
			}
		}
	}
	
	//declaração do metodo de colisão com itens do tipo Munição
	public void checkCollisionLifePack() {
		/*
		 Uma ideia interessante é colocar todas as armas, LifePacks ou Munições em suas próprias 
		 listas particulares alem da lista geral de entidades, assim como fizemos com os inimigos.
		 Isso ajuda muito na organização, escalabilidade e modularidade do projeto.
		 */
		for(int i = 0; i < Game.entities.size(); i++) { //roda todas as entidades do jogo
			Entity atual = Game.entities.get(i); //coloca o indice atual do laço na variavel atual do tipo Entity
			if(atual instanceof Lifepack) { //verifica se a entidade é do tipo Lifepack
				if(Entity.isColidding(this, atual)) { //caso esteja colidindo, verificado pelo metodo "isColidding();" da classe Entity
					Game.player.life += 20; //incrementa +20 pontos na vida atual do player
					if(life > 100) //caso vida > 100 
						life = 100; //torna vida = 100 para não passar do limite
					Game.entities.remove(atual); //destroi o Lifepack do mapa qual o player colidiu
				}
			}
		}
	}
	
	//metodo de renderização do player
	public void render(Graphics g) {
		//caso player NAO tenha tomado dano
		if(!isDamaged) {
			//caso a direção seja a direita
			if(dir == rightDir) {
				g.drawImage(playerWalkRight[index], this.getX() - Camera.x, this.getY() - Camera.y, null);//renderiza a animação andando para direita
				//caso player tenha arma
				if(hasGun) {
					//desenha a arma a direita do player
					g.drawImage(Entity.WEAPON_EN, this.getX() + 12 - Camera.x, this.getY() - Camera.y, null);//renderiza a arma na mão do player
				}
			} else if (dir == leftDir) {//caso a direção seja a esquerda
				g.drawImage(playerWalkLeft[index], this.getX() - Camera.x, this.getY() - Camera.y, null);//renderiza a animação andando para esquerda
				//caso player tenha arma
				if(hasGun) {
					//desenha a arma a esquerda do player
					g.drawImage(Entity.WEAPON_EN, this.getX() - 12 - Camera.x, this.getY() - Camera.y, null);//renderiza a arma na mão do player
				}
			} else if (dir == upDir) {//caso a direção seja para cima
				g.drawImage(playerWalkUp[index], this.getX() - Camera.x, this.getY() - Camera.y, null);//renderiza a animação andando para cima
				//caso player tenha arma
				if(hasGun) {
					//desenha a arma acima do player
					g.drawImage(Entity.WEAPON_EN, this.getX() - Camera.x, this.getY() -12 - Camera.y, null);//renderiza a arma na mão do player
				}
			} else if (dir == downDir) {//caso a direção seja para baixo
				g.drawImage(playerWalkDown[index], this.getX() - Camera.x, this.getY() - Camera.y, null);//renderiza a animação andando para baixo
				//caso player tenha arma
				if(hasGun) {
					//desenha a arma abaixo do player
					g.drawImage(Entity.WEAPON_EN, this.getX() - Camera.x, this.getY() + 12 - Camera.y, null);//renderiza a arma na mão do player
				}
			}
		//caso o player tenha tomado dano
		}else {
			g.drawImage(playerDamaged, this.getX() - Camera.x, this.getY() - Camera.y, null);//renderiza a animação do player tomando dano
		}
	}
}
