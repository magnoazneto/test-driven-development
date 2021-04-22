package br.com.caelum.leilao.dominio;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LanceTest {

    private Usuario usuario;

    @Before
    public void setUp(){
        this.usuario = new Usuario("Magno");
    }

    @Test(expected = IllegalArgumentException.class)
    public void naoDeveAceitarLanceComValorIgualAZero(){
        Lance lance = new Lance(usuario, 0);
        Assert.fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void naoDeveAceitarLanceComValorMenorQueZero(){
        Lance lance = new Lance(usuario, -1);
        Assert.fail();
    }

    @Test
    public void deveAceitarLanceComValorMaiorQueZero(){
        Lance lance = new Lance(usuario, 1.0);
        assertThat(lance.getValor(), equalTo(1.0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void naoDeveAceitarLanceComUsuarioNulo(){
        Lance lance = new Lance(null, 1.0);
        Assert.fail();
    }

}