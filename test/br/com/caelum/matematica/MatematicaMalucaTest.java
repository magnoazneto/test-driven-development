package br.com.caelum.matematica;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class MatematicaMalucaTest {

    @Test
    public void deveRetornarValorMultiplicadoPorQuatroQuandoNumeroMaiorQueTrinta(){
        MatematicaMaluca crazyMath = new MatematicaMaluca();
        int numero = 31;
        int resultado = crazyMath.contaMaluca(numero);

        assertEquals(31*4, resultado);
    }

    @Test
    public void deveRetornarValorMultiplicadoPorTresQuandoNumeroMaiorQueDezEMenorQue31(){
        MatematicaMaluca crazyMath = new MatematicaMaluca();
        int multiplicador = 3;
        int numero1 = 30;
        int numero2 = 29;
        int numero3 = 11;

        assertEquals(numero1*multiplicador, crazyMath.contaMaluca(numero1));
        assertEquals(numero2*multiplicador, crazyMath.contaMaluca(numero2));
        assertEquals(numero3*multiplicador, crazyMath.contaMaluca(numero3));
    }

    @Test
    public void deveRetornarValorVezes2QuandoNumeroMenorQue11(){
        MatematicaMaluca crazyMath = new MatematicaMaluca();
        int multiplicador = 2;
        int numero1 = 9;
        int numero2 = 10;

        assertEquals(numero1*multiplicador, crazyMath.contaMaluca(numero1));
        assertEquals(numero2*multiplicador, crazyMath.contaMaluca(numero2));
    }
}