package br.com.caelum.leilao.servico;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertEquals;

import br.com.caelum.leilao.builder.CriadorDeLeilao;
import br.com.caelum.leilao.dominio.Lance;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.dominio.Usuario;
import org.junit.*;

import java.util.List;

public class TesteAvaliador {

    private Avaliador leiloeiro;
    private Usuario joao;
    private Usuario maria;
    private Usuario jose;
    private double maiorEsperado;
    private double menorEsperado;
    private double valorSimples;

    @BeforeClass
    public static void testandoBeforeClass() {
        System.out.println("before class");
    }

    @AfterClass
    public static void testandoAfterClass() {
        System.out.println("after class");
    }

    @Before
    public void setUp(){
        this.leiloeiro = new Avaliador();
        this.joao = new Usuario("Joao");
        this.maria = new Usuario("Maria");
        this.jose = new Usuario("Joao");
        this.maiorEsperado = 1000.0;
        this.menorEsperado = 10.0;
        this.valorSimples = 500.0;
    }

    @After
    public void finaliza() {
        System.out.println("fim");
    }

    // modo de testar que uma exceção deve ser lançada
    @Test(expected = RuntimeException.class)
    public void naoDeveAvaliarLeiloesSemNenhumLanceDado(){
        Leilao leilao = new CriadorDeLeilao().para("Ps3 bolado").constroi();
        leiloeiro.avalia(leilao);
        Assert.fail();
    }

    @Test
    public void deveEntenderLancesEmOrdemCrescente() {

        Leilao leilao = new CriadorDeLeilao().para("Playstation 3 Novo")
                .lance(joao, menorEsperado)
                .lance(maria, 200.0)
                .lance(joao, 300.0)
                .lance(maria, 400.0)
                .lance(jose, maiorEsperado)
                .constroi();

        leiloeiro.avalia(leilao);

        assertThat(leiloeiro.getMaiorLance(), equalTo(maiorEsperado));
        assertThat(leiloeiro.getMenorLance(), equalTo(menorEsperado));

    }

    @Test
    public void deveEntenderLancesEmOrdemDecrescente() {

        Leilao leilao = new CriadorDeLeilao().para("Playstation 3 Novo")
                .lance(joao, maiorEsperado)
                .lance(maria, 400.0)
                .lance(joao, 300.0)
                .lance(maria, 200.0)
                .lance(joao, menorEsperado)
                .constroi();

        leiloeiro.avalia(leilao);

        assertThat(leiloeiro.getMenorLance(), equalTo(menorEsperado));
        assertThat(leiloeiro.getMaiorLance(), equalTo(maiorEsperado));
    }

    @Test
    public void deveEntenderLeilaoComApenasUmLance(){
        Leilao leilao = new CriadorDeLeilao().para("PS4 zerado")
                .lance(joao, valorSimples)
                .constroi();

        leiloeiro.avalia(leilao);

        assertEquals(valorSimples, leiloeiro.getMenorLance(), 0.00001);
        assertEquals(valorSimples, leiloeiro.getMaiorLance(), 0.00001);
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
        assertThat(maiores.size(), equalTo(3));
        assertThat(maiores, hasItems(
                new Lance(maria, 700.0),
                new Lance(maria, 450.0),
                new Lance(joao, 500.0)
        ));
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
        assertThat(maiores, hasItems(
                new Lance(joao, 100.0),
                new Lance(maria, 450.0)
        ));
    }
}
