package br.com.caelum.leilao.dominio;

import org.junit.Test;

import static org.junit.Assert.*;

public class LeilaoTest {

    @Test
    public void deveReceberUmLance(){
        Leilao leilao = new Leilao("Macbook Pro 15");
        assertEquals(0, leilao.getLances().size());

        leilao.propoe(new Lance(new Usuario("Steve Jobs"), 2000));
        assertEquals(1, leilao.getLances().size());
        assertEquals(2000.0, leilao.getLances().get(0).getValor(), 0.00001);
    }

    @Test
    public void deveReceberVariosLances(){
        Leilao leilao = new Leilao("Macbook Pro 15");
        leilao.propoe(new Lance(new Usuario("Steve Jobs"), 2000));
        leilao.propoe(new Lance(new Usuario("Steve Wozniak"), 3000));

        assertEquals(2, leilao.getLances().size());
        assertEquals(2000.0, leilao.getLances().get(0).getValor(), 0.00001);
        assertEquals(3000.0, leilao.getLances().get(1).getValor(), 0.00001);
    }

    @Test
    public void naoDeveAceitarDoisLancesSeguidosDoMesmoUsuario(){
        Leilao leilao = new Leilao("Macbook");
        Usuario steveJobs = new Usuario("Steve Jobs");

        leilao.propoe(new Lance(steveJobs, 2000.0));
        leilao.propoe(new Lance(steveJobs, 2100.0));

        assertEquals(1, leilao.getLances().size());
        assertEquals(2000.0, leilao.getLances().get(0).getValor(), 0.00001);
    }

    @Test
    public void naoDeveAceitarMaisQue5LancesDoMesmoUsuario(){
        Leilao leilao = new Leilao("Macbook");
        Usuario steveJobs = new Usuario("Steve Jobs");
        Usuario usuario2 = new Usuario("Magno Azevedo");

        leilao.propoe(new Lance(steveJobs, 2000.0));
        leilao.propoe(new Lance(usuario2, 2100.0));

        leilao.propoe(new Lance(steveJobs, 3000.0));
        leilao.propoe(new Lance(usuario2, 4500.0));

        leilao.propoe(new Lance(steveJobs, 4600.0));
        leilao.propoe(new Lance(usuario2, 5000.0));

        leilao.propoe(new Lance(steveJobs, 5500.0));
        leilao.propoe(new Lance(usuario2, 6000.0));

        leilao.propoe(new Lance(steveJobs, 8000.0));
        leilao.propoe(new Lance(usuario2, 9000.0));

        leilao.propoe(new Lance(steveJobs, 9100.0));

        assertEquals(10, leilao.getLances().size());
        assertEquals(9000.0, leilao.ultimoLanceDado().getValor(), 0.00001);
    }

    @Test
    public void deveRetornarUltimoLanceDeUsuarioCasoExista(){
        Leilao leilao = new Leilao("Macbook");
        Usuario steveJobs = new Usuario("Steve Jobs");
        Usuario magnoAzevedo = new Usuario("Magno");

        Lance ultimoLanceSteve = new Lance(steveJobs, 2000.0);
        Lance ultimoLanceMagno = new Lance(magnoAzevedo, 3000.0);

        leilao.propoe(new Lance(steveJobs, 1000.0));
        leilao.propoe(new Lance(magnoAzevedo, 1500.0));
        leilao.propoe(ultimoLanceSteve);
        leilao.propoe(ultimoLanceMagno);

        Lance ultimoSteve = leilao.getUltimoLanceUsuario(steveJobs);
        Lance ultimoMagno = leilao.getUltimoLanceUsuario(magnoAzevedo);

        assertEquals(ultimoLanceSteve, ultimoSteve);
        assertEquals(ultimoLanceMagno, ultimoMagno);
    }

    @Test
    public void naoDeveRetornarUltimoLanceCasoNaoExistamLancesDoUsuario(){
        Leilao leilao = new Leilao("Macbook");
        Usuario steveJobs = new Usuario("Steve Jobs");
        leilao.propoe(new Lance(new Usuario("Magno"), 1500.0));

        Lance lance = leilao.getUltimoLanceUsuario(steveJobs);
        assertNull(lance);
    }

    @Test
    public void deveRetornarLanceDobradoParaUsuario(){
        Leilao leilao = new Leilao("Macbook");
        Usuario steveJobs = new Usuario("Steve Jobs");

        leilao.propoe(new Lance(steveJobs, 1500.0));
        leilao.propoe(new Lance(new Usuario("Magno"), 1800.0));

        leilao.dobraLance(steveJobs);

        Lance ultiLanceSteve = leilao.getUltimoLanceUsuario(steveJobs);

        assertEquals(3000.0, ultiLanceSteve.getValor(), 0.00001);
    }

    @Test
    public void naoDeveDobrarCasoNaoHajaLanceAnterior() {
        Leilao leilao = new Leilao("Macbook Pro 15");
        Usuario steveJobs = new Usuario("Steve Jobs");

        leilao.dobraLance(steveJobs);

        assertEquals(0, leilao.getLances().size());
    }

}