//arquivo com implementação da classe Game, onde sera o núcleo do jogo

package com.dirtyCode.main;//pacotes

//importação de recursos do java
import java.awt.Canvas; //importando classe de Canvas
import java.awt.Color; //importação de cores pré configuradas
import java.awt.Dimension; //importando Classe de Dimension
import java.awt.Font; //importação do recurso de fontes
import java.awt.Graphics; //importação da biblioteca "Graphics"
import java.awt.Graphics2D;  //importação da biblioteca "Graphics2D"
import java.awt.event.KeyEvent; //importação dos rescursos de "KeyEvent"
import java.awt.event.KeyListener; //importação dos recursos da interface "KeyListener"
import java.awt.event.MouseEvent; //importação dos rescursos de "MouseEvent"
import java.awt.event.MouseListener; //importação dos recursos da interface "MouseListener"
import java.awt.image.BufferStrategy; //importação da biblioteca "BufferStrategy"
import java.awt.image.BufferedImage; //importação da biblioteca "BufferImage"
import java.util.ArrayList; //Importando o recurso de "ArrayList" do java.utils
import java.util.List; //Importando o recurso de "List" do java.utils
import java.util.Random; //Importando o recurso de "Random" do java.utils

import javax.swing.JFrame; //importando recurso de JFrame

import com.dirtyCode.entities.BulletShoot;
//importação de pacotes criados dentro do proprio projeto
import com.dirtyCode.entities.Enemy;
import com.dirtyCode.entities.Entity;
import com.dirtyCode.entities.Player;
import com.dirtyCode.graphics.Spritesheet;
import com.dirtyCode.graphics.UI;
import com.dirtyCode.world.Camera;
import com.dirtyCode.world.World;

//classe principal onde a interface é desenhada
public class Game extends Canvas implements Runnable, KeyListener, MouseListener{ //herda a classe Canvas e implementa as interfaces Runnable, KeyListener e MouseListener

	//código serial padrão do java
	private static final long serialVersionUID = 1L;
	
	//instancia uma variavel de controle para geração de valores randomicos
	public static Random rand = new Random();
	
	//variaveis para configuração da janela
	public static JFrame frame; //instanciando um objeto do tipo JFrame que ajuda no contrela do gráfico
	private Thread thread; //instanciando uma Thread
	private boolean isRunning = true; //variavel que verifica se o jogo está rodando ou não
	public static final int WIDTH = 160; //constante para o tamanho horizontal da tela
	public static final int HEIGHT = 120; //constante para o tamanho vertical da tela
	public static final int SCALE = 4; //escala de dimensionamento da tela
	public UI ui; //instancia um objeto do tipo UI para futura implementação da interface gráfica
	public static String gameState = "MENU"; //variavél usada para controlar os estados que o jogo se encontra
	private int CUR_LEVEL = 1, MAX_LEVEL =2; //variaveis de controle e gerenciamento do nivel
	private boolean showMessageGameOver = true; //variavel usada para verificar se o jogador perdeu
	private boolean restartGame = false; //variavel que verifica se o jogo deve reiniciar
	private int framesGameOver = 0; //variavel de controle da animação do texto na tela de game over
	
	//variaveis relacionadas a imagens esternas
	private BufferedImage image; //instancia um objeto BufferedImge, tem finalidades gráficas
	public static Spritesheet spritesheet; //instancia um objeto da classe Spritesheet para receber o arquivo das sprites
	
	//listas de objetos interagiveis
	public static List<Entity> entities; //Lista "entities" guarda objetos do tipo Entity, ou seja, todos os tipos de entidades são armasenados aqui ao serem criados
	public static List<Enemy> enemies; //Lista "enemies" guarda objetos do tipo Enemy, é usada para verificação de colisão dos inimigos entre si.
	public static List<BulletShoot> bullets; //Lista "bullets" guarda objetos do tipo BulletShoot, é usada para verificação de colisão das bullets com os inimigos.
	
	//objetos principais do jogo
	public static World world; //objeto da classe World que guarda o objeto world do jogo.
	public static Player player; //objeto da classe Player que guarda o objeto player do jogo.
	public Menu menu; //objeto da classe Menu que guarda o objeto menu do jogo.
	
