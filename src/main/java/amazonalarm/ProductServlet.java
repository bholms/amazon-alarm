package amazonalarm;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.jsoup.*;
import org.jsoup.nodes.*;

/**
 * Created by bwzhao on 5/13/17.
 */
@WebServlet("/getProductInfo")
public class ProductServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = request.getParameter("product_url");

        if (request.getParameter("product_url") != null) {
            Map<String, Object> info = getProductInfo(url);
            request.setAttribute("message", info);
        }
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    public Map<String, Object> getProductInfo(String product_url) {
        Map<String, Object> product = new HashMap<String, Object>();
        String title = "";
        String priceStr = "";
        Number price = null;

        // Scrapping Amazon product site using Jaunt
        try{
            Document doc = Jsoup.connect(product_url).get();
            title = doc.title();
            if (title.toLowerCase().contains("Amazon.com: Books".toLowerCase())) {
                priceStr = doc.select(
                        "span.a-size-medium.a-color-price.header-price").text();
            } else {
                priceStr = doc.select(
                        "div.centerColAlign span#priceblock_ourprice").text();
            }
            price = NumberFormat.getCurrencyInstance(Locale.US).parse(priceStr.trim());
        }
        catch(IOException|ParseException e){
            System.err.println(e);
        }

        product.put("url", product_url);
        product.put("itemName", title);
        product.put("price", price);
        return product;
    }

    public static String getMessage() {
        return "Welcome to Amazon Sales Notifier";
    }
}
