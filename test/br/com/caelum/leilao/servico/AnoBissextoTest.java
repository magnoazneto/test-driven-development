package br.com.caelum.leilao.servico;

import org.junit.Test;

import static org.junit.Assert.*;

public class AnoBissextoTest {
    @Test
    public void deveRetornarAnoBissexto() {
        AnoBissexto anoBissexto = new AnoBissexto();

        assertTrue(anoBissexto.isAnoBissexto(2016));
        assertTrue(anoBissexto.isAnoBissexto(2012));
    }

    @Test
    public void naoDeveRetornarAnoBissexto() {
        AnoBissexto anoBissexto = new AnoBissexto();

        assertFalse(anoBissexto.isAnoBissexto(2015));
        assertFalse(anoBissexto.isAnoBissexto(2011));
    }

}