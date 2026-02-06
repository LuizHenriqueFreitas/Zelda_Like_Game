//arquivo de implementação da classe Camera

package com.dirtyCode.world;//pacotes

//codigo da classe camera
public class Camera {

	//declaração das variaveiris controladoras de offset - estaticas para facilitar o acesso em outros scripts
	public static int x;
	public static int y;

	//metodo clamp - recurso muito popular para evitar ver 'atravez' do mapa, ajusta a camera aos limites do mapa
	public static int clamp(int atual, int min, int max) {
		//se o pixel atual for menor que o meinimo
		if(atual < min) {
			atual = min; //pixel atual = minimo
		}
		//se o pixel atual for maior que o maximo
		if(atual > max)
			atual = max; //pixel atual = maximo
		
		return atual;//retorna o valor atual depois de tratado e verificado
	}
}
