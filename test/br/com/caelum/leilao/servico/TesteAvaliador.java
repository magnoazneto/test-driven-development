package br.com.caelum.leilao.servico;

import br.com.caelum.leilao.builder.CriadorDeLeilao;
import br.com.caelum.leilao.dominio.Lance;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.dominio.Usuario;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;


public class TesteAvaliador {

    private Avaliador leiloeiro;
    private Usuario joao;
    private Usuario maria;
    private Usuario jose;

    @Before
    public void criaAvaliador(){
        this.leiloeiro = new Avaliador();
        joao = new Usuario("Joao");
        maria = new Usuario("Maria");
        jose = new Usuario("Joao");
    }

    @Test
    public void deveEntenderLancesEmOrdemCrescente() {
        Leilao leilao = new CriadorDeLeilao().para("Playstation 3 Novo")
                .lance(joao, 100.0)
                .lance(maria, 200.0)
                .lance(joao, 300.0)
                .lance(maria, 400.0)
                .lance(joao, 500.0)
                .constroi();

        leiloeiro.avalia(leilao);

        double maiorEsperado = 500.0;
        double menorEsperado = 100.0;

        assertEquals(maiorEsperado, leiloeiro.getMaiorLance(), 0.00001);
        assertEquals(menorEsperado, leiloeiro.getMenorLance(), 0.00001);

    }

    @Test
    public void deveEntenderLancesEmOrdemDecrescente() {

        Leilao leilao = new CriadorDeLeilao().para("Playstation 3 Novo")
                .lance(joao, 500.0)
                .lance(maria, 400.0)
                .lance(joao, 300.0)
                .lance(maria, 200.0)
                .lance(joao, 100.0)
                .constroi();

        // parte 2: acao
        leiloeiro.avalia(leilao);

        // parte 3: validacao
        double maiorEsperado = 500.0;
        double menorEsperado = 100.0;

        assertEquals(maiorEsperado, leiloeiro.getMaiorLance(), 0.00001);
        assertEquals(menorEsperado, leiloeiro.getMenorLance(), 0.00001);

    }

    @Test
    public void deveEntenderLeilaoComApenasUmLance(){
        Leilao leilao = new CriadorDeLeilao().para("PS4 zerado")
                .lance(joao, 200.0)
                .constroi();

        leiloeiro.avalia(leilao);

        assertEquals(200.0, leiloeiro.getMenorLance(), 0.00001);
        assertEquals(200.0, leiloeiro.getMaiorLance(), 0.00001);
    }

    @Test
    public void deveEncontrarOsTresMaioresLances(){
        Leilao leilao = new CriadorDeLeilao().para("Playstation 3 Novo")
                .lance(joao, 100.0)
                .lance(maria, 450.0)
                .lance(joao, 500.0)
                .lance(maria, 700.)
                .lance(joao, 100.0)
                .constroi();


        leiloeiro.avalia(leilao);

        List<Lance> maiores = leiloeiro.getTresMaiores();
        assertEquals(3, maiores.size());
        assertEquals(700.0, maiores.get(0).getValor(), 0.00001);
        assertEquals(500.0, maiores.get(1).getValor(), 0.00001);
        assertEquals(450.0, maiores.get(2).getValor(), 0.00001);
    }

    @Test
    public void deveRetornarTodosLancesCasoNaoHajaNoMinimoTres(){
        Leilao leilao = new CriadorDeLeilao().para("Playstation 3 Novo")
                .lance(joao, 100.0)
                .lance(maria, 450.0)
                .constroi();


        leiloeiro.avalia(leilao);

        List<Lance> maiores = leiloeiro.getTresMaiores();
        assertEquals(2, maiores.size());
        assertEquals(450.0, maiores.get(0).getValor(), 0.00001);
        assertEquals(100.0, maiores.get(1).getValor(), 0.00001);
    }

    @Test
    public void deveRetornarListaVaziaParaLeilaoSemLance(){
        Leilao leilao = new CriadorDeLeilao().para("Playstation 3 Novo")
                .constroi();

        leiloeiro.avalia(leilao);

        List<Lance> maiores = leiloeiro.getTresMaiores();
        assertEquals(0, maiores.size());
    }


}
