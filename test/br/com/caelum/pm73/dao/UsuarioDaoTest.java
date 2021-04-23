package br.com.caelum.pm73.dao;

import br.com.caelum.pm73.dominio.Usuario;
import org.hibernate.Session;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UsuarioDaoTest {

    private String nomeUsuario;
    private String emailUsuario;
    private Session session;
    private UsuarioDao usuarioDao;

    @Before
    public void setUp(){
        nomeUsuario = "Magno";
        emailUsuario = "magnoazneto@gmail.com";
        session = new CriadorDeSessao().getSession();
        usuarioDao = new UsuarioDao(session);

        session.beginTransaction();
    }

    @After
    public void finishSetUp(){
        session.getTransaction().rollback();
        session.close();
    }

    @Test
    public void deveEncontrarPeloNomeEEmail(){
        Usuario novoUsuario = new Usuario(nomeUsuario, emailUsuario);
        usuarioDao.salvar(novoUsuario);

        Usuario usuario = usuarioDao.porNomeEEmail(nomeUsuario, emailUsuario);
        assertEquals(nomeUsuario, usuario.getNome());
        assertEquals(emailUsuario, usuario.getEmail());


    }

    @Test
    public void deveRetornarNuloParaUsuarioInvalido(){
        Usuario usuario = usuarioDao.porNomeEEmail("Alberto", "alberto@gmail.com");
        assertNull(usuario);
    }

    @Test
    public void deveDeletarUsuario(){
        Usuario usuario = new Usuario(nomeUsuario, emailUsuario);
        usuarioDao.salvar(usuario);
        usuarioDao.deletar(usuario);

        session.flush(); // para lidar com os caches do hibernate
        session.clear();

        Usuario usuarioBuscado = usuarioDao.porNomeEEmail(nomeUsuario, emailUsuario);
        Assert.assertNull(usuarioBuscado);
    }

    @Test
    public void deveAlterarUmUsuario() {
        Usuario usuario =
                new Usuario("Mauricio Aniche", "mauricio@aniche.com.br");

        usuarioDao.salvar(usuario);

        usuario.setNome("João da Silva");
        usuario.setEmail("joao@silva.com.br");

        usuarioDao.atualizar(usuario);

        session.flush();

        Usuario novoUsuario =
                usuarioDao.porNomeEEmail("João da Silva", "joao@silva.com.br");
        assertNotNull(novoUsuario);
        System.out.println(novoUsuario);

        Usuario usuarioInexistente =
                usuarioDao.porNomeEEmail("Mauricio Aniche", "mauricio@aniche.com.br");
        assertNull(usuarioInexistente);

    }

}