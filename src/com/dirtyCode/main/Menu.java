//arquivo de implementação da classe Menu

package com.dirtyCode.main;//pacotes

//importação de recursos do java
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

//codigo da classe Menu
public class Menu {
	
	//atributos da classe menu
	public String[] options = {"novo jogo", "carregar jogo", "sair"}; //vetor com as opições do menu
	
	//variaveis de controle da seleção de opção
	public int currentOption = 0;
	public int maxOption = options.length -1;
	
	//variaveis de verificação
	public boolean up, down, enter;
	public boolean pause = false;
	
	//metodo de update
	public void tick() {
		//caso "up" seja verdadeiro
		if(up) {
			up = false; //"up" se torna falso
			currentOption--; //decrementa 1 da opção corrente
			if(currentOption < 0) { //caso a opção corrente seja menor que zero
				currentOption = maxOption; //opção corrente passa a ser a opção máxima
			}
		}
		//caso "down" seja verdadeiro
		if(down) {
			down = false; //"down" se torna falso
			currentOption++; // incrementa 1 na opção corrente
			if(currentOption > maxOption) { //caso a opção corrente seja menor que a opção máxima
				currentOption = 0; //opção corrente passa a ser zero
			}
		}
		//caso "enter" seja verdadeiro
		if(enter) {
			enter = false; //"enter" se torna falso
			 //caso a opção corrente seja = "novo jogo" OU = "continuar"
			if(options[currentOption] == "novo jogo" || options[currentOption] == "continuar") {
				Game.gameState = "NORMAL"; //torna o estado de jogo "NORMAL"
				pause = false; //torna pause em falso
			//caso a opção corrente seja "sair"
			}else if(options[currentOption] == "sair") {
				System.exit(1); //chama o metodo de encerramento do programa
			}
		}
	}
	
	//metodo de renderização
	public void render(Graphics g) {
		//configurando a janela de pause/menu
		Graphics2D g2 = (Graphics2D) g; //instanciar uma variavel do tipo Graphics2D
		g2.setColor(new Color(0, 0, 0, 100)); //configurar uma  cor com transparencia
		g2.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE); //desenhar o background da tela do menu
		g.setFont(new Font("arial", Font.BOLD, 40)); //configuração da fonte 
		g.setColor(Color.YELLOW); //escolhe a cor da fonte
		g.drawString("<< Zelda Like >>", (Game.WIDTH*Game.SCALE) / 2 - 170, (Game.HEIGHT*Game.SCALE) / 2 - 160); //Escreve o nome do jogo na tela
		
		//desenha as opções de jogo na tela
		g.setFont(new Font("arial", Font.BOLD, 34)); //configuração da fonte 
		g.setColor(Color.WHITE); //escolhe a cor da fonte
		//caso "pause" seja falso
		if(!pause)
			g.drawString("Novo Jogo", (Game.WIDTH*Game.SCALE) / 2 - 170, (Game.HEIGHT*Game.SCALE) / 2 - 60); //Escreve "Novo Jogo" na tela
		else//caso "pause" seja verdadeiro
			g.drawString("Resumir", (Game.WIDTH*Game.SCALE) / 2 - 170, (Game.HEIGHT*Game.SCALE) / 2 - 60); //Escreve "Resumir" na tela
		g.drawString("Carregar Jogo", (Game.WIDTH*Game.SCALE) / 2 - 170, (Game.HEIGHT*Game.SCALE) / 2 - 20); //Escreve "Carregar Jogo" na tela
		g.drawString("Sair", (Game.WIDTH*Game.SCALE) / 2 - 170, (Game.HEIGHT*Game.SCALE) / 2 + 20); //Escreve "Sair" na tela
		
		//configurando o feedback seletor de opção
		//caso a opção corrente seja "novo jogo"
		if(options[currentOption] == "novo jogo") {
			g.drawString(">", (Game.WIDTH*Game.SCALE) / 2 - 200,(Game.HEIGHT*Game.SCALE) / 2 -60); //desenha o seletor para "Novo Jogo"
		}else if(options[currentOption] == "carregar jogo") {//caso a opção corrente seja "carregar jogo"
			g.drawString(">", (Game.WIDTH*Game.SCALE) / 2 - 200,(Game.HEIGHT*Game.SCALE) / 2 -20); //desenha o seletor para "Carregar Jogo"
		}else if(options[currentOption] == "sair") {//caso a opção corrente seja "sair"
			g.drawString(">", (Game.WIDTH*Game.SCALE) / 2 - 200,(Game.HEIGHT*Game.SCALE) / 2 +20); //desenha o seletor para "Sair"
		}
		
		//configurando o txt de exibição da versão do jogo
		g.setFont(new Font("arial", Font.ITALIC, 18));//configuração da fonte 
		g.setColor(Color.WHITE); //escolhe a cor da fonte
		g.drawString("V1.0", (Game.WIDTH*Game.SCALE) / 2 + 270, (Game.HEIGHT*Game.SCALE) / 2 + 220); //Escreve a versao do jogo na tela na tela
	}
}