	//metodo construtor
	public Game() {
		//ativa os recursos da interface KeyListener
		addKeyListener(this);
		//ativa os recursos da interface MouseListener
		addMouseListener(this);
		//Torna a janela ativa ao iniciar o jogo
		this.setFocusable(true);
		//Requer que a janela esteja em foco para perceber teclas ativadas no teclado
		this.requestFocus();
		//cria a janela usando o recurso "Dimension()" e as constantes como parâmetro
		this.setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		//metodo de configuração da janela e JFrame
		initFrame();
		//atribui valor ao objeto que vai hospedar a interface do usuário
		ui = new UI();
		//atribui um objeto real na variavel image
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		//atribui um ArrayList do tipo Entity para a lista entities
		entities = new ArrayList<Entity>();
		//atribui um ArrayList do tipo Enemy para a lista enemies
		enemies = new ArrayList<Enemy>();
		//atribui um ArrayList do tipo BulletShoot para a lista bullets
		bullets = new ArrayList<BulletShoot>();
		//atribui ao objeto spritesheet uma imagem por meio do path: "/spritesheet.png"
		spritesheet = new Spritesheet("/spritesheet.png");
		//atribui valor ao player por meio do construtor
		player = new Player(0,0,16,16,spritesheet.getSprite(0, 0, 16, 16));
		//adiciona o player na lista de entidades
		entities.add(player);
		//atribui ao objeto map com uma imagem de geração por referencia pelo path: "/map.png"
		world = new World("/level1.png");
		//atribui valor ao objeto que vai rodar o menu do jogo
		menu = new Menu();
	}
	
	//metodo de configuração da janela e JFrame
	public void initFrame() {
		frame = new JFrame("Game #01"); //atribui valor um objeto a frame
		frame.add(this); //adiciona esta classe como parametro para o objeto frame
		frame.setResizable(false); //informa que redimensionar a janela é falso, portanto não é possível redimensiona-la
		frame.pack(); //recurso de performance
		frame.setLocationRelativeTo(null); //informa que não há localização relativa a nada
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //faz com que o Jframe encerre todo o programa ao ser fechado
		frame.setVisible(true); //torna visível
	}
	
	//metodo de start sincronizado
	public synchronized void start() {
		thread = new Thread(this); //atribui um objeto Thread com esta classe como parametro
		isRunning = true; //coloca o jogo para rodar
		thread.start(); //inicia a thread
	}
	
