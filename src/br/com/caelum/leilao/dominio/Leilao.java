package br.com.caelum.leilao.dominio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Leilao {

	private String descricao;
	private List<Lance> lances;
	
	public Leilao(String descricao) {
		this.descricao = descricao;
		this.lances = new ArrayList<Lance>();
	}

	/**
	 * adiciona no array de lances um lance, caso seja permitido
	 * @param lance
	 */
	public void propoe(Lance lance) {
		int total = getTotalLancesUsuario(lance.getUsuario());

		if(lances.isEmpty() || podeDarLance(lance.getUsuario())){
			lances.add(lance);
		}
	}

	public void dobraLance(Usuario usuario){
		Lance ultimoLance = getUltimoLanceUsuario(usuario);
		if(ultimoLance != null && podeDarLance(usuario)){
			Lance novoLance = new Lance(usuario, ultimoLance.getValor() * 2.0);
			this.propoe(novoLance);
		}
	}

	public Lance getUltimoLanceUsuario(Usuario usuario){
		Lance ultimoLance = null;
		for (Lance l: lances){
			if (l.getUsuario().equals(usuario)) ultimoLance = l;
		}
		return ultimoLance;
	}

	private boolean podeDarLance(Usuario usuario) {
		return !ultimoLanceDado().getUsuario().equals(usuario) && getTotalLancesUsuario(usuario) < 5;
	}

	private int getTotalLancesUsuario(Usuario usuario){
		int total = 0;
		for (Lance l : lances) {
			if (l.getUsuario().equals(usuario)) total++;
		}
		return total;
	}

	public Lance ultimoLanceDado(){
		return lances.get(lances.size()-1);
	}

	public String getDescricao() {
		return descricao;
	}

	public List<Lance> getLances() {
		return Collections.unmodifiableList(lances);
	}

	
	
}
