package br.com.caelum.leillao.servico;

import br.com.caelum.leilao.dominio.Lance;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.dominio.Usuario;
import br.com.caelum.leilao.servico.Avaliador;
import org.junit.Assert;
import org.junit.Test;


public class TesteAvaliador {

    @Test
    public void deveEntenderLancesEmOrdemCrescente() {
        // parte 1: cenario
        Usuario joao = new Usuario("Joao");
        Usuario maria = new Usuario("Maria");
        Usuario jose = new Usuario("Joao");

        Leilao leilao = new Leilao("PS3 novo");

        leilao.propoe(new Lance(joao, 240));
        leilao.propoe(new Lance(maria, 400.0));
        leilao.propoe(new Lance(jose, 700));

        // parte 2: acao
        Avaliador leiloeiro = new Avaliador();
        leiloeiro.avalia(leilao);

        // parte 3: validacao
        double maiorEsperado = 700;
        double menorEsperado = 240;

        Assert.assertEquals(maiorEsperado, leiloeiro.getMaiorLance(), 0.00001);
        Assert.assertEquals(menorEsperado, leiloeiro.getMenorLance(), 0.00001);

    }
}
