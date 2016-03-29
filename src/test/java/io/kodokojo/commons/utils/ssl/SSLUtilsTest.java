package io.kodokojo.commons.utils.ssl;

/*
 * #%L
 * kodokojo-commons
 * %%
 * Copyright (C) 2016 Kodo-kojo
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SSLUtilsTest {

    @Test
    public void should_createSelfSignedSSLKeyPair() throws Exception {
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        KeyPairGenerator kpGen = KeyPairGenerator.getInstance("RSA");
        kpGen.initialize(2048, random);
        KeyPair keyPair = kpGen.generateKeyPair();
        RSAPublicKey RSAPubKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey RSAPrivateKey = (RSAPrivateKey) keyPair.getPrivate();

        SSLKeyPair sslKeyPair = SSLUtils.createSelfSignedSSLKeyPair("test", RSAPrivateKey, RSAPubKey);

        assertThat(sslKeyPair).isNotNull();
        assertThat(sslKeyPair.getCertificates()[0].getIssuerDN().getName()).isEqualTo("CN=test");
    }
}
