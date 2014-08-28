package com.ontometrics.scraper.extraction;

import com.ontometrics.scraper.TestUtil;
import org.apache.commons.io.IOUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * UrlConnectionContentProviderTest.java
  */
public class UrlConnectionContentProviderTest {

    private static final Logger logger = LoggerFactory.getLogger(UrlConnectionContentProviderTest.class);

    private UrlConnectionContentProvider provider;

    @Before
    public void init(){
        provider = new UrlConnectionContentProvider.Builder().build();
    }

    @Test
    public void canReadDataFromLocalFileUrl() {
        checkUrlAccessible(TestUtil.getFileAsURL("/testpages/ids-page-2.html"), provider);
    }

    @Test
    public void canReadDataFromLocalFileUrlWithRequestAttributesSet() {
        provider = new UrlConnectionContentProvider.Builder().setRequestProperty("User-Agent", "Mozilla/5.0").build();
        checkUrlAccessible(TestUtil.getFileAsURL("/testpages/ids-page-2.html"), provider);
    }

    @Test
    public void canReadDataFromExternalUrl() throws MalformedURLException {
        checkUrlAccessible(new URL("http://google.com"), provider);
    }

    @Test
    public void canReadDataFromExternalUrlWhichRequireUserAgent() throws MalformedURLException {
        provider = new UrlConnectionContentProvider.Builder().setRequestProperty("User-Agent", "Mozilla/5.0").build();
        checkUrlAccessible(new URL("http://www.glassdoor.com/Job/california-java-developer-job-opportunities-SRCH_IL.0,10_IS2280_KO11,25_IP6.htm"), provider);
    }


    private void checkUrlAccessible(URL url, UrlContentProvider provider) {
        InputStream is = null;
        try {
            is = provider.getContent(url);
            assertThat(is, notNullValue());
            assertThat(is.available(), Matchers.greaterThan(0));
            byte byteArray [] = new byte[64];
            assertThat(IOUtils.read(is, byteArray), is(64));

        } catch (IOException ex) {
            logger.error("Failed to load resource from url "+url, ex);
            Assert.fail("Failed to load resource from url "+url);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }
}
