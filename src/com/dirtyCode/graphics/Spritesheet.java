//arquivo de configuração de spritesheet
//Este arquivo faz o recebimento de spritesheets e sua configuração de maniera organizada


package com.dirtyCode.graphics;//pacotes

//importações de recursos do java
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

//declaração da classe Spritesheet
public class Spritesheet {

	//BufferedImage que armazena o aquivo de imagem bruto da spritesheet
	private BufferedImage spritesheet;
	
	//metodo de recebimento do arquivo
	public Spritesheet(String path) {//recebe o indereço como parâmetro
		try {
			spritesheet = ImageIO.read(getClass().getResource(path));//usando "ImageIO" - coloca a imagem do indereço passado como valor da variável spritesheet do tipo BufferedImage
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//metodo de recorte de sprites
	public BufferedImage getSprite(int x, int y, int width, int height) {//recebe o ponto inicial X e Y do sprite na spritesheet, assim como suas simensões em ambos os eixos
		return spritesheet.getSubimage(x, y, width, height);//retorna um BufferedImage com o recorte do spritesheet de acordo com os parâmetros
	}
	
}
