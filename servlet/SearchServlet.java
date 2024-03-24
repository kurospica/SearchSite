package servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;



@WebServlet("/SearchServlet")

public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8"); // 文字化け防止
		
		String query = request.getParameter("query");
	    
	    String genre = request.getParameter("genre");
        String datetime = request.getParameter("datetime");
        String location = request.getParameter("location");
        String numPeople = request.getParameter("numPeople");
        
        // 各パラメータの値を連結してqueryにセットする
        //String searchQuery = query + " " + genre + " " + datetime + " " + location + " " + numPeople + "人" + " " + "restaurant";
        
        // 除外するサイトのドメイン
        String[] excludedSites = {"https://tabelog.com", "https://www.hotpepper.jp/", "https://retty.me/","https://hitosara.com/",
        		"https://loco.yahoo.co.jp/","https://restaurant.ikyu.com","https://newsdig.tbs.co.jp",
        		"https://pachisuro100.com/2022/04/23/sizuoka-schedule/","https://www.excite.co.jp/news/",
        		"https://chrome.google.com/webstore","https://www.google.com/","https://www.jalan.net/","https://support.google.com/",
        		"https://maps.google.com/","https://map.yahoo.co.jp","https://news.yahoo.co.jp","https://www.google.co.jp","https://news.","https://www.bashamichi.co.jp/",
        		"https://r.gnavi.co.jp/","https://ja.wikipedia.org/","https://www.youtube.com","https://www.kyounoryouri.jp","https://www.kikkoman.co.jp/",
        		"https://www.amazon.co.jp","https://park.ajinomoto.co.jp","https://search.rakuten.co.jp","https://www.sapporobeer.jp","https://chromewebstore.google.com/detail/search-and-new-tab-by-yah/lannejjfkoabhaaeapeiemfefgccjjik?extInstall=1&partner=oo-srp-promo-chr"};
        StringBuilder excludedSitesQuery = new StringBuilder();
        for (String site : excludedSites) {
            excludedSitesQuery.append(" -site:").append(site);
        }
	    
	    //Yahoo!検索エンジンを使用する場合
        List<String> searchQueries = new ArrayList<>();
        searchQueries.add(query + " " + genre + " " + datetime + " " + location + " " + numPeople);
       
	    if (query != null && !query.isEmpty()) {
	    	String yahooSearchUrl = "https://search.yahoo.com/search?p=" + URLEncoder.encode(searchQueries + " site:instagram.com" + excludedSitesQuery.toString(), "UTF-8");
	        Document doc = Jsoup.connect(yahooSearchUrl).get();
	        Elements searchResults = doc.select(".compTitle a");
	        List<String> urls = searchResults.stream()
	                .map(element -> element.attr("href"))
	                .filter(url -> url.startsWith("http"))
	                .distinct() // URLの重複を除去
	                .collect(Collectors.toList());
	        
	     // 入力値をリクエスト属性として設定してJSPに渡す
	        request.setAttribute("genre", genre);
	        request.setAttribute("datetime", datetime);
	        request.setAttribute("location", location);
	        request.setAttribute("numPeople", numPeople);
	        
	        if (!urls.isEmpty()) {
	            List<String> randomUrls = new ArrayList<>();
	            Random random = new Random();
	            for (int i = 0; i < 5 && !urls.isEmpty(); i++) {
	                int index = random.nextInt(urls.size());
	                randomUrls.add(urls.get(index));
	                urls.remove(index);
	            }
	            request.setAttribute("randomUrls", randomUrls);
	            request.getRequestDispatcher("index.jsp").forward(request, response);
	        } else {
	            response.getWriter().println("No results found.");
	        }
	    } else {
	        response.getWriter().println("Please enter a search query.");
	    }
        /*
        int page = 0;
        List<String> randomUrls = new ArrayList<>();
        while (randomUrls.size() < 5 && page < 5) { // 5つ未満かつ5ページ以内の条件でループ
        	String yahooSearchUrl = "https://search.yahoo.com/search?p=" + URLEncoder.encode(searchQueries + excludedSitesQuery.toString(), "UTF-8");
        	//String googleSearchUrl = "https://www.google.com/search?q=" + URLEncoder.encode(searchQueries + excludedSitesQuery.toString(), "UTF-8") + "&start=" + (page * 10);
            Document doc = Jsoup.connect(yahooSearchUrl).get();
            //Elements searchResults = doc.select("a[href]");
            Elements searchResults = doc.select(".compTitle a");
	        List<String> urls = searchResults.stream().map(element -> element.absUrl("href")).collect(Collectors.toList());
	        
            //List<String> urls = searchResults.stream()
            //        .map(element -> element.attr("href"))
            //        .filter(url -> url.startsWith("http"))
            //        .distinct() // URLの重複を除去
            //        .collect(Collectors.toList());
            if (urls.isEmpty()) {
                break; // 検索結果がない場合はループを終了
            }
            for (int i = 0; i < urls.size() && randomUrls.size() < 5; i++) {
                randomUrls.add(urls.get(i));
            }
            page++;
            
        }
        request.setAttribute("randomUrls", randomUrls);
        request.getRequestDispatcher("search.jsp").forward(request, response);
        */
    }
}



