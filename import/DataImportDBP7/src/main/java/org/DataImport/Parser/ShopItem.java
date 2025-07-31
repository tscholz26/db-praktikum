package org.DataImport.Parser;

// Data container
public class ShopItem {
    // Allgemeine Attribute
    private String pgroup;
    private String asin;
    private String title;
    private String priceRaw;
    private String priceCurrency;
    private String priceMult;
    private String state;
    private String publisher;



    // Bookspec
    private String binding;
    private String isbn;
    private String pages;
    private String pubDate;

    // Dvdspec
    private String dvdFormat;
    private String regionCode;
    private String dvdRelease;
    private String runningTime;

    // Musicspec
    private String musicBinding;
    private String musicFormat;
    private String musicRelease;
    private String upc;



    // Getter und Setter
    // Allgemein
    public String getPgroup() { return pgroup; }
    public void setPgroup(String pgroup) { this.pgroup = pgroup; }

    public String getAsin() { return asin; }
    public void setAsin(String asin) { this.asin = asin; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getPriceRaw() { return priceRaw; }
    public void setPriceRaw(String price) { this.priceRaw = priceRaw; }

    public String getPriceCurrency() { return priceCurrency; }
    public void setPriceCurrency(String priceCurrency) { this.priceCurrency = priceCurrency; }

    public String getPriceMult() { return priceMult; }
    public void setPriceMult(String priceMult) { this.priceMult = priceMult; }

    public String getState() { return state; }
    public void setState(String state) {this.state = state; }

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }

    // Bookspec
    public String getBinding() { return binding; }
    public void setBinding(String binding) { this.binding = binding; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getPages() { return pages; }
    public void setPages(String pages) { this.pages = pages; }

    public String getPubDate() { return pubDate; }
    public void setPubDate(String pubDate) { this.pubDate = pubDate; }

    // Dvdspec
    public String getDvdFormat() { return dvdFormat; }
    public void setDvdFormat(String dvdFormat) { this.dvdFormat = dvdFormat; }

    public String getRegionCode() { return regionCode; }
    public void setRegionCode(String regionCode) { this.regionCode = regionCode; }

    public String getDvdRelease() { return dvdRelease; }
    public void setDvdRelease(String dvdRelease) { this.dvdRelease = dvdRelease; }

    public String getRunningTime() { return runningTime; }
    public void setRunningTime(String runningTime) { this.runningTime = runningTime; }

    // Musicspec
    public String getMusicBinding() { return musicBinding; }
    public void setMusicBinding(String musicBinding) { this.musicBinding = musicBinding; }

    public String getMusicFormat() { return musicFormat; }
    public void setMusicFormat(String musicFormat) { this.musicFormat = musicFormat; }

    public String getMusicRelease() { return musicRelease; }
    public void setMusicRelease(String musicRelease) { this.musicRelease = musicRelease; }

    public String getUpc() { return upc; }
    public void setUpc(String upc) { this.upc = upc; }


    @Override
    public String toString() {
        return "org.example.Parser.ShopItem{" +
                "pgroup='" + pgroup + '\'' +
                ", asin='" + asin + '\'' +
                ", title='" + title + '\'' +
                ", priceRaw='" + priceRaw + '\'' +
                ", priceCurrency='" + priceCurrency + '\'' +
                ", priceMult='" + priceMult + '\'' +
                ", state='" + state + '\'' +
                ", publisher='" + publisher + '\'' +
                ", binding='" + binding + '\'' +
                ", isbn='" + isbn + '\'' +
                ", pages='" + pages + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", dvdFormat='" + dvdFormat + '\'' +
                ", regionCode='" + regionCode + '\'' +
                ", dvdRelease='" + dvdRelease + '\'' +
                ", runningTime='" + runningTime + '\'' +
                ", musicBinding='" + musicBinding + '\'' +
                ", musicFormat='" + musicFormat + '\'' +
                ", musicRelease='" + musicRelease + '\'' +
                ", upc='" + upc + '\'' +
                '}';
    }
}