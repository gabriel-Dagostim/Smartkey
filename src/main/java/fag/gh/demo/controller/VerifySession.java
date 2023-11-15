package fag.gh.demo.controller;

import java.time.LocalDateTime;

import java.time.Duration;
import fag.gh.demo.tools.DateStringConverter;
import fag.gh.demo.tools.EncryptionUtils;
import fag.gh.demo.tools.TextTools;

public class VerifySession {

    public static void verifyToken(String token, String chaveUser) throws Exception {
        String descriptografia = EncryptionUtils.decryptData(token, chaveUser);
        String dataSemNull = TextTools.CheckNullField(descriptografia);
        LocalDateTime dataAtual = LocalDateTime.now();
        LocalDateTime dataToken = DateStringConverter.convert(dataSemNull);

        Duration duracao = Duration.between(dataAtual, dataToken);

        boolean passou = duracao.toMinutes() < -120;
        if (passou) {
            throw new TokenException("Token expirado");
        }
    }

}
