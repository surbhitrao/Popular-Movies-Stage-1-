
package com.example.surbhitrao.myapplication;

/**
 * Created by Surbhit Rao on 20-02-2016.
 */

public class GridItem {
    private String image;
    private String title;
    private String overview;
    private String votes;
    private String r_date;

    public GridItem() {
        super();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes)
    {
        this.votes = votes;
    }
    public String getR_date() {
        return r_date;
    }

    public void setR_date(String r_date) {
        this.r_date = r_date;
    }

}
