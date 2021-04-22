package br.com.caelum.leilao.servico;

import br.com.caelum.leilao.dominio.Lance;
import br.com.caelum.leilao.dominio.Leilao;

import java.util.ArrayList;
import java.util.List;

public class Avaliador {

    private double maiorDeTodos = Double.NEGATIVE_INFINITY;
    private double menorDeTodos = Double.POSITIVE_INFINITY;
    private List<Lance> maiores;

    public void avalia(Leilao leilao){

        if(leilao.getLances().size() == 0){
            throw new RuntimeException("Não é possível avaliar leilão sem lances.");
        }

        for (Lance lance : leilao.getLances()){
            if(lance.getValor() > maiorDeTodos) maiorDeTodos = lance.getValor();
            if (lance.getValor() < menorDeTodos) menorDeTodos = lance.getValor();
        }
        maiores = new ArrayList<Lance>(leilao.getLances());
        maiores.sort((o1, o2) -> {
            return Double.compare(o2.getValor(), o1.getValor());
        });
        maiores = maiores.subList(0, Math.min(maiores.size(), 3));

    }

    public List<Lance> getTresMaiores() {
        return maiores;
    }

    public double getMaiorLance() {
        return maiorDeTodos;
    }

    public double getMenorLance() {
        return menorDeTodos;
    }
}
