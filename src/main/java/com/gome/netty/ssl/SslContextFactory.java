package com.gome.netty.ssl;

import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SslContextFactory {
	
	private static final String PROTOCOL = "TLS";
	private static final SSLContext SERVER_CONTEXT;
	private static Logger logger = LoggerFactory.getLogger(SslContextFactory.class);
	static {
			SSLContext serverContext = null;
			// get keystore and trustore locations and passwords
			String keyStorePassword = "!@#gomeqwZXcloud#$%";
			try {
				KeyStore ks = KeyStore.getInstance("jceks");
				InputStream in = SslContextFactory.class.getClassLoader().getResourceAsStream("config/sgomecloud.keystore");
				if (null == in) {
					throw new Exception("keystore file not found!");
				}
				ks.load(in,
						keyStorePassword.toCharArray());
				
				// Set up key manager factory to use our key store
				KeyManagerFactory kmf = KeyManagerFactory
						.getInstance("SunX509");
				
				kmf.init(ks, keyStorePassword.toCharArray());

				// Initialize the SSLContext to work with our key managers.
				serverContext = SSLContext.getInstance(PROTOCOL);
				serverContext.init(kmf.getKeyManagers(), null, null);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			SERVER_CONTEXT = serverContext;
	}

	public static SSLContext getServerContext() {
		return SERVER_CONTEXT;
	}
}