//arquivo de implementação da classe UI

package com.dirtyCode.graphics;//pacotes

//importando recursos do java
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

//importando recursos do projeto
import com.dirtyCode.main.Game;

//implementação da classe UI
public class UI {
	//metodo de renderização da interface de usuario
	public void render(Graphics g) {
		g.setColor(Color.red); //configura a cor de fundo da barra de vida
		g.fillRect(4, 4, 60, 8); //posição do fundo da barra de vida
		g.setColor(Color.green); //configura a cor da barra de vida
		g.fillRect(4, 4, (int)((Game.player.life/Game.player.maxLife)*60), 8); //posição da barra de vida
		
		//configura e desenha a vida do personagem/ vida total
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 8));
		g.drawString((int)Game.player.life + "/" + (int)Game.player.maxLife, 25,11);
	}
	
}
