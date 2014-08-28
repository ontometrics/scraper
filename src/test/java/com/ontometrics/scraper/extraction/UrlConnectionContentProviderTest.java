package com.ontometrics.scraper.extraction;

import com.ontometrics.scraper.TestUtil;
import org.apache.commons.io.IOUtils;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

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

    @Test
    public void canReadDataFromLocalFileUrl() {
        UrlConnectionContentProvider provider = new UrlConnectionContentProvider();
        checkUrlAccessible(TestUtil.getFileAsURL("/testpages/ids-page-2.html"), provider);
    }

    @Test
    public void canReadDataFromLocalFileUrlWithRequestAttributesSet() {
        UrlConnectionContentProvider provider = new UrlConnectionContentProvider();
//        provider.addRequestAttribute("User-Agent", "Mozilla/5.0");
        checkUrlAccessible(TestUtil.getFileAsURL("/testpages/ids-page-2.html"), provider);
    }

    @Test
    public void canReadDataFromExternalUrl() throws MalformedURLException {
        UrlConnectionContentProvider provider = new UrlConnectionContentProvider();
        checkUrlAccessible(new URL("http://google.com"), provider);
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
            Assert.fail("Failed to load resource from url "+url);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }
}
