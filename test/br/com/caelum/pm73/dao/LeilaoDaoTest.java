package br.com.caelum.pm73.dao;

import br.com.caelum.pm73.dominio.Lance;
import br.com.caelum.pm73.dominio.Leilao;
import br.com.caelum.pm73.dominio.Usuario;
import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.*;

public class LeilaoDaoTest {

    private Session session;
    private UsuarioDao usuarioDao;
    private LeilaoDao leilaoDao;
    private Usuario usuarioPadrao;

    @Before
    public void setUp(){
        session = new CriadorDeSessao().getSession();
        usuarioDao = new UsuarioDao(session);
        leilaoDao = new LeilaoDao(session);
        usuarioPadrao = new Usuario("Mauricio", "mauricio@mauricio.com.br");

        // importante para limpar o banco de dados das informacoes geradas pelos testes
        session.beginTransaction();
    }

    @After
    public void finishSetUp(){
        session.getTransaction().rollback(); // importante para limpar o banco de dados das informacoes geradas pelos testes
        session.close();
    }

    @Test
    public void deveContarLeiloesNaoEncerrados(){
        Leilao ativo = new LeilaoBuilder()
                .comDono(usuarioPadrao).constroi();

        Leilao encerrado = new LeilaoBuilder()
                .comDono(usuarioPadrao)
                .encerrado()
                .constroi();

        usuarioDao.salvar(usuarioPadrao);
        leilaoDao.salvar(ativo);
        leilaoDao.salvar(encerrado);

        long total = leilaoDao.total();
        assertEquals(1L, total);
    }

    @Test
    public void deveRetornarZeroQuandoNaoHouverLeiloesNaoEncerrados(){
        Leilao encerrado = new LeilaoBuilder().comDono(usuarioPadrao).encerrado().constroi();

        usuarioDao.salvar(usuarioPadrao);
        leilaoDao.salvar(encerrado);

        long total = leilaoDao.total();
        assertEquals(0L, total);
    }

    @Test
    public void deveRetornarApenasLeiloesNaoUsados(){
        Leilao ativo = new LeilaoBuilder().comDono(usuarioPadrao).comNome("Geladeira").constroi();
        Leilao encerrado = new LeilaoBuilder().comDono(usuarioPadrao).comNome("XBOX").
                usado().encerrado().constroi();


        usuarioDao.salvar(usuarioPadrao);
        leilaoDao.salvar(encerrado);
        leilaoDao.salvar(ativo);

        List<Leilao> total = leilaoDao.novos();
        assertEquals(total.size(), 1);
    }

    @Test
    public void deveTrazerSomenteLeiloesAntigos() {
        Leilao recente = new LeilaoBuilder().comDono(usuarioPadrao).comNome("XBox").constroi();
        Leilao antigo = new LeilaoBuilder().comDono(usuarioPadrao).comNome("Geladeira").usado()
                .diasAtras(10).constroi();

        usuarioDao.salvar(usuarioPadrao);
        leilaoDao.salvar(recente);
        leilaoDao.salvar(antigo);

        List<Leilao> antigos = leilaoDao.antigos();

        assertEquals(1, antigos.size());
        assertEquals("Geladeira", antigos.get(0).getNome());
    }

    @Test
    public void deveTrazerSomenteLeiloesAntigosHaMaisDe7Dias() {
        Leilao noLimite = new LeilaoBuilder().comDono(usuarioPadrao).diasAtras(7).constroi();

        usuarioDao.salvar(usuarioPadrao);
        leilaoDao.salvar(noLimite);

        List<Leilao> antigos = leilaoDao.antigos();

        assertEquals(1, antigos.size());
    }

    @Test
    public void deveTrazerLeiloesNaoEncerradosNoPeriodo(){
        Calendar comecoDoIntervalo = Calendar.getInstance();
        comecoDoIntervalo.add(Calendar.DAY_OF_MONTH, -10);
        Calendar fimDoIntervalo = Calendar.getInstance();

        Leilao leilao1 = new LeilaoBuilder().comNome("XBOX").comDono(usuarioPadrao).diasAtras(2).constroi();
        Leilao leilao2 = new LeilaoBuilder().comNome("GELADEIRA").comDono(usuarioPadrao).diasAtras(20).constroi();

        usuarioDao.salvar(usuarioPadrao);
        leilaoDao.salvar(leilao1);
        leilaoDao.salvar(leilao2);

        List<Leilao> leiloes = leilaoDao.porPeriodo(comecoDoIntervalo, fimDoIntervalo);

        assertEquals(1, leiloes.size());
        assertEquals("XBOX", leiloes.get(0).getNome());
    }

    @Test
    public void naoDeveTrazerLeiloesEncerradosNoPeriodo(){ // sem a refatoracao do builder apenas para proprositos de comparacao
        Calendar comecoDoIntervalo = Calendar.getInstance();
        comecoDoIntervalo.add(Calendar.DAY_OF_MONTH, -10);

        Calendar fimDoIntervalo = Calendar.getInstance();

        Usuario mauricio = new Usuario("Mauricio", "mauricio@gmail.com");
        Leilao leilao1 = new Leilao("XBOX", 700.0, mauricio, false);
        Calendar dataLeilao1 = Calendar.getInstance();
        dataLeilao1.add(Calendar.DAY_OF_MONTH, -2);
        leilao1.setDataAbertura(dataLeilao1);
        leilao1.encerra();

        usuarioDao.salvar(mauricio);
        leilaoDao.salvar(leilao1);

        List<Leilao> leiloes = leilaoDao.porPeriodo(comecoDoIntervalo, fimDoIntervalo);
        assertEquals(0, leiloes.size());
    }

    @Test
    public void deveRetornarLeiloesDisputados() {
        Usuario mauricio = new Usuario("Mauricio", "mauricio@aniche.com.br");
        Usuario marcelo = new Usuario("Marcelo", "marcelo@aniche.com.br");

        Leilao leilao1 = new LeilaoBuilder()
                .comDono(marcelo)
                .comValor(3000.0)
                .comLance(Calendar.getInstance(), mauricio, 3000.0)
                .comLance(Calendar.getInstance(), marcelo, 3100.0)
                .constroi();

        Leilao leilao2 = new LeilaoBuilder()
                .comDono(mauricio)
                .comValor(3200.0)
                .comLance(Calendar.getInstance(), mauricio, 3000.0)
                .comLance(Calendar.getInstance(), marcelo, 3100.0)
                .comLance(Calendar.getInstance(), mauricio, 3200.0)
                .comLance(Calendar.getInstance(), marcelo, 3300.0)
                .comLance(Calendar.getInstance(), mauricio, 3400.0)
                .comLance(Calendar.getInstance(), marcelo, 3500.0)
                .constroi();

        usuarioDao.salvar(marcelo);
        usuarioDao.salvar(mauricio);
        leilaoDao.salvar(leilao1);
        leilaoDao.salvar(leilao2);

        List<Leilao> leiloes = leilaoDao.disputadosEntre(2500, 3500);

        assertEquals(1, leiloes.size());
        assertEquals(3200.0, leiloes.get(0).getValorInicial(), 0.00001);
    }

}