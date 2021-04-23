package br.com.caelum.pm73.dao;

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

    @Before
    public void setUp(){
        session = new CriadorDeSessao().getSession();
        usuarioDao = new UsuarioDao(session);
        leilaoDao = new LeilaoDao(session);

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
        Usuario mauricio = new Usuario("Mauricio", "mauricio@gmail.com");
        Leilao ativo = new Leilao("Geladeira", 1500.0, mauricio, false);
        Leilao encerrado = new Leilao("XBOX", 800.0, mauricio, false);
        encerrado.encerra();

        usuarioDao.salvar(mauricio);
        leilaoDao.salvar(ativo);
        leilaoDao.salvar(encerrado);

        long total = leilaoDao.total();
        assertEquals(1L, total);
    }

    @Test
    public void deveRetornarZeroQuandoNaoHouverLeiloesNaoEncerrados(){
        Usuario mauricio = new Usuario("Mauricio", "mauricio@gmail.com");
        Leilao encerrado = new Leilao("XBOX", 800.0, mauricio, false);
        encerrado.encerra();

        usuarioDao.salvar(mauricio);
        leilaoDao.salvar(encerrado);

        long total = leilaoDao.total();
        assertEquals(0L, total);
    }

    @Test
    public void deveRetornarApenasLeiloesNaoUsados(){
        Usuario mauricio = new Usuario("Mauricio", "mauricio@gmail.com");
        Leilao ativo = new Leilao("Geladeira", 1500.0, mauricio, false);
        Leilao encerrado = new Leilao("XBOX", 800.0, mauricio, true);
        encerrado.encerra();

        usuarioDao.salvar(mauricio);
        leilaoDao.salvar(encerrado);
        leilaoDao.salvar(ativo);

        List<Leilao> total = leilaoDao.novos();
        assertEquals(total.size(), 1);
    }

    @Test
    public void deveTrazerSomenteLeiloesAntigos() {
        Usuario mauricio = new Usuario("Mauricio Aniche",
                "mauricio@aniche.com.br");

        Leilao recente =
                new Leilao("XBox", 700.0, mauricio, false);
        Leilao antigo =
                new Leilao("Geladeira", 1500.0, mauricio,true);

        Calendar dataRecente = Calendar.getInstance();
        Calendar dataAntiga = Calendar.getInstance();
        dataAntiga.add(Calendar.DAY_OF_MONTH, -10);

        recente.setDataAbertura(dataRecente);
        antigo.setDataAbertura(dataAntiga);

        usuarioDao.salvar(mauricio);
        leilaoDao.salvar(recente);
        leilaoDao.salvar(antigo);

        List<Leilao> antigos = leilaoDao.antigos();

        assertEquals(1, antigos.size());
        assertEquals("Geladeira", antigos.get(0).getNome());
    }

    @Test
    public void deveTrazerSomenteLeiloesAntigosHaMaisDe7Dias() {
        Usuario mauricio = new Usuario("Mauricio Aniche",
                "mauricio@aniche.com.br");

        Leilao noLimite =
                new Leilao("XBox", 700.0, mauricio, false);

        Calendar dataAntiga = Calendar.getInstance();
        dataAntiga.add(Calendar.DAY_OF_MONTH, -7);

        noLimite.setDataAbertura(dataAntiga);

        usuarioDao.salvar(mauricio);
        leilaoDao.salvar(noLimite);

        List<Leilao> antigos = leilaoDao.antigos();

        assertEquals(1, antigos.size());
    }

    @Test
    public void deveTrazerLeiloesNaoEncerradosNoPeriodo(){

        Calendar comecoDoIntervalo = Calendar.getInstance();
        comecoDoIntervalo.add(Calendar.DAY_OF_MONTH, -10);

        Calendar fimDoIntervalo = Calendar.getInstance();

        Usuario mauricio = new Usuario("Mauricio", "mauricio@mauricio.com.br");

        Leilao leilao1 = new Leilao("XBOX", 700.0, mauricio, false);
        Calendar dataLeilao1 = Calendar.getInstance();
        dataLeilao1.add(Calendar.DAY_OF_MONTH, -2);
        leilao1.setDataAbertura(dataLeilao1);

        Leilao leilao2 = new Leilao("GELADEIRA", 1700.0, mauricio, false);
        Calendar dataLeilao2 = Calendar.getInstance();
        dataLeilao2.add(Calendar.DAY_OF_MONTH, -20);
        leilao2.setDataAbertura(dataLeilao2);

        usuarioDao.salvar(mauricio);
        leilaoDao.salvar(leilao1);
        leilaoDao.salvar(leilao2);

        List<Leilao> leiloes = leilaoDao.porPeriodo(comecoDoIntervalo, fimDoIntervalo);

        assertEquals(1, leiloes.size());
        assertEquals("XBOX", leiloes.get(0).getNome());
    }

    @Test
    public void naoDeveTrazerLeiloesEncerradosNoPeriodo(){
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

}