package noodnik.openai;

import org.junit.Test;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Attribute;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.HTMLEditorKit.ParserCallback;
import javax.swing.text.html.parser.ParserDelegator;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;

public class Interview {

    @Test(timeout = 10000L)
    public void firstTestCase() {
        Solution.crawl("https://www.openai.com");
    }

    static class Solution {

        public static List<String> extractLinks(Reader reader) throws IOException {
            ArrayList<String> list = new ArrayList<String>();

            ParserDelegator parserDelegator = new ParserDelegator();
            ParserCallback parserCallback = new ParserCallback() {
                public void handleStartTag(Tag tag, MutableAttributeSet attribute, int pos) {
                    if (tag == Tag.A) {
                        String address = (String) attribute.getAttribute(Attribute.HREF);
                        list.add(address);
                    }
                }

                public void handleText(final char[] data, final int pos) {
                }

                public void handleEndTag(Tag t, final int pos) {
                }

                public void handleSimpleTag(Tag t, MutableAttributeSet a, final int pos) {
                }

                public void handleComment(final char[] data, final int pos) {
                }

                public void handleError(final String errMsg, final int pos) {
                }
            };

            parserDelegator.parse(reader, parserCallback, false);
            return list;
        }

        /**
         * Links from `get_hyperlinks(url)` can be of two possible forms:
         * <p>
         * Full URL e.g. "https://www.openai.com/progress/"
         * Partial URL e.g. "/progress/" or "progress"
         * <p>
         * Full URLs will always begin with "http:", "https:", or "mailto:"
         * Partial URLs:
         * If they begin with "/" they should be prefixed with the root URL passed to `crawl()`
         * If they do not, they should be prefixed with the url that was passed to `get_hyperlinks()`
         * <p>
         * Feel free to use any documentation for the language you choose while solving this problem.
         */
        public static List<String> getHyperlinks(String url_string) {
            try {
                // get URL content
                URL url = new URL(url_string);
                URLConnection conn = url.openConnection();

                // InputStream into a BufferedReader
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                List<String> links = extractLinks(br);
                br.close();

                return links;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        public static void main(String[] args) {
            crawl("https://www.openai.com");
        }

        /*
         * You should write a function, `crawl()` that will be given the root URL of a domain,
         * in this case "https://www.openai.com", and must print out all unique urls under
         * the domain reachable from the root.
         *
         * You will be provided a function, `get_hyperlinks(url)` that will return a list of links at the given URL.
         * */

        static Set<String> seenResolvedUrls = new TreeSet<>();

        static Consumer<String> urlPrinterFn = resolvedUrl -> {
            if (!seenResolvedUrls.contains(resolvedUrl)) {
                System.out.println("resolvedUrl: " + resolvedUrl);
                seenResolvedUrls.add(resolvedUrl);
            }
        };

        public static void crawl(String resolvedUrl) {

            urlPrinterFn.accept(resolvedUrl);

            List<String> hyperLinks = getHyperlinks(resolvedUrl);
            // System.out.println("hyperlinks: " + hyperLinks) ;

            if (hyperLinks == null) {
                return;
            }

            hyperLinks
                .stream()
                .map(url -> resolveUrl(resolvedUrl, url))
                .filter(rurl -> rurl != null)
                .filter(rurl -> !seenResolvedUrls.contains(rurl))
                .forEach(Solution::crawl);

        }

        static String resolveUrl(String contextUrl, String hyperlinkUrl) {

            try {
                String rurl = new URL(new URL(contextUrl), hyperlinkUrl).toString();
                // System.out.println("hurl, rurl resolved: " + hyperlinkUrl + ", " + rurl);
                return rurl;
            } catch (Exception e) {
                // System.out.println("thrown: " + e);
                return null;
            }
        }

        /**
         Full URL e.g. "https://www.openai.com/progress/"
         Partial URL e.g. "/progress/" or "progress"

         Full URLs will always begin with "http:", "https:", or "mailto:"
         Partial URLs:
         If they begin with "/" they should be prefixed with the root URL passed to `crawl()`
         If they do not, they should be prefixed with the url that was passed to `get_hyperlinks()`

         */

    }


}
