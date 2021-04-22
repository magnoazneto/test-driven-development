package br.com.caelum.leilao.dominio;

import org.junit.Assert;
import org.junit.Test;


public class RespostaQuestaoTest {

    @Test(expected = IllegalArgumentException.class)
    public void naoDeveriaAceitaNotaMaiorQueDez(){
        RespostaQuestao resposta = new RespostaQuestao(11);
        Assert.fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void naoDeveriaAceitaNotaMenorQueZero(){
        RespostaQuestao resposta = new RespostaQuestao(-1);
        Assert.fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void naoDeveriaAceitaNotaNula(){
        RespostaQuestao resposta = new RespostaQuestao(null);
        Assert.fail();
    }

}