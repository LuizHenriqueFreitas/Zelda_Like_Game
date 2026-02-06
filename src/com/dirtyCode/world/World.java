//arquivo de implementação da classe World
//é neste arquivo que grande parte das coisas se juntam, como o player, as texturas e algumas interações

package com.dirtyCode.world;//pacotes

//importação de recursos do java
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

//importação de recursos criados durante o desenvolvimento do proprio projeto
import com.dirtyCode.entities.*; //importa tudas as classes que existem dentro do pacote com.dirtyCode.entities
import com.dirtyCode.graphics.Spritesheet;
import com.dirtyCode.main.Game;

//Codigo da classe World
public class World {
	
	//declação de variaveis locais da classe
	private static Tile[] tiles;//Array dinamico que armazena os tiles
	public static int WIDTH, HEIGHT;//tamanho do mundp, declarado de forma estatica pode ser acessado em qualquer classe ao importar a classe world
	public static final int TILE_SIZE = 16;//constante com o tamanho dos tiles/ sprites
	
	//metodo construtor
	public World(String path) {//recebe o caminho da imagem a ser usada como mapa
		try {
			//variavel local map recebe a imagem do caminho de pastas
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			//array dinamico recebe o numero de pixels da imagem presente em map como tamanho
			int[] pixels = new int[map.getWidth() * map.getHeight()];// esse calculo que dizer que o array pixeçs tem o tamanho de map-horizontal * map-vertical, como um calculo de área de um retângulo
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());//configuração do mapa com base no esquema de cores, sendo (origrmX, origemY, tamnhoX, tamanhoY, ArrayRGB, offset e scansize)
			//utlizados para configuração da camera com clamp
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			//instancia o armazenamento dos tiles no array tiles e define o tamnho do array = a area em pixels do mapa
			tiles = new Tile[map.getWidth() * map.getHeight()];
			//para cada xx, enquanto xx for menor que a largura do mapa, xx incrementa
			for(int xx = 0; xx < map.getWidth(); xx++) {
				//para cada yy, enquanto yy for menor que a 'altura' do mapa, yy incrementa
				for(int yy = 0; yy < map.getHeight(); yy++) {
					//declara variavel local para melhor controle das alterações
					int pixelAtual = pixels[xx + (yy*map.getWidth())];//"pixelAtual" sempre tem o valor do pixel no laço
					//tile sempre começa como se fosse floor
					//tiles[INDEX] || INDEX == xx + (yy * WIDTH) || INDEX == 0 + (0 * 20) por exemplo, que é INDEX = 0
					tiles[xx + (yy * WIDTH)] = new FloorTile(xx*TILE_SIZE, yy*TILE_SIZE, Tile.TILE_FLOOR);//o construtor de FloorTile pede um valor de x, y e um sprite
					//IMPORTANTE - as verificações se cor são feitas por meio de código hexadecimal
					//verifica se a cor do pixel atual é preto - caso verdadeiro, constrio mais piso
					if(pixelAtual == 0xFF000000) {
						//floor
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx*TILE_SIZE, yy*TILE_SIZE, Tile.TILE_FLOOR);
					//verefica se a cor do pixel atual é branco - caso verdadeiro, controi uma parece
					}else if(pixelAtual == 0xFFFFFFFF) {
						//wall
						tiles[xx + (yy * WIDTH)] = new WallTile(xx*TILE_SIZE, yy*TILE_SIZE, Tile.TILE_WALL);//o construtor de WallTile é igual ao de FloorTile, afinal ambos herdam de Tile
					//verifica se a cor do pixel atual é azul - caso verdadeiro desenha a posiçaõ inicial do player
					}else if(pixelAtual == 0xFF002cFF) {
						//player
						//inicializa os parametros de posição espacial do player na janela do jogo com base no pixel que imagem do mapa diz ser onde o player deve spawnar
						Game.player.setX(xx*TILE_SIZE);
						Game.player.setY(yy*TILE_SIZE);
					//verifica se a cor do pixel atual é vermelho - caso verdadeiro, cria um inimigo
					}else if(pixelAtual == 0xFFF70000) {
						//Enemy
						//instancia uma variavel local para que o inimigo possa ser criado e adicionado a 2 diferentes listas - representa melhor organização do código
						Enemy en = new Enemy(xx*TILE_SIZE, yy*TILE_SIZE, 16,16, Entity.ENEMY_EN);
						//adiciona o inimigo na lista geral de entidades
						Game.entities.add(en);
						//adiciona o inimigo na lista geral de inimigos
						Game.enemies.add(en);
					//verifica se a cor do pixel atual é laranja - caso verdadeiro cria uma arma
					}else if(pixelAtual == 0xFFFF8500) {
						//Weapon
						//adiciona a arma na lista geral de entidades
						Game.entities.add(new Weapon(xx*TILE_SIZE, yy*TILE_SIZE, 16,16, Entity.WEAPON_EN));
					//verifica se a cor do pixel atual é verde - caso verdadeiro cria uma cura
					}else if(pixelAtual == 0xFF87F320) {
						//Life Pack
						//adiciona a cura na lista geral de entidades
						Game.entities.add(new Lifepack(xx*TILE_SIZE, yy*TILE_SIZE, 16,16, Entity.LIFEPACK_EN));
					//verifica se a cor do pixel atual é amarelo - caso verdadeiro, cria munição
					}else if(pixelAtual == 0xFFFFF300) {
						//Bullet
						//adiciona a munição na lista geral de entidades
						Game.entities.add(new Bullet(xx*TILE_SIZE, yy*TILE_SIZE, 16,16, Entity.BULLET_EN));
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/*
	  metodo verificador de movimento - calcula se o proximo tile, nas direções: cima, baixo, direita ou esquerda 
	  são instancias de parece, caso seja, não é possivel passar por ele, e há colisão 
	*/
	public static boolean isFree(int xNext, int yNext) {
		//ainda nao entendi como esse calculo funiona, mas ele tem alguma lógica que faz sentido
		int x1 = xNext / TILE_SIZE;
		int y1 = yNext / TILE_SIZE;
		
		int x2 = (xNext + TILE_SIZE-1) / TILE_SIZE;
		int y2 = yNext / TILE_SIZE;
		
		int x3 = xNext / TILE_SIZE;
		int y3 = (yNext + TILE_SIZE-1) / TILE_SIZE;
		
		int x4 = (xNext + TILE_SIZE-1) / TILE_SIZE;
		int y4 = (yNext + TILE_SIZE-1) / TILE_SIZE;
		
		return !((tiles[x1 + (y1*WIDTH)] instanceof WallTile) ||
				(tiles[x2 + (y2*WIDTH)] instanceof WallTile) ||
				(tiles[x3 + (y3*WIDTH)] instanceof WallTile) ||
				(tiles[x4 + (y4*WIDTH)] instanceof WallTile));
	}
	
	//metodo para reiniciar o jogo
	public static void restartGame(String level) {
		Game.entities = new ArrayList<Entity>();
		//atribui um ArrayList do tipo Enemy para a lista enemies
		Game.enemies = new ArrayList<Enemy>();
		//atribui ao objeto spritesheet uma imagem por meio do path: "/spritesheet.png"
		Game.spritesheet = new Spritesheet("/spritesheet.png");
		//atribui valor ao player por meio do construtor
		Game.player = new Player(0,0,16,16,Game.spritesheet.getSprite(0, 0, 16, 16));
		//adiciona o player na lista de entidades
		Game.entities.add(Game.player);
		//atribui ao objeto map com uma imagem de geração por referencia pelo path: "/map.png"
		Game.world = new World("/" + level);
	}
	
	//metodo de renderização da classe World
	public void render(Graphics g) {
		/*
		 Estamos utilizando um modo de renderização otimizado, onde só renderizamos o que esta aparecendo na tela do jogador
		 sendo assim, de maneira pratica, só renderizamos um recorte do mapa. 
		 Controlamos esse recorte com x e y start e final.
		 */
		
		//variareis de controle para o inicio da renderização, basedas nos valores da camera dividido por 16
		int xstart = Camera.x >> 4;// o operador << aparentemente deixa esse calculo muito mais otimizado, mas não sei como ele funciona
		int ystart = Camera.y >> 4;
		//variavel de controle para o fim da renderização, onde são a soma, entre o tamanho da tela do jogo dividido por 16, com o ponto de inicio.
		int xfinal = xstart + (Game.WIDTH >> 4);
		int yfinal = ystart + (Game.HEIGHT >> 4)+1;
		//verifica que enquanto xx começando em xstart seja menor ou igual a xfinal, incrementa
		for(int xx = xstart; xx <= xfinal; xx++) {
			//verifica que enquanto yy começando em ystart seja menor ou igual a yfinal, incrementa
			for(int yy = ystart; yy <= yfinal; yy++) {
				//caso xx ou yy sejam menores que 0, ou maiores que os limites espaciais da classe Wolrd - continue diretamente para o proximo ciclo do laço
				if(xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT)
					continue;
				//cria uma variavel local tile com INDEX = xx + (yy * WIDTH)
				Tile tile = tiles[xx + (yy * WIDTH)]; //semrpe seleciona o tile ativo no momento
				tile.render(g); //renderiza o tile ativo
			}
		}
	}
}
