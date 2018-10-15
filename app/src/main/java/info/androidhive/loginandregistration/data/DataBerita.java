package info.androidhive.loginandregistration.data;

/**
 * Created by Kuncoro on 26/03/2016.
 */
public class DataBerita {
    private String nid, news_heading, news_description, news_image, news_date;

    public DataBerita() {
    }

    public DataBerita(String nid, String news_heading, String news_description, String news_image, String news_date) {
        this.nid = nid;
        this.news_heading = news_heading;
        this.news_description = news_description;
        this.news_image = news_image;
        this.news_date = news_date;
        }

    public String getNid() { return nid; }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getNews_heading() {
        return news_heading;
    }

    public void setNews_heading(String news_heading) {
        this.news_heading = news_heading;
    }

    public String getNews_description() { return news_description; }

    public void setNews_description(String news_description) {
        this.news_description = news_description;
    }

    public String getNews_image() {
        return news_image;
    }

    public void setNews_image(String news_image) {
        this.news_image = news_image;
    }

    public String getNews_date() {
        return news_date;
    }

    public void setNews_date(String news_date) {
        this.news_date = news_date;
    }

}