	//metodo de finalização sincronizado
	public synchronized void stop() {
		//faz o jogo parar de rodar
		isRunning = false;
		try {
			thread.join();//encerra a thread corretamente
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	//metodo estático principal de inicio
	public static void main(String args[]) {
		Game game = new Game(); //instancia um novo objeto game da classe Game
		game.start();//inicia o jogo pelo metodo "start();" do objeto game
	}
	
	//metodo de update de logica
	public void tick() {
		//caso o estado atual do jogo seja "NORMAL"
		if(gameState == "NORMAL") {//roda o update geral normalmente
			this.restartGame = false; //impede o jogador de reinicar sem querer no meio da partida
			//laço que varre o todas as entidades do jogo usando a lista entities
			for(int i = 0; i< entities.size(); i++) {//o ciclo se repete até que todas as entidades passem pelo update, enquanto i < que entities.size()
				Entity e = entities.get(i);//coleta a entidade do ciclo
				e.tick();//executa o update da entidade atual
			}
			//laço que varre o todas as bullets vivas no jogo usando a lista bullets
			for(int i = 0; i< bullets.size(); i++) {//o ciclo se repete até que todas as balas passem pelo update, enquanto i < que bullets.size()
				bullets.get(i).tick();;//coleta a bullet do ciclo e executa o update da bullet atual
			}
			//laço que varre o todas os inimigos no jogo usando a lista enimies
			if(enemies.size() == 0) {//caso a lista esteja vazia
				CUR_LEVEL++;//incrementa o valor do level corrente
				if(CUR_LEVEL > MAX_LEVEL) {//caso o valor do lever corrente seja maior que o valor do lever maximo
					CUR_LEVEL = 1; //level corrente se torna 1
				}
				String newWolrd = "level" + CUR_LEVEL + ".png"; //atribui o path do nivel corrente na variavel "newWolrd"
				World.restartGame(newWolrd);//chama o metodo "restartGame();" da classe World
			}
		//caso o estado do jogo seja "GAME_OVER"
		}else if(gameState == "GAME_OVER") {
			this.framesGameOver++; //incrementa o contador de frames do game over
			if(this.framesGameOver == 25) { //caso o valor do contador de frames seja maior que 25
				this.framesGameOver = 0; //contador de frames é zeradoa
				if(this.showMessageGameOver) //caso "showMessageGameOver" seja verdadeiro
					this.showMessageGameOver = false; // torna "showMessageGameOver" falso
				else //caso contrario
					this.showMessageGameOver = true; // torna "showMessageGameOver" verdadeiro
			}
			//caso a variavel "restartGame" seja verdadeira
			if(restartGame) {
				this.restartGame = false; // torna "restartGame" falsa
				gameState = "NORMAL"; //muda o gameState atual para "NORMAL"
				CUR_LEVEL = 1; //muda o level corrente para 1
				String newWolrd = "level" + CUR_LEVEL + ".png"; //formar o path para o arquivo do mapa referente ao level corrente
				World.restartGame(newWolrd);//chama o metodo "restartGame();" da classe World
			}
		//caso o estado de jogo seja "MENU"
		}else if(gameState == "MENU") {
			menu.tick();//chama o metodo de update do objeto menu
		}
	}
	
	//metodo de update de renderização
	public void render() {
		// variavel "bs" controla a estratégia de buffer do render
		BufferStrategy bs = this.getBufferStrategy();
		//caso bs seja nulo
		if(bs == null) {
			this.createBufferStrategy(3);//cria 3 estágios do bufferStrategy, ele deixa de estar vazio
			return;//sai da condição
		}
		//perceba que g vai funcionar como um ponteiro, por enquanto vai apontar os recursos de "image" com o metodo "getGraphics()" de image
		Graphics g =image.getGraphics();//variavel g pega os graficos do objeto image
		
		//configurando retalgulo referente ao background
		g.setColor(new Color(0, 0, 0)); //configura a cor do objeto image (com RGB manual)
		g.fillRect(0, 0, WIDTH, HEIGHT); //desenha um retangulo apartir do objeto image
		
		//renderização do objeto world e tudo que é renderizado a partir dele
		world.render(g);
		
		//laço que passa por todas as entidades do jogo usando a lista entities
		for(int i = 0; i< entities.size(); i++) {//o ciclo se repete até que todas as entidades passem pelo update, enquanto i < que entities.size()
			Entity e = entities.get(i);//coleta a entidade do ciclo
			e.render(g);//renderiza a entidade atual
		}
		//laço que varre o todas as bullets vivas no jogo usando a lista bullets
		for(int i = 0; i< bullets.size(); i++) {//o ciclo se repete até que todas as balas passem pelo render, enquanto i < que bullets.size()
			bullets.get(i).render(g);;//coleta a bullet do ciclo e executa o render da bullet atual
		}
		
		//chama o metodo de renderização da UI
		ui.render(g);
		
		//recurso de performance
		g.dispose();
		
		//troca o significado de g, agora ele aponta para "bs", com o metodo "getDrawGraphics()" de bs
		g = bs.getDrawGraphics(); //atribui g aos metodos de desenhar graficos de bs
		g.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null); //desenha a imagem geral do canvas a partir de bs
		
		//configurando e desenhando a renderização dos valores de munição do player na UI
		g.setFont(new Font("arial", Font.BOLD, 20));
		g.setColor(Color.white);
		g.drawString("Munição: " + player.ammo, 500, 35);
		
		//caso o estado de jogo seja "GAME_OVER"
		if(gameState == "GAME_OVER") {
			Graphics2D g2 = (Graphics2D) g; //instanciar uma variavel do tipo Graphics2D
			g2.setColor(new Color(0,0,0,100)); //configurar uma  cor com transparencia
			g2.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE); //desenhar o background da tela de derrota
			g.setFont(new Font("arial", Font.BOLD, 40)); //configuração da fonte 
			g.setColor(Color.WHITE); //escolhe a cor da fonte
			g.drawString("Game Over", (WIDTH*SCALE) / 2 - 130, (HEIGHT*SCALE) / 2 - 60); //Escreve "Game Over" na tela
			if(showMessageGameOver) { //caso "showMessageGameOver" seja verdadeiro
				g.setFont(new Font("arial", Font.BOLD, 34)); //configuração da fonte  
				g.drawString(">Pressione Enter para Reiniciar<", (WIDTH*SCALE) / 2 - 270, (HEIGHT*SCALE) / 2 - 10); //Escreve ">Pressione Enter para Reiniciar<" na tela
			}
		//caso o estado de jogo seja "MENU"
		}else if(gameState == "MENU") {
			menu.render(g);//chama o metodo de render do objeto menu
		}
			
		bs.show(); //mostra a tela com o metodo "show()" diretamente de bs
	}
	
	//metodo de loop
	@Override
	public void run() {
		//declaração das variaveis locais de controle
		long lastTime = System.nanoTime(); //variavel referente ao ultimo update
		double amountOfTicks = 60.0; //variavel referente ao numero de updates desejados por segundo
		double ns = 1000000000 / amountOfTicks; //recurso para os calculos
		double delta = 0; //recurso para os calculos
		int frames = 0; //contador de frames gerados
		double timer = System.currentTimeMillis(); //contador de tempo
		
		//loop de repetição
		while(isRunning) {
			long now = System.nanoTime(); //coleta o tempo do momento presente
			delta += (now - lastTime) / ns; //delta é acrescido da divisão do resto entre agora e o ultimo update pelo valor de ns 
			lastTime = now; //o ultimo update se torna o momento presente
			//caso delta seja maior ou igual a 1
			if(delta >= 1) {
				tick(); //update de logica
				render(); //update de tenderização
				frames++; //soma +1 aos frames
				delta--; //decrementa 1 de delta
			}
			//caso o tempo presente - contador de tempo seja maior ou igual a 1000
			if(System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS: "+ frames); // escreva no console: FPS + o valor da variavel frame
				frames = 0; //zera o numero de frames
				timer+=1000; //aumenta o valor de timer em 1000
			}
		}
		//chama o metodo de encerramento adequado do programa
		stop();
	}

