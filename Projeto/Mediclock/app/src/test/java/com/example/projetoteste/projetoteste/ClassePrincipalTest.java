package com.example.projetoteste.projetoteste;

import org.junit.Test;
import static org.junit.Assert.*;

public class ClassePrincipalTest {

    @Test
    public void testandoVerificaUsuario(){

       MainActivity ma = new MainActivity();

       boolean resultado = ma.validaUsuario("cristian-peres2009@hotmail.com");
       assertTrue(resultado);

    }

}
