package amazonalarm;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

// servlet
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// logging services
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// XML processor
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/*
 * This class shows how to make a simple authenticated call to the
 * Amazon Product Advertising API.
 *
 * See the README.html that came with this sample for instructions on
 * configuring and running the sample.
 */
@WebServlet("/getProductInfo")
public class ProductServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger( ProductServlet.class );
    private static final String ENDPOINT = "webservices.amazon.com";
    private Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String asin = request.getParameter("product_asin");

        if (request.getParameter("product_asin") != null) {
            Product item = getProductInfoFromXML(asin);
            Map<String, Object> itemInfo = new HashMap<String, Object>();

            // store the necessary info to itemInfo
            itemInfo.put("title", item.title);
            itemInfo.put("imageURL", item.imageURL);
            itemInfo.put("url", item.url);
            itemInfo.put("price", item.price.formattedAmount);

            LOGGER.debug(gson.toJson(itemInfo));
            request.setAttribute("message", itemInfo);
        }
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    private Product getProductInfoFromXML(String asin) {
        Product item = new Product();
        String requestURL = formatProductURL(asin);
        Document doc = getXMLResponse(requestURL);
        Price price = new Price();

        // get item title
        Node titleNode = doc.getElementsByTagName("Title").item(0);
        item.title = titleNode.getTextContent();

        // get item url
        Node detailedPageURLNode = doc.getElementsByTagName("DetailPageURL").item(0);
        item.url = detailedPageURLNode.getTextContent();

        // get item imageURL
        Node imageURLNode = doc.getElementsByTagName("MediumImage").item(0).getFirstChild();
        item.imageURL = imageURLNode.getTextContent();

        // get item price
        Element priceNode = (Element) doc.getElementsByTagName("LowestNewPrice").item(0)
                .getChildNodes();
        price.currencyCode = priceNode.getElementsByTagName("CurrencyCode").item(0)
                .getTextContent();
        price.amount = Integer.parseInt(priceNode.getElementsByTagName("Amount").item(0)
                .getTextContent());
        price.formattedAmount = priceNode.getElementsByTagName("FormattedPrice").item(0)
                .getTextContent();
        item.price = price;

        LOGGER.debug(gson.toJson(item));
        return item;
    }

    private Document getXMLResponse(String requestURL) {
        Document doc = null;
        try {
            // Fetch the product info based on the URL
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(requestURL);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return doc;
    }

    private String formatProductURL(String asin) {
        /*
         * Set up the signed requests helper.
         */
        SignedRequestsHelper helper;
        String requestUrl = "";
        Map<String, String> params = new HashMap<String, String>();
        Authentication auth = getAuth();

        try {
            helper = SignedRequestsHelper.getInstance(ENDPOINT, auth.accessKeyID, auth.secretKey);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        params.put("Service", "AWSECommerceService");
        params.put("Operation", "ItemLookup");
        params.put("AWSAccessKeyId", auth.accessKeyID);
        params.put("AssociateTag", auth.associateTag);
        params.put("ItemId", asin);
        params.put("IdType", "ASIN");
        params.put("ResponseGroup", "Images,ItemAttributes,Offers,Reviews");

        requestUrl = helper.sign(params);

        return requestUrl;
    }

    private Authentication getAuth() {
        Authentication auth = new Authentication();
        try {
            File file = new File(getClass().getResource("/auth.txt").getFile());;

            BufferedReader b = new BufferedReader(new FileReader(file));
            String readLine = "";
            String[] parts;

            while ((readLine = b.readLine()) != null) {
                parts = readLine.split("=");
                if (parts[0].equals("accessKeyID")) auth.accessKeyID = parts[1];
                if (parts[0].equals("secretKey")) auth.secretKey = parts[1];
                if (parts[0].equals("associateTag")) auth.associateTag = parts[1];
            }

            LOGGER.debug(gson.toJson(auth));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return auth;

    }

    public static String getMessage() {
        return "Welcome to Amazon price notifier";
    }
}