	//os proximos 3 metodos são herdado da interface KeyListener
	@Override
	public void keyTyped(KeyEvent e) {
	}

	//metodo que verifica teclas pressionadas
	@Override
	public void keyPressed(KeyEvent e) {
		//se a tecla pressionada for "seta direita" ou "D" -> transforma o atributo "right" do player em verdadeiro
		if(e.getKeyCode() == KeyEvent.VK_RIGHT ||
				e.getKeyCode() == KeyEvent.VK_D) {
			player.right = true;
		//se a tecla pressionada for "seta esquerda" ou "A" -> transforma o atributo "left" do player em verdadeiro
		} else if(e.getKeyCode() == KeyEvent.VK_LEFT ||
				e.getKeyCode() == KeyEvent.VK_A) {
			player.left = true;
		}
		//se a tecla pressionada for "seta para cima" ou "W" -> transforma o atributo "up" do player em verdadeiro
		if(e.getKeyCode() == KeyEvent.VK_UP ||
				e.getKeyCode() == KeyEvent.VK_W) {
			player.up = true;
			//caso o estado de jogo seja "MENU"
			if(gameState == "MENU") {
				menu.up = true; //coloca o atributo "up" do objeto menu como verdadeiro
			}
		//se a tecla pressionada for "seta para baixo" ou "S" -> transforma o atributo "down" do player em verdadeiro
		} else if(e.getKeyCode() == KeyEvent.VK_DOWN ||
				e.getKeyCode() == KeyEvent.VK_S) {
			player.down = true;
			//caso o estado de jogo seja "MENU"
			if(gameState == "MENU") {
				menu.down = true; //coloca o atributo "down" do objeto menu como verdadeiro
			}
		}
		
		//verifica caso a tecla "X" seja clicada -> player atira (TECLADO)
		if(e.getKeyCode() == KeyEvent.VK_X) {
			player.shoot = true; // transforma o atributo shoot do player em verdadeiro
		}
		
		//verifica caso a tecla "ENTER" seja clicada 
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			this.restartGame = true; //coloca o "restarGame" como verdadeiro
			if(gameState == "MENU") { //caso o estado de jogo seja "MENU"
				menu.enter = true; //coloca o atributo "enter" do objeto menu como verdadeiro
			}
		}
		
		//verifica caso a tecla "ESC" seja clicada 
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			gameState = "MENU"; //torna o estado do jogo em "MENU" 
			menu.pause = true; //coloca o atributo "pause" do objeto menu como verdadeiro
		}
	}

	//metodo que verifica teclas despressionadas
	@Override
	public void keyReleased(KeyEvent e) {
		//se a tecla soltada for "seta direita" ou "D" -> transforma o atributo "right" do player em falso
		if(e.getKeyCode() == KeyEvent.VK_RIGHT ||
				e.getKeyCode() == KeyEvent.VK_D) {
			player.right = false;
		//se a tecla solta for "seta esquerda" ou "A" -> transforma o atributo "left" do player em falso
		} else if(e.getKeyCode() == KeyEvent.VK_LEFT ||
				e.getKeyCode() == KeyEvent.VK_A) {
			player.left = false;
		}
		//se a tecla solta for "seta para cima" ou "W" -> transforma o atributo "up" do player em falso
		if(e.getKeyCode() == KeyEvent.VK_UP ||
				e.getKeyCode() == KeyEvent.VK_W) {
			player.up = false;
		//se a tecla pressionada for "seta para baixo" ou "S" -> transforma o atributo "down" do player em falso
		} else if(e.getKeyCode() == KeyEvent.VK_DOWN ||
				e.getKeyCode() == KeyEvent.VK_S) {
			player.down = false;
		}
	}

	//metodo para teclas pressionadas no mouse
	@Override
	public void mousePressed(MouseEvent e) {
		player.mouseShoot = true; // transforma o atributo MouseShoot do player em verdadeiro
		//configura os valores das variaveis de controle das coordenadas do mouse da classe Player
		player.mx = (e.getX() / SCALE) + Camera.x; //getX do mouse dividido pela escala da janela + offset da camera.x atribuidos a player.mx
		player.my = (e.getY() / SCALE) + Camera.y; //getY do mouse dividido pela escala da janela + offset da camera.y atribuidos a player.my
	}
	
	//todos os outros metodos da interface MouseListerer que não estou usando
	@Override
	public void mouseClicked(MouseEvent e) {	
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}
