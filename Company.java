import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Company {
    Document doc;
    String domain;
    String name;
    String number;
    String website;
    String emails;
    public Company(Element data , String domain){
        this.domain=domain;
        this.name=extractName(data);
        this.number=extractNumber(data);
        this.website=extractWebsite(data);
        this.emails=extractEmail(!(doc==null));
    }

    public String extractName(Element e){
        return e.select(".listing__name > a").text().replace(',',' ');
        
    }

    public String extractNumber(Element e){
        return e.select(".mlr").select(".mlr__item--phone").select(".mlr__submenu__item").text();
        
    }

    public String extractWebsite(Element e){
        String website=" ";
           if (!(e.select(".mlr").select(".mlr__item--website > a")
                .attr("href") + " ")
                .equalsIgnoreCase(" ")
                ) {
                String Url = domain + e.select(".mlr").select(".mlr__item--website > a").attr("abs:href");
                Connection.Response response;
                try {
                    response = Jsoup.connect(Url).execute();
                    doc=response.parse();
                    website=response.url().toString();
                } catch (IOException ex) {
                    doc=null;
                } catch (IllegalArgumentException ex2){
                    System.out.println(Url);
                    throw new IllegalArgumentException("");
                }
            }
        return website;
    }

    public String toString(){
        if (!emails.equals(""))
            return name+","+emails+","+" "+","+" ";
        else if (!website.equals(" "))
            return name+","+" "+","+website+","+" ";
        else
            return name+","+" "+","+" "+","+number;
    }
    
    public String extractEmail(boolean b){
        if (!b)
            return "";
         Pattern p = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+");
         Matcher matcher = p.matcher(doc.text());

        String emailist = "";
        while (matcher.find()) {
            emailist=emailist.concat(matcher.group()+",");
        }
        return emailist;
    }

}
